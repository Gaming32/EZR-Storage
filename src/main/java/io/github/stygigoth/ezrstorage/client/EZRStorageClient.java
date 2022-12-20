package io.github.stygigoth.ezrstorage.client;

import io.github.stygigoth.ezrstorage.EZRStorage;
import io.github.stygigoth.ezrstorage.block.ModificationBoxBlock;
import io.github.stygigoth.ezrstorage.client.gui.StorageCoreScreen;
import io.github.stygigoth.ezrstorage.gui.StorageCoreScreenHandler;
import io.github.stygigoth.ezrstorage.registry.EZRReg;
import io.github.stygigoth.ezrstorage.util.MoreBufs;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class EZRStorageClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        HandledScreens.register(EZRStorage.STORAGE_CORE_SCREEN_HANDLER, StorageCoreScreen::new);

        registerGlobalReceiver(EZRStorage.SYNC_INVENTORY, (client, handler, buf, responseSender) -> {
            final int syncId = buf.readUnsignedByte();
            if (client.player.currentScreenHandler instanceof StorageCoreScreenHandler screenHandler && screenHandler.syncId == syncId) {
                final NbtCompound inventoryData = buf.readNbt();
                final var modifications = MoreBufs.readEnumSet(buf, ModificationBoxBlock.Type.class);
                if (inventoryData != null) {
                    screenHandler.getCoreInventory().readNbt(inventoryData);
                    screenHandler.getModifications().clear();
                    screenHandler.getModifications().addAll(modifications);
                }
            }
        });

        if (!ResourceManagerHelper.registerBuiltinResourcePack(
            EZRReg.id("classic_resources"),
            FabricLoader.getInstance().getModContainer("ezrstorage").orElseThrow(),
            "Classic Resources",
            ResourcePackActivationType.NORMAL
        )) {
            EZRStorage.LOGGER.warn("Failed to register Classic Resources.");
        }
    }

    private static void registerGlobalReceiver(Identifier packet, ClientPlayNetworking.PlayChannelHandler packetHandler) {
        ClientPlayNetworking.registerGlobalReceiver(packet, (client, handler, buf, responseSender) -> {
            if (client.isOnThread()) {
                packetHandler.receive(client, handler, buf, responseSender);
            } else {
                final PacketByteBuf newBuf = new PacketByteBuf(buf.copy());
                client.executeSync(() -> {
                    if (handler.getConnection().isOpen()) {
                        try {
                            packetHandler.receive(client, handler, newBuf, responseSender);
                        } catch (Exception e) {
                            if (handler.shouldCrashOnException()) {
                                throw e;
                            }

                            EZRStorage.LOGGER.error("Failed to handle packet " + packet + ", suppressing error", e);
                        }
                    } else {
                        EZRStorage.LOGGER.debug("Ignoring packet due to disconnection: {}", packet);
                    }
                });
            }
        });
    }
}

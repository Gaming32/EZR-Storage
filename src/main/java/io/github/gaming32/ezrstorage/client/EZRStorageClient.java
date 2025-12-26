package io.github.gaming32.ezrstorage.client;

import io.github.gaming32.ezrstorage.EZRStorage;
import io.github.gaming32.ezrstorage.block.ModificationBoxBlock;
import io.github.gaming32.ezrstorage.client.gui.ExtractionPortScreen;
import io.github.gaming32.ezrstorage.client.gui.StorageCoreScreen;
import io.github.gaming32.ezrstorage.client.gui.StorageCoreScreenWithCrafting;
import io.github.gaming32.ezrstorage.gui.StorageCoreScreenHandler;
import io.github.gaming32.ezrstorage.registry.EZRReg;
import io.github.gaming32.ezrstorage.util.MoreBufs;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

@Environment(EnvType.CLIENT)
public class EZRStorageClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        MenuScreens.register(EZRStorage.STORAGE_CORE_SCREEN_HANDLER, StorageCoreScreen::new);
        MenuScreens.register(EZRStorage.STORAGE_CORE_SCREEN_HANDLER_WITH_CRAFTING, StorageCoreScreenWithCrafting::new);
        MenuScreens.register(EZRStorage.EXTRACTION_PORT_SCREEN_HANDLER, ExtractionPortScreen::new);

        ClientTickEvents.START_CLIENT_TICK.register(client -> EZRStorage.clientTicks++);
        ClientTickEvents.END_CLIENT_TICK.register(client -> EZRStorage.clientTicks++);

        registerGlobalReceiver(
            EZRStorage.SYNC_INVENTORY, (client, handler, buf, responseSender) -> {
                final int syncId = buf.readUnsignedByte();
                if (client.player.containerMenu instanceof StorageCoreScreenHandler screenHandler
                    && screenHandler.containerId
                       == syncId) {
                    final CompoundTag inventoryData = buf.readNbt();
                    final var modifications = MoreBufs.readEnumSet(buf, ModificationBoxBlock.Type.class);
                    if (inventoryData != null) {
                        screenHandler.getCoreInventory().readNbt(inventoryData);
                        screenHandler.getModifications().clear();
                        screenHandler.getModifications().addAll(modifications);
                        if (screenHandler.getUpdateNotification() != null) {
                            screenHandler.getUpdateNotification().run();
                        }
                    }
                }
            }
        );

        if (!ResourceManagerHelper.registerBuiltinResourcePack(
            EZRReg.id("classic_resources"),
            FabricLoader.getInstance().getModContainer("ezrstorage").orElseThrow(),
            "Classic Resources",
            ResourcePackActivationType.NORMAL
        )) {
            EZRStorage.LOGGER.warn("Failed to register Classic Resources.");
        }
    }

    private static void registerGlobalReceiver(
        ResourceLocation packet,
        ClientPlayNetworking.PlayChannelHandler packetHandler
    ) {
        ClientPlayNetworking.registerGlobalReceiver(
            packet, (client, handler, buf, responseSender) -> {
                if (client.isSameThread()) {
                    packetHandler.receive(client, handler, buf, responseSender);
                } else {
                    final FriendlyByteBuf newBuf = new FriendlyByteBuf(buf.copy());
                    client.executeIfPossible(() -> {
                        if (handler.getConnection().isConnected()) {
                            try {
                                packetHandler.receive(client, handler, newBuf, responseSender);
                            } catch (Exception e) {
                                if (handler.shouldPropagateHandlingExceptions()) {
                                    throw e;
                                }

                                EZRStorage.LOGGER.error("Failed to handle packet " + packet + ", suppressing error", e);
                            }
                        } else {
                            EZRStorage.LOGGER.debug("Ignoring packet due to disconnection: {}", packet);
                        }
                    });
                }
            }
        );
    }
}

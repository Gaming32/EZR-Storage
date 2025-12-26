package io.github.gaming32.ezrstorage.client;

import io.github.gaming32.ezrstorage.EZRStorage;
import io.github.gaming32.ezrstorage.client.gui.ExtractionPortScreen;
import io.github.gaming32.ezrstorage.client.gui.StorageCoreScreen;
import io.github.gaming32.ezrstorage.client.gui.StorageCoreScreenWithCrafting;
import io.github.gaming32.ezrstorage.gui.StorageCoreMenu;
import io.github.gaming32.ezrstorage.networking.SyncInventoryPacket;
import io.github.gaming32.ezrstorage.registry.EZRReg;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.network.chat.Component;

@Environment(EnvType.CLIENT)
public class EZRStorageClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        MenuScreens.register(EZRStorage.STORAGE_CORE_MENU, StorageCoreScreen::new);
        MenuScreens.register(EZRStorage.STORAGE_CORE_MENU_WITH_CRAFTING, StorageCoreScreenWithCrafting::new);
        MenuScreens.register(EZRStorage.EXTRACTION_PORT_MENU, ExtractionPortScreen::new);

        ClientTickEvents.START_CLIENT_TICK.register(client -> EZRStorage.clientTicks++);
        ClientTickEvents.END_CLIENT_TICK.register(client -> EZRStorage.clientTicks++);

        ClientPlayNetworking.registerGlobalReceiver(
            SyncInventoryPacket.TYPE, (packet, player, responseSender) -> {
                if (player.containerMenu instanceof StorageCoreMenu menu && menu.containerId == packet.containerId()) {
                    menu.getCoreInventory().overrideFrom(packet.inventory());
                    menu.getModifications().clear();
                    menu.getModifications().addAll(packet.modifications());
                    if (menu.getUpdateNotification() != null) {
                        menu.getUpdateNotification().run();
                    }
                }
            }
        );

        if (!ResourceManagerHelper.registerBuiltinResourcePack(
            EZRReg.id("classic_resources"),
            FabricLoader.getInstance().getModContainer("ezrstorage").orElseThrow(),
            Component.translatable("ezrstorage.classic_resources"),
            ResourcePackActivationType.NORMAL
        )) {
            EZRStorage.LOGGER.warn("Failed to register Classic Resources.");
        }
    }
}

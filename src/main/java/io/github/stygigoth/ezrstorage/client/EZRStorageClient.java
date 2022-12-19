package io.github.stygigoth.ezrstorage.client;

import io.github.stygigoth.ezrstorage.EZRStorage;
import io.github.stygigoth.ezrstorage.client.gui.StorageCoreScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.ingame.HandledScreens;

@Environment(EnvType.CLIENT)
public class EZRStorageClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        HandledScreens.register(EZRStorage.STORAGE_CORE_SCREEN_HANDLER, StorageCoreScreen::new);
    }
}

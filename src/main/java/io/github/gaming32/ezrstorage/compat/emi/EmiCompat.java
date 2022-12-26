package io.github.gaming32.ezrstorage.compat.emi;

import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import io.github.gaming32.ezrstorage.EZRStorage;
import io.github.gaming32.ezrstorage.client.gui.StorageCoreScreen;
import io.github.gaming32.ezrstorage.client.gui.StorageCoreScreenWithCrafting;

public class EmiCompat implements EmiPlugin {
    @Override
    public void register(EmiRegistry registry) {
        registry.addRecipeHandler(EZRStorage.STORAGE_CORE_SCREEN_HANDLER_WITH_CRAFTING, new StorageCoreRecipeHandler());
        registry.addStackProvider(StorageCoreScreen.class, new StorageCoreStackProvider<>());
        registry.addStackProvider(StorageCoreScreenWithCrafting.class, new StorageCoreStackProvider<>());
    }
}

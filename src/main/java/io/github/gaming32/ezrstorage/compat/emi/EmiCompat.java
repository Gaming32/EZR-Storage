package io.github.gaming32.ezrstorage.compat.emi;

import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import io.github.gaming32.ezrstorage.EZRStorage;

public class EmiCompat implements EmiPlugin {
    public static final StorageCoreRecipeHandler RECIPE_HANDLER = new StorageCoreRecipeHandler();

    @Override
    public void register(EmiRegistry registry) {
        registry.addRecipeHandler(EZRStorage.STORAGE_CORE_SCREEN_HANDLER_WITH_CRAFTING, RECIPE_HANDLER);
    }
}

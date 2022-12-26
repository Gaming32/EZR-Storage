package io.github.gaming32.ezrstorage.compat.emi;

import dev.emi.emi.api.EmiRecipeHandler;
import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.VanillaEmiRecipeCategories;
import io.github.gaming32.ezrstorage.gui.StorageCoreScreenHandlerWithCrafting;
import net.minecraft.screen.slot.Slot;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class StorageCoreRecipeHandler implements EmiRecipeHandler<StorageCoreScreenHandlerWithCrafting> {
    @Override
    public List<Slot> getInputSources(StorageCoreScreenHandlerWithCrafting handler) {
        return handler.getInputSources();
    }

    @Override
    public List<Slot> getCraftingSlots(StorageCoreScreenHandlerWithCrafting handler) {
        return handler.getCraftingSlots();
    }

    @Override
    public @Nullable Slot getOutputSlot(StorageCoreScreenHandlerWithCrafting handler) {
        return handler.getCraftingResultSlot();
    }

    @Override
    public boolean supportsRecipe(EmiRecipe recipe) {
        return recipe.getCategory() == VanillaEmiRecipeCategories.CRAFTING && recipe.supportsRecipeTree();
    }
}

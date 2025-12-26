package io.github.gaming32.ezrstorage.compat.emi;

import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.VanillaEmiRecipeCategories;
import dev.emi.emi.api.recipe.handler.StandardRecipeHandler;
import io.github.gaming32.ezrstorage.gui.StorageCoreMenuWithCrafting;
import java.util.List;
import net.minecraft.world.inventory.Slot;
import org.jetbrains.annotations.Nullable;

public class StorageCoreRecipeHandler implements StandardRecipeHandler<StorageCoreMenuWithCrafting> {
    @Override
    public List<Slot> getInputSources(StorageCoreMenuWithCrafting handler) {
        return handler.getInputSources();
    }

    @Override
    public List<Slot> getCraftingSlots(StorageCoreMenuWithCrafting handler) {
        return handler.getCraftingSlots();
    }

    @Override
    public @Nullable Slot getOutputSlot(StorageCoreMenuWithCrafting handler) {
        return handler.getCraftingResultSlot();
    }

    @Override
    public boolean supportsRecipe(EmiRecipe recipe) {
        return recipe.getCategory() == VanillaEmiRecipeCategories.CRAFTING && recipe.supportsRecipeTree();
    }
}

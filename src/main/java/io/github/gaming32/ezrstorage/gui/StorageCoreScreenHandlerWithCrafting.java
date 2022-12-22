package io.github.gaming32.ezrstorage.gui;

import io.github.gaming32.ezrstorage.EZRStorage;
import io.github.gaming32.ezrstorage.InfiniteInventory;
import io.github.gaming32.ezrstorage.block.ModificationBoxBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.CraftingResultInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.CraftingScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.slot.CraftingResultSlot;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.EnumSet;
import java.util.Set;

public class StorageCoreScreenHandlerWithCrafting extends StorageCoreScreenHandler {
    private final CraftingInventory craftingGrid = new CraftingInventory(this, 3, 3);
    private final CraftingResultInventory craftingResult = new CraftingResultInventory();
    private final ScreenHandlerContext context;
    private final PlayerEntity player;
    private long lastTick = -1;

    public StorageCoreScreenHandlerWithCrafting(int syncId, PlayerInventory playerInventory) {
        this(syncId, playerInventory, new InfiniteInventory(), EnumSet.noneOf(ModificationBoxBlock.Type.class), ScreenHandlerContext.EMPTY);
    }

    public StorageCoreScreenHandlerWithCrafting(int syncId, PlayerInventory playerInventory, InfiniteInventory coreInventory, Set<ModificationBoxBlock.Type> modifications, ScreenHandlerContext context) {
        super(EZRStorage.STORAGE_CORE_SCREEN_HANDLER_WITH_CRAFTING, syncId, playerInventory, coreInventory, modifications);
        this.context = context;
        this.player = playerInventory.player;

        addSlot(new CraftingResultSlot(playerInventory.player, craftingGrid, craftingResult, 0, 116, 117));

        for (int row = 0; row < 3; row++) {
            for (int column = 0; column < 3; column++) {
                addSlot(new Slot(craftingGrid, column + row * 3, 44 + column * 18, 99 + row * 18));
            }
        }
        onContentChanged(craftingGrid);
    }

    @Override
    public void onContentChanged(Inventory inventory) {
        context.run((world, pos) -> CraftingScreenHandler.updateResult(this, world, player, craftingGrid, craftingResult));
    }

    @Override
    public ItemStack transferSlot(PlayerEntity player, int index) {
        if (player instanceof ServerPlayerEntity serverPlayer) {
            if (EZRStorage.serverTicks == lastTick) {
                syncToClient(serverPlayer);
                return ItemStack.EMPTY;
            }
            lastTick = EZRStorage.serverTicks;
        } else {
            if (EZRStorage.clientTicks == lastTick) {
                return ItemStack.EMPTY;
            }
            lastTick = EZRStorage.clientTicks;
        }

        final Slot slot = slots.get(index);
        //noinspection ConstantValue
        if (slot != null && slot.hasStack()) {
            if (slot instanceof CraftingResultSlot) {
                final ItemStack[] recipe = new ItemStack[9];
                for (int i = 0; i < 9; i++) {
                    recipe[i] = craftingGrid.getStack(i);
                }
                ItemStack craftingResult = slot.getStack();
                ItemStack result = ItemStack.EMPTY;
                final ItemStack original = craftingResult.copy();
                int crafted = 0;
                final int maxStackSize = craftingResult.getMaxCount();
                final int crafting = craftingResult.getCount();
                for (int i = 0; i < craftingResult.getMaxCount(); i++) {
                    if (slot.hasStack() && slot.getStack().isItemEqual(craftingResult)) {
                        if (crafting > maxStackSize) {
                            return ItemStack.EMPTY;
                        }
                        craftingResult = slot.getStack();
                        result = craftingResult.copy();
                        if (crafted + craftingResult.getCount() > craftingResult.getMaxCount()) {
                            return ItemStack.EMPTY;
                        }
                        if (!insertItem(craftingResult, rowCount() * 9, rowCount() * 9 + 36, true)) {
                            return ItemStack.EMPTY;
                        }
                        crafted += result.getCount();
                        slot.onQuickTransfer(craftingResult, result);
                        slot.onTakeItem(player, craftingResult);

                        if (original.isItemEqual(slot.getStack())) continue;

                        tryToPopulateCraftingGrid(recipe, player);
                    } else {
                        break;
                    }
                }

                if (craftingResult.getCount() == result.getCount()) {
                    return ItemStack.EMPTY;
                }

                return result;
            } else {
                final ItemStack stackInSlot = slot.getStack();
                slot.setStack(getCoreInventory().moveFrom(stackInSlot));
            }
        }
        return ItemStack.EMPTY;
    }

    @Override
    public void onSlotClick(int slotIndex, int button, SlotActionType actionType, PlayerEntity player) {
        if (slotIndex >= 0 && slotIndex < slots.size()) {
            final Slot slot = slots.get(slotIndex);
            if (slot instanceof CraftingResultSlot) {
                final ItemStack[] recipe = new ItemStack[9];
                for (int i = 0; i < 9; i++) {
                    recipe[i] = craftingGrid.getStack(i).copy();
                }

                super.onSlotClick(slotIndex, button, actionType, player);
                tryToPopulateCraftingGrid(recipe, player);
                return;
            }
        }
        super.onSlotClick(slotIndex, button, actionType, player);
    }

    private void tryToPopulateCraftingGrid(ItemStack[] recipe, PlayerEntity player) {
        clearGrid(player);
        for (int i = 0; i < recipe.length; i++) {
            if (recipe[i].isEmpty() || recipe[i].getCount() > 1) continue;
            final int finalI = i;
            slots.stream().filter(
                slot -> slot.inventory == craftingGrid && slot.getIndex() == finalI
            ).findFirst().ifPresent(slot -> {
                final ItemStack retrieved = getCoreInventory().extractStack(recipe[finalI]);
                if (!retrieved.isEmpty()) {
                    slot.setStack(retrieved);
                }
            });
        }
    }

    @Override
    protected int playerInventoryY() {
        return 162;
    }

    @Override
    protected int rowCount() {
        return 4;
    }

    @Override
    public void close(PlayerEntity player) {
        super.close(player);
        clearGrid(player);
    }

    public void clearGrid(PlayerEntity player) {
        for (int i = 0; i < 9; i++) {
            final ItemStack stack = craftingGrid.getStack(i);
            if (!stack.isEmpty()) {
                final ItemStack result = getCoreInventory().moveFrom(stack);
                craftingGrid.setStack(i, ItemStack.EMPTY);
                if (!result.isEmpty()) {
                    player.dropItem(result, false);
                }
            }
        }
    }
}

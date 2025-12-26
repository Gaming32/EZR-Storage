package io.github.gaming32.ezrstorage.gui;

import io.github.gaming32.ezrstorage.EZRStorage;
import io.github.gaming32.ezrstorage.InfiniteInventory;
import io.github.gaming32.ezrstorage.block.ModificationBoxBlock;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.inventory.CraftingMenu;
import net.minecraft.world.inventory.ResultContainer;
import net.minecraft.world.inventory.ResultSlot;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class StorageCoreScreenHandlerWithCrafting extends StorageCoreScreenHandler {
    private final CraftingContainer craftingGrid = new CraftingContainer(this, 3, 3);
    private final ResultContainer craftingResult = new ResultContainer();
    private final Slot craftingResultSlot;
    private final ContainerLevelAccess context;
    private final List<Slot> craftingSlots = new ArrayList<>();
    private long lastTick = -1;

    public StorageCoreScreenHandlerWithCrafting(int syncId, Inventory playerInventory) {
        this(
            syncId,
            playerInventory,
            new InfiniteInventory(),
            EnumSet.noneOf(ModificationBoxBlock.Type.class),
            ContainerLevelAccess.NULL
        );
    }

    public StorageCoreScreenHandlerWithCrafting(
        int syncId,
        Inventory playerInventory,
        InfiniteInventory coreInventory,
        Set<ModificationBoxBlock.Type> modifications,
        ContainerLevelAccess context
    ) {
        super(
            EZRStorage.STORAGE_CORE_SCREEN_HANDLER_WITH_CRAFTING,
            syncId,
            playerInventory,
            coreInventory,
            modifications
        );
        this.context = context;

        addSlot(craftingResultSlot = new ResultSlot(playerInventory.player, craftingGrid, craftingResult, 0, 116, 117));

        for (int row = 0; row < 3; row++) {
            for (int column = 0; column < 3; column++) {
                addCraftingSlot(new Slot(craftingGrid, column + row * 3, 44 + column * 18, 99 + row * 18));
            }
        }
        slotsChanged(craftingGrid);
    }

    private void addCraftingSlot(Slot slot) {
        super.addInputSource(slot);
        craftingSlots.add(slot);
    }

    public List<Slot> getCraftingSlots() {
        return craftingSlots;
    }

    public Slot getCraftingResultSlot() {
        return craftingResultSlot;
    }

    @Override
    public void slotsChanged(Container inventory) {
        context.execute((world, pos) -> CraftingMenu.slotChangedCraftingGrid(
            this,
            world,
            player,
            craftingGrid,
            craftingResult
        ));
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        if (player instanceof ServerPlayer serverPlayer) {
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
        if (slot != null && slot.hasItem()) {
            if (slot instanceof ResultSlot) {
                final ItemStack[] recipe = new ItemStack[9];
                for (int i = 0; i < 9; i++) {
                    recipe[i] = craftingGrid.getItem(i);
                }
                ItemStack craftingResult = slot.getItem();
                ItemStack result = ItemStack.EMPTY;
                final ItemStack original = craftingResult.copy();
                int crafted = 0;
                final int maxStackSize = craftingResult.getMaxStackSize();
                final int crafting = craftingResult.getCount();
                for (int i = 0; i < craftingResult.getMaxStackSize(); i++) {
                    if (slot.hasItem() && slot.getItem().sameItemStackIgnoreDurability(craftingResult)) {
                        if (crafting > maxStackSize) {
                            return ItemStack.EMPTY;
                        }
                        craftingResult = slot.getItem();
                        result = craftingResult.copy();
                        if (crafted + craftingResult.getCount() > craftingResult.getMaxStackSize()) {
                            return ItemStack.EMPTY;
                        }
                        if (!moveItemStackTo(craftingResult, rowCount() * 9, rowCount() * 9 + 36, true)) {
                            return ItemStack.EMPTY;
                        }
                        crafted += result.getCount();
                        slot.onQuickCraft(craftingResult, result);
                        slot.onTake(player, craftingResult);

                        if (original.sameItemStackIgnoreDurability(slot.getItem())) continue;

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
                final ItemStack stackInSlot = slot.getItem();
                slot.set(getCoreInventory().moveFrom(stackInSlot));
            }
        }
        return ItemStack.EMPTY;
    }

    @Override
    public void clicked(int slotIndex, int button, ClickType actionType, Player player) {
        if (slotIndex >= 0 && slotIndex < slots.size()) {
            final Slot slot = slots.get(slotIndex);
            if (slot instanceof ResultSlot) {
                final ItemStack[] recipe = new ItemStack[9];
                for (int i = 0; i < 9; i++) {
                    recipe[i] = craftingGrid.getItem(i).copy();
                }

                super.clicked(slotIndex, button, actionType, player);
                tryToPopulateCraftingGrid(recipe, player);
                return;
            }
        }
        super.clicked(slotIndex, button, actionType, player);
    }

    private void tryToPopulateCraftingGrid(ItemStack[] recipe, Player player) {
        clearGrid(player);
        for (int i = 0; i < recipe.length; i++) {
            if (recipe[i].isEmpty() || recipe[i].getCount() > 1) continue;
            final int finalI = i;
            slots.stream().filter(
                slot -> slot.container == craftingGrid && slot.getContainerSlot() == finalI
            ).findFirst().ifPresent(slot -> {
                final ItemStack retrieved = getCoreInventory().extractStack(recipe[finalI]);
                if (!retrieved.isEmpty()) {
                    slot.set(retrieved);
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
    public void removed(Player player) {
        super.removed(player);
        clearGrid(player);
    }

    public void clearGrid(Player player) {
        for (int i = 0; i < 9; i++) {
            final ItemStack stack = craftingGrid.getItem(i);
            if (!stack.isEmpty()) {
                final ItemStack result = getCoreInventory().moveFrom(stack);
                craftingGrid.setItem(i, ItemStack.EMPTY);
                if (!result.isEmpty()) {
                    player.drop(result, false);
                }
            }
        }
    }
}

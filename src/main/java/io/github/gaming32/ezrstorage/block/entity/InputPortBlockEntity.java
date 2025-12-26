package io.github.gaming32.ezrstorage.block.entity;

import io.github.gaming32.ezrstorage.registry.EZRBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class InputPortBlockEntity extends RefBlockEntity implements WorldlyContainer {
    private ItemStack inventory = ItemStack.EMPTY;

    public InputPortBlockEntity(BlockPos pos, BlockState state) {
        super(EZRBlockEntities.INPUT_PORT, pos, state);
    }

    public InputPortBlockEntity(BlockEntityType<? extends BlockEntity> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public void tick() {
        getCoreBlockEntity().ifPresent(core -> {
            final ItemStack stack = inventory;
            if (!stack.isEmpty()) {
                inventory = core.getInventory().moveFrom(stack);
                setChanged();
            }
        });
    }

    @Override
    public int getContainerSize() {
        return 1;
    }

    @Override
    public boolean isEmpty() {
        return inventory.isEmpty();
    }

    @Override
    public @NotNull ItemStack getItem(int slot) {
        return slot == 0 ? inventory : ItemStack.EMPTY;
    }

    @Override
    public @NotNull ItemStack removeItem(int slot, int amount) {
        setChanged();
        return slot == 0 ? inventory.split(amount) : ItemStack.EMPTY;
    }

    @Override
    public @NotNull ItemStack removeItemNoUpdate(int slot) {
        if (slot != 0) {
            return ItemStack.EMPTY;
        }
        final ItemStack stack = inventory;
        inventory = ItemStack.EMPTY;
        setChanged();
        return stack;
    }

    @Override
    public void setItem(int slot, ItemStack stack) {
        if (slot != 0) return;
        inventory = stack;
        setChanged();
    }

    @Override
    public boolean stillValid(Player player) {
        return false;
    }

    @Override
    public void clearContent() {
        inventory = ItemStack.EMPTY;
        setChanged();
    }

    @Override
    public int @NotNull [] getSlotsForFace(Direction side) {
        return new int[] {0};
    }

    @Override
    public boolean canPlaceItemThroughFace(int slot, ItemStack stack, @Nullable Direction dir) {
        return level == null || !level.hasNeighborSignal(worldPosition);
    }

    @Override
    public boolean canTakeItemThroughFace(int slot, ItemStack stack, Direction dir) {
        return false;
    }

    @Override
    protected void saveAdditional(CompoundTag nbt) {
        super.saveAdditional(nbt);
        ContainerHelper.saveAllItems(nbt, NonNullList.of(ItemStack.EMPTY, inventory));
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        final NonNullList<ItemStack> list = NonNullList.withSize(1, ItemStack.EMPTY);
        ContainerHelper.loadAllItems(nbt, list);
        inventory = list.getFirst();
    }
}

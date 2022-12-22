package io.github.gaming32.ezrstorage.block.entity;

import io.github.gaming32.ezrstorage.registry.EZRBlockEntities;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.Nullable;

public class InputPortBlockEntity extends RefBlockEntity implements SidedInventory {
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
            }
        });
    }

    @Override
    public int size() {
        return 1;
    }

    @Override
    public boolean isEmpty() {
        return inventory.isEmpty();
    }

    @Override
    public ItemStack getStack(int slot) {
        return slot == 0 ? inventory : ItemStack.EMPTY;
    }

    @Override
    public ItemStack removeStack(int slot, int amount) {
        return slot == 0 ? inventory.split(amount) : ItemStack.EMPTY;
    }

    @Override
    public ItemStack removeStack(int slot) {
        if (slot != 0) {
            return ItemStack.EMPTY;
        }
        final ItemStack stack = inventory;
        inventory = ItemStack.EMPTY;
        return stack;
    }

    @Override
    public void setStack(int slot, ItemStack stack) {
        if (slot != 0) return;
        inventory = stack;
    }

    @Override
    public boolean canPlayerUse(PlayerEntity player) {
        return false;
    }

    @Override
    public void clear() {
        inventory = ItemStack.EMPTY;
    }

    @Override
    public int[] getAvailableSlots(Direction side) {
        return new int[] {0};
    }

    @Override
    public boolean canInsert(int slot, ItemStack stack, @Nullable Direction dir) {
        return world == null || !world.isReceivingRedstonePower(pos);
    }

    @Override
    public boolean canExtract(int slot, ItemStack stack, Direction dir) {
        return false;
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        Inventories.writeNbt(nbt, DefaultedList.copyOf(ItemStack.EMPTY, inventory));
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        final DefaultedList<ItemStack> list = DefaultedList.ofSize(1, ItemStack.EMPTY);
        Inventories.readNbt(nbt, list);
        inventory = list.get(0);
    }
}

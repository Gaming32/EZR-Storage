package io.github.gaming32.ezrstorage.block.entity;

import io.github.gaming32.ezrstorage.registry.ModBlockEntities;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;

public class InputPortBlockEntity extends RefBlockEntity implements Inventory {
    private final ItemStack[] INVENTORY = {ItemStack.EMPTY};

    public InputPortBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.INPUT_PORT, pos, state);
    }

    public InputPortBlockEntity(BlockEntityType<? extends BlockEntity> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public void tick() {
        getCoreBlockEntity().ifPresent(core -> {
            final ItemStack stack = INVENTORY[0];
            if (!stack.isEmpty()) {
                INVENTORY[0] = core.getInventory().moveFrom(stack);
            }
        });
    }

    @Override
    public int size() {
        return 1;
    }

    @Override
    public boolean isEmpty() {
        return INVENTORY[0].isEmpty();
    }

    @Override
    public ItemStack getStack(int slot) {
        return slot == 0 ? INVENTORY[0] : ItemStack.EMPTY;
    }

    @Override
    public ItemStack removeStack(int slot, int amount) {
        return slot == 0 ? INVENTORY[0].split(amount) : ItemStack.EMPTY;
    }

    @Override
    public ItemStack removeStack(int slot) {
        if (slot != 0) {
            return ItemStack.EMPTY;
        }
        final ItemStack stack = INVENTORY[0];
        INVENTORY[0] = ItemStack.EMPTY;
        return stack;
    }

    @Override
    public void setStack(int slot, ItemStack stack) {
        if (slot != 0) return;
        INVENTORY[0] = stack;
    }

    @Override
    public boolean canPlayerUse(PlayerEntity player) {
        return false;
    }

    @Override
    public void clear() {
        INVENTORY[0] = ItemStack.EMPTY;
    }
}

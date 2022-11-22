package io.github.stygigoth.ezrstorage.block.entity;

import io.github.stygigoth.ezrstorage.impl.ImplementedInventory;
import io.github.stygigoth.ezrstorage.registry.ModBlockEntities;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;


public class StorageBoxBlockEntity extends RefBlockEntity implements ImplementedInventory {
    public DefaultedList<ItemStack> inventory;

    public StorageBoxBlockEntity(int capacity, BlockPos pos, BlockState state) {
        super(ModBlockEntities.STORAGE_BOX_BLOCK_ENTITY, pos, state);
        inventory = DefaultedList.ofSize(capacity, ItemStack.EMPTY);
    }

    public StorageBoxBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.STORAGE_BOX_BLOCK_ENTITY, pos, state);
    }

    @Override
    public DefaultedList<ItemStack> getItems() {
        return inventory;
    }

    @Override
    public int getMaxCountPerStack() {
        return 1;
    }
}

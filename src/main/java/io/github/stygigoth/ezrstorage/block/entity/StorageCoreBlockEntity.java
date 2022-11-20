package io.github.stygigoth.ezrstorage.block.entity;

import io.github.stygigoth.ezrstorage.registry.ModBlockEntities;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;

import java.util.HashMap;
import java.util.Map;

public class StorageCoreBlockEntity extends BlockEntity {
    private final Map<BlockPos, DefaultedList<ItemStack>> items = new HashMap<>();

    public StorageCoreBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.STORAGE_CORE_BLOCK_ENTITY, pos, state);
    }
}

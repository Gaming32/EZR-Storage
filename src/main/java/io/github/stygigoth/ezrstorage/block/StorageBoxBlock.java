package io.github.stygigoth.ezrstorage.block;

import io.github.stygigoth.ezrstorage.block.entity.StorageBoxBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

public class StorageBoxBlock extends BoxBlock {
    public final int capacity;

    public StorageBoxBlock(int capacity, Settings settings) {
        super(settings);
        this.capacity = capacity;
    }

    @Override
    public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new StorageBoxBlockEntity(capacity, pos, state);
    }
}

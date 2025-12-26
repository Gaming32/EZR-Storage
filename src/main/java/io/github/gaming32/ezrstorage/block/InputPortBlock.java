package io.github.gaming32.ezrstorage.block;

import io.github.gaming32.ezrstorage.block.entity.InputPortBlockEntity;
import io.github.gaming32.ezrstorage.registry.EZRBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.Containers;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class InputPortBlock extends BoxBlock {
    public InputPortBlock(Properties settings) {
        super(settings);
    }

    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return new InputPortBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(
        Level level,
        @NotNull BlockState state,
        @NotNull BlockEntityType<T> type
    ) {
        return level.isClientSide ? null : (world1, pos, state1, blockEntity) -> {
            if (blockEntity instanceof InputPortBlockEntity inputPort) {
                inputPort.tick();
            }
        };
    }

    @Override
    public void onRemove(
        BlockState state,
        @NotNull Level level,
        @NotNull BlockPos pos,
        BlockState newState,
        boolean moved
    ) {
        if (!state.is(newState.getBlock())) {
            level.getBlockEntity(pos, EZRBlockEntities.INPUT_PORT)
                .ifPresent(entity -> Containers.dropContents(level, pos, entity));
            super.onRemove(state, level, pos, newState, moved);
        }
    }
}

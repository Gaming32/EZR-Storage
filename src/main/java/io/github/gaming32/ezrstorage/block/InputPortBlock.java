package io.github.gaming32.ezrstorage.block;

import io.github.gaming32.ezrstorage.block.entity.InputPortBlockEntity;
import io.github.gaming32.ezrstorage.registry.EZRBlockEntities;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.Containers;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class InputPortBlock extends BoxBlock {
    public InputPortBlock(Properties settings) {
        super(settings);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new InputPortBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState state, BlockEntityType<T> type) {
        return world.isClientSide ? null : (world1, pos, state1, blockEntity) -> {
            if (blockEntity instanceof InputPortBlockEntity inputPort) {
                inputPort.tick();
            }
        };
    }

    @Override
    public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean moved) {
        if (!state.is(newState.getBlock())) {
            world.getBlockEntity(pos, EZRBlockEntities.INPUT_PORT).ifPresent(entity -> Containers.dropContents(world, pos, entity));
            super.onRemove(state, world, pos, newState, moved);
        }
    }
}

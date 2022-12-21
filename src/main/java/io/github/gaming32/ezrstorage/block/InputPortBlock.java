package io.github.gaming32.ezrstorage.block;

import io.github.gaming32.ezrstorage.block.entity.InputPortBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class InputPortBlock extends BoxBlock {
    public InputPortBlock(Settings settings) {
        super(settings);
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new InputPortBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return world.isClient ? null : (world1, pos, state1, blockEntity) -> {
            if (blockEntity instanceof InputPortBlockEntity inputPort) {
                inputPort.tick();
            }
        };
    }
}

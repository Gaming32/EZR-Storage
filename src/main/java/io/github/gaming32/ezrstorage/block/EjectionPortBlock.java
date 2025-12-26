package io.github.gaming32.ezrstorage.block;

import io.github.gaming32.ezrstorage.block.entity.EjectionPortBlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class EjectionPortBlock extends BoxBlock {
    public EjectionPortBlock(Properties settings) {
        super(settings);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new EjectionPortBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState state, BlockEntityType<T> type) {
        return world.isClientSide ? null : (world1, pos, state1, blockEntity) -> {
            if (blockEntity instanceof EjectionPortBlockEntity ejectionPort) {
                ejectionPort.tick();
            }
        };
    }
}

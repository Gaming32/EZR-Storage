package io.github.gaming32.ezrstorage.block;

import io.github.gaming32.ezrstorage.block.entity.EjectionPortBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class EjectionPortBlock extends BoxBlock {
    public EjectionPortBlock(Properties settings) {
        super(settings);
    }

    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return new EjectionPortBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(
        Level level,
        @NotNull BlockState state,
        @NotNull BlockEntityType<T> type
    ) {
        return level.isClientSide ? null : (world1, pos, state1, blockEntity) -> {
            if (blockEntity instanceof EjectionPortBlockEntity ejectionPort) {
                ejectionPort.tick();
            }
        };
    }
}

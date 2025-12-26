package io.github.gaming32.ezrstorage.block;

import io.github.gaming32.ezrstorage.block.entity.ExtractionPortBlockEntity;
import io.github.gaming32.ezrstorage.registry.EZRBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ExtractionPortBlock extends BoxBlock {
    public ExtractionPortBlock(Properties settings) {
        super(settings);
    }

    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return new ExtractionPortBlockEntity(pos, state);
    }

    @Override
    @SuppressWarnings("deprecation")
    public @NotNull InteractionResult use(
        @NotNull BlockState state,
        Level level,
        @NotNull BlockPos pos,
        @NotNull Player player,
        @NotNull InteractionHand hand,
        @NotNull BlockHitResult hit
    ) {
        if (!level.isClientSide) {
            final MenuProvider factory = state.getMenuProvider(level, pos);
            if (factory != null) {
                player.openMenu(factory);
            }
        }
        return InteractionResult.SUCCESS;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(
        Level level,
        @NotNull BlockState state,
        @NotNull BlockEntityType<T> type
    ) {
        return level.isClientSide ? null : (world1, pos, state1, blockEntity) -> {
            if (blockEntity instanceof ExtractionPortBlockEntity extractionPort) {
                extractionPort.tick();
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
            level.getBlockEntity(pos, EZRBlockEntities.EXTRACTION_PORT).ifPresent(entity -> {
                Containers.dropContents(level, pos, entity);
                Containers.dropContents(level, pos, entity.getExtractList());
            });
            super.onRemove(state, level, pos, newState, moved);
        }
    }
}

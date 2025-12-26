package io.github.gaming32.ezrstorage.block;

import io.github.gaming32.ezrstorage.block.entity.ExtractionPortBlockEntity;
import io.github.gaming32.ezrstorage.registry.EZRBlockEntities;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.Containers;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class ExtractionPortBlock extends BoxBlock {
    public ExtractionPortBlock(Properties settings) {
        super(settings);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new ExtractionPortBlockEntity(pos, state);
    }

    @Override
    @SuppressWarnings("deprecation")
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (!world.isClientSide) {
            final MenuProvider factory = state.getMenuProvider(world, pos);
            if (factory != null) {
                player.openMenu(factory);
            }
        }
        return InteractionResult.SUCCESS;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState state, BlockEntityType<T> type) {
        return world.isClientSide ? null : (world1, pos, state1, blockEntity) -> {
            if (blockEntity instanceof ExtractionPortBlockEntity extractionPort) {
                extractionPort.tick();
            }
        };
    }

    @Override
    public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean moved) {
        if (!state.is(newState.getBlock())) {
            world.getBlockEntity(pos, EZRBlockEntities.EXTRACTION_PORT).ifPresent(entity -> {
                Containers.dropContents(world, pos, entity);
                Containers.dropContents(world, pos, entity.getExtractList());
            });
            super.onRemove(state, world, pos, newState, moved);
        }
    }
}

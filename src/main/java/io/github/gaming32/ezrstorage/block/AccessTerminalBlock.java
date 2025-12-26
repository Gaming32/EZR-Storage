package io.github.gaming32.ezrstorage.block;

import io.github.gaming32.ezrstorage.block.entity.AccessTerminalBlockEntity;
import io.github.gaming32.ezrstorage.registry.EZRBlockEntities;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

public class AccessTerminalBlock extends BoxBlock {
    public AccessTerminalBlock(Properties settings) {
        super(settings);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new AccessTerminalBlockEntity(pos, state);
    }

    @Override
    @SuppressWarnings("deprecation")
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (!world.isClientSide) {
            final AccessTerminalBlockEntity blockEntity =
                world.getBlockEntity(pos, EZRBlockEntities.ACCESS_TERMINAL).orElse(null);
            if (blockEntity == null || blockEntity.getCore() == null) return InteractionResult.PASS;
            final MenuProvider factory = state.getMenuProvider(world, pos);
            if (factory != null) {
                player.openMenu(factory);
            }
        }
        return InteractionResult.SUCCESS;
    }
}

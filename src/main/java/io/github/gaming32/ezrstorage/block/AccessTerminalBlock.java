package io.github.gaming32.ezrstorage.block;

import io.github.gaming32.ezrstorage.block.entity.AccessTerminalBlockEntity;
import io.github.gaming32.ezrstorage.registry.EZRBlockEntities;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class AccessTerminalBlock extends BoxBlock {
    public AccessTerminalBlock(Settings settings) {
        super(settings);
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new AccessTerminalBlockEntity(pos, state);
    }

    @Override
    @SuppressWarnings("deprecation")
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (!world.isClient) {
            final AccessTerminalBlockEntity blockEntity =
                world.getBlockEntity(pos, EZRBlockEntities.ACCESS_TERMINAL).orElse(null);
            if (blockEntity == null || blockEntity.getCore() == null) return ActionResult.PASS;
            final NamedScreenHandlerFactory factory = state.createScreenHandlerFactory(world, pos);
            if (factory != null) {
                player.openHandledScreen(factory);
            }
        }
        return ActionResult.SUCCESS;
    }
}

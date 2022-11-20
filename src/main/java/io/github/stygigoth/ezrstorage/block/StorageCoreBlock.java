package io.github.stygigoth.ezrstorage.block;

import io.github.stygigoth.ezrstorage.block.entity.StorageCoreBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class StorageCoreBlock extends BlockWithEntity {
    public StorageCoreBlock(Settings settings) {
        super(settings.resistance(6000f));
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new StorageCoreBlockEntity(pos, state);
    }

    @Override
    public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        ((StorageCoreBlockEntity)world.getBlockEntity(pos)).notifyBreak();
        super.onBreak(world, pos, state, player);
    }
}

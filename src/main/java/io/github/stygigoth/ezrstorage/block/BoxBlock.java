package io.github.stygigoth.ezrstorage.block;

import io.github.stygigoth.ezrstorage.block.entity.RefBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class BoxBlock extends BlockWithEntity {
    public BoxBlock(Settings settings) {
        super(settings);
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new RefBlockEntity(pos, state);
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        super.onPlaced(world, pos, state, placer, itemStack);
        BlockEntity be = world.getBlockEntity(pos);
        if (be == null) {return;}
        ((RefBlockEntity) be).attemptFindNetwork(world);
    }

    @Override
    public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        ((RefBlockEntity) Objects.requireNonNull(world.getBlockEntity(pos))).notifyBreak();
        super.onBreak(world, pos, state, player);
    }
}

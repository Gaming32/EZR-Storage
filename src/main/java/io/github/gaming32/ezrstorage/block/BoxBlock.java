package io.github.gaming32.ezrstorage.block;

import io.github.gaming32.ezrstorage.block.entity.RefBlockEntity;
import io.github.gaming32.ezrstorage.block.entity.StorageCoreBlockEntity;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Set;

public class BoxBlock extends BlockWithEntity {
    public BoxBlock(Settings settings) {
        super(settings);
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new RefBlockEntity(pos, state);
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        super.onPlaced(world, pos, state, placer, itemStack);
        final Set<StorageCoreBlockEntity> cores = findCores(world, pos);
        if (!cores.isEmpty()) {
            cores.iterator().next().scan(world);
        }
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        return findCores(world, pos).size() < 2;
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    private static Set<StorageCoreBlockEntity> findCores(WorldView world, BlockPos pos) {
        final Set<StorageCoreBlockEntity> cores = new HashSet<>(1);
        for (Direction d : Direction.values()) {
            BlockEntity neighbor = world.getBlockEntity(pos.offset(d));
            if (neighbor instanceof StorageCoreBlockEntity otherCore) {
                cores.add(otherCore);
            } else if (neighbor instanceof RefBlockEntity ref) {
                ref.getCoreBlockEntity().ifPresent(cores::add);
            }
        }
        return cores;
    }
}

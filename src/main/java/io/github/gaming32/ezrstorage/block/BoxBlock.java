package io.github.gaming32.ezrstorage.block;

import io.github.gaming32.ezrstorage.block.entity.RefBlockEntity;
import io.github.gaming32.ezrstorage.block.entity.StorageCoreBlockEntity;
import java.util.HashSet;
import java.util.Set;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BoxBlock extends BaseEntityBlock {
    public BoxBlock(Properties settings) {
        super(settings);
    }

    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return new RefBlockEntity(pos, state);
    }

    @Override
    public void setPlacedBy(
        @NotNull Level level,
        @NotNull BlockPos pos,
        @NotNull BlockState state,
        @Nullable LivingEntity placer,
        @NotNull ItemStack itemStack
    ) {
        super.setPlacedBy(level, pos, state, placer, itemStack);
        final Set<StorageCoreBlockEntity> cores = findCores(level, pos);
        if (!cores.isEmpty()) {
            cores.iterator().next().scan(level, null);
        }
    }

    @Override
    @SuppressWarnings("deprecation")
    public void onRemove(
        BlockState state,
        @NotNull Level level,
        @NotNull BlockPos pos,
        BlockState newState,
        boolean moved
    ) {
        if (!state.is(newState.getBlock())) {
            final BlockEntity entity = level.getBlockEntity(pos);
            if (entity instanceof RefBlockEntity ref) {
                ref.getCoreBlockEntity().ifPresent(core -> core.scan(level, entity));
            }
            super.onRemove(state, level, pos, newState, moved);
        }
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean canSurvive(@NotNull BlockState state, @NotNull LevelReader world, @NotNull BlockPos pos) {
        return findCores(world, pos).size() < 2;
    }

    @Override
    public @NotNull RenderShape getRenderShape(@NotNull BlockState state) {
        return RenderShape.MODEL;
    }

    private static Set<StorageCoreBlockEntity> findCores(LevelReader world, BlockPos pos) {
        final Set<StorageCoreBlockEntity> cores = new HashSet<>(1);
        for (Direction d : Direction.values()) {
            BlockEntity neighbor = world.getBlockEntity(pos.relative(d));
            if (neighbor instanceof StorageCoreBlockEntity otherCore) {
                cores.add(otherCore);
            } else if (neighbor instanceof RefBlockEntity ref) {
                ref.getCoreBlockEntity().ifPresent(cores::add);
            }
        }
        return cores;
    }
}

package io.github.gaming32.ezrstorage.block;

import io.github.gaming32.ezrstorage.block.entity.RefBlockEntity;
import io.github.gaming32.ezrstorage.block.entity.StorageCoreBlockEntity;
import io.github.gaming32.ezrstorage.registry.EZRBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class StorageCoreBlock extends BaseEntityBlock {
    public StorageCoreBlock(Properties settings) {
        super(settings.explosionResistance(6000f));
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return new StorageCoreBlockEntity(pos, state);
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean canSurvive(@NotNull BlockState state, @NotNull LevelReader world, @NotNull BlockPos pos) {
        for (final Direction direction : Direction.values()) {
            final BlockEntity entity = world.getBlockEntity(pos.relative(direction));
            if (
                entity instanceof StorageCoreBlockEntity ||
                (entity instanceof RefBlockEntity ref && ref.getCore() != null)
            ) return false;
        }
        return true;
    }

    @Override
    public void playerWillDestroy(
        Level level,
        @NotNull BlockPos pos,
        @NotNull BlockState state,
        @NotNull Player player
    ) {
        level.getBlockEntity(pos, EZRBlockEntities.STORAGE_CORE)
            .ifPresent(StorageCoreBlockEntity::notifyBreak);
        super.playerWillDestroy(level, pos, state, player);
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
        level.getBlockEntity(pos, EZRBlockEntities.STORAGE_CORE)
            .ifPresent(entity -> entity.scan(level, null));
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

    @Override
    public @NotNull RenderShape getRenderShape(@NotNull BlockState state) {
        return RenderShape.MODEL;
    }
}

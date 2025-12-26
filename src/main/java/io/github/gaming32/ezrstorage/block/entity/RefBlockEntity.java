package io.github.gaming32.ezrstorage.block.entity;

import io.github.gaming32.ezrstorage.registry.EZRBlockEntities;
import io.github.gaming32.ezrstorage.util.NbtUtil;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.LevelAccessor;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Optional;

public class RefBlockEntity extends BlockEntity {
    private BlockPos core;

    public RefBlockEntity(BlockPos pos, BlockState state) {
        super(EZRBlockEntities.BOX, pos, state);
    }

    public RefBlockEntity(BlockEntityType<? extends BlockEntity> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    void recurse(LevelAccessor world, StorageCoreBlockEntity coreEntity, @Nullable BlockEntity skipEntity) {
        final Deque<BlockPos> queue = new ArrayDeque<>();
        queue.addLast(worldPosition);
        while (!queue.isEmpty()) {
            final BlockPos checkPos = queue.removeFirst();
            for (Direction d : Direction.values()) {
                final BlockPos offsetPos = checkPos.relative(d);
                BlockEntity be = world.getBlockEntity(offsetPos);
                if (be != skipEntity && be instanceof RefBlockEntity ref && !coreEntity.networkContains(offsetPos)) {
                    coreEntity.addToNetwork(offsetPos, ref.getBlockState());
                    ref.core = core;
                    queue.addLast(offsetPos);
                }
            }
        }
    }

    public BlockPos getCore() {
        return core;
    }

    public Optional<StorageCoreBlockEntity> getCoreBlockEntity() {
        return core == null || level == null ? Optional.empty() : level.getBlockEntity(core, EZRBlockEntities.STORAGE_CORE);
    }

    public void setCore(BlockPos core) {
        this.core = core;
        setChanged();
    }

    @Override
    protected void saveAdditional(CompoundTag nbt) {
        if (core != null) {
            nbt.put("Core", NbtUtil.blockPosToNbt(core));
        }
    }

    @Override
    public void load(CompoundTag nbt) {
        core = NbtUtil.getBlockPos(nbt, "Core");
    }
}

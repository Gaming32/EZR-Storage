package io.github.gaming32.ezrstorage.block.entity;

import io.github.gaming32.ezrstorage.registry.ModBlockEntities;
import io.github.gaming32.ezrstorage.util.NbtUtil;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.WorldAccess;

import java.util.ArrayDeque;
import java.util.Deque;

public class RefBlockEntity extends BlockEntity {
    private BlockPos core;

    public RefBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.REF_BLOCK_ENTITY, pos, state);
    }

    public RefBlockEntity(BlockEntityType<? extends BlockEntity> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    void recurse(WorldAccess world, StorageCoreBlockEntity coreEntity) {
        final Deque<BlockPos> queue = new ArrayDeque<>();
        queue.addLast(pos);
        while (!queue.isEmpty()) {
            final BlockPos checkPos = queue.removeFirst();
            for (Direction d : Direction.values()) {
                final BlockPos offsetPos = checkPos.offset(d);
                BlockEntity be = world.getBlockEntity(offsetPos);
                if (be instanceof RefBlockEntity ref && !coreEntity.networkContains(offsetPos)) {
                    coreEntity.addToNetwork(offsetPos, ref.getCachedState());
                    ref.core = core;
                    queue.addLast(offsetPos);
                }
            }
        }
    }

    public BlockPos getCore() {
        return core;
    }

    public void setCore(BlockPos core) {
        this.core = core;
        markDirty();
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        if (core != null) {
            nbt.put("Core", NbtUtil.blockPosToNbt(core));
        }
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        core = NbtUtil.getBlockPos(nbt, "Core");
    }
}

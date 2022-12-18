package io.github.stygigoth.ezrstorage.block.entity;

import io.github.stygigoth.ezrstorage.NbtUtil;
import io.github.stygigoth.ezrstorage.registry.ModBlockEntities;
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
    StorageCoreBlockEntity core;

    public RefBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.REF_BLOCK_ENTITY, pos, state);
    }

    public RefBlockEntity(BlockEntityType<? extends BlockEntity> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    void recurse(WorldAccess world) {
        final Deque<BlockPos> queue = new ArrayDeque<>();
        queue.addLast(pos);
        while (!queue.isEmpty()) {
            final BlockPos checkPos = queue.removeFirst();
            for (Direction d : Direction.values()) {
                final BlockPos offsetPos = checkPos.offset(d);
                BlockEntity be = world.getBlockEntity(offsetPos);
                if (be instanceof RefBlockEntity ref && !core.networkContains(offsetPos)) {
                    core.addToNetwork(offsetPos);
                    ref.core = core;
                    queue.addLast(offsetPos);
                }
            }
        }
    }

    public StorageCoreBlockEntity getCore() {
        return core;
    }

    @Override
    public void markRemoved() {
        super.markRemoved();
        if (core != null) {
            core.scan(world);
        }
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        if (core != null) {
            nbt.put("Core", NbtUtil.blockPosToNbt(core.getPos()));
        }
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        final BlockPos corePos = NbtUtil.getBlockPos(nbt, "Core");
        if (corePos != null && world != null) {
            core = world.getBlockEntity(corePos, ModBlockEntities.STORAGE_CORE_BLOCK_ENTITY).orElse(null);
        }
    }
}

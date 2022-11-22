package io.github.stygigoth.ezrstorage.block.entity;

import io.github.stygigoth.ezrstorage.registry.ModBlockEntities;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
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

    public void attemptFindNetwork(WorldAccess world) {
        for (Direction d : Direction.values()) {
            BlockEntity neighbor = world.getBlockEntity(pos.offset(d));
            if (neighbor instanceof StorageCoreBlockEntity) {
                core = (StorageCoreBlockEntity) neighbor;
                break;
            } else if (neighbor instanceof RefBlockEntity && ((RefBlockEntity) neighbor).core != null) {
                core = ((RefBlockEntity) neighbor).core;
            }
        }
        if (core != null)
            core.scan(world);
    }

    void recurse(WorldAccess world) {
        final Deque<BlockPos> queue = new ArrayDeque<>();
        queue.addLast(pos);
        while (!queue.isEmpty()) {
            final BlockPos checkPos = queue.removeFirst();
            for (Direction d : Direction.values()) {
                final BlockPos offsetPos = checkPos.offset(d);
                BlockEntity be = world.getBlockEntity(offsetPos);
                if (be instanceof RefBlockEntity && ((RefBlockEntity) be).core != null && !core.networkContains(offsetPos)) {
                    core.addToNetwork(offsetPos, (RefBlockEntity) be);
                    queue.addLast(offsetPos);
                }
            }
        }
    }

    public void notifyBreak() {
        if (core != null)
            core.scan(world);
    }
}

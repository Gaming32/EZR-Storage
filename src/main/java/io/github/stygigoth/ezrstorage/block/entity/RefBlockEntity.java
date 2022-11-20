package io.github.stygigoth.ezrstorage.block.entity;

import io.github.stygigoth.ezrstorage.registry.ModBlockEntities;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.WorldAccess;

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
        for (Direction d : Direction.values()) {
            BlockEntity be = world.getBlockEntity(pos.offset(d));
            if (be instanceof RefBlockEntity && ((RefBlockEntity) be).core != null && !core.networkContains(pos.offset(d))) {
                core.addToNetwork(pos.offset(d), (RefBlockEntity) be);
                ((RefBlockEntity) be).recurse(world);
            }
        }
    }

    public void notifyBreak() {
        if (core != null)
            core.scan(world);
    }
}

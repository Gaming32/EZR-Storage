package io.github.stygigoth.ezrstorage.block.entity;

import io.github.stygigoth.ezrstorage.InfiniteInventory;
import io.github.stygigoth.ezrstorage.block.StorageBoxBlock;
import io.github.stygigoth.ezrstorage.registry.ModBlockEntities;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.WorldAccess;

import java.util.HashMap;
import java.util.Map;

public class StorageCoreBlockEntity extends BlockEntity {
    private final Map<BlockPos, RefBlockEntity> network = new HashMap<>();
    private final InfiniteInventory inventory = new InfiniteInventory();

    public StorageCoreBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.STORAGE_CORE_BLOCK_ENTITY, pos, state);
        if (world != null) {
            scan(world);
        }
    }

    public void scan(WorldAccess world) {
        scanBlocks(world);
        inventory.setMaxCount(0L);
        for (final BlockPos otherPos : network.keySet()) {
            final BlockState otherState = world.getBlockState(otherPos);
            if (otherState.getBlock() instanceof StorageBoxBlock storageBox) {
                inventory.setMaxCount(inventory.getMaxCount() + storageBox.capacity);
            }
        }
    }

    private void scanBlocks(WorldAccess world) {
        for (RefBlockEntity be : network.values()) {
            be.core = null;
        }
        startRecursion(world);
    }

    private void startRecursion(WorldAccess world) {
        network.clear();
        for (Direction d : Direction.values()) {
            BlockEntity be = world.getBlockEntity(pos.offset(d));
            if (be instanceof RefBlockEntity) {
                addToNetwork(pos.offset(d), (RefBlockEntity) be);
                ((RefBlockEntity) be).core = this;
                ((RefBlockEntity) be).recurse(world);
            }
        }
    }

    void addToNetwork(BlockPos pos, RefBlockEntity be) {
        network.put(pos, be);
    }

    public boolean networkContains(BlockPos pos) {
        return network.containsKey(pos);
    }

    public void notifyBreak() {
        for (RefBlockEntity be : network.values()) {
            be.core = null;
        }
    }

    public InfiniteInventory getInventory() {
        return inventory;
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        nbt.put("Inventory", inventory.writeNbt());
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        inventory.readNbt(nbt.getCompound("Inventory"));
    }
}

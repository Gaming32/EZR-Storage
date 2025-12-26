package io.github.gaming32.ezrstorage.block;

import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

public class StorageBoxBlock extends BoxBlock {
    public final int capacity;

    public StorageBoxBlock(Properties settings, int capacity) {
        super(settings);
        this.capacity = capacity;
    }
}

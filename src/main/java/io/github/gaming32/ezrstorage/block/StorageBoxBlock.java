package io.github.gaming32.ezrstorage.block;

public class StorageBoxBlock extends BoxBlock {
    public final int capacity;

    public StorageBoxBlock(Settings settings, int capacity) {
        super(settings);
        this.capacity = capacity;
    }
}

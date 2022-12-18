package io.github.stygigoth.ezrstorage.block;

public class StorageBoxBlock extends BoxBlock {
    public final int capacity;

    public StorageBoxBlock(int capacity, Settings settings) {
        super(settings);
        this.capacity = capacity;
    }
}

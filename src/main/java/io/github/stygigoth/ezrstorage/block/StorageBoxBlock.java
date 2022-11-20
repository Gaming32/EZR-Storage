package io.github.stygigoth.ezrstorage.block;

public class StorageBoxBlock extends BoxBlock {
    public final long capacity;

    public StorageBoxBlock(long capacity, Settings settings) {
        super(settings);
        this.capacity = capacity;
    }
}

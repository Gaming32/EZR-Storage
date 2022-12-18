package io.github.stygigoth.ezrstorage.block;

public class ModificationBoxBlock extends BoxBlock {
    public final ModificationBoxBlockType type;

    public ModificationBoxBlock(ModificationBoxBlockType type, Settings settings) {
        super(settings);
        this.type = type;
    }

    public enum ModificationBoxBlockType {
        CRAFTING,
        SEARCH,
        SORTING,
        SECURITY
    }
}

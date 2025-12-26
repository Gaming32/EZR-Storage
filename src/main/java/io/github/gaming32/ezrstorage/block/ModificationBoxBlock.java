package io.github.gaming32.ezrstorage.block;

import net.minecraft.util.StringRepresentable;

public class ModificationBoxBlock extends BoxBlock {
    public final Type type;

    public ModificationBoxBlock(Properties settings, Type type) {
        super(settings);
        this.type = type;
    }

    public enum Type implements StringRepresentable {
        CRAFTING("crafting"),
        SEARCH("search"),
        SORTING("sorting"),
        SECURITY("security");

        public final String id;

        Type(String id) {
            this.id = id;
        }

        @Override
        public String getSerializedName() {
            return id;
        }
    }
}

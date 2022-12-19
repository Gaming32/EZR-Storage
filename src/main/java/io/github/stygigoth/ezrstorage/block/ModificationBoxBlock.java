package io.github.stygigoth.ezrstorage.block;

import net.minecraft.util.StringIdentifiable;

public class ModificationBoxBlock extends BoxBlock {
    public final Type type;

    public ModificationBoxBlock(Type type, Settings settings) {
        super(settings);
        this.type = type;
    }

    public enum Type implements StringIdentifiable {
        CRAFTING("crafting"),
        SEARCH("search"),
        SORTING("sorting"),
        SECURITY("security");

        public final String id;

        Type(String id) {
            this.id = id;
        }

        @Override
        public String asString() {
            return id;
        }
    }
}

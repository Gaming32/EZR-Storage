package io.github.stygigoth.ezrstorage.registry;

import io.github.stygigoth.ezrstorage.Main;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import net.minecraft.util.registry.Registry;

public class EZRReg {
    private static Identifier id(String name) {
        return new Identifier(Main.MOD_ID, name);
    }

    public static void registerMod() {
        ModBlocks.registerBlocks();
    }

    public static <T extends Item> Item registerItem(T item, String name) {
        return Registry.register(Registry.ITEM, id(name), item);
    }

    public static <T extends Block> Block registerBlock(T block, String name) {
        return Registry.register(Registry.BLOCK, id(name), block);
    }

    public static <T extends Block> Pair<Block, Item> registerBlock(T block, String name, Item.Settings blockItemSettings) {
        return new Pair<>(registerBlock(block, name), registerItem(new BlockItem(block, blockItemSettings), name));
    }
}

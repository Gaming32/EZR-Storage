package io.github.stygigoth.ezrstorage.registry;

import io.github.stygigoth.ezrstorage.EZRStorage;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import net.minecraft.util.registry.Registry;

public class EZRReg {
    private static Identifier id(String name) {
        return new Identifier(EZRStorage.MOD_ID, name);
    }

    public static void registerMod() {
        ModBlockEntities.registerBlockEntities();
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

    public static <T extends BlockEntity> BlockEntityType<T> registerBlockEntityType(BlockEntityType<T> type, String name) {
        return Registry.register(Registry.BLOCK_ENTITY_TYPE, id(name), type);
    }
}

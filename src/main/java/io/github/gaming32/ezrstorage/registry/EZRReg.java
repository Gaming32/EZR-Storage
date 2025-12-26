package io.github.gaming32.ezrstorage.registry;

import io.github.gaming32.ezrstorage.EZRStorage;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Tuple;
import net.minecraft.core.Registry;

public class EZRReg {
    public static ResourceLocation id(String name) {
        return new ResourceLocation(EZRStorage.MOD_ID, name);
    }

    public static void registerMod() {
        EZRBlockEntities.registerBlockEntities();
        EZRBlocks.registerBlocks();
    }

    public static <T extends Item> Item registerItem(T item, String name) {
        return Registry.register(Registry.ITEM, id(name), item);
    }

    public static <T extends Block> Block registerBlock(T block, String name) {
        return Registry.register(Registry.BLOCK, id(name), block);
    }

    public static <T extends Block> Tuple<Block, Item> registerBlock(T block, String name, Item.Properties blockItemSettings) {
        return new Tuple<>(registerBlock(block, name), registerItem(new BlockItem(block, blockItemSettings), name));
    }

    public static <T extends BlockEntity> BlockEntityType<T> registerBlockEntityType(BlockEntityType<T> type, String name) {
        return Registry.register(Registry.BLOCK_ENTITY_TYPE, id(name), type);
    }
}

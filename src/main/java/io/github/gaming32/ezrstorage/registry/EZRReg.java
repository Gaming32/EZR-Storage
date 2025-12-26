package io.github.gaming32.ezrstorage.registry;

import io.github.gaming32.ezrstorage.EZRStorage;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class EZRReg {
    public static ResourceLocation id(String name) {
        return new ResourceLocation(EZRStorage.MOD_ID, name);
    }

    public static void registerMod() {
        EZRBlockEntities.registerBlockEntities();
        EZRBlocks.registerBlocks();
    }

    public static <T extends Item> T registerItem(T item, String name) {
        return Registry.register(BuiltInRegistries.ITEM, id(name), item);
    }

    public static <T extends Block> T registerBlockAndItem(
        T block,
        String name
    ) {
        Registry.register(BuiltInRegistries.BLOCK, id(name), block);
        registerItem(new BlockItem(block, new Item.Properties()), name);
        return block;
    }

    public static <T extends BlockEntity> BlockEntityType<T> registerBlockEntityType(
        BlockEntityType<T> type,
        String name
    ) {
        return Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, id(name), type);
    }
}

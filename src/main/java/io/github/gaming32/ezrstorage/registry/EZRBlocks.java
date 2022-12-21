package io.github.gaming32.ezrstorage.registry;

import io.github.gaming32.ezrstorage.EZRStorage;
import io.github.gaming32.ezrstorage.block.*;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.item.Item;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Pair;

public class EZRBlocks {
    private static final FabricBlockSettings METAL = FabricBlockSettings.of(Material.METAL)
        .hardness(2f)
        .requiresTool()
        .sounds(BlockSoundGroup.METAL);
    private static final FabricBlockSettings WOOD = FabricBlockSettings.of(Material.WOOD)
        .hardness(2f)
        .sounds(BlockSoundGroup.WOOD);
    private static final FabricItemSettings ITEM_SETTINGS = new FabricItemSettings().group(EZRStorage.EZR_GROUP);

    public static final Pair<Block, Item> STORAGE_CORE = EZRReg.registerBlock(
        new StorageCoreBlock(FabricBlockSettings.copyOf(METAL).resistance(6080f)), "storage_core", ITEM_SETTINGS
    );
    public static final Pair<Block, Item> ACCESS_TERMINAL = EZRReg.registerBlock(
        new AccessTerminalBlock(METAL), "access_terminal", ITEM_SETTINGS
    );

    public static final Pair<Block, Item> BLANK_BOX = EZRReg.registerBlock(
        new BoxBlock(WOOD), "blank_box", ITEM_SETTINGS
    );

    public static final Pair<Block, Item> STORAGE_BOX = EZRReg.registerBlock(
        new StorageBoxBlock(WOOD, 400), "storage_box", ITEM_SETTINGS
    );
    public static final Pair<Block, Item> CONDENSED_STORAGE_BOX = EZRReg.registerBlock(
        new StorageBoxBlock(METAL, 4_000), "condensed_storage_box", ITEM_SETTINGS
    );
    public static final Pair<Block, Item> SUPER_STORAGE_BOX = EZRReg.registerBlock(
        new StorageBoxBlock(METAL, 20_000), "super_storage_box", ITEM_SETTINGS
    );
    public static final Pair<Block, Item> ULTRA_STORAGE_BOX = EZRReg.registerBlock(
        new StorageBoxBlock(METAL, 80_000), "ultra_storage_box", ITEM_SETTINGS
    );
    public static final Pair<Block, Item> HYPER_STORAGE_BOX = EZRReg.registerBlock(
        new StorageBoxBlock(METAL, 400_000), "hyper_storage_box", ITEM_SETTINGS
    );

    public static final Pair<Block, Item> CRAFTING_BOX = EZRReg.registerBlock(
        new ModificationBoxBlock(METAL, ModificationBoxBlock.Type.CRAFTING),
        "crafting_box",
        ITEM_SETTINGS
    );
    public static final Pair<Block, Item> SEARCH_BOX = EZRReg.registerBlock(
        new ModificationBoxBlock(METAL, ModificationBoxBlock.Type.SEARCH), "search_box", ITEM_SETTINGS
    );
    public static final Pair<Block, Item> SORTING_BOX = EZRReg.registerBlock(
        new ModificationBoxBlock(WOOD, ModificationBoxBlock.Type.SORTING), "sorting_box", ITEM_SETTINGS
    );
    public static final Pair<Block, Item> SECURITY_BOX = EZRReg.registerBlock(
        new ModificationBoxBlock(METAL, ModificationBoxBlock.Type.SECURITY), "security_box", ITEM_SETTINGS
    );

    public static final Pair<Block, Item> INPUT_PORT = EZRReg.registerBlock(
        new InputPortBlock(METAL), "input_port", ITEM_SETTINGS
    );
    public static final Pair<Block, Item> EJECTION_PORT = EZRReg.registerBlock(
        new EjectionPortBlock(METAL), "ejection_port", ITEM_SETTINGS
    );

    public static void registerBlocks() {
    }
}

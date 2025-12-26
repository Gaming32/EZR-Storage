package io.github.gaming32.ezrstorage.registry;

import io.github.gaming32.ezrstorage.EZRStorage;
import io.github.gaming32.ezrstorage.block.AccessTerminalBlock;
import io.github.gaming32.ezrstorage.block.BoxBlock;
import io.github.gaming32.ezrstorage.block.EjectionPortBlock;
import io.github.gaming32.ezrstorage.block.ExtractionPortBlock;
import io.github.gaming32.ezrstorage.block.InputPortBlock;
import io.github.gaming32.ezrstorage.block.ModificationBoxBlock;
import io.github.gaming32.ezrstorage.block.StorageBoxBlock;
import io.github.gaming32.ezrstorage.block.StorageCoreBlock;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.util.Tuple;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;

public class EZRBlocks {
    private static final BlockBehaviour.Properties METAL = BlockBehaviour.Properties.of(Material.METAL)
        .destroyTime(2f)
        .requiresCorrectToolForDrops()
        .sound(SoundType.METAL);
    private static final BlockBehaviour.Properties WOOD = BlockBehaviour.Properties.of(Material.WOOD)
        .destroyTime(2f)
        .sound(SoundType.WOOD);
    private static final Item.Properties ITEM_SETTINGS = new Item.Properties().tab(EZRStorage.EZR_GROUP);

    public static final Tuple<Block, Item> STORAGE_CORE = EZRReg.registerBlock(
        new StorageCoreBlock(FabricBlockSettings.copyOf(METAL).explosionResistance(6080f)),
        "storage_core",
        ITEM_SETTINGS
    );
    public static final Tuple<Block, Item> ACCESS_TERMINAL = EZRReg.registerBlock(
        new AccessTerminalBlock(METAL), "access_terminal", ITEM_SETTINGS
    );

    public static final Tuple<Block, Item> BLANK_BOX = EZRReg.registerBlock(
        new BoxBlock(WOOD), "blank_box", ITEM_SETTINGS
    );

    public static final Tuple<Block, Item> STORAGE_BOX = EZRReg.registerBlock(
        new StorageBoxBlock(WOOD, 400), "storage_box", ITEM_SETTINGS
    );
    public static final Tuple<Block, Item> CONDENSED_STORAGE_BOX = EZRReg.registerBlock(
        new StorageBoxBlock(METAL, 4_000), "condensed_storage_box", ITEM_SETTINGS
    );
    public static final Tuple<Block, Item> SUPER_STORAGE_BOX = EZRReg.registerBlock(
        new StorageBoxBlock(METAL, 20_000), "super_storage_box", ITEM_SETTINGS
    );
    public static final Tuple<Block, Item> ULTRA_STORAGE_BOX = EZRReg.registerBlock(
        new StorageBoxBlock(METAL, 80_000), "ultra_storage_box", ITEM_SETTINGS
    );
    public static final Tuple<Block, Item> HYPER_STORAGE_BOX = EZRReg.registerBlock(
        new StorageBoxBlock(METAL, 400_000), "hyper_storage_box", ITEM_SETTINGS
    );

    public static final Tuple<Block, Item> CRAFTING_BOX = EZRReg.registerBlock(
        new ModificationBoxBlock(METAL, ModificationBoxBlock.Type.CRAFTING),
        "crafting_box",
        ITEM_SETTINGS
    );
    public static final Tuple<Block, Item> SEARCH_BOX = EZRReg.registerBlock(
        new ModificationBoxBlock(METAL, ModificationBoxBlock.Type.SEARCH), "search_box", ITEM_SETTINGS
    );
    public static final Tuple<Block, Item> SORTING_BOX = EZRReg.registerBlock(
        new ModificationBoxBlock(WOOD, ModificationBoxBlock.Type.SORTING), "sorting_box", ITEM_SETTINGS
    );
    public static final Tuple<Block, Item> SECURITY_BOX = EZRReg.registerBlock(
        new ModificationBoxBlock(METAL, ModificationBoxBlock.Type.SECURITY), "security_box", ITEM_SETTINGS
    );

    public static final Tuple<Block, Item> INPUT_PORT = EZRReg.registerBlock(
        new InputPortBlock(METAL), "input_port", ITEM_SETTINGS
    );
    public static final Tuple<Block, Item> EJECTION_PORT = EZRReg.registerBlock(
        new EjectionPortBlock(METAL), "ejection_port", ITEM_SETTINGS
    );
    public static final Tuple<Block, Item> EXTRACTION_PORT = EZRReg.registerBlock(
        new ExtractionPortBlock(METAL), "extraction_port", ITEM_SETTINGS
    );

    public static void registerBlocks() {
    }
}

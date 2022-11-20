package io.github.stygigoth.ezrstorage.registry;

import io.github.stygigoth.ezrstorage.Main;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.util.Pair;

public class ModBlocks {
    public static final Pair<Block, Item> STORAGE_CORE = EZRReg.registerBlock(
            , "storage_core", new FabricItemSettings().group(Main.EZR_GROUP)
    );
    public static final Pair<Block, Item> ACCESS_TERMINAL = EZRReg.registerBlock(
            , "access_terminal", new FabricItemSettings().group(Main.EZR_GROUP)
    );

    public static final Pair<Block, Item> BLANK_BOX = EZRReg.registerBlock(
            , "blank_box", new FabricItemSettings().group(Main.EZR_GROUP)
    );

    public static final Pair<Block, Item> STORAGE_BOX = EZRReg.registerBlock(
            , "storage_box", new FabricItemSettings().group(Main.EZR_GROUP)
    );
    public static final Pair<Block, Item> CONDENSED_STORAGE_BOX = EZRReg.registerBlock(
            , "condensed_storage_box", new FabricItemSettings().group(Main.EZR_GROUP)
    );
    public static final Pair<Block, Item> SUPER_STORAGE_BOX = EZRReg.registerBlock(
            , "super_storage_box", new FabricItemSettings().group(Main.EZR_GROUP)
    );
    public static final Pair<Block, Item> ULTRA_STORAGE_BOX = EZRReg.registerBlock(
            , "ultra_storage_box", new FabricItemSettings().group(Main.EZR_GROUP)
    );
    public static final Pair<Block, Item> HYPER_STORAGE_BOX = EZRReg.registerBlock(
            , "hyper_storage_box", new FabricItemSettings().group(Main.EZR_GROUP)
    );

    public static final Pair<Block, Item> CRAFTING_BOX = EZRReg.registerBlock(
            , "crafting_box", new FabricItemSettings().group(Main.EZR_GROUP)
    );
    public static final Pair<Block, Item> SEARCH_BOX = EZRReg.registerBlock(
            , "search_box", new FabricItemSettings().group(Main.EZR_GROUP)
    );
    public static final Pair<Block, Item> SORTING_BOX = EZRReg.registerBlock(
            , "sorting_box", new FabricItemSettings().group(Main.EZR_GROUP)
    );
    public static final Pair<Block, Item> SECURITY_BOX = EZRReg.registerBlock(
            , "security_box", new FabricItemSettings().group(Main.EZR_GROUP)
    );

    public static final Pair<Block, Item> INPUT_PORT = EZRReg.registerBlock(
            , "input_port", new FabricItemSettings().group(Main.EZR_GROUP)
    );
    public static final Pair<Block, Item> OUTPUT_PORT = EZRReg.registerBlock(
            , "output_port", new FabricItemSettings().group(Main.EZR_GROUP)
    );
    public static final Pair<Block, Item> EJECTION_PORT = EZRReg.registerBlock(
            , "ejection_port", new FabricItemSettings().group(Main.EZR_GROUP)
    );

    public static void registerBlocks() {
        Main.LOGGER.info("Registering blocks...");
    }
}

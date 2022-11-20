package io.github.stygigoth.ezrstorage.registry;

import io.github.stygigoth.ezrstorage.Main;
import io.github.stygigoth.ezrstorage.block.BoxBlock;
import io.github.stygigoth.ezrstorage.block.ModificationBoxBlock;
import io.github.stygigoth.ezrstorage.block.StorageBoxBlock;
import io.github.stygigoth.ezrstorage.block.StorageCoreBlock;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.Pair;

public class ModBlocks {
    public static final Pair<Block, Item> STORAGE_CORE = EZRReg.registerBlock(
            new StorageCoreBlock(FabricBlockSettings.copyOf(Blocks.IRON_BLOCK)),
            "storage_core", new FabricItemSettings().group(Main.EZR_GROUP)
    );
    /*public static final Pair<Block, Item> ACCESS_TERMINAL = EZRReg.registerBlock(
            , "access_terminal", new FabricItemSettings().group(Main.EZR_GROUP)
    );*/

    public static final Pair<Block, Item> BLANK_BOX = EZRReg.registerBlock(
            new BoxBlock(FabricBlockSettings.copyOf(Blocks.IRON_BLOCK)),
            "blank_box", new FabricItemSettings().group(Main.EZR_GROUP)
    );

    public static final Pair<Block, Item> STORAGE_BOX = EZRReg.registerBlock(
            new StorageBoxBlock(400, FabricBlockSettings.copyOf(Blocks.IRON_BLOCK)),
            "storage_box", new FabricItemSettings().group(Main.EZR_GROUP)
    );
    public static final Pair<Block, Item> CONDENSED_STORAGE_BOX = EZRReg.registerBlock(
            new StorageBoxBlock(4_000, FabricBlockSettings.copyOf(Blocks.IRON_BLOCK)),
            "condensed_storage_box", new FabricItemSettings().group(Main.EZR_GROUP)
    );
    public static final Pair<Block, Item> SUPER_STORAGE_BOX = EZRReg.registerBlock(
            new StorageBoxBlock(20_000, FabricBlockSettings.copyOf(Blocks.IRON_BLOCK)),
            "super_storage_box", new FabricItemSettings().group(Main.EZR_GROUP)
    );
    public static final Pair<Block, Item> ULTRA_STORAGE_BOX = EZRReg.registerBlock(
            new StorageBoxBlock(80_000, FabricBlockSettings.copyOf(Blocks.IRON_BLOCK)),
            "ultra_storage_box", new FabricItemSettings().group(Main.EZR_GROUP)
    );
    public static final Pair<Block, Item> HYPER_STORAGE_BOX = EZRReg.registerBlock(
            new StorageBoxBlock(400_000, FabricBlockSettings.copyOf(Blocks.IRON_BLOCK)),
            "hyper_storage_box", new FabricItemSettings().group(Main.EZR_GROUP)
    );

    public static final Pair<Block, Item> CRAFTING_BOX = EZRReg.registerBlock(
            new ModificationBoxBlock(ModificationBoxBlock.ModificationBoxBlockType.CRAFTING,
                    FabricBlockSettings.copyOf(Blocks.IRON_BLOCK)), "crafting_box",
            new FabricItemSettings().group(Main.EZR_GROUP)
    );
    public static final Pair<Block, Item> SEARCH_BOX = EZRReg.registerBlock(
            new ModificationBoxBlock(ModificationBoxBlock.ModificationBoxBlockType.SEARCH,
                    FabricBlockSettings.copyOf(Blocks.IRON_BLOCK)), "search_box",
            new FabricItemSettings().group(Main.EZR_GROUP)
    );
    public static final Pair<Block, Item> SORTING_BOX = EZRReg.registerBlock(
            new ModificationBoxBlock(ModificationBoxBlock.ModificationBoxBlockType.SORTING,
                    FabricBlockSettings.copyOf(Blocks.IRON_BLOCK)), "sorting_box",
            new FabricItemSettings().group(Main.EZR_GROUP)
    );
    public static final Pair<Block, Item> SECURITY_BOX = EZRReg.registerBlock(
            new ModificationBoxBlock(ModificationBoxBlock.ModificationBoxBlockType.SECURITY,
                    FabricBlockSettings.copyOf(Blocks.IRON_BLOCK)), "security_box",
            new FabricItemSettings().group(Main.EZR_GROUP)
    );

    /*public static final Pair<Block, Item> INPUT_PORT = EZRReg.registerBlock(
            , "input_port", new FabricItemSettings().group(Main.EZR_GROUP)
    );
    public static final Pair<Block, Item> OUTPUT_PORT = EZRReg.registerBlock(
            , "output_port", new FabricItemSettings().group(Main.EZR_GROUP)
    );
    public static final Pair<Block, Item> EJECTION_PORT = EZRReg.registerBlock(
            , "ejection_port", new FabricItemSettings().group(Main.EZR_GROUP)
    );*/

    public static void registerBlocks() {
        Main.LOGGER.info("Registering blocks...");
    }
}

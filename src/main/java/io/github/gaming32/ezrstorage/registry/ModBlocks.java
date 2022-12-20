package io.github.gaming32.ezrstorage.registry;

import io.github.gaming32.ezrstorage.EZRStorage;
import io.github.gaming32.ezrstorage.block.*;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.Pair;

public class ModBlocks {
    public static final Pair<Block, Item> STORAGE_CORE = EZRReg.registerBlock(
        new StorageCoreBlock(FabricBlockSettings.copyOf(Blocks.IRON_BLOCK)),
        "storage_core", new FabricItemSettings().group(EZRStorage.EZR_GROUP)
    );
    public static final Pair<Block, Item> ACCESS_TERMINAL = EZRReg.registerBlock(
        new AccessTerminalBlock(FabricBlockSettings.copyOf(Blocks.IRON_BLOCK)),
        "access_terminal", new FabricItemSettings().group(EZRStorage.EZR_GROUP)
    );

    public static final Pair<Block, Item> BLANK_BOX = EZRReg.registerBlock(
        new BoxBlock(FabricBlockSettings.copyOf(Blocks.IRON_BLOCK)),
        "blank_box", new FabricItemSettings().group(EZRStorage.EZR_GROUP)
    );

    public static final Pair<Block, Item> STORAGE_BOX = EZRReg.registerBlock(
        new StorageBoxBlock(FabricBlockSettings.copyOf(Blocks.IRON_BLOCK), 400),
        "storage_box", new FabricItemSettings().group(EZRStorage.EZR_GROUP)
    );
    public static final Pair<Block, Item> CONDENSED_STORAGE_BOX = EZRReg.registerBlock(
        new StorageBoxBlock(FabricBlockSettings.copyOf(Blocks.IRON_BLOCK), 4_000),
        "condensed_storage_box", new FabricItemSettings().group(EZRStorage.EZR_GROUP)
    );
    public static final Pair<Block, Item> SUPER_STORAGE_BOX = EZRReg.registerBlock(
        new StorageBoxBlock(FabricBlockSettings.copyOf(Blocks.IRON_BLOCK), 20_000),
        "super_storage_box", new FabricItemSettings().group(EZRStorage.EZR_GROUP)
    );
    public static final Pair<Block, Item> ULTRA_STORAGE_BOX = EZRReg.registerBlock(
        new StorageBoxBlock(FabricBlockSettings.copyOf(Blocks.IRON_BLOCK), 80_000),
        "ultra_storage_box", new FabricItemSettings().group(EZRStorage.EZR_GROUP)
    );
    public static final Pair<Block, Item> HYPER_STORAGE_BOX = EZRReg.registerBlock(
        new StorageBoxBlock(FabricBlockSettings.copyOf(Blocks.IRON_BLOCK), 400_000),
        "hyper_storage_box", new FabricItemSettings().group(EZRStorage.EZR_GROUP)
    );

    public static final Pair<Block, Item> CRAFTING_BOX = EZRReg.registerBlock(
        new ModificationBoxBlock(
            ModificationBoxBlock.Type.CRAFTING,
            FabricBlockSettings.copyOf(Blocks.IRON_BLOCK)
        ),
        "crafting_box",
        new FabricItemSettings().group(EZRStorage.EZR_GROUP)
    );
    public static final Pair<Block, Item> SEARCH_BOX = EZRReg.registerBlock(
        new ModificationBoxBlock(
            ModificationBoxBlock.Type.SEARCH,
            FabricBlockSettings.copyOf(Blocks.IRON_BLOCK)
        ),
        "search_box",
        new FabricItemSettings().group(EZRStorage.EZR_GROUP)
    );
    public static final Pair<Block, Item> SORTING_BOX = EZRReg.registerBlock(
        new ModificationBoxBlock(
            ModificationBoxBlock.Type.SORTING,
            FabricBlockSettings.copyOf(Blocks.IRON_BLOCK)
        ),
        "sorting_box",
        new FabricItemSettings().group(EZRStorage.EZR_GROUP)
    );
    public static final Pair<Block, Item> SECURITY_BOX = EZRReg.registerBlock(
        new ModificationBoxBlock(
            ModificationBoxBlock.Type.SECURITY,
            FabricBlockSettings.copyOf(Blocks.IRON_BLOCK)
        ),
        "security_box",
        new FabricItemSettings().group(EZRStorage.EZR_GROUP)
    );

    /*public static final Pair<Block, Item> INPUT_PORT = EZRReg.registerBlock(
            , "input_port", new FabricItemSettings().group(EZRStorage.EZR_GROUP)
    );
    public static final Pair<Block, Item> OUTPUT_PORT = EZRReg.registerBlock(
            , "output_port", new FabricItemSettings().group(EZRStorage.EZR_GROUP)
    );
    public static final Pair<Block, Item> EJECTION_PORT = EZRReg.registerBlock(
            , "ejection_port", new FabricItemSettings().group(EZRStorage.EZR_GROUP)
    );*/

    public static void registerBlocks() {
    }
}

package io.github.gaming32.ezrstorage.registry;

import io.github.gaming32.ezrstorage.EZRStorage;
import io.github.gaming32.ezrstorage.block.*;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.item.Item;
import net.minecraft.util.Pair;

public class EZRBlocks {
    public static final Pair<Block, Item> STORAGE_CORE = EZRReg.registerBlock(
        new StorageCoreBlock(
            FabricBlockSettings.of(Material.METAL)
                .hardness(2f)
                .resistance(6080f)
        ),
        "storage_core",
        new FabricItemSettings().group(EZRStorage.EZR_GROUP)
    );
    public static final Pair<Block, Item> ACCESS_TERMINAL = EZRReg.registerBlock(
        new AccessTerminalBlock(
            FabricBlockSettings.of(Material.METAL)
                .hardness(2f)
        ),
        "access_terminal",
        new FabricItemSettings().group(EZRStorage.EZR_GROUP)
    );

    public static final Pair<Block, Item> BLANK_BOX = EZRReg.registerBlock(
        new BoxBlock(
            FabricBlockSettings.of(Material.WOOD)
                .hardness(2f)
        ),
        "blank_box",
        new FabricItemSettings().group(EZRStorage.EZR_GROUP)
    );

    public static final Pair<Block, Item> STORAGE_BOX = EZRReg.registerBlock(
        new StorageBoxBlock(
            FabricBlockSettings.of(Material.WOOD)
                .hardness(2f),
            400
        ),
        "storage_box",
        new FabricItemSettings().group(EZRStorage.EZR_GROUP)
    );
    public static final Pair<Block, Item> CONDENSED_STORAGE_BOX = EZRReg.registerBlock(
        new StorageBoxBlock(
            FabricBlockSettings.of(Material.METAL)
                .hardness(2f),
            4_000
        ),
        "condensed_storage_box",
        new FabricItemSettings().group(EZRStorage.EZR_GROUP)
    );
    public static final Pair<Block, Item> SUPER_STORAGE_BOX = EZRReg.registerBlock(
        new StorageBoxBlock(
            FabricBlockSettings.of(Material.METAL)
                .hardness(2f),
            20_000
        ),
        "super_storage_box",
        new FabricItemSettings().group(EZRStorage.EZR_GROUP)
    );
    public static final Pair<Block, Item> ULTRA_STORAGE_BOX = EZRReg.registerBlock(
        new StorageBoxBlock(
            FabricBlockSettings.of(Material.METAL)
                .hardness(2f),
            80_000
        ),
        "ultra_storage_box",
        new FabricItemSettings().group(EZRStorage.EZR_GROUP)
    );
    public static final Pair<Block, Item> HYPER_STORAGE_BOX = EZRReg.registerBlock(
        new StorageBoxBlock(
            FabricBlockSettings.of(Material.METAL)
                .hardness(2f),
            400_000
        ),
        "hyper_storage_box",
        new FabricItemSettings().group(EZRStorage.EZR_GROUP)
    );

    public static final Pair<Block, Item> CRAFTING_BOX = EZRReg.registerBlock(
        new ModificationBoxBlock(
            ModificationBoxBlock.Type.CRAFTING,
            FabricBlockSettings.of(Material.METAL)
                .hardness(2f)
        ),
        "crafting_box",
        new FabricItemSettings().group(EZRStorage.EZR_GROUP)
    );
    public static final Pair<Block, Item> SEARCH_BOX = EZRReg.registerBlock(
        new ModificationBoxBlock(
            ModificationBoxBlock.Type.SEARCH,
            FabricBlockSettings.of(Material.METAL)
                .hardness(2f)
        ),
        "search_box",
        new FabricItemSettings().group(EZRStorage.EZR_GROUP)
    );
    public static final Pair<Block, Item> SORTING_BOX = EZRReg.registerBlock(
        new ModificationBoxBlock(
            ModificationBoxBlock.Type.SORTING,
            FabricBlockSettings.of(Material.WOOD)
                .hardness(2f)
        ),
        "sorting_box",
        new FabricItemSettings().group(EZRStorage.EZR_GROUP)
    );
    public static final Pair<Block, Item> SECURITY_BOX = EZRReg.registerBlock(
        new ModificationBoxBlock(
            ModificationBoxBlock.Type.SECURITY,
            FabricBlockSettings.of(Material.METAL)
                .hardness(2f)
        ),
        "security_box",
        new FabricItemSettings().group(EZRStorage.EZR_GROUP)
    );

    public static final Pair<Block, Item> INPUT_PORT = EZRReg.registerBlock(
        new InputPortBlock(
            FabricBlockSettings.of(Material.METAL)
                .hardness(2f)
        ),
        "input_port",
        new FabricItemSettings().group(EZRStorage.EZR_GROUP)
    );

    public static void registerBlocks() {
    }
}

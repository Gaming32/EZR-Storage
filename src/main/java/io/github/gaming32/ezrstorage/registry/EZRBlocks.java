package io.github.gaming32.ezrstorage.registry;

import io.github.gaming32.ezrstorage.block.AccessTerminalBlock;
import io.github.gaming32.ezrstorage.block.BoxBlock;
import io.github.gaming32.ezrstorage.block.EjectionPortBlock;
import io.github.gaming32.ezrstorage.block.ExtractionPortBlock;
import io.github.gaming32.ezrstorage.block.InputPortBlock;
import io.github.gaming32.ezrstorage.block.ModificationBoxBlock;
import io.github.gaming32.ezrstorage.block.StorageBoxBlock;
import io.github.gaming32.ezrstorage.block.StorageCoreBlock;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;

public class EZRBlocks {
    private static final BlockBehaviour.Properties METAL = BlockBehaviour.Properties.of()
        .mapColor(MapColor.METAL)
        .destroyTime(2f)
        .requiresCorrectToolForDrops()
        .sound(SoundType.METAL);
    private static final BlockBehaviour.Properties WOOD = BlockBehaviour.Properties.of()
        .mapColor(MapColor.WOOD)
        .ignitedByLava()
        .destroyTime(2f)
        .sound(SoundType.WOOD);

    public static final StorageCoreBlock STORAGE_CORE = EZRReg.registerBlockAndItem(
        new StorageCoreBlock(FabricBlockSettings.copyOf(METAL).explosionResistance(6080f)),
        "storage_core"
    );
    public static final AccessTerminalBlock ACCESS_TERMINAL = EZRReg.registerBlockAndItem(
        new AccessTerminalBlock(METAL), "access_terminal"
    );

    public static final BoxBlock BLANK_BOX = EZRReg.registerBlockAndItem(
        new BoxBlock(WOOD), "blank_box"
    );

    public static final StorageBoxBlock STORAGE_BOX = EZRReg.registerBlockAndItem(
        new StorageBoxBlock(WOOD, 400), "storage_box"
    );
    public static final StorageBoxBlock CONDENSED_STORAGE_BOX = EZRReg.registerBlockAndItem(
        new StorageBoxBlock(METAL, 4_000), "condensed_storage_box"
    );
    public static final StorageBoxBlock SUPER_STORAGE_BOX = EZRReg.registerBlockAndItem(
        new StorageBoxBlock(METAL, 20_000), "super_storage_box"
    );
    public static final StorageBoxBlock ULTRA_STORAGE_BOX = EZRReg.registerBlockAndItem(
        new StorageBoxBlock(METAL, 80_000), "ultra_storage_box"
    );
    public static final StorageBoxBlock HYPER_STORAGE_BOX = EZRReg.registerBlockAndItem(
        new StorageBoxBlock(METAL, 400_000), "hyper_storage_box"
    );

    public static final ModificationBoxBlock CRAFTING_BOX = EZRReg.registerBlockAndItem(
        new ModificationBoxBlock(METAL, ModificationBoxBlock.Type.CRAFTING),
        "crafting_box"
    );
    public static final ModificationBoxBlock SEARCH_BOX = EZRReg.registerBlockAndItem(
        new ModificationBoxBlock(METAL, ModificationBoxBlock.Type.SEARCH), "search_box"
    );
    public static final ModificationBoxBlock SORTING_BOX = EZRReg.registerBlockAndItem(
        new ModificationBoxBlock(WOOD, ModificationBoxBlock.Type.SORTING), "sorting_box"
    );
    public static final ModificationBoxBlock SECURITY_BOX = EZRReg.registerBlockAndItem(
        new ModificationBoxBlock(METAL, ModificationBoxBlock.Type.SECURITY), "security_box"
    );

    public static final InputPortBlock INPUT_PORT = EZRReg.registerBlockAndItem(
        new InputPortBlock(METAL), "input_port"
    );
    public static final EjectionPortBlock EJECTION_PORT = EZRReg.registerBlockAndItem(
        new EjectionPortBlock(METAL), "ejection_port"
    );
    public static final ExtractionPortBlock EXTRACTION_PORT = EZRReg.registerBlockAndItem(
        new ExtractionPortBlock(METAL), "extraction_port"
    );

    public static void registerBlocks() {
    }
}

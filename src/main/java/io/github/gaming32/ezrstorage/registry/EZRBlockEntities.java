package io.github.gaming32.ezrstorage.registry;

import io.github.gaming32.ezrstorage.block.entity.AccessTerminalBlockEntity;
import io.github.gaming32.ezrstorage.block.entity.EjectionPortBlockEntity;
import io.github.gaming32.ezrstorage.block.entity.ExtractionPortBlockEntity;
import io.github.gaming32.ezrstorage.block.entity.InputPortBlockEntity;
import io.github.gaming32.ezrstorage.block.entity.RefBlockEntity;
import io.github.gaming32.ezrstorage.block.entity.StorageCoreBlockEntity;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class EZRBlockEntities {
    public static final BlockEntityType<StorageCoreBlockEntity> STORAGE_CORE = EZRReg.registerBlockEntityType(
        FabricBlockEntityTypeBuilder.create(
            StorageCoreBlockEntity::new,
            EZRBlocks.STORAGE_CORE
        ).build(),
        "storage_core"
    );

    public static final BlockEntityType<AccessTerminalBlockEntity> ACCESS_TERMINAL = EZRReg.registerBlockEntityType(
        FabricBlockEntityTypeBuilder.create(
            AccessTerminalBlockEntity::new,
            EZRBlocks.ACCESS_TERMINAL
        ).build(),
        "access_terminal"
    );

    public static final BlockEntityType<RefBlockEntity> BOX = EZRReg.registerBlockEntityType(
        FabricBlockEntityTypeBuilder.create(
            RefBlockEntity::new,
            EZRBlocks.BLANK_BOX,
            EZRBlocks.CRAFTING_BOX,
            EZRBlocks.SEARCH_BOX,
            EZRBlocks.SECURITY_BOX,
            EZRBlocks.SORTING_BOX,
            EZRBlocks.STORAGE_BOX,
            EZRBlocks.CONDENSED_STORAGE_BOX,
            EZRBlocks.SUPER_STORAGE_BOX,
            EZRBlocks.ULTRA_STORAGE_BOX,
            EZRBlocks.HYPER_STORAGE_BOX
        ).build(),
        "box"
    );

    public static final BlockEntityType<InputPortBlockEntity> INPUT_PORT = EZRReg.registerBlockEntityType(
        FabricBlockEntityTypeBuilder.create(
            InputPortBlockEntity::new,
            EZRBlocks.INPUT_PORT
        ).build(),
        "input_port"
    );

    public static final BlockEntityType<EjectionPortBlockEntity> EJECTION_PORT = EZRReg.registerBlockEntityType(
        FabricBlockEntityTypeBuilder.create(
            EjectionPortBlockEntity::new,
            EZRBlocks.EJECTION_PORT
        ).build(),
        "ejection_port"
    );

    public static final BlockEntityType<ExtractionPortBlockEntity> EXTRACTION_PORT = EZRReg.registerBlockEntityType(
        FabricBlockEntityTypeBuilder.create(
            ExtractionPortBlockEntity::new,
            EZRBlocks.EXTRACTION_PORT
        ).build(),
        "extraction_port"
    );

    public static void registerBlockEntities() {
    }
}

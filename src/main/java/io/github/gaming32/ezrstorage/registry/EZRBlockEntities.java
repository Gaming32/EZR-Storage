package io.github.gaming32.ezrstorage.registry;

import io.github.gaming32.ezrstorage.block.entity.*;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class EZRBlockEntities {
    public static final BlockEntityType<StorageCoreBlockEntity> STORAGE_CORE = EZRReg.registerBlockEntityType(
        FabricBlockEntityTypeBuilder.create(
            StorageCoreBlockEntity::new,
            EZRBlocks.STORAGE_CORE.getA()
        ).build(),
        "storage_core"
    );

    public static final BlockEntityType<AccessTerminalBlockEntity> ACCESS_TERMINAL = EZRReg.registerBlockEntityType(
        FabricBlockEntityTypeBuilder.create(
            AccessTerminalBlockEntity::new,
            EZRBlocks.ACCESS_TERMINAL.getA()
        ).build(),
        "access_terminal"
    );

    public static final BlockEntityType<RefBlockEntity> BOX = EZRReg.registerBlockEntityType(
        FabricBlockEntityTypeBuilder.create(
            RefBlockEntity::new,
            EZRBlocks.BLANK_BOX.getA(),
            EZRBlocks.CRAFTING_BOX.getA(),
            EZRBlocks.SEARCH_BOX.getA(),
            EZRBlocks.SECURITY_BOX.getA(),
            EZRBlocks.SORTING_BOX.getA(),
            EZRBlocks.STORAGE_BOX.getA(),
            EZRBlocks.CONDENSED_STORAGE_BOX.getA(),
            EZRBlocks.SUPER_STORAGE_BOX.getA(),
            EZRBlocks.ULTRA_STORAGE_BOX.getA(),
            EZRBlocks.HYPER_STORAGE_BOX.getA()
        ).build(),
        "box"
    );

    public static final BlockEntityType<InputPortBlockEntity> INPUT_PORT = EZRReg.registerBlockEntityType(
        FabricBlockEntityTypeBuilder.create(
            InputPortBlockEntity::new,
            EZRBlocks.INPUT_PORT.getA()
        ).build(),
        "input_port"
    );

    public static final BlockEntityType<EjectionPortBlockEntity> EJECTION_PORT = EZRReg.registerBlockEntityType(
        FabricBlockEntityTypeBuilder.create(
            EjectionPortBlockEntity::new,
            EZRBlocks.EJECTION_PORT.getA()
        ).build(),
        "ejection_port"
    );

    public static final BlockEntityType<ExtractionPortBlockEntity> EXTRACTION_PORT = EZRReg.registerBlockEntityType(
        FabricBlockEntityTypeBuilder.create(
            ExtractionPortBlockEntity::new,
            EZRBlocks.EXTRACTION_PORT.getA()
        ).build(),
        "extraction_port"
    );

    public static void registerBlockEntities() {
    }
}

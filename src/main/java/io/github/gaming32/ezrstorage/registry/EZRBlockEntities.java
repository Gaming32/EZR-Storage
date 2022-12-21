package io.github.gaming32.ezrstorage.registry;

import io.github.gaming32.ezrstorage.block.entity.*;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.entity.BlockEntityType;

public class EZRBlockEntities {
    public static final BlockEntityType<StorageCoreBlockEntity> STORAGE_CORE = EZRReg.registerBlockEntityType(
        FabricBlockEntityTypeBuilder.create(
            StorageCoreBlockEntity::new,
            EZRBlocks.STORAGE_CORE.getLeft()
        ).build(),
        "storage_core"
    );

    public static final BlockEntityType<AccessTerminalBlockEntity> ACCESS_TERMINAL = EZRReg.registerBlockEntityType(
        FabricBlockEntityTypeBuilder.create(
            AccessTerminalBlockEntity::new,
            EZRBlocks.ACCESS_TERMINAL.getLeft()
        ).build(),
        "access_terminal"
    );

    public static final BlockEntityType<RefBlockEntity> BOX = EZRReg.registerBlockEntityType(
        FabricBlockEntityTypeBuilder.create(
            RefBlockEntity::new,
            EZRBlocks.BLANK_BOX.getLeft(),
            EZRBlocks.CRAFTING_BOX.getLeft(),
            EZRBlocks.SEARCH_BOX.getLeft(),
            EZRBlocks.SECURITY_BOX.getLeft(),
            EZRBlocks.SORTING_BOX.getLeft(),
            EZRBlocks.STORAGE_BOX.getLeft(),
            EZRBlocks.CONDENSED_STORAGE_BOX.getLeft(),
            EZRBlocks.SUPER_STORAGE_BOX.getLeft(),
            EZRBlocks.ULTRA_STORAGE_BOX.getLeft(),
            EZRBlocks.HYPER_STORAGE_BOX.getLeft()
        ).build(),
        "box"
    );

    public static final BlockEntityType<InputPortBlockEntity> INPUT_PORT = EZRReg.registerBlockEntityType(
        FabricBlockEntityTypeBuilder.create(
            InputPortBlockEntity::new,
            EZRBlocks.INPUT_PORT.getLeft()
        ).build(),
        "input_port"
    );

    public static final BlockEntityType<EjectionPortBlockEntity> EJECTION_PORT = EZRReg.registerBlockEntityType(
        FabricBlockEntityTypeBuilder.create(
            EjectionPortBlockEntity::new,
            EZRBlocks.EJECTION_PORT.getLeft()
        ).build(),
        "ejection_port"
    );

    public static void registerBlockEntities() {
    }
}

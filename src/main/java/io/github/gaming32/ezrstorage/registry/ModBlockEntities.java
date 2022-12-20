package io.github.gaming32.ezrstorage.registry;

import io.github.gaming32.ezrstorage.block.entity.AccessTerminalBlockEntity;
import io.github.gaming32.ezrstorage.block.entity.RefBlockEntity;
import io.github.gaming32.ezrstorage.block.entity.StorageCoreBlockEntity;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.entity.BlockEntityType;

public class ModBlockEntities {
    public static final BlockEntityType<StorageCoreBlockEntity> STORAGE_CORE_BLOCK_ENTITY = EZRReg.registerBlockEntityType(
        FabricBlockEntityTypeBuilder.create(
            StorageCoreBlockEntity::new,
            ModBlocks.STORAGE_CORE.getLeft()
        ).build(),
        "storage_core"
    );
    public static final BlockEntityType<AccessTerminalBlockEntity> ACCESS_TERMINAL_BLOCK_ENTITY = EZRReg.registerBlockEntityType(
        FabricBlockEntityTypeBuilder.create(
            AccessTerminalBlockEntity::new,
            ModBlocks.ACCESS_TERMINAL.getLeft()
        ).build(),
        "access_terminal"
    );
    public static final BlockEntityType<RefBlockEntity> REF_BLOCK_ENTITY = EZRReg.registerBlockEntityType(
        FabricBlockEntityTypeBuilder.create(
            RefBlockEntity::new,
            ModBlocks.BLANK_BOX.getLeft(),
            ModBlocks.CRAFTING_BOX.getLeft(),
            ModBlocks.SEARCH_BOX.getLeft(),
            ModBlocks.SECURITY_BOX.getLeft(),
            ModBlocks.SORTING_BOX.getLeft(),
            ModBlocks.STORAGE_BOX.getLeft(),
            ModBlocks.CONDENSED_STORAGE_BOX.getLeft(),
            ModBlocks.SUPER_STORAGE_BOX.getLeft(),
            ModBlocks.ULTRA_STORAGE_BOX.getLeft(),
            ModBlocks.HYPER_STORAGE_BOX.getLeft()
        ).build(),
        "box"
    );

    public static void registerBlockEntities() {
    }
}

package io.github.stygigoth.ezrstorage.registry;

import io.github.stygigoth.ezrstorage.Main;
import io.github.stygigoth.ezrstorage.block.entity.RefBlockEntity;
import io.github.stygigoth.ezrstorage.block.entity.StorageBoxBlockEntity;
import io.github.stygigoth.ezrstorage.block.entity.StorageCoreBlockEntity;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;

public class ModBlockEntities {
    public static final BlockEntityType<StorageCoreBlockEntity> STORAGE_CORE_BLOCK_ENTITY =
            EZRReg.registerBlockEntityType(FabricBlockEntityTypeBuilder
                    .create(StorageCoreBlockEntity::new, ModBlocks.STORAGE_CORE.getLeft()).build()
            , "storage_core_block_entity");
    public static final BlockEntityType<RefBlockEntity> REF_BLOCK_ENTITY =
            EZRReg.registerBlockEntityType(FabricBlockEntityTypeBuilder
                            .create(RefBlockEntity::new)
                    .addBlocks(
                            ModBlocks.BLANK_BOX.getLeft(),
                            ModBlocks.CRAFTING_BOX.getLeft(),
                            ModBlocks.SEARCH_BOX.getLeft(),
                            ModBlocks.SECURITY_BOX.getLeft(),
                            ModBlocks.SORTING_BOX.getLeft()
                    ).build()
                    , "ref_block_entity");
    public static final BlockEntityType<StorageBoxBlockEntity> STORAGE_BOX_BLOCK_ENTITY =
            EZRReg.registerBlockEntityType(FabricBlockEntityTypeBuilder
                    .create(StorageBoxBlockEntity::new)
                    .addBlocks(
                            ModBlocks.STORAGE_BOX.getLeft(),
                            ModBlocks.CONDENSED_STORAGE_BOX.getLeft(),
                            ModBlocks.SUPER_STORAGE_BOX.getLeft(),
                            ModBlocks.ULTRA_STORAGE_BOX.getLeft(),
                            ModBlocks.HYPER_STORAGE_BOX.getLeft()
                    ).build(),
                    "storage_box_block_entity");

    public static void registerBlockEntities() {
        Main.LOGGER.info("Registering block entities...");
    }
}

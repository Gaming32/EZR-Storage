package io.github.stygigoth.ezrstorage.registry;

import io.github.stygigoth.ezrstorage.Main;
import io.github.stygigoth.ezrstorage.block.entity.StorageCoreBlockEntity;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.entity.BlockEntityType;

public class ModBlockEntities {
    public static final BlockEntityType<StorageCoreBlockEntity> STORAGE_CORE_BLOCK_ENTITY =
            EZRReg.registerBlockEntityType(FabricBlockEntityTypeBuilder
                    .create(StorageCoreBlockEntity::new, ModBlocks.STORAGE_CORE.getLeft()).build()
            , "storage_core_block_entity");

    public static void registerModBlockEntities() {
        Main.LOGGER.info("Registering block entities...");
    }
}

package io.github.stygigoth.ezrstorage;

import io.github.stygigoth.ezrstorage.registry.EZRReg;
import io.github.stygigoth.ezrstorage.registry.ModBlocks;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EZRStorage implements ModInitializer {
    public static final String MOD_ID = "ezrstorage";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static final ItemGroup EZR_GROUP = FabricItemGroupBuilder.build(
        new Identifier(MOD_ID, MOD_ID),
        () -> new ItemStack(ModBlocks.STORAGE_CORE.getLeft())
    );

    @Override
    public void onInitialize() {
        EZRReg.registerMod();
    }
}

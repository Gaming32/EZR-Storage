package io.github.stygigoth.ezrstorage.block.entity;

import io.github.stygigoth.ezrstorage.gui.StorageCoreScreenHandler;
import io.github.stygigoth.ezrstorage.registry.ModBlockEntities;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.math.BlockPos;

public class AccessTerminalBlockEntity extends RefBlockEntity implements NamedScreenHandlerFactory {
    public AccessTerminalBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.ACCESS_TERMINAL_BLOCK_ENTITY, pos, state);
    }

    public AccessTerminalBlockEntity(BlockEntityType<? extends BlockEntity> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
        if (world == null || getCore() == null) return null;
        final StorageCoreBlockEntity core =
            world.getBlockEntity(getCore(), ModBlockEntities.STORAGE_CORE_BLOCK_ENTITY).orElse(null);
        if (core == null) return null;
        return new StorageCoreScreenHandler(syncId, inv, core.getInventory(), core.getModifications());
    }

    @Override
    public Text getDisplayName() {
        return new TranslatableText(getCachedState().getBlock().getTranslationKey());
    }
}

package io.github.gaming32.ezrstorage.block.entity;

import io.github.gaming32.ezrstorage.gui.StorageCoreScreenHandler;
import io.github.gaming32.ezrstorage.registry.EZRBlockEntities;
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
        super(EZRBlockEntities.ACCESS_TERMINAL, pos, state);
    }

    public AccessTerminalBlockEntity(BlockEntityType<? extends BlockEntity> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
        return getCoreBlockEntity().map(core ->
            new StorageCoreScreenHandler(syncId, inv, core.getInventory(), core.getModifications())
        ).orElse(null);
    }

    @Override
    public Text getDisplayName() {
        return new TranslatableText(getCachedState().getBlock().getTranslationKey());
    }
}

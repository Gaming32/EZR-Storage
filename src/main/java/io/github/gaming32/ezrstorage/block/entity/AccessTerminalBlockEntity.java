package io.github.gaming32.ezrstorage.block.entity;

import io.github.gaming32.ezrstorage.gui.StorageCoreMenu;
import io.github.gaming32.ezrstorage.registry.EZRBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class AccessTerminalBlockEntity extends RefBlockEntity implements MenuProvider {
    public AccessTerminalBlockEntity(BlockPos pos, BlockState state) {
        super(EZRBlockEntities.ACCESS_TERMINAL, pos, state);
    }

    public AccessTerminalBlockEntity(BlockEntityType<? extends BlockEntity> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public AbstractContainerMenu createMenu(int syncId, @NotNull Inventory inv, @NotNull Player player) {
        return getCoreBlockEntity().map(core -> new StorageCoreMenu(
            syncId,
            inv,
            core.getInventory(),
            core.getModifications()
        )).orElse(null);
    }

    @Override
    public @NotNull Component getDisplayName() {
        return Component.translatable(getBlockState().getBlock().getDescriptionId());
    }
}

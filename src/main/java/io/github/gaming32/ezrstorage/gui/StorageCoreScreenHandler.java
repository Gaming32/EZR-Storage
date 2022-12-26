package io.github.gaming32.ezrstorage.gui;

import io.github.gaming32.ezrstorage.EZRStorage;
import io.github.gaming32.ezrstorage.InfiniteInventory;
import io.github.gaming32.ezrstorage.InfiniteItemStack;
import io.github.gaming32.ezrstorage.block.ModificationBoxBlock;
import io.github.gaming32.ezrstorage.util.MoreBufs;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.EnumSet;
import java.util.Set;

public class StorageCoreScreenHandler extends ScreenHandler {
    private final InfiniteInventory coreInventory;
    private final Set<ModificationBoxBlock.Type> modifications;
    protected final PlayerEntity player;
    private Runnable updateNotification;

    public StorageCoreScreenHandler(int syncId, PlayerInventory playerInventory) {
        this(syncId, playerInventory, new InfiniteInventory(), EnumSet.noneOf(ModificationBoxBlock.Type.class));
    }

    public StorageCoreScreenHandler(
        int syncId,
        PlayerInventory playerInventory,
        InfiniteInventory coreInventory,
        Set<ModificationBoxBlock.Type> modifications
    ) {
        this(EZRStorage.STORAGE_CORE_SCREEN_HANDLER, syncId, playerInventory, coreInventory, modifications);
    }

    protected StorageCoreScreenHandler(
        ScreenHandlerType<?> type,
        int syncId,
        PlayerInventory playerInventory,
        InfiniteInventory coreInventory,
        Set<ModificationBoxBlock.Type> modifications
    ) {
        super(type, syncId);
        this.coreInventory = coreInventory;
        this.modifications = modifications;
        this.player = playerInventory.player;

        final Inventory inventory = new SimpleInventory(rowCount() * 9);
        for (int row = 0; row < rowCount(); row++) {
            for (int column = 0; column < 9; column++) {
                addSlot(new Slot(inventory, column + row * 9, 8 + column * 18, 18 + row * 18));
            }
        }

        for (int row = 0; row < 3; row++) {
            for (int column = 0; column < 9; column++) {
                addSlot(new Slot(playerInventory, column + row * 9 + 9, 8 + column * 18, playerInventoryY() + row * 18));
            }
        }

        for (int column = 0; column < 9; column++) {
            addSlot(new Slot(playerInventory, column, 8 + column * 18, playerInventoryY() + 58));
        }
    }

    @Override
    public void sendContentUpdates() {
        super.sendContentUpdates();
        sync();
    }

    @Override
    public void syncState() {
        super.syncState();
        sync();
    }

    private void sync() {
        if (player instanceof ServerPlayerEntity serverPlayer) {
            syncToClient(serverPlayer);
        }
    }

    protected void syncToClient(ServerPlayerEntity player) {
        final PacketByteBuf buf = PacketByteBufs.create();
        buf.writeByte(syncId);
        buf.writeNbt(coreInventory.writeNbt());
        MoreBufs.writeEnumSet(buf, modifications);
        ServerPlayNetworking.send(player, EZRStorage.SYNC_INVENTORY, buf);
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return true;
    }

    @Override
    public boolean onButtonClick(PlayerEntity player, int id) {
        return switch (id) {
            case 0 -> {
                coreInventory.setSortType(coreInventory.getSortType().rotate());
                if (player instanceof ServerPlayerEntity serverPlayer) {
                    syncToClient(serverPlayer);
                }
                yield true;
            }
            case 1 -> {
                if (this instanceof StorageCoreScreenHandlerWithCrafting withCrafting) {
                    withCrafting.clearGrid(player);
                    coreInventory.reSort();
                    yield true;
                }
                yield false;
            }
            default -> false;
        };
    }

    @Override
    public ItemStack transferSlot(PlayerEntity player, int index) {
        final Slot slot = slots.get(index);
        //noinspection ConstantValue
        if (slot != null && slot.hasStack()) {
            final ItemStack stack = slot.getStack();
            slot.setStack(coreInventory.moveFrom(stack));
        }
        if (player instanceof ServerPlayerEntity serverPlayer) {
            syncToClient(serverPlayer);
        }
        return ItemStack.EMPTY;
    }

    @Override
    public void onSlotClick(int slotIndex, int button, SlotActionType actionType, PlayerEntity player) {
        if (slotIndex >= rowCount() * 9 || slotIndex < 0) {
            super.onSlotClick(slotIndex, button, actionType, player);
            if (actionType == SlotActionType.QUICK_MOVE) {
                getCoreInventory().reSort();
            }
        }
    }

    public ItemStack customSlotClick(int slotIndex, SlotActionType actionType) {
        final ItemStack heldStack = getCursorStack();

        if (heldStack.isEmpty()) {
            final InfiniteItemStack infiniteStack = coreInventory.getStack(slotIndex);
            if (infiniteStack.isEmpty()) {
                return ItemStack.EMPTY;
            }
            final ItemStack stack = coreInventory.extractStack(infiniteStack);
            if (actionType == SlotActionType.QUICK_MOVE) {
                if (!insertItem(stack, rowCount() * 9, rowCount() * 9 + 36, true)) {
                    coreInventory.moveFrom(stack);
                }
            } else {
                setCursorStack(stack);
            }
            return stack;
        } else {
            setCursorStack(coreInventory.moveFrom(heldStack));
        }
        return ItemStack.EMPTY;
    }

    @Override
    public void close(PlayerEntity player) {
        super.close(player);
        coreInventory.reSort();
    }

    public InfiniteInventory getCoreInventory() {
        return coreInventory;
    }

    public Set<ModificationBoxBlock.Type> getModifications() {
        return modifications;
    }

    protected int playerInventoryY() {
        return 140;
    }

    protected int rowCount() {
        return 6;
    }

    public Runnable getUpdateNotification() {
        return updateNotification;
    }

    public void setUpdateNotification(Runnable updateNotification) {
        this.updateNotification = updateNotification;
    }
}

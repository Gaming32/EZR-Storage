package io.github.stygigoth.ezrstorage.gui;

import io.github.stygigoth.ezrstorage.EZRStorage;
import io.github.stygigoth.ezrstorage.HasServerPlayerOuterClass;
import io.github.stygigoth.ezrstorage.InfiniteInventory;
import io.github.stygigoth.ezrstorage.InfiniteItemStack;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.server.network.ServerPlayerEntity;

public class StorageCoreScreenHandler extends ScreenHandler {
    private final InfiniteInventory coreInventory;

    public StorageCoreScreenHandler(int syncId, PlayerInventory playerInventory) {
        this(syncId, playerInventory, new InfiniteInventory());
    }

    public StorageCoreScreenHandler(int syncId, PlayerInventory playerInventory, InfiniteInventory coreInventory) {
        super(EZRStorage.STORAGE_CORE_SCREEN_HANDLER, syncId);
        this.coreInventory = coreInventory;

        final Inventory inventory = new SimpleInventory(54);
        for (int row = 0; row < 6; row++) {
            for (int column = 0; column < 9; column++) {
                addSlot(new Slot(inventory, column + row * 9, 8 + column * 18, 18 + row * 18));
            }
        }

        for (int row = 0; row < 3; row++) {
            for (int column = 0; column < 9; column++) {
                addSlot(new Slot(playerInventory, column + row * 9 + 9, 8 + column * 18, 140 + row * 18));
            }
        }

        for (int column = 0; column < 9; column++) {
            addSlot(new Slot(playerInventory, column, 8 + column * 18, 140 + 58));
        }
    }

    @Override
    public void syncState() {
        super.syncState();
        if (syncHandler instanceof HasServerPlayerOuterClass playerSyncHandler) {
            syncToClient(playerSyncHandler.getPlayer());
        }
    }

    public void syncToClient(ServerPlayerEntity player) {
        final PacketByteBuf buf = PacketByteBufs.create();
        buf.writeByte(syncId);
        buf.writeNbt(coreInventory.writeNbt());
        ServerPlayNetworking.send(player, EZRStorage.SYNC_INVENTORY, buf);
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return true;
    }

    @Override
    public boolean onButtonClick(PlayerEntity player, int id) {
        // TODO: Sort mode and crafting grid
        return super.onButtonClick(player, id);
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
        if (slotIndex >= 54 || slotIndex < 0) {
            super.onSlotClick(slotIndex, button, actionType, player);
        }
    }

    public ItemStack customSlotClick(int slotIndex, int button, SlotActionType actionType, PlayerEntity player) {
        final ItemStack heldStack = getCursorStack();

        if (heldStack.isEmpty()) {
            final int type = button == 1 ? 1 : 0;
            final InfiniteItemStack infiniteStack = coreInventory.getStack(slotIndex);
            if (infiniteStack.isEmpty()) {
                return ItemStack.EMPTY;
            }
            final ItemStack stack = coreInventory.extractStack(infiniteStack);
            if (actionType == SlotActionType.QUICK_MOVE) {
                if (!insertItem(stack, 54, 90, true)) {
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
}

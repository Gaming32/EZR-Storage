package io.github.gaming32.ezrstorage.gui;

import io.github.gaming32.ezrstorage.EZRStorage;
import io.github.gaming32.ezrstorage.InfiniteInventory;
import io.github.gaming32.ezrstorage.InfiniteItemStack;
import io.github.gaming32.ezrstorage.block.ModificationBoxBlock;
import io.github.gaming32.ezrstorage.util.MoreBufs;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.server.level.ServerPlayer;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

public class StorageCoreScreenHandler extends AbstractContainerMenu {
    private final InfiniteInventory coreInventory;
    private final Set<ModificationBoxBlock.Type> modifications;
    private final List<Slot> inputSources = new ArrayList<>();
    protected final Player player;
    private Runnable updateNotification;

    public StorageCoreScreenHandler(int syncId, Inventory playerInventory) {
        this(syncId, playerInventory, new InfiniteInventory(), EnumSet.noneOf(ModificationBoxBlock.Type.class));
    }

    public StorageCoreScreenHandler(
        int syncId,
        Inventory playerInventory,
        InfiniteInventory coreInventory,
        Set<ModificationBoxBlock.Type> modifications
    ) {
        this(EZRStorage.STORAGE_CORE_SCREEN_HANDLER, syncId, playerInventory, coreInventory, modifications);
    }

    protected StorageCoreScreenHandler(
        MenuType<?> type,
        int syncId,
        Inventory playerInventory,
        InfiniteInventory coreInventory,
        Set<ModificationBoxBlock.Type> modifications
    ) {
        super(type, syncId);
        this.coreInventory = coreInventory;
        this.modifications = modifications;
        this.player = playerInventory.player;

        final Container inventory = new SimpleContainer(rowCount() * 9);
        for (int row = 0; row < rowCount(); row++) {
            for (int column = 0; column < 9; column++) {
                addSlot(new Slot(inventory, column + row * 9, 8 + column * 18, 18 + row * 18));
            }
        }

        for (int row = 0; row < 3; row++) {
            for (int column = 0; column < 9; column++) {
                addInputSource(new Slot(playerInventory, column + row * 9 + 9, 8 + column * 18, playerInventoryY() + row * 18));
            }
        }

        for (int column = 0; column < 9; column++) {
            addInputSource(new Slot(playerInventory, column, 8 + column * 18, playerInventoryY() + 58));
        }
    }

    protected final void addInputSource(Slot slot) {
        inputSources.add(slot);
        addSlot(slot);
    }

    public List<Slot> getInputSources() {
        return inputSources;
    }

    @Override
    public void broadcastChanges() {
        super.broadcastChanges();
        sync();
    }

    @Override
    public void sendAllDataToRemote() {
        super.sendAllDataToRemote();
        sync();
    }

    private void sync() {
        if (player instanceof ServerPlayer serverPlayer) {
            syncToClient(serverPlayer);
        }
    }

    protected void syncToClient(ServerPlayer player) {
        final FriendlyByteBuf buf = PacketByteBufs.create();
        buf.writeByte(containerId);
        buf.writeNbt(coreInventory.writeNbt());
        MoreBufs.writeEnumSet(buf, modifications);
        ServerPlayNetworking.send(player, EZRStorage.SYNC_INVENTORY, buf);
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }

    @Override
    public boolean clickMenuButton(Player player, int id) {
        return switch (id) {
            case 0 -> {
                coreInventory.setSortType(coreInventory.getSortType().rotate());
                if (player instanceof ServerPlayer serverPlayer) {
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
    public ItemStack quickMoveStack(Player player, int index) {
        final Slot slot = slots.get(index);
        //noinspection ConstantValue
        if (slot != null && slot.hasItem()) {
            final ItemStack stack = slot.getItem();
            slot.set(coreInventory.moveFrom(stack));
        }
        if (player instanceof ServerPlayer serverPlayer) {
            syncToClient(serverPlayer);
        }
        return ItemStack.EMPTY;
    }

    @Override
    public void clicked(int slotIndex, int button, ClickType actionType, Player player) {
        if (slotIndex >= rowCount() * 9 || slotIndex < 0) {
            super.clicked(slotIndex, button, actionType, player);
            if (actionType == ClickType.QUICK_MOVE) {
                getCoreInventory().reSort();
            }
        }
    }

    public ItemStack customSlotClick(int slotIndex, ClickType actionType) {
        final ItemStack heldStack = getCarried();

        if (heldStack.isEmpty()) {
            final InfiniteItemStack infiniteStack = coreInventory.getStack(slotIndex);
            if (infiniteStack.isEmpty()) {
                return ItemStack.EMPTY;
            }
            final ItemStack stack = coreInventory.extractStack(infiniteStack);
            if (actionType == ClickType.QUICK_MOVE) {
                if (!moveItemStackTo(stack, rowCount() * 9, rowCount() * 9 + 36, true)) {
                    coreInventory.moveFrom(stack);
                }
            } else {
                setCarried(stack);
            }
            return stack;
        } else {
            setCarried(coreInventory.moveFrom(heldStack));
        }
        return ItemStack.EMPTY;
    }

    @Override
    public void removed(Player player) {
        super.removed(player);
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

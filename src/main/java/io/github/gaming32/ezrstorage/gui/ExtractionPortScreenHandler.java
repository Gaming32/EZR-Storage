package io.github.gaming32.ezrstorage.gui;

import io.github.gaming32.ezrstorage.EZRStorage;
import io.github.gaming32.ezrstorage.InfiniteInventory.ExtractListMode;
import io.github.gaming32.ezrstorage.registry.EZRBlockEntities;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.ContainerListener;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class ExtractionPortScreenHandler extends AbstractContainerMenu {
    private final SimpleContainer extractList;
    private final DataSlot listModeProperty;
    private final ContainerLevelAccess context;

    public ExtractionPortScreenHandler(int syncId, Inventory playerInventory) {
        this(syncId, playerInventory, new SimpleContainer(9), ContainerLevelAccess.NULL);
    }

    public ExtractionPortScreenHandler(
        int syncId,
        Inventory playerInventory,
        SimpleContainer extractList,
        ContainerLevelAccess context
    ) {
        super(EZRStorage.EXTRACTION_PORT_SCREEN_HANDLER, syncId);
        this.extractList = extractList;
        this.context = context;

        for (int i = 0; i < 9; i++) {
            addSlot(new Slot(extractList, i, 8 + i * 18, 20));
        }

        for (int row = 0; row < 3; row++) {
            for (int column = 0; column < 9; column++) {
                addSlot(new Slot(playerInventory, column + row * 9 + 9, 8 + column * 18, row * 18 + 69));
            }
        }
        for (int i = 0; i < 9; i++) {
            addSlot(new Slot(playerInventory, i, 8 + i * 18, 127));
        }

        listModeProperty = addDataSlot(DataSlot.standalone());
        context.execute((world, pos) ->
            world.getBlockEntity(pos, EZRBlockEntities.EXTRACTION_PORT).ifPresent(extractionPort ->
                listModeProperty.set(extractionPort.getListMode().ordinal())
            )
        );

        addSlotListener(new ContainerListener() {
            @Override
            public void slotChanged(AbstractContainerMenu handler, int slotId, ItemStack stack) {
                context.execute(Level::blockEntityChanged);
            }

            @Override
            public void dataChanged(AbstractContainerMenu handler, int property, int value) {
            }
        });
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }

    @Override
    public boolean clickMenuButton(Player player, int id) {
        if (id == 0) {
            final ExtractListMode newMode = getExtractListMode().rotate();
            listModeProperty.set(newMode.ordinal());
            context.execute((world, pos) ->
                world.getBlockEntity(pos, EZRBlockEntities.EXTRACTION_PORT).ifPresent(extractionPort -> {
                    extractionPort.setListMode(newMode);
                    extractionPort.setChanged();
                })
            );
        }
        return super.clickMenuButton(player, id);
    }

    public ExtractListMode getExtractListMode() {
        return ExtractListMode.values()[listModeProperty.get()];
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot = slots.get(index);
        //noinspection ConstantValue
        if (slot != null && slot.hasItem()) {
            ItemStack itemStack2 = slot.getItem();
            itemStack = itemStack2.copy();
            if (index < 9) {
                if (!moveItemStackTo(itemStack2, 9, slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!moveItemStackTo(itemStack2, 0, 9, false)) {
                return ItemStack.EMPTY;
            }

            if (itemStack2.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }

        return itemStack;
    }
}

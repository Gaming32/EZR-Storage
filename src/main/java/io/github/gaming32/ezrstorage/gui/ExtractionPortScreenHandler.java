package io.github.gaming32.ezrstorage.gui;

import io.github.gaming32.ezrstorage.EZRStorage;
import io.github.gaming32.ezrstorage.InfiniteInventory.ExtractListMode;
import io.github.gaming32.ezrstorage.registry.EZRBlockEntities;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.Property;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.ScreenHandlerListener;
import net.minecraft.screen.slot.Slot;
import net.minecraft.world.World;

public class ExtractionPortScreenHandler extends ScreenHandler {
    private final SimpleInventory extractList;
    private final Property listModeProperty;
    private final ScreenHandlerContext context;

    public ExtractionPortScreenHandler(int syncId, PlayerInventory playerInventory) {
        this(syncId, playerInventory, new SimpleInventory(9), ScreenHandlerContext.EMPTY);
    }

    public ExtractionPortScreenHandler(int syncId, PlayerInventory playerInventory, SimpleInventory extractList, ScreenHandlerContext context) {
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

        listModeProperty = addProperty(Property.create());
        context.run((world, pos) ->
            world.getBlockEntity(pos, EZRBlockEntities.EXTRACTION_PORT).ifPresent(extractionPort ->
                listModeProperty.set(extractionPort.getListMode().ordinal())
            )
        );

        addListener(new ScreenHandlerListener() {
            @Override
            public void onSlotUpdate(ScreenHandler handler, int slotId, ItemStack stack) {
                context.run(World::markDirty);
            }

            @Override
            public void onPropertyUpdate(ScreenHandler handler, int property, int value) {
            }
        });
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return true;
    }

    @Override
    public boolean onButtonClick(PlayerEntity player, int id) {
        if (id == 0) {
            final ExtractListMode newMode = getExtractListMode().rotate();
            listModeProperty.set(newMode.ordinal());
            context.run((world, pos) ->
                world.getBlockEntity(pos, EZRBlockEntities.EXTRACTION_PORT).ifPresent(extractionPort -> {
                    extractionPort.setListMode(newMode);
                    extractionPort.markDirty();
                })
            );
        }
        return super.onButtonClick(player, id);
    }

    public ExtractListMode getExtractListMode() {
        return ExtractListMode.values()[listModeProperty.get()];
    }

    @Override
    public ItemStack transferSlot(PlayerEntity player, int index) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot = slots.get(index);
        //noinspection ConstantValue
        if (slot != null && slot.hasStack()) {
            ItemStack itemStack2 = slot.getStack();
            itemStack = itemStack2.copy();
            if (index < 9) {
                if (!insertItem(itemStack2, 9, slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!insertItem(itemStack2, 0, 9, false)) {
                return ItemStack.EMPTY;
            }

            if (itemStack2.isEmpty()) {
                slot.setStack(ItemStack.EMPTY);
            } else {
                slot.markDirty();
            }
        }

        return itemStack;
    }
}

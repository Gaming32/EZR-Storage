package io.github.gaming32.ezrstorage.client.gui;

import io.github.gaming32.ezrstorage.gui.StorageCoreMenu;
import io.github.gaming32.ezrstorage.registry.EZRReg;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class StorageCoreScreenWithCrafting extends StorageCoreScreen {
    private static final ResourceLocation BACKGROUND = EZRReg.id("textures/gui/storage_crafting_gui.png");

    public StorageCoreScreenWithCrafting(StorageCoreMenu handler, Inventory inventory, Component title) {
        super(handler, inventory, title);
        imageWidth = 195;
        imageHeight = 244;
    }

    @Override
    protected void init() {
        super.init();
        craftClearButton.visible = true;
    }

    @Override
    protected ResourceLocation getBackground() {
        return BACKGROUND;
    }

    @Override
    protected int rowsVisible() {
        return 4;
    }
}

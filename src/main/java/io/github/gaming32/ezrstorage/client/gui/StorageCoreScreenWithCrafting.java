package io.github.gaming32.ezrstorage.client.gui;

import io.github.gaming32.ezrstorage.gui.StorageCoreScreenHandler;
import io.github.gaming32.ezrstorage.registry.EZRReg;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class StorageCoreScreenWithCrafting extends StorageCoreScreen {
    private static final Identifier BACKGROUND = EZRReg.id("textures/gui/storage_crafting_gui.png");

    public StorageCoreScreenWithCrafting(StorageCoreScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
        backgroundWidth = 195;
        backgroundHeight = 244;
    }

    @Override
    protected void init() {
        super.init();
        craftClearButton.visible = true;
    }

    @Override
    protected Identifier getBackground() {
        return BACKGROUND;
    }

    @Override
    protected int rowsVisible() {
        return 4;
    }
}

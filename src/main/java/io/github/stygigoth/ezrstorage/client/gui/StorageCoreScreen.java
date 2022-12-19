package io.github.stygigoth.ezrstorage.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import io.github.stygigoth.ezrstorage.gui.StorageCoreScreenHandler;
import io.github.stygigoth.ezrstorage.registry.EZRReg;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class StorageCoreScreen extends HandledScreen<StorageCoreScreenHandler> {
    private static final Identifier CREATIVE_INVENTORY_TAB = new Identifier("textures/gui/container/creative_inventory/tabs.png");
    private static final Identifier SEARCH_BAR = new Identifier("textures/gui/container/creative_inventory/tab_item_search.png");
    private static final Identifier SORT_GUI = EZRReg.id("textures/gui/custom_gui.png");
    private static final Identifier BACKGROUND = EZRReg.id("textures/gui/storage_scroll_gui.png");

    public StorageCoreScreen(StorageCoreScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
        backgroundWidth = 195;
        backgroundHeight = 222;
    }

    @Override
    protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1, 1, 1, 1);
        RenderSystem.setShaderTexture(0, BACKGROUND);
        final int x = (width - backgroundWidth) / 2;
        final int y = (height - backgroundHeight) / 2;
        drawTexture(matrices, x, y, 0, 0, 195, 222);
    }
}

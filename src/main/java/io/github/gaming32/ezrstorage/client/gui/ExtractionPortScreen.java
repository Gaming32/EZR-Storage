package io.github.gaming32.ezrstorage.client.gui;

import io.github.gaming32.ezrstorage.gui.ExtractionPortMenu;
import io.github.gaming32.ezrstorage.registry.EZRReg;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class ExtractionPortScreen extends AbstractContainerScreen<ExtractionPortMenu> {
    private static final ResourceLocation BACKGROUND = EZRReg.id("textures/gui/extract_port.png");

    private Button listModeButton;

    public ExtractionPortScreen(ExtractionPortMenu handler, Inventory inventory, Component title) {
        super(handler, inventory, title);
        imageWidth = 176;
        imageHeight = 151;
    }

    @Override
    protected void init() {
        super.init();
        //noinspection DataFlowIssue
        addRenderableWidget(listModeButton = Button.builder(
            Component.empty(),
            button -> minecraft.gameMode.handleInventoryButtonClick(menu.containerId, 0)
        ).pos(leftPos + 53, topPos + 42).width(70).build());
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float delta, int mouseX, int mouseY) {
        graphics.blit(BACKGROUND, leftPos, topPos, 0, 0, imageWidth, imageHeight);
        listModeButton.setMessage(Component.translatable(
            "extractListType." + menu.getExtractListMode().getSerializedName()));
    }

    @Override
    protected void renderLabels(GuiGraphics graphics, int mouseX, int mouseY) {
        graphics.drawString(font, title, titleLabelX, titleLabelY, 0x404040, false);
    }
}

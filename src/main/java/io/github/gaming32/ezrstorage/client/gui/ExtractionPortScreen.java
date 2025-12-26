package io.github.gaming32.ezrstorage.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import io.github.gaming32.ezrstorage.gui.ExtractionPortScreenHandler;
import io.github.gaming32.ezrstorage.registry.EZRReg;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class ExtractionPortScreen extends AbstractContainerScreen<ExtractionPortScreenHandler> {
    private static final ResourceLocation BACKGROUND = EZRReg.id("textures/gui/extract_port.png");

    private Button listModeButton;

    public ExtractionPortScreen(ExtractionPortScreenHandler handler, Inventory inventory, Component title) {
        super(handler, inventory, title);
        imageWidth = 176;
        imageHeight = 151;
    }

    @Override
    protected void init() {
        super.init();
        //noinspection DataFlowIssue
        addRenderableWidget(listModeButton = new Button(
            leftPos + 53, topPos + 42, 70, 20, Component.nullToEmpty(""),
            button -> minecraft.gameMode.handleInventoryButtonClick(menu.containerId, 0)
        ));
    }

    @Override
    public void render(PoseStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);
        super.render(matrices, mouseX, mouseY, delta);
        this.renderTooltip(matrices, mouseX, mouseY);
    }

    @Override
    protected void renderBg(PoseStack matrices, float delta, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1, 1, 1, 1);
        RenderSystem.setShaderTexture(0, BACKGROUND);
        blit(matrices, leftPos, topPos, 0, 0, imageWidth, imageHeight);

        listModeButton.setMessage(Component.translatable(
            "extractListType." + menu.getExtractListMode().getSerializedName()));
    }

    @Override
    protected void renderLabels(PoseStack matrices, int mouseX, int mouseY) {
        font.draw(matrices, title, (float) titleLabelX, (float) titleLabelY, 0x404040);
    }
}

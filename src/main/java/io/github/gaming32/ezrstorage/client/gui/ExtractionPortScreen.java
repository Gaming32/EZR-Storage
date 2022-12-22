package io.github.gaming32.ezrstorage.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import io.github.gaming32.ezrstorage.gui.ExtractionPortScreenHandler;
import io.github.gaming32.ezrstorage.registry.EZRReg;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;

public class ExtractionPortScreen extends HandledScreen<ExtractionPortScreenHandler> {
    private static final Identifier BACKGROUND = EZRReg.id("textures/gui/extract_port.png");

    private ButtonWidget listModeButton;

    public ExtractionPortScreen(ExtractionPortScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
        backgroundWidth = 176;
        backgroundHeight = 151;
    }

    @Override
    protected void init() {
        super.init();
        //noinspection DataFlowIssue
        addDrawableChild(listModeButton = new ButtonWidget(
            x + 53, y + 42, 70, 20, Text.of(""),
            button -> client.interactionManager.clickButton(handler.syncId, 0)
        ));
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);
        super.render(matrices, mouseX, mouseY, delta);
        this.drawMouseoverTooltip(matrices, mouseX, mouseY);
    }

    @Override
    protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1, 1, 1, 1);
        RenderSystem.setShaderTexture(0, BACKGROUND);
        drawTexture(matrices, x, y, 0, 0, backgroundWidth, backgroundHeight);

        listModeButton.setMessage(new TranslatableText("extractListType." + handler.getExtractListMode().asString()));
    }

    @Override
    protected void drawForeground(MatrixStack matrices, int mouseX, int mouseY) {
        textRenderer.draw(matrices, title, (float)titleX, (float)titleY, 0x404040);
    }
}

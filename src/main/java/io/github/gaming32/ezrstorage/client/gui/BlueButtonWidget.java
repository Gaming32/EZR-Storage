package io.github.gaming32.ezrstorage.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import io.github.gaming32.ezrstorage.registry.EZRReg;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

public class BlueButtonWidget extends ButtonWidget {
    private static final Identifier TEXTURE = EZRReg.id("textures/gui/custom_gui.png");

    public BlueButtonWidget(int x, int y, int width, int height, Text message, PressAction onPress) {
        super(x, y, width, height, message, onPress);
        visible = false;
    }

    @Override
    public void renderButton(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        MinecraftClient minecraftClient = MinecraftClient.getInstance();
        TextRenderer textRenderer = minecraftClient.textRenderer;
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, TEXTURE);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, this.alpha);
        int i = this.getYImage(this.isHovered());
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.enableDepthTest();
        this.drawTexture(matrices, this.x, this.y, 0, i * 14, this.width / 2, this.height);
        this.drawTexture(matrices, this.x + this.width / 2, this.y, 256 - this.width / 2, i * 14, this.width / 2, this.height);
        this.renderBackground(matrices, minecraftClient, mouseX, mouseY);
        int j = this.active ? 16777215 : 10526880;
        drawCenteredText(
            matrices, textRenderer, this.getMessage(), this.x + this.width / 2, this.y + (this.height - 8) / 2, j | MathHelper.ceil(this.alpha * 255.0F) << 24
        );

        if (this.isHovered()) {
            this.renderTooltip(matrices, mouseX, mouseY);
        }
    }
}

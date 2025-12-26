package io.github.gaming32.ezrstorage.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import io.github.gaming32.ezrstorage.registry.EZRReg;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Button.OnPress;
import net.minecraft.client.renderer.GameRenderer;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class BlueButtonWidget extends Button {
    private static final ResourceLocation TEXTURE = EZRReg.id("textures/gui/custom_gui.png");

    public BlueButtonWidget(int x, int y, int width, int height, Component message, OnPress onPress) {
        super(x, y, width, height, message, onPress);
        visible = false;
    }

    @Override
    public void renderButton(PoseStack matrices, int mouseX, int mouseY, float delta) {
        Minecraft minecraftClient = Minecraft.getInstance();
        Font textRenderer = minecraftClient.font;
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, TEXTURE);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, this.alpha);
        int i = this.getYImage(this.isHoveredOrFocused());
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.enableDepthTest();
        this.blit(matrices, this.x, this.y, 0, i * 14, this.width / 2, this.height);
        this.blit(matrices, this.x + this.width / 2, this.y, 256 - this.width / 2, i * 14, this.width / 2, this.height);
        this.renderBg(matrices, minecraftClient, mouseX, mouseY);
        int j = this.active ? 16777215 : 10526880;
        drawCenteredString(
            matrices, textRenderer, this.getMessage(), this.x + this.width / 2, this.y + (this.height - 8) / 2, j | Mth.ceil(this.alpha * 255.0F) << 24
        );

        if (this.isHoveredOrFocused()) {
            this.renderToolTip(matrices, mouseX, mouseY);
        }
    }
}

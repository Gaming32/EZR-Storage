package io.github.gaming32.ezrstorage.client.gui;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.components.Checkbox;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class ResizableTooltipOnlyCheckboxWidget extends Checkbox {
    private static final ResourceLocation TEXTURE = new ResourceLocation("textures/gui/checkbox.png");

    private final Screen screen;

    public ResizableTooltipOnlyCheckboxWidget(int x, int y, int width, int height, Component message, boolean checked, Screen screen) {
        super(x, y, width, height, message, checked);
        this.screen = screen;
    }

    @Override
    public void renderToolTip(PoseStack matrices, int mouseX, int mouseY) {
        screen.renderTooltip(matrices, getMessage(), mouseX, mouseY);
    }

    @Override
    public void renderButton(PoseStack matrices, int mouseX, int mouseY, float delta) {
        Minecraft minecraftClient = Minecraft.getInstance();
        RenderSystem.setShaderTexture(0, TEXTURE);
        RenderSystem.enableDepthTest();
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, alpha);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        matrices.pushPose();
        final float xScale = width / 20f;
        final float yScale = width / 20f;
        matrices.scale(xScale, yScale, 1f);
        blit(matrices, (int)(x / xScale), (int)(y / yScale), isFocused() ? 20.0F : 0.0F, selected() ? 20.0F : 0.0F, 20, 20, 64, 64);
        matrices.popPose();
        renderBg(matrices, minecraftClient, mouseX, mouseY);
        if (isHoveredOrFocused()) {
            renderToolTip(matrices, mouseX, mouseY);
        }
    }
}

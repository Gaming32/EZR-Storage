package io.github.gaming32.ezrstorage.client.gui;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.CheckboxWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ResizableTooltipOnlyCheckboxWidget extends CheckboxWidget {
    private static final Identifier TEXTURE = new Identifier("textures/gui/checkbox.png");

    private final Screen screen;

    public ResizableTooltipOnlyCheckboxWidget(int x, int y, int width, int height, Text message, boolean checked, Screen screen) {
        super(x, y, width, height, message, checked);
        this.screen = screen;
    }

    @Override
    public void renderTooltip(MatrixStack matrices, int mouseX, int mouseY) {
        screen.renderTooltip(matrices, getMessage(), mouseX, mouseY);
    }

    @Override
    public void renderButton(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        MinecraftClient minecraftClient = MinecraftClient.getInstance();
        RenderSystem.setShaderTexture(0, TEXTURE);
        RenderSystem.enableDepthTest();
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, alpha);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.blendFunc(GlStateManager.SrcFactor.SRC_ALPHA, GlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA);
        matrices.push();
        final float xScale = width / 20f;
        final float yScale = width / 20f;
        matrices.scale(xScale, yScale, 1f);
        drawTexture(matrices, (int)(x / xScale), (int)(y / yScale), isFocused() ? 20.0F : 0.0F, isChecked() ? 20.0F : 0.0F, 20, 20, 64, 64);
        matrices.pop();
        renderBackground(matrices, minecraftClient, mouseX, mouseY);
        if (isHovered()) {
            renderTooltip(matrices, mouseX, mouseY);
        }
    }
}

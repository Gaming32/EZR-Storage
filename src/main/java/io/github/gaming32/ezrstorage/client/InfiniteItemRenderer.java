package io.github.gaming32.ezrstorage.client;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.player.LocalPlayer;
import com.mojang.blaze3d.vertex.BufferBuilder;
import net.minecraft.client.renderer.LightTexture;
import com.mojang.blaze3d.vertex.Tesselator;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.world.item.ItemStack;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.Nullable;

public class InfiniteItemRenderer extends ItemRenderer {
    public InfiniteItemRenderer() {
        super(null, null, null, null);
    }

    @Override
    public void renderGuiItemDecorations(Font renderer, ItemStack stack, int x, int y, @Nullable String countLabel) {
        if (countLabel == null) {
            countLabel = Integer.toString(stack.getCount());
        }
        if (!stack.isEmpty()) {
            final long count = Long.parseLong(countLabel);
            PoseStack matrixStack = new PoseStack();
            if (count != 0) {
                String string = String.valueOf(Math.abs(count));
                if (count > 999999999999L) {
                    string = String.valueOf((int) Math.floor(count / 1000000000000.0)) + 'T';
                } else if (count > 9999999999L) {
                    string = "." + (int)Math.floor(count / 1000000000000.0) + 'T';
                } else if (count > 999999999L) {
                    string = String.valueOf((int) Math.floor(count / 1000000000.0)) + 'B';
                } else if (count > 99999999L) {
                    string = "." + (int) Math.floor(count / 100000000.0) + 'B';
                } else if (count > 999999L) {
                    string = String.valueOf((int) Math.floor(count / 1000000.0)) + 'M';
                } else if (count > 99999L) {
                    string = "." + (int) Math.floor(count / 100000.0) + 'M';
                } else if (count > 9999L) {
                    string = String.valueOf((int) Math.floor(count / 1000.0)) + 'K';
                }

                matrixStack.translate(0.0, 0.0, this.blitOffset + 200.0F);
                matrixStack.scale(0.5f, 0.5f, 0.5f);
                MultiBufferSource.BufferSource immediate = MultiBufferSource.immediate(Tesselator.getInstance().getBuilder());
                renderer.drawInBatch(
                    string,
                    (float)(x + 19 - 2 - renderer.width(string) * 0.5) * 2,
                    (float)(y + 6 + 3 + 3.5) * 2,
                    16777215,
                    true,
                    matrixStack.last().pose(),
                    immediate,
                    false,
                    0,
                    LightTexture.FULL_BRIGHT
                );
                immediate.endBatch();
            }

            if (stack.isBarVisible()) {
                RenderSystem.disableDepthTest();
                RenderSystem.disableTexture();
                RenderSystem.disableBlend();
                Tesselator tessellator = Tesselator.getInstance();
                BufferBuilder bufferBuilder = tessellator.getBuilder();
                int i = stack.getBarWidth();
                int j = stack.getBarColor();
                this.fillRect(bufferBuilder, x + 2, y + 13, 13, 2, 0, 0, 0, 255);
                this.fillRect(bufferBuilder, x + 2, y + 13, i, 1, j >> 16 & 0xFF, j >> 8 & 0xFF, j & 0xFF, 255);
                RenderSystem.enableBlend();
                RenderSystem.enableTexture();
                RenderSystem.enableDepthTest();
            }

            LocalPlayer clientPlayerEntity = Minecraft.getInstance().player;
            float f = clientPlayerEntity == null
                ? 0.0F
                : clientPlayerEntity.getCooldowns().getCooldownPercent(stack.getItem(), Minecraft.getInstance().getFrameTime());
            if (f > 0.0F) {
                RenderSystem.disableDepthTest();
                RenderSystem.disableTexture();
                RenderSystem.enableBlend();
                RenderSystem.defaultBlendFunc();
                Tesselator tessellator2 = Tesselator.getInstance();
                BufferBuilder bufferBuilder2 = tessellator2.getBuilder();
                this.fillRect(bufferBuilder2, x, y + Mth.floor(16.0F * (1.0F - f)), 16, Mth.ceil(16.0F * f), 255, 255, 255, 127);
                RenderSystem.enableTexture();
                RenderSystem.enableDepthTest();
            }
        }
    }
}

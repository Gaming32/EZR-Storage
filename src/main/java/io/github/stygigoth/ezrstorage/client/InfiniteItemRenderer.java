package io.github.stygigoth.ezrstorage.client;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.Nullable;

public class InfiniteItemRenderer extends ItemRenderer {
    public InfiniteItemRenderer() {
        super(null, null, null, null);
    }

    @Override
    public void renderGuiItemOverlay(TextRenderer renderer, ItemStack stack, int x, int y, @Nullable String countLabel) {
        if (countLabel == null) {
            countLabel = Integer.toString(stack.getCount());
        }
        if (!stack.isEmpty()) {
            final long count = Long.parseLong(countLabel);
            MatrixStack matrixStack = new MatrixStack();
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

                matrixStack.translate(0.0, 0.0, this.zOffset + 200.0F);
                matrixStack.scale(0.5f, 0.5f, 0.5f);
                VertexConsumerProvider.Immediate immediate = VertexConsumerProvider.immediate(Tessellator.getInstance().getBuffer());
                renderer.draw(
                    string,
                    (float)(x + 19 - 2 - renderer.getWidth(string) * 0.5) * 2,
                    (float)(y + 6 + 3 + 3.5) * 2,
                    16777215,
                    true,
                    matrixStack.peek().getPositionMatrix(),
                    immediate,
                    false,
                    0,
                    LightmapTextureManager.MAX_LIGHT_COORDINATE
                );
                immediate.draw();
            }

            if (stack.isItemBarVisible()) {
                RenderSystem.disableDepthTest();
                RenderSystem.disableTexture();
                RenderSystem.disableBlend();
                Tessellator tessellator = Tessellator.getInstance();
                BufferBuilder bufferBuilder = tessellator.getBuffer();
                int i = stack.getItemBarStep();
                int j = stack.getItemBarColor();
                this.renderGuiQuad(bufferBuilder, x + 2, y + 13, 13, 2, 0, 0, 0, 255);
                this.renderGuiQuad(bufferBuilder, x + 2, y + 13, i, 1, j >> 16 & 0xFF, j >> 8 & 0xFF, j & 0xFF, 255);
                RenderSystem.enableBlend();
                RenderSystem.enableTexture();
                RenderSystem.enableDepthTest();
            }

            ClientPlayerEntity clientPlayerEntity = MinecraftClient.getInstance().player;
            float f = clientPlayerEntity == null
                ? 0.0F
                : clientPlayerEntity.getItemCooldownManager().getCooldownProgress(stack.getItem(), MinecraftClient.getInstance().getTickDelta());
            if (f > 0.0F) {
                RenderSystem.disableDepthTest();
                RenderSystem.disableTexture();
                RenderSystem.enableBlend();
                RenderSystem.defaultBlendFunc();
                Tessellator tessellator2 = Tessellator.getInstance();
                BufferBuilder bufferBuilder2 = tessellator2.getBuffer();
                this.renderGuiQuad(bufferBuilder2, x, y + MathHelper.floor(16.0F * (1.0F - f)), 16, MathHelper.ceil(16.0F * f), 255, 255, 255, 127);
                RenderSystem.enableTexture();
                RenderSystem.enableDepthTest();
            }
        }
    }
}

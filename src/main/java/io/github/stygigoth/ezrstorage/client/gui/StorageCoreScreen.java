package io.github.stygigoth.ezrstorage.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import io.github.stygigoth.ezrstorage.InfiniteItemStack;
import io.github.stygigoth.ezrstorage.client.InfiniteItemRenderer;
import io.github.stygigoth.ezrstorage.gui.StorageCoreScreenHandler;
import io.github.stygigoth.ezrstorage.registry.EZRReg;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.text.DecimalFormat;

public class StorageCoreScreen extends HandledScreen<StorageCoreScreenHandler> {
    private static final Identifier CREATIVE_INVENTORY_TABS = new Identifier("textures/gui/container/creative_inventory/tabs.png");
    private static final Identifier SEARCH_BAR = new Identifier("textures/gui/container/creative_inventory/tab_item_search.png");
    private static final Identifier SORT_GUI = EZRReg.id("textures/gui/custom_gui.png");
    private static final Identifier BACKGROUND = EZRReg.id("textures/gui/storage_scroll_gui.png");

    private float currentScroll = 0f;
    private int scrollRow = 0;
    private InfiniteItemRenderer infiniteItemRenderer;

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

    @Override
    protected void drawForeground(MatrixStack matrices, int mouseX, int mouseY) {
        final DecimalFormat formatter = new DecimalFormat("#,###");
        final String totalCount = formatter.format(handler.getCoreInventory().getCount());
        final String max = formatter.format(handler.getCoreInventory().getMaxCount());
        final String amount = totalCount + '/' + max;

        final int stringWidth = textRenderer.getWidth(amount);

        if (stringWidth > 88) {
            final float scaleFactor = 0.7f;
            final float rScaleFactor = 1 / scaleFactor;
            matrices.push();
            matrices.scale(scaleFactor, scaleFactor, scaleFactor);
            final int x = (int)((187 - stringWidth * scaleFactor) * rScaleFactor);
            textRenderer.draw(matrices, amount, x, 10, 0x404040);
            matrices.pop();
        } else {
            textRenderer.draw(matrices, amount, 187 - stringWidth, 6, 0x404040);
        }

        setZOffset(100);
        itemRenderer.zOffset = 100f;
        if (infiniteItemRenderer == null) {
            infiniteItemRenderer = new InfiniteItemRenderer();
        }
        infiniteItemRenderer.zOffset = 100f;

        outer:
        for (int row = 0, y = 8; row < 6; row++, y += 18) {
            for (int column = 0, x = 8; column < 9; column++, x += 18) {
                final int index = scrollRow * 9 + row * 9 + column;
                if (index >= handler.getCoreInventory().getUniqueCount()) {
                    break outer;
                }

                final InfiniteItemStack infiniteStack = handler.getCoreInventory().getStack(index);
                final ItemStack stack = infiniteStack.toItemStack();

                itemRenderer.renderInGui(stack, x, y);
                infiniteItemRenderer.renderGuiItemOverlay(textRenderer, stack, x, y, Long.toString(infiniteStack.getCount()));

                x += 18;
            }
        }

        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, CREATIVE_INVENTORY_TABS);
        final int i = 175;
        final int j = 18;
        final int k = j + 108;
        drawTexture(matrices, i, j + (int)((float)(k - j - 17) * currentScroll), 232, 0, 12, 15);
        setZOffset(0);
        itemRenderer.zOffset = 0f;
    }
}

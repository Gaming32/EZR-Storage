package io.github.stygigoth.ezrstorage.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import io.github.stygigoth.ezrstorage.EZRStorage;
import io.github.stygigoth.ezrstorage.InfiniteItemStack;
import io.github.stygigoth.ezrstorage.client.InfiniteItemRenderer;
import io.github.stygigoth.ezrstorage.gui.StorageCoreScreenHandler;
import io.github.stygigoth.ezrstorage.registry.EZRReg;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

import java.text.DecimalFormat;
import java.util.List;

public class StorageCoreScreen extends HandledScreen<StorageCoreScreenHandler> {
    public static final DecimalFormat COUNT_FORMATTER = new DecimalFormat("#,###");
    private static final Identifier CREATIVE_INVENTORY_TABS = new Identifier("textures/gui/container/creative_inventory/tabs.png");
    private static final Identifier SEARCH_BAR = new Identifier("textures/gui/container/creative_inventory/tab_item_search.png");
    private static final Identifier SORT_GUI = EZRReg.id("textures/gui/custom_gui.png");
    private static final Identifier BACKGROUND = EZRReg.id("textures/gui/storage_scroll_gui.png");

    private boolean scrolling;
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
        final String totalCount = COUNT_FORMATTER.format(handler.getCoreInventory().getCount());
        final String max = COUNT_FORMATTER.format(handler.getCoreInventory().getMaxCount());
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
        for (int row = 0, y = 18; row < 6; row++, y += 18) {
            for (int column = 0, x = 8; column < 9; column++, x += 18) {
                final int index = scrollRow * 9 + row * 9 + column;
                if (index >= handler.getCoreInventory().getUniqueCount()) {
                    break outer;
                }

                final InfiniteItemStack infiniteStack = handler.getCoreInventory().getStack(index);
                final ItemStack stack = infiniteStack.toItemStack();

                itemRenderer.renderInGui(stack, x, y);
                infiniteItemRenderer.renderGuiItemOverlay(textRenderer, stack, x, y, Long.toString(infiniteStack.getCount()));
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

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        super.render(matrices, mouseX, mouseY, delta);
        final Integer slot = getSlotAt(mouseX, mouseY);
        if (slot != null) {
            int index;
            if (slot < handler.getCoreInventory().getUniqueCount()) {
                final InfiniteItemStack infiniteStack = handler.getCoreInventory().getStack(slot);
                if (!infiniteStack.isEmpty()) {
                    index = handler.getCoreInventory().indexOf(infiniteStack);
                    if (index < 0) {
                        return;
                    }
                    renderTooltip(matrices, infiniteStack, mouseX, mouseY);
                }
            }
        }
        drawMouseoverTooltip(matrices, mouseX, mouseY);
    }

    protected void renderTooltip(MatrixStack matrices, InfiniteItemStack infiniteStack, int x, int y) {
        final ItemStack stack = infiniteStack.toItemStack();
        assert client != null;
        final List<Text> lines = getTooltipFromItem(stack);
        lines.add(
            new LiteralText("Count: " + COUNT_FORMATTER.format(infiniteStack.getCount()))
                .styled(style -> style.withItalic(true))
        );
        renderTooltip(matrices, lines, x, y);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == 0 && this.isClickInScrollbar(mouseX, mouseY)) {
            this.scrolling = true;
            return true;
        }

        final Integer slot = getSlotAt((int)mouseX, (int)mouseY);
        if (slot != null) {
            final SlotActionType mode = hasShiftDown() ? SlotActionType.QUICK_MOVE : SlotActionType.PICKUP;
            int index = handler.getCoreInventory().getUniqueCount();
            if (slot < handler.getCoreInventory().getUniqueCount()) {
                final InfiniteItemStack infiniteStack = handler.getCoreInventory().getStack(slot);
                if (!infiniteStack.isEmpty()) {
                    index = handler.getCoreInventory().indexOf(infiniteStack);
                    if (index < 0) {
                        return false;
                    }
                }
            }
            assert client != null;
            handler.customSlotClick(index, button, mode, client.player);
            final PacketByteBuf buf = PacketByteBufs.create();
            buf.writeByte(handler.syncId);
            buf.writeVarInt(index);
            buf.writeVarInt(button);
            buf.writeEnumConstant(mode);
            ClientPlayNetworking.send(EZRStorage.CUSTOM_SLOT_CLICK, buf);
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (button == 0) {
            this.scrolling = false;
        }

        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (this.scrolling) {
            int i = this.y + 18;
            int j = i + 112;
            this.currentScroll = ((float)mouseY - (float)i - 7.5F) / ((float)(j - i) - 15.0F);
            this.currentScroll = MathHelper.clamp(this.currentScroll, 0.0F, 1.0F);
            scrollTo(this.currentScroll);
            return true;
        }
        return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
        int i = (this.handler.getCoreInventory().getUniqueCount() + 9 - 1) / 9 - 5;
        float f = (float)(amount / (double)i);
        this.currentScroll = MathHelper.clamp(this.currentScroll - f, 0.0F, 1.0F);
        scrollTo(this.currentScroll);
        return true;
    }

    protected boolean isClickInScrollbar(double mouseX, double mouseY) {
        int i = this.x;
        int j = this.y;
        int k = i + 175;
        int l = j + 18;
        int m = k + 14;
        int n = l + 112;
        return mouseX >= (double)k && mouseY >= (double)l && mouseX < (double)m && mouseY < (double)n;
    }

    private Integer getSlotAt(int x, int y) {
        final int startX = this.x + 7;
        final int startY = this.y + 17;

        final int clickedX = x - startX;
        final int clickedY = y - startY;

        if (clickedX >= 0 && clickedY >= 0) {
            final int column = clickedX / 18;
            if (column < 9) {
                final int row = clickedY / 18;
                if (row < 6) {
                    return scrollRow * 9 + row * 9 + column;
                }
            }
        }
        return null;
    }

    private void scrollTo(float scroll) {
        int i = (this.handler.getCoreInventory().getUniqueCount() + 8) / 9 - 6;
        int j = (int) (scroll * i + 0.5D);
        if (j < 0) {
            j = 0;
        }
        this.scrollRow = j;
    }
}

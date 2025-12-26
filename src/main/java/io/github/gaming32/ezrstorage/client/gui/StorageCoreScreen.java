package io.github.gaming32.ezrstorage.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.screen.EmiScreenManager;
import dev.emi.emi.search.EmiSearch;
import io.github.gaming32.ezrstorage.EZRStorage;
import io.github.gaming32.ezrstorage.InfiniteItemStack;
import io.github.gaming32.ezrstorage.block.ModificationBoxBlock;
import io.github.gaming32.ezrstorage.client.InfiniteItemRenderer;
import io.github.gaming32.ezrstorage.gui.StorageCoreScreenHandler;
import io.github.gaming32.ezrstorage.registry.EZRReg;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Checkbox;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.core.Registry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.item.ItemStack;

public class StorageCoreScreen extends AbstractContainerScreen<StorageCoreScreenHandler> {
    public static final DecimalFormat COUNT_FORMATTER = new DecimalFormat("#,###");
    private static final ResourceLocation CREATIVE_INVENTORY_TABS = new ResourceLocation(
        "textures/gui/container/creative_inventory/tabs.png");
    private static final ResourceLocation SEARCH_BAR = new ResourceLocation(
        "textures/gui/container/creative_inventory/tab_item_search.png");
    private static final ResourceLocation SORT_GUI = EZRReg.id("textures/gui/custom_gui.png");
    private static final ResourceLocation BACKGROUND = EZRReg.id("textures/gui/storage_scroll_gui.png");
    private static final boolean SUPPORT_EMI_SYNC = FabricLoader.getInstance().isModLoaded("emi");

    private boolean scrolling;
    private float currentScroll = 0f;
    private int scrollRow = 0;
    private InfiniteItemRenderer infiniteItemRenderer;

    private final List<InfiniteItemStack> filteredItems = new ArrayList<>();

    private EditBox searchField;
    private Button sortTypeSelector;
    protected Button craftClearButton;
    protected Checkbox emiSyncButton;

    public StorageCoreScreen(StorageCoreScreenHandler handler, Inventory inventory, Component title) {
        super(handler, inventory, title);
        imageWidth = 195;
        imageHeight = 222;
        handler.setUpdateNotification(() -> {
            if (emiSyncButton.selected()) {
                searchField.setValue(EmiScreenManager.search.getValue());
            }
            if (!skipSearch()) {
                updateFilteredItems();
            }
        });
    }

    @Override
    protected void init() {
        super.init();

        searchField = new EditBox(font, leftPos + 10, topPos + 6, 80, font.lineHeight, Component.nullToEmpty(""));
        searchField.setMaxLength(20);
        searchField.setBordered(false);
        searchField.setCanLoseFocus(true);
        searchField.setTextColor(0xffffff);
        searchField.setFocus(true);
        searchField.setValue("");
        addRenderableWidget(searchField);

        //noinspection DataFlowIssue
        addRenderableWidget(sortTypeSelector = new Button(
            leftPos - 100, topPos + 16, 90, 20, Component.nullToEmpty(""),
            button -> minecraft.gameMode.handleInventoryButtonClick(menu.containerId, 0)
        ));
        sortTypeSelector.visible = false;

        //noinspection DataFlowIssue
        addRenderableWidget(craftClearButton = new BlueButtonWidget(
            leftPos + 20, topPos + 100, 14, 14, Component.nullToEmpty("x"),
            button -> minecraft.gameMode.handleInventoryButtonClick(menu.containerId, 1)
        ));
        craftClearButton.visible = false;

        addRenderableWidget(emiSyncButton = new ResizableTooltipOnlyCheckboxWidget(
            searchField.x + searchField.getWidth(), searchField.y - 2,
            searchField.getHeight() + 2, searchField.getHeight() + 2,
            Component.translatable("ezrstorage.emiSync"), SUPPORT_EMI_SYNC, this
        ));
        emiSyncButton.visible = SUPPORT_EMI_SYNC;
        if (SUPPORT_EMI_SYNC) {
            searchField.setWidth(searchField.getWidth() - searchField.getHeight());
        }
    }

    @Override
    protected void renderBg(PoseStack matrices, float delta, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1, 1, 1, 1);
        RenderSystem.setShaderTexture(0, getBackground());
        blit(matrices, leftPos, topPos, 0, 0, imageWidth, imageHeight);

        sortTypeSelector.visible = menu.getModifications().contains(ModificationBoxBlock.Type.SORTING);
        if (sortTypeSelector.visible) {
            RenderSystem.setShaderTexture(0, SORT_GUI);
            blit(matrices, leftPos - 108, topPos, 0, 128, 112, 128);
        }

        searchField.visible = menu.getModifications().contains(ModificationBoxBlock.Type.SEARCH);
        emiSyncButton.visible = searchField.visible && SUPPORT_EMI_SYNC;
        if (searchField.visible) {
            RenderSystem.setShaderTexture(0, SEARCH_BAR);
            blit(matrices, leftPos + 8, topPos + 4, 80, 4, searchField.getInnerWidth() + 10, 12);
        }
    }

    @Override
    protected void renderLabels(PoseStack matrices, int mouseX, int mouseY) {
        final String totalCount = COUNT_FORMATTER.format(menu.getCoreInventory().getCount());
        final String max = COUNT_FORMATTER.format(menu.getCoreInventory().getMaxCount());
        final String amount = totalCount + '/' + max;

        final int stringWidth = font.width(amount);

        if (stringWidth > 88) {
            final float scaleFactor = 0.7f;
            final float rScaleFactor = 1 / scaleFactor;
            matrices.pushPose();
            matrices.scale(scaleFactor, scaleFactor, scaleFactor);
            final int x = (int) ((187 - stringWidth * scaleFactor) * rScaleFactor);
            font.draw(matrices, amount, x, 10, 0x404040);
            matrices.popPose();
        } else {
            font.draw(matrices, amount, 187 - stringWidth, 6, 0x404040);
        }

        if (sortTypeSelector.visible) {
            sortTypeSelector.setMessage(Component.translatable(
                "sortType." + menu.getCoreInventory().getSortType().getSerializedName()));
            font.draw(matrices, Component.translatable("sortType"), -100, 6, 0x404040);
            matrices.pushPose();
            matrices.scale(0.7f, 0.7f, 0.7f);
            int drawY = (int) (42 / 0.7);
            for (final FormattedCharSequence line : font.split(
                Component.translatable(
                    "sortType." + menu.getCoreInventory().getSortType().getSerializedName() + ".desc"), (int) (96 / 0.7)
            )) {
                font.draw(matrices, line, (int) (-100 / 0.7), drawY, 0x404040);
                drawY += 9;
            }
            matrices.popPose();
        }

        setBlitOffset(100);
        itemRenderer.blitOffset = 100f;
        if (infiniteItemRenderer == null) {
            infiniteItemRenderer = new InfiniteItemRenderer();
        }
        infiniteItemRenderer.blitOffset = 100f;

        outer:
        for (int row = 0, y = 18; row < rowsVisible(); row++, y += 18) {
            for (int column = 0, x = 8; column < 9; column++, x += 18) {
                final int index = scrollRow * 9 + row * 9 + column;
                if (index >= getSlotCount()) {
                    break outer;
                }

                final InfiniteItemStack infiniteStack = getSlot(index);
                final ItemStack stack = infiniteStack.toItemStack();

                itemRenderer.renderAndDecorateFakeItem(stack, x, y);
                infiniteItemRenderer.renderGuiItemDecorations(
                    font,
                    stack,
                    x,
                    y,
                    Long.toString(infiniteStack.getCount())
                );
            }
        }

        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, CREATIVE_INVENTORY_TABS);
        final int i = 175;
        final int j = 18;
        final int k = j + 108;
        blit(matrices, i, j + (int) ((float) (k - j - 17) * currentScroll), 232, 0, 12, 15);
        setBlitOffset(0);
        itemRenderer.blitOffset = 0f;
    }

    @Override
    public void render(PoseStack matrices, int mouseX, int mouseY, float delta) {
        renderBackground(matrices);
        super.render(matrices, mouseX, mouseY, delta);
        final Integer slot = getSlotAt(mouseX, mouseY);
        if (slot != null) {
            int index;
            if (slot < getSlotCount()) {
                final InfiniteItemStack infiniteStack = getSlot(slot);
                if (!infiniteStack.isEmpty()) {
                    index = menu.getCoreInventory().indexOf(infiniteStack);
                    if (index < 0) {
                        return;
                    }
                    renderTooltip(matrices, infiniteStack, mouseX, mouseY);
                }
            }
        }
        renderTooltip(matrices, mouseX, mouseY);
    }

    protected void renderTooltip(PoseStack matrices, InfiniteItemStack infiniteStack, int x, int y) {
        final ItemStack stack = infiniteStack.toItemStack();
        assert minecraft != null;
        final List<Component> lines = getTooltipFromItem(stack);
        lines.add(
            Component.literal("Count: " + COUNT_FORMATTER.format(infiniteStack.getCount()))
                .withStyle(style -> style.withItalic(true))
        );
        renderComponentTooltip(matrices, lines, x, y);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (
            menu.getModifications().contains(ModificationBoxBlock.Type.SEARCH) &&
            searchField.isFocused() &&
            searchField.keyPressed(keyCode, scanCode, modifiers)
        ) {
            searchBoxChange(null);
            return true;
        }
        assert minecraft != null;
        if (minecraft.options.keyInventory.matches(keyCode, scanCode)) return false;
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean charTyped(char chr, int modifiers) {
        if (
            menu.getModifications().contains(ModificationBoxBlock.Type.SEARCH) &&
            searchField.isFocused() &&
            searchField.charTyped(chr, modifiers)
        ) {
            searchBoxChange(null);
            return true;
        }
        return super.charTyped(chr, modifiers);
    }

    private void searchBoxChange(String newText) {
        if (newText != null) {
            searchField.setValue(newText);
        }

        if (emiSyncButton.selected()) {
            EmiScreenManager.search.setValue(searchField.getValue());
            return;
        }

        updateFilteredItems();
        scrollTo(currentScroll = 0f);
    }

    private void updateFilteredItems() {
        filteredItems.clear();

        if (emiSyncButton.selected()) {
            final Set<ResourceLocation> allowedItems = EmiSearch.stacks.stream()
                .flatMap(s -> s.getEmiStacks().stream())
                .map(EmiStack::getId)
                .collect(Collectors.toSet());
            StreamSupport.stream(menu.getCoreInventory().spliterator(), false)
                .filter(s -> allowedItems.contains(Registry.ITEM.getKey(s.getItem())))
                .forEach(filteredItems::add);
            return;
        }

        final String searchText = searchField.getValue().toLowerCase();

        if (searchText.isEmpty()) return;

        stackIter:
        for (final InfiniteItemStack stack : menu.getCoreInventory()) {
            if (stack.getItem().getDescription().getString().toLowerCase().contains(searchText)) {
                filteredItems.add(stack);
                continue;
            }
            for (final Component line : getTooltipFromItem(stack.toItemStack())) {
                if (line.getString().toLowerCase().contains(searchText)) {
                    filteredItems.add(stack);
                    continue stackIter;
                }
            }
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == 0 && this.isClickInScrollbar(mouseX, mouseY)) {
            this.scrolling = true;
            return true;
        }

        final Integer slot = getSlotAt((int) mouseX, (int) mouseY);
        if (slot != null) {
            final ClickType mode = hasShiftDown() ? ClickType.QUICK_MOVE : ClickType.PICKUP;
            int index = menu.getCoreInventory().getUniqueCount();
            if (slot < getSlotCount()) {
                final InfiniteItemStack infiniteStack = getSlot(slot);
                if (!infiniteStack.isEmpty()) {
                    index = menu.getCoreInventory().indexOf(infiniteStack);
                    if (index < 0) {
                        return false;
                    }
                }
            }
            assert minecraft != null;
            menu.customSlotClick(index, mode);
            final FriendlyByteBuf buf = PacketByteBufs.create();
            buf.writeByte(menu.containerId);
            buf.writeVarInt(index);
            buf.writeEnum(mode);
            ClientPlayNetworking.send(EZRStorage.CUSTOM_SLOT_CLICK, buf);
        } else {
            final int elementX = searchField.x;
            final int elementY = searchField.y;
            if (
                mouseX >= elementX && mouseX <= elementX + searchField.getWidth() &&
                mouseY >= elementY && mouseY <= elementY + searchField.getHeight()
            ) {
                if (button == 1 || hasShiftDown()) {
                    searchBoxChange("");
                }
                searchField.setFocus(true);
            } else {
                searchField.setFocus(false);
            }
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
            int i = this.topPos + 18;
            int j = i + 112;
            this.currentScroll = ((float) mouseY - (float) i - 7.5F) / ((float) (j - i) - 15.0F);
            this.currentScroll = Mth.clamp(this.currentScroll, 0.0F, 1.0F);
            scrollTo(this.currentScroll);
            return true;
        }
        return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
        int i = (this.menu.getCoreInventory().getUniqueCount() + 9 - 1) / 9 - 5;
        float f = (float) (amount / (double) i);
        this.currentScroll = Mth.clamp(this.currentScroll - f, 0.0F, 1.0F);
        scrollTo(this.currentScroll);
        return true;
    }

    protected boolean isClickInScrollbar(double mouseX, double mouseY) {
        int i = this.leftPos;
        int j = this.topPos;
        int k = i + 175;
        int l = j + 18;
        int m = k + 14;
        int n = l + 112;
        return mouseX >= (double) k && mouseY >= (double) l && mouseX < (double) m && mouseY < (double) n;
    }

    private Integer getSlotAt(int x, int y) {
        final int startX = this.leftPos + 7;
        final int startY = this.topPos + 17;

        final int clickedX = x - startX;
        final int clickedY = y - startY;

        if (clickedX >= 0 && clickedY >= 0) {
            final int column = clickedX / 18;
            if (column < 9) {
                final int row = clickedY / 18;
                if (row < rowsVisible()) {
                    return scrollRow * 9 + row * 9 + column;
                }
            }
        }
        return null;
    }

    private void scrollTo(float scroll) {
        int i = (this.menu.getCoreInventory().getUniqueCount() + 8) / 9 - rowsVisible();
        int j = (int) (scroll * i + 0.5D);
        if (j < 0) {
            j = 0;
        }
        this.scrollRow = j;
    }

    private boolean skipSearch() {
        if (!menu.getModifications().contains(ModificationBoxBlock.Type.SEARCH)) {
            return true;
        }
        return searchField.getValue().isEmpty();
    }

    private int getSlotCount() {
        return skipSearch() ? menu.getCoreInventory().getUniqueCount() : filteredItems.size();
    }

    private InfiniteItemStack getSlot(int index) {
        if (index < 0 || index >= getSlotCount()) return InfiniteItemStack.EMPTY;
        return skipSearch() ? menu.getCoreInventory().getStack(index) : filteredItems.get(index);
    }

    protected ResourceLocation getBackground() {
        return BACKGROUND;
    }

    protected int rowsVisible() {
        return 6;
    }

    public Optional<InfiniteItemStack> getStack(int x, int y) {
        final Integer slot = getSlotAt(x, y);
        if (slot == null) {
            return Optional.empty();
        }
        return Optional.of(getSlot(slot));
    }
}

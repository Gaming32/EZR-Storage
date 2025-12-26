package io.github.gaming32.ezrstorage.block.entity;

import io.github.gaming32.ezrstorage.InfiniteInventory;
import io.github.gaming32.ezrstorage.gui.ExtractionPortScreenHandler;
import io.github.gaming32.ezrstorage.registry.EZRBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class ExtractionPortBlockEntity extends RefBlockEntity implements MenuProvider, WorldlyContainer {
    private final SimpleContainer extractList = new SimpleContainer(9);
    private ItemStack buffer = ItemStack.EMPTY;

    private InfiniteInventory.ExtractListMode listMode = InfiniteInventory.ExtractListMode.IGNORE;

    public ExtractionPortBlockEntity(BlockPos pos, BlockState state) {
        super(EZRBlockEntities.EXTRACTION_PORT, pos, state);
    }

    public ExtractionPortBlockEntity(BlockEntityType<? extends BlockEntity> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public void tick() {
        if (level == null || level.isClientSide) return;
        getCoreBlockEntity().ifPresent(core -> {
            if (buffer.isEmpty() && !level.hasNeighborSignal(worldPosition)) {
                buffer = core.getInventory().getMatchingStack(1, listMode, extractList);
                setChanged();
            }
            if (!buffer.isEmpty() && level.getGameTime() % 20 == 0) {
                core.getInventory().moveFrom(buffer);
                setChanged();
            }
        });
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable(getBlockState().getBlock().getDescriptionId());
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int syncId, Inventory inv, Player player) {
        return new ExtractionPortScreenHandler(
            syncId, inv, extractList, ContainerLevelAccess.create(
            level,
            worldPosition
        )
        );
    }

    public InfiniteInventory.ExtractListMode getListMode() {
        return listMode;
    }

    public void setListMode(InfiniteInventory.ExtractListMode listMode) {
        this.listMode = listMode;
    }

    @Override
    public int getContainerSize() {
        return 1;
    }

    @Override
    public boolean isEmpty() {
        return buffer.isEmpty();
    }

    @Override
    public ItemStack getItem(int slot) {
        return slot == 0 ? buffer : ItemStack.EMPTY;
    }

    @Override
    public ItemStack removeItem(int slot, int amount) {
        setChanged();
        return slot == 0 ? buffer.split(amount) : ItemStack.EMPTY;
    }

    @Override
    public ItemStack removeItemNoUpdate(int slot) {
        if (slot != 0) {
            return ItemStack.EMPTY;
        }
        final ItemStack stack = buffer;
        buffer = ItemStack.EMPTY;
        setChanged();
        return stack;
    }

    @Override
    public void setItem(int slot, ItemStack stack) {
        if (slot != 0) return;
        buffer = stack;
        setChanged();
    }

    @Override
    public boolean stillValid(Player player) {
        return false;
    }

    @Override
    public void clearContent() {
        buffer = ItemStack.EMPTY;
        setChanged();
    }

    @Override
    public int[] getSlotsForFace(Direction side) {
        return new int[] {0};
    }

    @Override
    public boolean canPlaceItemThroughFace(int slot, ItemStack stack, @Nullable Direction dir) {
        return false;
    }

    @Override
    public boolean canTakeItemThroughFace(int slot, ItemStack stack, Direction dir) {
        return true;
    }

    @Override
    public void saveAdditional(CompoundTag nbt) {
        super.saveAdditional(nbt);
        nbt.putInt("ListMode", listMode.ordinal());
        ContainerHelper.saveAllItems(nbt, NonNullList.of(ItemStack.EMPTY, buffer));
        nbt.put("ExtractList", extractList.createTag());
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        listMode = InfiniteInventory.ExtractListMode.values()[nbt.getInt("ListMode")];

        final NonNullList<ItemStack> list = NonNullList.withSize(1, ItemStack.EMPTY);
        ContainerHelper.loadAllItems(nbt, list);
        buffer = list.get(0);

        extractList.fromTag(nbt.getList("ExtractList", Tag.TAG_COMPOUND));
    }

    public SimpleContainer getExtractList() {
        return extractList;
    }
}

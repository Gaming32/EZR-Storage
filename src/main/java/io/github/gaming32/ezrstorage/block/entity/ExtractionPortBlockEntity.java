package io.github.gaming32.ezrstorage.block.entity;

import io.github.gaming32.ezrstorage.InfiniteInventory;
import io.github.gaming32.ezrstorage.gui.ExtractionPortScreenHandler;
import io.github.gaming32.ezrstorage.registry.EZRBlockEntities;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.Nullable;

public class ExtractionPortBlockEntity extends RefBlockEntity implements NamedScreenHandlerFactory, SidedInventory {
    private final SimpleInventory extractList = new SimpleInventory(9);
    private ItemStack buffer = ItemStack.EMPTY;

    private InfiniteInventory.ExtractListMode listMode = InfiniteInventory.ExtractListMode.IGNORE;

    public ExtractionPortBlockEntity(BlockPos pos, BlockState state) {
        super(EZRBlockEntities.EXTRACTION_PORT, pos, state);
    }

    public ExtractionPortBlockEntity(BlockEntityType<? extends BlockEntity> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public void tick() {
        if (world == null || world.isClient) return;
        getCoreBlockEntity().ifPresent(core -> {
            if (buffer.isEmpty() && !world.isReceivingRedstonePower(pos)) {
                buffer = core.getInventory().getMatchingStack(1, listMode, extractList);
                markDirty();
            }
            if (!buffer.isEmpty() && world.getTime() % 20 == 0) {
                core.getInventory().moveFrom(buffer);
                markDirty();
            }
        });
    }

    @Override
    public Text getDisplayName() {
        return Text.translatable(getCachedState().getBlock().getTranslationKey());
    }

    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
        return new ExtractionPortScreenHandler(syncId, inv, extractList, ScreenHandlerContext.create(world, pos));
    }

    public InfiniteInventory.ExtractListMode getListMode() {
        return listMode;
    }

    public void setListMode(InfiniteInventory.ExtractListMode listMode) {
        this.listMode = listMode;
    }

    @Override
    public int size() {
        return 1;
    }

    @Override
    public boolean isEmpty() {
        return buffer.isEmpty();
    }

    @Override
    public ItemStack getStack(int slot) {
        return slot == 0 ? buffer : ItemStack.EMPTY;
    }

    @Override
    public ItemStack removeStack(int slot, int amount) {
        markDirty();
        return slot == 0 ? buffer.split(amount) : ItemStack.EMPTY;
    }

    @Override
    public ItemStack removeStack(int slot) {
        if (slot != 0) {
            return ItemStack.EMPTY;
        }
        final ItemStack stack = buffer;
        buffer = ItemStack.EMPTY;
        markDirty();
        return stack;
    }

    @Override
    public void setStack(int slot, ItemStack stack) {
        if (slot != 0) return;
        buffer = stack;
        markDirty();
    }

    @Override
    public boolean canPlayerUse(PlayerEntity player) {
        return false;
    }

    @Override
    public void clear() {
        buffer = ItemStack.EMPTY;
        markDirty();
    }

    @Override
    public int[] getAvailableSlots(Direction side) {
        return new int[] {0};
    }

    @Override
    public boolean canInsert(int slot, ItemStack stack, @Nullable Direction dir) {
        return false;
    }

    @Override
    public boolean canExtract(int slot, ItemStack stack, Direction dir) {
        return true;
    }

    @Override
    public void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        nbt.putInt("ListMode", listMode.ordinal());
        Inventories.writeNbt(nbt, DefaultedList.copyOf(ItemStack.EMPTY, buffer));
        nbt.put("ExtractList", extractList.toNbtList());
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        listMode = InfiniteInventory.ExtractListMode.values()[nbt.getInt("ListMode")];

        final DefaultedList<ItemStack> list = DefaultedList.ofSize(1, ItemStack.EMPTY);
        Inventories.readNbt(nbt, list);
        buffer = list.get(0);

        extractList.readNbtList(nbt.getList("ExtractList", NbtElement.COMPOUND_TYPE));
    }

    public SimpleInventory getExtractList() {
        return extractList;
    }
}

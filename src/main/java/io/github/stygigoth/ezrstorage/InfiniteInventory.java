package io.github.stygigoth.ezrstorage;

import com.google.common.collect.Iterators;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.registry.Registry;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public final class InfiniteInventory implements Iterable<InfiniteItemStack> {
    private final List<InfiniteItemStack> items = new ArrayList<>();
    private final Runnable markDirty;
    private SortType sortType = SortType.COUNT_DOWN;
    private long maxCount;

    public InfiniteInventory() {
        this(() -> {});
    }

    public InfiniteInventory(Runnable markDirty) {
        this.markDirty = markDirty;
    }

    public NbtCompound writeNbt(NbtCompound out) {
        out.putInt("SortType", sortType.ordinal());
        out.putLong("MaxCount", maxCount);
        final NbtList itemData = new NbtList();
        items.stream().map(InfiniteItemStack::writeNbt).forEach(itemData::add);
        out.put("Items", itemData);
        return out;
    }

    public NbtCompound writeNbt() {
        return writeNbt(new NbtCompound());
    }

    public InfiniteInventory readNbt(NbtCompound in) {
        maxCount = in.getLong("MaxCount");
        sortType = SortType.values()[in.getInt("SortType")];
        items.clear();
        in.getList("Items", NbtElement.COMPOUND_TYPE)
            .stream()
            .map(element -> InfiniteItemStack.readNbt((NbtCompound)element))
            .forEach(items::add);
        return this;
    }

    public long getCount() {
        return items.stream().mapToLong(InfiniteItemStack::getCount).sum();
    }

    public long getMaxCount() {
        return maxCount;
    }

    public void setMaxCount(long maxCount) {
        this.maxCount = maxCount;
        markDirty();
    }

    public int getUniqueCount() {
        return items.size();
    }

    public InfiniteItemStack getByContents(InfiniteItemStack.Contents contents) {
        for (final InfiniteItemStack stack : items) {
            if (stack.getContents().equals(contents)) {
                return stack;
            }
        }
        return null;
    }

    public ItemStack moveFrom(ItemStack stack) {
        if (stack.isEmpty()) return stack;
        final long toMove = Math.min(maxCount - getCount(), stack.getCount());
        if (toMove > 0) {
            final InfiniteItemStack.Contents contents = new InfiniteItemStack.Contents(stack);
            InfiniteItemStack to = getByContents(contents);
            final boolean isNew = to == null;
            if (isNew) {
                items.add(to = new InfiniteItemStack(contents, 0));
            }
            to.setCount(to.getCount() + toMove);
            stack.setCount((int)(stack.getCount() - toMove));
            reSort();
        }
        return stack;
    }

    public ItemStack extractStack(InfiniteItemStack stack) {
        return extract(stack, stack.getItem().getMaxCount());
    }

    public ItemStack extract(InfiniteItemStack stack, int n) {
        if (n == 0) return ItemStack.EMPTY;
        stack = getStack(stack.getContents());
        if (stack == null) return ItemStack.EMPTY;
        final ItemStack result = stack.extract(n);
        if (stack.isEmpty()) {
            remove(stack.getContents());
        } else {
            reSort(); // Count changed, resort needed
        }
        return result;
    }

    public void reSort() {
        items.sort(sortType.comparator);
        markDirty();
    }

    public int indexOf(InfiniteItemStack.Contents contents) {
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).getContents().equals(contents)) {
                return i;
            }
        }
        return -1;
    }

    public int indexOf(InfiniteItemStack contents) {
        return items.indexOf(contents);
    }

    public InfiniteItemStack getStack(InfiniteItemStack.Contents contents) {
        final int index = indexOf(contents);
        if (index < 0) return null;
        return items.get(index);
    }

    public InfiniteItemStack getStack(int index) {
        if (index < 0 || index >= items.size()) {
            return InfiniteItemStack.EMPTY;
        }
        return items.get(index);
    }

    public boolean remove(InfiniteItemStack.Contents contents) {
        final int index = indexOf(contents);
        if (index < 0) return false;
        items.remove(index);
        markDirty();
        return true;
    }

    public SortType getSortType() {
        return sortType;
    }

    public void setSortType(SortType sortType) {
        this.sortType = sortType;
        reSort();
    }

    public void markDirty() {
        markDirty.run();
    }

    @NotNull
    @Override
    public Iterator<InfiniteItemStack> iterator() {
        return Iterators.unmodifiableIterator(items.iterator());
    }

    private static final Comparator<InfiniteItemStack> COUNT_UP_BASE = Comparator.comparingLong(InfiniteItemStack::getCount);
    private static final Comparator<InfiniteItemStack> COUNT_DOWN_BASE = COUNT_UP_BASE.reversed();
    private static final Comparator<InfiniteItemStack> AZ_BASE = Comparator.comparing(x -> Registry.ITEM.getId(x.getItem()).getPath());
    private static final Comparator<InfiniteItemStack> ZA_BASE = AZ_BASE.reversed();
    private static final Comparator<InfiniteItemStack> MOD_AZ_BASE = Comparator.comparing(x -> Registry.ITEM.getId(x.getItem()).getNamespace());
    private static final Comparator<InfiniteItemStack> MOD_ZA_BASE = MOD_AZ_BASE.reversed();

    public enum SortType implements Comparator<InfiniteItemStack>, StringIdentifiable {
        COUNT_DOWN("countDown", COUNT_DOWN_BASE.thenComparing(AZ_BASE)),
        COUNT_UP("countUp", COUNT_UP_BASE.thenComparing(ZA_BASE)),
        AZ("az", AZ_BASE.thenComparing(COUNT_DOWN_BASE)),
        ZA("za", ZA_BASE.thenComparing(COUNT_UP_BASE)),
        MOD_AZ("modAz", MOD_AZ_BASE.thenComparing(COUNT_DOWN_BASE)),
        MOD_ZA("modZa", MOD_ZA_BASE.thenComparing(COUNT_UP_BASE))
        ;

        public final String id;
        public final Comparator<InfiniteItemStack> comparator;

        SortType(String id, Comparator<InfiniteItemStack> comparator) {
            this.id = id;
            this.comparator = comparator;
        }

        @Override
        public String asString() {
            return id;
        }

        @Override
        public int compare(InfiniteItemStack o1, InfiniteItemStack o2) {
            return comparator.compare(o1, o2);
        }

        public SortType rotate() {
            return values()[(ordinal() + 1) % values().length];
        }
    }
}

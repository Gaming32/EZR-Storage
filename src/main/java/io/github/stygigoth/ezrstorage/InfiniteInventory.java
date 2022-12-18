package io.github.stygigoth.ezrstorage;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;

import java.util.HashMap;
import java.util.Map;

public final class InfiniteInventory {
    private final Map<InfiniteItemStack.Contents, InfiniteItemStack> items = new HashMap<>();
    private long count;
    private long maxCount;

    public NbtCompound writeNbt(NbtCompound out) {
        final NbtList itemData = new NbtList();
        items.values().stream().map(InfiniteItemStack::writeNbt).forEach(itemData::add);
        out.put("Items", itemData);
        return out;
    }

    public NbtCompound writeNbt() {
        return writeNbt(new NbtCompound());
    }

    public InfiniteInventory readNbt(NbtCompound in) {
        items.clear();
        in.getList("Items", NbtElement.COMPOUND_TYPE)
            .stream()
            .map(element -> InfiniteItemStack.readNbt((NbtCompound)element))
            .forEach(stack -> items.put(stack.getContents(), stack));
        return this;
    }

    public long getCount() {
        return count;
    }

    public long getMaxCount() {
        return maxCount;
    }

    public void setMaxCount(long maxCount) {
        this.maxCount = maxCount;
    }

    public ItemStack moveFrom(ItemStack stack) {
        if (stack.isEmpty()) return stack;
        final long toMove = Math.min(maxCount - count, stack.getCount());
        if (toMove > 0) {
            final InfiniteItemStack.Contents contents = new InfiniteItemStack.Contents(stack);
            InfiniteItemStack to = items.get(contents);
            if (to == null) {
                items.put(contents, to = new InfiniteItemStack(contents, 0));
            }
            count += toMove;
            to.setCount(to.getCount() + toMove);
            stack.setCount((int)(stack.getCount() - toMove));
        }
        return stack;
    }

    public ItemStack extract(InfiniteItemStack stack, int n) {
        stack = items.get(stack.getContents());
        final ItemStack result = stack.extract(n);
        if (stack.isEmpty()) {
            items.remove(stack.getContents());
        }
        return result;
    }
}

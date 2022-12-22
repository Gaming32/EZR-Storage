package io.github.gaming32.ezrstorage;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public final class InfiniteItemStack {
    public record Contents(@Nullable Item item, @NotNull NbtCompound nbt) {
        public Contents(ItemStack stack) {
            //noinspection DataFlowIssue
            this(
                stack.getItem(),
                stack.hasNbt() ? stack.getNbt() : new NbtCompound()
            );
        }
    }

    public static final InfiniteItemStack EMPTY = new InfiniteItemStack(ItemStack.EMPTY);

    private final Contents contents;
    private long count;

    public InfiniteItemStack(@NotNull Item item, long count) {
        contents = new Contents(item, new NbtCompound());
        this.count = count;
    }

    public InfiniteItemStack(Contents contents, long count) {
        this.contents = contents;
        this.count = count;
    }

    public InfiniteItemStack(ItemStack stack) {
        contents = new Contents(stack);
        count = stack.getCount();
    }

    private InfiniteItemStack(NbtCompound in) {
        contents = new Contents(
            Registry.ITEM.get(new Identifier(in.getString("Item"))),
            in.getCompound("Nbt")
        );
        count = in.getLong("Count");
    }

    public NbtCompound writeNbt(NbtCompound out) {
        out.putString("Item", Registry.ITEM.getId(contents.item).toString());
        if (!contents.nbt.isEmpty()) {
            out.put("Nbt", contents.nbt);
        }
        out.putLong("Count", count);
        return out;
    }

    public NbtCompound writeNbt() {
        return writeNbt(new NbtCompound());
    }

    public static InfiniteItemStack readNbt(NbtCompound in) {
        return new InfiniteItemStack(in);
    }

    public Contents getContents() {
        return contents;
    }

    public Item getItem() {
        return contents.item;
    }

    public NbtCompound getNbt() {
        return contents.nbt;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public boolean isEmpty() {
        return this == EMPTY || count == 0 || contents.item == null || contents.item.equals(Items.AIR);
    }

    public boolean add(ItemStack itemStack) {
        if (!itemStack.isOf(contents.item)) return false;
        if (itemStack.hasNbt()) {
            if (!contents.nbt.equals(itemStack.getNbt())) return false;
        } else {
            if (!contents.nbt.isEmpty()) return false;
        }
        count += itemStack.getCount();
        return true;
    }

    public ItemStack extract(int n) {
        if (isEmpty()) return ItemStack.EMPTY;
        if (n > count) n = (int)count;
        assert contents.item != null;
        if (n > contents.item.getMaxCount()) n = contents.item.getMaxCount();
        count -= n;
        final ItemStack stack = new ItemStack(contents.item, n);
        if (!contents.nbt.isEmpty()) {
            stack.setNbt(contents.nbt.copy());
        }
        return stack;
    }

    public ItemStack toItemStack() {
        if (isEmpty()) return ItemStack.EMPTY;
        assert contents.item != null;
        final ItemStack result = new ItemStack(contents.item, (int)Math.min(count, contents.item.getMaxCount()));
        if (!contents.nbt.isEmpty()) {
            result.setNbt(contents.nbt);
        }
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InfiniteItemStack that = (InfiniteItemStack)o;
        return count == that.count && contents.equals(that.contents);
    }

    @Override
    public int hashCode() {
        return Objects.hash(contents, count);
    }
}

package io.github.gaming32.ezrstorage;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.Registry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public final class InfiniteItemStack {
    public record Contents(@Nullable Item item, @NotNull CompoundTag nbt) {
        public Contents(ItemStack stack) {
            //noinspection DataFlowIssue
            this(
                stack.getItem(),
                stack.hasTag() ? stack.getTag() : new CompoundTag()
            );
        }
    }

    public static final InfiniteItemStack EMPTY = new InfiniteItemStack(ItemStack.EMPTY);

    private final Contents contents;
    private long count;

    public InfiniteItemStack(@NotNull Item item, long count) {
        contents = new Contents(item, new CompoundTag());
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

    private InfiniteItemStack(CompoundTag in) {
        contents = new Contents(
            Registry.ITEM.get(new ResourceLocation(in.getString("Item"))),
            in.getCompound("Nbt")
        );
        count = in.getLong("Count");
    }

    public CompoundTag writeNbt(CompoundTag out) {
        out.putString("Item", Registry.ITEM.getKey(contents.item).toString());
        if (!contents.nbt.isEmpty()) {
            out.put("Nbt", contents.nbt);
        }
        out.putLong("Count", count);
        return out;
    }

    public CompoundTag writeNbt() {
        return writeNbt(new CompoundTag());
    }

    public static InfiniteItemStack readNbt(CompoundTag in) {
        return new InfiniteItemStack(in);
    }

    public Contents getContents() {
        return contents;
    }

    public Item getItem() {
        return contents.item;
    }

    public CompoundTag getNbt() {
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
        if (!itemStack.is(contents.item)) return false;
        if (itemStack.hasTag()) {
            if (!contents.nbt.equals(itemStack.getTag())) return false;
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
        if (n > contents.item.getMaxStackSize()) n = contents.item.getMaxStackSize();
        count -= n;
        final ItemStack stack = new ItemStack(contents.item, n);
        if (!contents.nbt.isEmpty()) {
            stack.setTag(contents.nbt.copy());
        }
        return stack;
    }

    public ItemStack toItemStack() {
        if (isEmpty()) return ItemStack.EMPTY;
        assert contents.item != null;
        final ItemStack result = new ItemStack(contents.item, (int)Math.min(count, contents.item.getMaxStackSize()));
        if (!contents.nbt.isEmpty()) {
            result.setTag(contents.nbt);
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

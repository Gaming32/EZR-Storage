package io.github.gaming32.ezrstorage;

import java.util.Objects;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class InfiniteItemStack {
    public record Contents(@NotNull Item item, @Nullable CompoundTag nbt) {
        public Contents(ItemStack stack) {
            this(stack.getItem(), stack.getTag());
        }

        public Contents(FriendlyByteBuf buf) {
            this(Objects.requireNonNull(buf.readById(BuiltInRegistries.ITEM)), buf.readNbt());
        }

        public static void writeToBuf(FriendlyByteBuf buf, Contents contents) {
            buf.writeId(BuiltInRegistries.ITEM, contents.item);
            buf.writeNbt(contents.nbt);
        }
    }

    public static final InfiniteItemStack EMPTY = new InfiniteItemStack(ItemStack.EMPTY);

    private final Contents contents;
    private long count;

    public InfiniteItemStack(@NotNull Item item, long count) {
        contents = new Contents(item, null);
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
        final var item = BuiltInRegistries.ITEM.get(new ResourceLocation(in.getString("Item")));
        CompoundTag tag = null;
        if (in.contains("Nbt", Tag.TAG_COMPOUND)) {
            tag = in.getCompound("Nbt");
            item.verifyTagAfterLoad(tag);
        }

        contents = new Contents(item, tag);
        count = in.getLong("Count");
    }

    public InfiniteItemStack(FriendlyByteBuf buf) {
        this(new Contents(buf), buf.readVarLong());
    }

    public CompoundTag writeNbt(CompoundTag out) {
        out.putString("Item", BuiltInRegistries.ITEM.getKey(contents.item).toString());
        if (contents.nbt != null) {
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

    public static void writeToBuf(FriendlyByteBuf buf, InfiniteItemStack stack) {
        Contents.writeToBuf(buf, stack.contents);
        buf.writeVarLong(stack.count);
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
        return this == EMPTY || count == 0 || contents.item.equals(Items.AIR);
    }

    public boolean add(ItemStack itemStack) {
        if (!itemStack.is(contents.item)) return false;
        if (!Objects.equals(itemStack.getTag(), contents.nbt)) return false;
        count += itemStack.getCount();
        return true;
    }

    public ItemStack extract(int n) {
        if (isEmpty()) return ItemStack.EMPTY;
        if (n > count) n = (int) count;
        if (n > contents.item.getMaxStackSize()) n = contents.item.getMaxStackSize();
        count -= n;
        final ItemStack stack = new ItemStack(contents.item, n);
        if (contents.nbt != null) {
            stack.setTag(contents.nbt.copy());
        }
        return stack;
    }

    public ItemStack toItemStack() {
        if (isEmpty()) return ItemStack.EMPTY;
        final ItemStack result = new ItemStack(contents.item, (int) Math.min(count, contents.item.getMaxStackSize()));
        if (contents.nbt != null) {
            result.setTag(contents.nbt);
        }
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InfiniteItemStack that = (InfiniteItemStack) o;
        return count == that.count && contents.equals(that.contents);
    }

    @Override
    public int hashCode() {
        return Objects.hash(contents, count);
    }
}

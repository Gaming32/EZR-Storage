package io.github.gaming32.ezrstorage.util;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntArrayTag;

public class NbtUtil {
    public static IntArrayTag blockPosToNbt(BlockPos pos) {
        return new IntArrayTag(new int[] {pos.getX(), pos.getY(), pos.getZ()});
    }

    public static BlockPos getBlockPos(CompoundTag nbt, String name) {
        final int[] arr = nbt.getIntArray(name);
        if (arr.length == 0) return null;
        return new BlockPos(arr[0], arr[1], arr[2]);
    }

    public static BlockPos nbtToBlockPos(IntArrayTag nbt) {
        final int[] arr = nbt.getAsIntArray();
        return new BlockPos(arr[0], arr[1], arr[2]);
    }
}

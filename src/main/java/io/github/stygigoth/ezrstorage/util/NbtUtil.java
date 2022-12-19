package io.github.stygigoth.ezrstorage.util;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtIntArray;
import net.minecraft.util.math.BlockPos;

public class NbtUtil {
    public static NbtIntArray blockPosToNbt(BlockPos pos) {
        return new NbtIntArray(new int[] {pos.getX(), pos.getY(), pos.getZ()});
    }

    public static BlockPos getBlockPos(NbtCompound nbt, String name) {
        final int[] arr = nbt.getIntArray(name);
        if (arr.length == 0) return null;
        return new BlockPos(arr[0], arr[1], arr[2]);
    }

    public static BlockPos nbtToBlockPos(NbtIntArray nbt) {
        final int[] arr = nbt.getIntArray();
        return new BlockPos(arr[0], arr[1], arr[2]);
    }
}

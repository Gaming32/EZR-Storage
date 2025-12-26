package io.github.gaming32.ezrstorage.util;

import java.util.EnumSet;
import java.util.Set;
import net.minecraft.network.FriendlyByteBuf;

public class MoreBufs {
    public static <T extends Enum<T>> void writeEnumSet(FriendlyByteBuf buf, Set<T> set) {
        buf.writeCollection(set, FriendlyByteBuf::writeEnum);
    }

    public static <T extends Enum<T>> EnumSet<T> readEnumSet(FriendlyByteBuf buf, Class<T> type) {
        return buf.readCollection(n -> EnumSet.noneOf(type), buf2 -> buf2.readEnum(type));
    }
}

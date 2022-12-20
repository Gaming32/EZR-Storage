package io.github.gaming32.ezrstorage.util;

import net.minecraft.network.PacketByteBuf;

import java.util.EnumSet;
import java.util.Set;

public class MoreBufs {
    public static <T extends Enum<T>> void writeEnumSet(PacketByteBuf buf, Set<T> set) {
        buf.writeCollection(set, PacketByteBuf::writeEnumConstant);
    }

    public static <T extends Enum<T>> EnumSet<T> readEnumSet(PacketByteBuf buf, Class<T> type) {
        return buf.readCollection(n -> EnumSet.noneOf(type), buf2 -> buf2.readEnumConstant(type));
    }
}

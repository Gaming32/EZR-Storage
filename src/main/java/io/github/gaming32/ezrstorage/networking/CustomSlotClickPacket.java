package io.github.gaming32.ezrstorage.networking;

import io.github.gaming32.ezrstorage.registry.EZRReg;
import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.inventory.ClickType;

public record CustomSlotClickPacket(int containerId, int index, ClickType type) implements FabricPacket {
    public static final PacketType<CustomSlotClickPacket> TYPE = PacketType.create(
        EZRReg.id("custom_slot_click"),
        CustomSlotClickPacket::new
    );

    public CustomSlotClickPacket(FriendlyByteBuf buf) {
        this(buf.readUnsignedByte(), buf.readVarInt(), buf.readEnum(ClickType.class));
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeByte(containerId);
        buf.writeVarInt(index);
        buf.writeEnum(type);
    }

    @Override
    public PacketType<?> getType() {
        return TYPE;
    }
}

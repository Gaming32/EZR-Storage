package io.github.gaming32.ezrstorage.networking;

import io.github.gaming32.ezrstorage.InfiniteInventory;
import io.github.gaming32.ezrstorage.block.ModificationBoxBlock;
import io.github.gaming32.ezrstorage.registry.EZRReg;
import java.util.EnumSet;
import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.minecraft.network.FriendlyByteBuf;

public record SyncInventoryPacket(int containerId, InfiniteInventory inventory,
                                  EnumSet<ModificationBoxBlock.Type> modifications) implements FabricPacket {
    public static final PacketType<SyncInventoryPacket> TYPE = PacketType.create(
        EZRReg.id("sync_inventory"),
        SyncInventoryPacket::new
    );

    public SyncInventoryPacket(FriendlyByteBuf buf) {
        this(buf.readUnsignedByte(), new InfiniteInventory(buf), buf.readEnumSet(ModificationBoxBlock.Type.class));
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeByte(containerId);
        InfiniteInventory.writeToBuf(buf, inventory);
        buf.writeEnumSet(modifications, ModificationBoxBlock.Type.class);
    }

    @Override
    public PacketType<?> getType() {
        return TYPE;
    }
}

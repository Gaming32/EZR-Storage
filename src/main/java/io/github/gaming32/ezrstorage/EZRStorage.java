package io.github.gaming32.ezrstorage;

import io.github.gaming32.ezrstorage.block.entity.StorageCoreBlockEntity;
import io.github.gaming32.ezrstorage.compat.create.CreateCompat;
import io.github.gaming32.ezrstorage.gui.ExtractionPortScreenHandler;
import io.github.gaming32.ezrstorage.gui.StorageCoreScreenHandler;
import io.github.gaming32.ezrstorage.gui.StorageCoreScreenHandlerWithCrafting;
import io.github.gaming32.ezrstorage.registry.EZRBlocks;
import io.github.gaming32.ezrstorage.registry.EZRReg;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EZRStorage implements ModInitializer {
    public static final String MOD_ID = "ezrstorage";
    public static final Identifier SYNC_INVENTORY = EZRReg.id("sync_inventory");
    public static final Identifier CUSTOM_SLOT_CLICK = EZRReg.id("custom_slot_click");

    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static final ScreenHandlerType<StorageCoreScreenHandler> STORAGE_CORE_SCREEN_HANDLER = Registry.register(
        Registry.SCREEN_HANDLER,
        EZRReg.id("storage_core"),
        new ScreenHandlerType<>(StorageCoreScreenHandler::new)
    );

    public static final ScreenHandlerType<StorageCoreScreenHandler> STORAGE_CORE_SCREEN_HANDLER_WITH_CRAFTING = Registry.register(
        Registry.SCREEN_HANDLER,
        EZRReg.id("storage_core_with_crafting"),
        new ScreenHandlerType<>(StorageCoreScreenHandlerWithCrafting::new)
    );

    public static final ScreenHandlerType<ExtractionPortScreenHandler> EXTRACTION_PORT_SCREEN_HANDLER = Registry.register(
        Registry.SCREEN_HANDLER,
        EZRReg.id("extraction_port"),
        new ScreenHandlerType<>(ExtractionPortScreenHandler::new)
    );

    public static final ItemGroup EZR_GROUP = FabricItemGroupBuilder.build(
        new Identifier(MOD_ID, MOD_ID),
        () -> new ItemStack(EZRBlocks.STORAGE_CORE.getLeft())
    );

    public static final Item KEY_ITEM = new Item(new FabricItemSettings().group(EZR_GROUP));

    public static long serverTicks;
    public static long clientTicks;

    @Override
    public void onInitialize() {
        EZRReg.registerMod();

        Registry.register(Registry.ITEM, EZRReg.id("key"), KEY_ITEM);

        PlayerBlockBreakEvents.BEFORE.register((world, player, pos, state, blockEntity) ->
            !(blockEntity instanceof StorageCoreBlockEntity core) || core.getInventory().getCount() <= 0L
        );

        ServerTickEvents.START_SERVER_TICK.register(server -> serverTicks++);
        ServerTickEvents.END_SERVER_TICK.register(server -> serverTicks++);

        registerGlobalReceiver(CUSTOM_SLOT_CLICK, (server, player, handler, buf, responseSender) -> {
            final int syncId = buf.readUnsignedByte();
            if (player.currentScreenHandler.syncId != syncId) return;
            final int index = buf.readVarInt();
            final SlotActionType mode = buf.readEnumConstant(SlotActionType.class);
            ((StorageCoreScreenHandler)player.currentScreenHandler).customSlotClick(index, mode);
        });

        if (FabricLoader.getInstance().isModLoaded("create")) {
            CreateCompat.init();
        }
    }

    private static void registerGlobalReceiver(Identifier packet, ServerPlayNetworking.PlayChannelHandler packetHandler) {
        ServerPlayNetworking.registerGlobalReceiver(packet, (server, player, handler, buf, responseSender) -> {
            if (server.isOnThread()) {
                packetHandler.receive(server, player, handler, buf, responseSender);
            } else {
                final PacketByteBuf newBuf = new PacketByteBuf(buf.copy());
                server.executeSync(() -> {
                    if (handler.getConnection().isOpen()) {
                        try {
                            packetHandler.receive(server, player, handler, newBuf, responseSender);
                        } catch (Exception e) {
                            if (handler.shouldCrashOnException()) {
                                throw e;
                            }

                            EZRStorage.LOGGER.error("Failed to handle packet " + packet + ", suppressing error", e);
                        }
                    } else {
                        EZRStorage.LOGGER.debug("Ignoring packet due to disconnection: {}", packet);
                    }
                });
            }
        });
    }
}

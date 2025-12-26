package io.github.gaming32.ezrstorage;

import io.github.gaming32.ezrstorage.block.entity.StorageCoreBlockEntity;
import io.github.gaming32.ezrstorage.compat.create.CreateCompat;
import io.github.gaming32.ezrstorage.gui.ExtractionPortMenu;
import io.github.gaming32.ezrstorage.gui.StorageCoreMenu;
import io.github.gaming32.ezrstorage.gui.StorageCoreMenuWithCrafting;
import io.github.gaming32.ezrstorage.networking.CustomSlotClickPacket;
import io.github.gaming32.ezrstorage.registry.EZRBlocks;
import io.github.gaming32.ezrstorage.registry.EZRReg;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EZRStorage implements ModInitializer {
    public static final String MOD_ID = "ezrstorage";

    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static final MenuType<StorageCoreMenu> STORAGE_CORE_MENU = Registry.register(
        BuiltInRegistries.MENU,
        EZRReg.id("storage_core"),
        new MenuType<>(StorageCoreMenu::new, FeatureFlagSet.of())
    );

    public static final MenuType<StorageCoreMenuWithCrafting> STORAGE_CORE_MENU_WITH_CRAFTING = Registry.register(
        BuiltInRegistries.MENU,
        EZRReg.id("storage_core_with_crafting"),
        new MenuType<>(StorageCoreMenuWithCrafting::new, FeatureFlagSet.of())
    );

    public static final MenuType<ExtractionPortMenu> EXTRACTION_PORT_MENU = Registry.register(
        BuiltInRegistries.MENU,
        EZRReg.id("extraction_port"),
        new MenuType<>(ExtractionPortMenu::new, FeatureFlagSet.of())
    );

    public static final ResourceKey<CreativeModeTab> EZR_GROUP_KEY = ResourceKey.create(
        Registries.CREATIVE_MODE_TAB,
        EZRReg.id("creative_tab")
    );
    public static final CreativeModeTab EZR_GROUP = Registry.register(
        BuiltInRegistries.CREATIVE_MODE_TAB, EZR_GROUP_KEY, FabricItemGroup.builder()
            .title(Component.translatable("itemGroup.ezrstorage.ezrstorage"))
            .icon(() -> new ItemStack(EZRBlocks.STORAGE_CORE))
            .build()
    );

    public static final Item KEY_ITEM = EZRReg.registerItem(new Item(new FabricItemSettings()), "key");

    public static long serverTicks;
    public static long clientTicks;

    @Override
    public void onInitialize() {
        EZRReg.registerMod();
        ItemGroupEvents.modifyEntriesEvent(EZR_GROUP_KEY).register(group -> {
            group.accept(EZRBlocks.STORAGE_CORE);
            group.accept(EZRBlocks.ACCESS_TERMINAL);
            group.accept(EZRBlocks.BLANK_BOX);
            group.accept(EZRBlocks.STORAGE_BOX);
            group.accept(EZRBlocks.CONDENSED_STORAGE_BOX);
            group.accept(EZRBlocks.SUPER_STORAGE_BOX);
            group.accept(EZRBlocks.ULTRA_STORAGE_BOX);
            group.accept(EZRBlocks.HYPER_STORAGE_BOX);
            group.accept(EZRBlocks.CRAFTING_BOX);
            group.accept(EZRBlocks.SEARCH_BOX);
            group.accept(EZRBlocks.SORTING_BOX);
            group.accept(EZRBlocks.SECURITY_BOX);
            group.accept(EZRBlocks.INPUT_PORT);
            group.accept(EZRBlocks.EJECTION_PORT);
            group.accept(EZRBlocks.EXTRACTION_PORT);
            group.accept(KEY_ITEM);
        });

        PlayerBlockBreakEvents.BEFORE.register((world, player, pos, state, blockEntity) ->
            !(blockEntity instanceof StorageCoreBlockEntity core) || core.getInventory().getCount() <= 0L
        );

        ServerTickEvents.START_SERVER_TICK.register(server -> serverTicks++);
        ServerTickEvents.END_SERVER_TICK.register(server -> serverTicks++);

        ServerPlayNetworking.registerGlobalReceiver(
            CustomSlotClickPacket.TYPE, (packet, player, responseSender) -> {
                if (player.containerMenu instanceof StorageCoreMenu menu && menu.containerId == packet.containerId()) {
                    menu.customSlotClick(packet.index(), packet.type());
                }
            }
        );

        if (FabricLoader.getInstance().isModLoaded("create")) {
            CreateCompat.init();
        }
    }
}

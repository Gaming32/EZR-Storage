package io.github.gaming32.ezrstorage.block.entity;

import io.github.gaming32.ezrstorage.InfiniteInventory;
import io.github.gaming32.ezrstorage.block.ModificationBoxBlock;
import io.github.gaming32.ezrstorage.block.StorageBoxBlock;
import io.github.gaming32.ezrstorage.gui.StorageCoreScreenHandler;
import io.github.gaming32.ezrstorage.gui.StorageCoreScreenHandlerWithCrafting;
import io.github.gaming32.ezrstorage.registry.EZRBlockEntities;
import io.github.gaming32.ezrstorage.util.MoreCollectors;
import io.github.gaming32.ezrstorage.util.NbtUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntArrayTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.nbt.*;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.LevelAccessor;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

public class StorageCoreBlockEntity extends BlockEntity implements MenuProvider {
    private final Set<BlockPos> network = new HashSet<>();
    private final InfiniteInventory inventory = new InfiniteInventory(this::setChanged);
    private final Set<ModificationBoxBlock.Type> modifications = EnumSet.noneOf(ModificationBoxBlock.Type.class);

    public StorageCoreBlockEntity(BlockPos pos, BlockState state) {
        super(EZRBlockEntities.STORAGE_CORE, pos, state);
    }

    public void scan(LevelAccessor world, @Nullable BlockEntity skipEntity) {
        for (final BlockPos pos : network) {
            final BlockEntity entity = world.getBlockEntity(pos);
            if (entity instanceof RefBlockEntity ref) {
                ref.setCore(null);
            }
        }
        network.clear();
        modifications.clear();
        startRecursion(world, skipEntity);
        inventory.setMaxCount(0L);
        for (final BlockPos otherPos : network) {
            final BlockState otherState = world.getBlockState(otherPos);
            if (otherState.getBlock() instanceof StorageBoxBlock storageBox) {
                inventory.setMaxCount(inventory.getMaxCount() + storageBox.capacity);
            }
        }
    }

    private void startRecursion(LevelAccessor world, @Nullable BlockEntity skipEntity) {
        network.clear();
        for (Direction d : Direction.values()) {
            BlockEntity be = world.getBlockEntity(worldPosition.relative(d));
            if (be != skipEntity && be instanceof RefBlockEntity ref) {
                addToNetwork(worldPosition.relative(d), ref.getBlockState());
                ref.setCore(worldPosition);
                ref.recurse(world, this, skipEntity);
            }
        }
    }

    void addToNetwork(BlockPos pos, BlockState state) {
        network.add(pos);
        if (state.getBlock() instanceof ModificationBoxBlock modification) {
            modifications.add(modification.type);
        }
        setChanged();
    }

    public boolean networkContains(BlockPos pos) {
        return network.contains(pos);
    }

    public void notifyBreak() {
        if (level == null) return;
        for (final BlockPos pos : network) {
            final BlockEntity entity = level.getBlockEntity(pos);
            if (entity instanceof RefBlockEntity ref) {
                ref.setCore(null);
            }
        }
    }

    public InfiniteInventory getInventory() {
        return inventory;
    }

    public Set<ModificationBoxBlock.Type> getModifications() {
        return modifications;
    }

    @Override
    public AbstractContainerMenu createMenu(int syncId, Inventory inv, Player player) {
        return modifications.contains(ModificationBoxBlock.Type.CRAFTING)
            ? new StorageCoreScreenHandlerWithCrafting(syncId, inv, inventory, modifications, ContainerLevelAccess.create(
            level,
            worldPosition
        ))
            : new StorageCoreScreenHandler(syncId, inv, inventory, modifications);
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable(getBlockState().getBlock().getDescriptionId());
    }

    @Override
    protected void saveAdditional(CompoundTag nbt) {
        nbt.put("Inventory", inventory.writeNbt());
        nbt.put(
            "Modifications",
            modifications.stream()
                .map(ModificationBoxBlock.Type::getSerializedName)
                .map(StringTag::valueOf)
                .collect(MoreCollectors.customCollection(ListTag::new))
        );
        nbt.put(
            "Network",
            network.stream()
                .map(NbtUtil::blockPosToNbt)
                .collect(MoreCollectors.customCollection(ListTag::new))
        );
    }

    @Override
    public void load(CompoundTag nbt) {
        inventory.readNbt(nbt.getCompound("Inventory"));

        modifications.clear();
        nbt.getList("Modifications", Tag.TAG_STRING)
            .stream()
            .map(element -> ModificationBoxBlock.Type.valueOf(element.getAsString().toUpperCase(Locale.ROOT)))
            .forEach(modifications::add);

        network.clear();
        nbt.getList("Network", Tag.TAG_INT_ARRAY)
            .stream()
            .map(element -> NbtUtil.nbtToBlockPos((IntArrayTag)element))
            .forEach(network::add);
    }
}

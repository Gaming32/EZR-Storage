package io.github.gaming32.ezrstorage.block.entity;

import io.github.gaming32.ezrstorage.InfiniteInventory;
import io.github.gaming32.ezrstorage.block.ModificationBoxBlock;
import io.github.gaming32.ezrstorage.block.StorageBoxBlock;
import io.github.gaming32.ezrstorage.gui.StorageCoreScreenHandler;
import io.github.gaming32.ezrstorage.gui.StorageCoreScreenHandlerWithCrafting;
import io.github.gaming32.ezrstorage.registry.EZRBlockEntities;
import io.github.gaming32.ezrstorage.util.MoreCollectors;
import io.github.gaming32.ezrstorage.util.NbtUtil;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.nbt.*;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

public class StorageCoreBlockEntity extends BlockEntity implements NamedScreenHandlerFactory {
    private final Set<BlockPos> network = new HashSet<>();
    private final InfiniteInventory inventory = new InfiniteInventory(this::markDirty);
    private final Set<ModificationBoxBlock.Type> modifications = EnumSet.noneOf(ModificationBoxBlock.Type.class);

    public StorageCoreBlockEntity(BlockPos pos, BlockState state) {
        super(EZRBlockEntities.STORAGE_CORE, pos, state);
    }

    public void scan(WorldAccess world, @Nullable BlockEntity skipEntity) {
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

    private void startRecursion(WorldAccess world, @Nullable BlockEntity skipEntity) {
        network.clear();
        for (Direction d : Direction.values()) {
            BlockEntity be = world.getBlockEntity(pos.offset(d));
            if (be != skipEntity && be instanceof RefBlockEntity ref) {
                addToNetwork(pos.offset(d), ref.getCachedState());
                ref.setCore(pos);
                ref.recurse(world, this, skipEntity);
            }
        }
    }

    void addToNetwork(BlockPos pos, BlockState state) {
        network.add(pos);
        if (state.getBlock() instanceof ModificationBoxBlock modification) {
            modifications.add(modification.type);
        }
        markDirty();
    }

    public boolean networkContains(BlockPos pos) {
        return network.contains(pos);
    }

    public void notifyBreak() {
        if (world == null) return;
        for (final BlockPos pos : network) {
            final BlockEntity entity = world.getBlockEntity(pos);
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
    public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
        return modifications.contains(ModificationBoxBlock.Type.CRAFTING)
            ? new StorageCoreScreenHandlerWithCrafting(syncId, inv, inventory, modifications, ScreenHandlerContext.create(world, pos))
            : new StorageCoreScreenHandler(syncId, inv, inventory, modifications);
    }

    @Override
    public Text getDisplayName() {
        return Text.translatable(getCachedState().getBlock().getTranslationKey());
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        nbt.put("Inventory", inventory.writeNbt());
        nbt.put(
            "Modifications",
            modifications.stream()
                .map(ModificationBoxBlock.Type::asString)
                .map(NbtString::of)
                .collect(MoreCollectors.customCollection(NbtList::new))
        );
        nbt.put(
            "Network",
            network.stream()
                .map(NbtUtil::blockPosToNbt)
                .collect(MoreCollectors.customCollection(NbtList::new))
        );
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        inventory.readNbt(nbt.getCompound("Inventory"));

        modifications.clear();
        nbt.getList("Modifications", NbtElement.STRING_TYPE)
            .stream()
            .map(element -> ModificationBoxBlock.Type.valueOf(element.asString().toUpperCase(Locale.ROOT)))
            .forEach(modifications::add);

        network.clear();
        nbt.getList("Network", NbtElement.INT_ARRAY_TYPE)
            .stream()
            .map(element -> NbtUtil.nbtToBlockPos((NbtIntArray)element))
            .forEach(network::add);
    }
}

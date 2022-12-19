package io.github.stygigoth.ezrstorage.block.entity;

import io.github.stygigoth.ezrstorage.InfiniteInventory;
import io.github.stygigoth.ezrstorage.MoreCollectors;
import io.github.stygigoth.ezrstorage.NbtUtil;
import io.github.stygigoth.ezrstorage.block.StorageBoxBlock;
import io.github.stygigoth.ezrstorage.gui.StorageCoreScreenHandler;
import io.github.stygigoth.ezrstorage.registry.ModBlockEntities;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtIntArray;
import net.minecraft.nbt.NbtList;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.WorldAccess;

import java.util.HashSet;
import java.util.Set;

public class StorageCoreBlockEntity extends BlockEntity implements NamedScreenHandlerFactory {
    private final Set<BlockPos> network = new HashSet<>();
    private final InfiniteInventory inventory = new InfiniteInventory();
    private boolean scanning = false;

    public StorageCoreBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.STORAGE_CORE_BLOCK_ENTITY, pos, state);
    }

    public void scan(WorldAccess world) {
        if (scanning) return;
        scanning = true;
        for (final BlockPos pos : network) {
            final BlockEntity entity = world.getBlockEntity(pos);
            if (entity instanceof RefBlockEntity ref) {
                ref.core = null;
            }
        }
        network.clear();
        startRecursion(world);
        inventory.setMaxCount(0L);
        for (final BlockPos otherPos : network) {
            final BlockState otherState = world.getBlockState(otherPos);
            if (otherState.getBlock() instanceof StorageBoxBlock storageBox) {
                inventory.setMaxCount(inventory.getMaxCount() + storageBox.capacity);
            }
        }
        scanning = false;
    }

    private void startRecursion(WorldAccess world) {
        network.clear();
        for (Direction d : Direction.values()) {
            BlockEntity be = world.getBlockEntity(pos.offset(d));
            if (be instanceof RefBlockEntity ref) {
                addToNetwork(pos.offset(d));
                ref.core = this;
                ref.recurse(world);
            }
        }
    }

    void addToNetwork(BlockPos pos) {
        network.add(pos);
    }

    public boolean networkContains(BlockPos pos) {
        return network.contains(pos);
    }

    public void notifyBreak() {
        if (world == null) return;
        for (final BlockPos pos : network) {
            final BlockEntity entity = world.getBlockEntity(pos);
            if (entity instanceof RefBlockEntity ref) {
                ref.core = null;
            }
        }
    }

    public InfiniteInventory getInventory() {
        return inventory;
    }

    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
        return new StorageCoreScreenHandler(syncId, inv, inventory);
    }

    @Override
    public Text getDisplayName() {
        return new TranslatableText(getCachedState().getBlock().getTranslationKey());
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        nbt.put("Inventory", inventory.writeNbt());
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
        network.clear();
        nbt.getList("Network", NbtElement.INT_ARRAY_TYPE)
            .stream()
            .map(element -> NbtUtil.nbtToBlockPos((NbtIntArray)element))
            .forEach(network::add);
    }
}

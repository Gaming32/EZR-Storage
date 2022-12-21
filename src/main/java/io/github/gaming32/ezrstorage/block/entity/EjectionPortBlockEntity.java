package io.github.gaming32.ezrstorage.block.entity;

import io.github.gaming32.ezrstorage.InfiniteItemStack;
import io.github.gaming32.ezrstorage.registry.EZRBlockEntities;
import net.minecraft.block.BlockState;
import net.minecraft.block.ChestBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.HopperBlockEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public class EjectionPortBlockEntity extends RefBlockEntity {
    public EjectionPortBlockEntity(BlockPos pos, BlockState state) {
        super(EZRBlockEntities.EJECTION_PORT, pos, state);
    }

    public EjectionPortBlockEntity(BlockEntityType<? extends BlockEntity> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public void tick() {
        if (world == null || world.isClient || world.isReceivingRedstonePower(pos)) return;
        getCoreBlockEntity().ifPresent(core -> {
            boolean needsSort = false;
            final BlockPos targetPos = pos.up();
            final BlockEntity targetEntity = world.getBlockEntity(targetPos);

            if (targetEntity instanceof Inventory inventory) {
                final BlockState targetState = world.getBlockState(targetPos);
                if (targetState.getBlock() instanceof ChestBlock chestBlock) {
                    inventory = ChestBlock.getInventory(chestBlock, targetState, world, targetPos, true);
                }

                if (inventory != null) {
                    final InfiniteItemStack stack = core.getInventory().getStack(0);
                    if (!stack.isEmpty()) {
                        final ItemStack destStack = stack.toItemStack();
                        final int stackSize = destStack.getCount();
                        final ItemStack leftOver = HopperBlockEntity.transfer(null, inventory, destStack, Direction.DOWN);
                        if (!leftOver.isEmpty()) {
                            final int remaining = stackSize - leftOver.getCount();
                            if (remaining > 0) {
                                stack.setCount(stack.getCount() - (long)remaining);
                                needsSort = true;
                            }
                        } else {
                            stack.setCount(stack.getCount() - (long)stackSize);
                            needsSort = true;
                        }
                        if (stack.isEmpty()) {
                            core.getInventory().remove(0);
                        }
                    }
                }
            }

            if (needsSort) {
                core.getInventory().reSort();
            }
        });
    }
}

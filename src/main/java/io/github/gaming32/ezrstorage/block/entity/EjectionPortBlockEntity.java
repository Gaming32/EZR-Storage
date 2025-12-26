package io.github.gaming32.ezrstorage.block.entity;

import io.github.gaming32.ezrstorage.InfiniteItemStack;
import io.github.gaming32.ezrstorage.registry.EZRBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.HopperBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class EjectionPortBlockEntity extends RefBlockEntity {
    public EjectionPortBlockEntity(BlockPos pos, BlockState state) {
        super(EZRBlockEntities.EJECTION_PORT, pos, state);
    }

    public EjectionPortBlockEntity(BlockEntityType<? extends BlockEntity> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public void tick() {
        if (level == null || level.isClientSide || level.hasNeighborSignal(worldPosition)) return;
        getCoreBlockEntity().ifPresent(core -> {
            boolean needsSort = false;
            final BlockPos targetPos = worldPosition.above();
            final BlockEntity targetEntity = level.getBlockEntity(targetPos);

            if (targetEntity instanceof Container inventory) {
                final BlockState targetState = level.getBlockState(targetPos);
                if (targetState.getBlock() instanceof ChestBlock chestBlock) {
                    inventory = ChestBlock.getContainer(chestBlock, targetState, level, targetPos, true);
                }

                if (inventory != null) {
                    final InfiniteItemStack stack = core.getInventory().getStack(0);
                    if (!stack.isEmpty()) {
                        final ItemStack destStack = stack.toItemStack();
                        final int stackSize = destStack.getCount();
                        final ItemStack leftOver = HopperBlockEntity.addItem(
                            null,
                            inventory,
                            destStack,
                            Direction.DOWN
                        );
                        if (!leftOver.isEmpty()) {
                            final int remaining = stackSize - leftOver.getCount();
                            if (remaining > 0) {
                                stack.setCount(stack.getCount() - (long) remaining);
                                needsSort = true;
                            }
                        } else {
                            stack.setCount(stack.getCount() - (long) stackSize);
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

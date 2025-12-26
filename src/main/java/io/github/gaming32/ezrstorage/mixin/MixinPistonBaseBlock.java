package io.github.gaming32.ezrstorage.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import io.github.gaming32.ezrstorage.block.entity.RefBlockEntity;
import io.github.gaming32.ezrstorage.block.entity.StorageCoreBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.piston.PistonBaseBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(PistonBaseBlock.class)
public class MixinPistonBaseBlock {
    @WrapOperation(
        method = "triggerEvent",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/level/Level;removeBlock(Lnet/minecraft/core/BlockPos;Z)Z"
        )
    )
    private boolean fixPistonBedrockBreaking(Level instance, BlockPos pos, boolean move, Operation<Boolean> original) {
        final BlockEntity entity = instance.getBlockEntity(pos);
        if (!(entity instanceof StorageCoreBlockEntity) && !(entity instanceof RefBlockEntity)) {
            return original.call(instance, pos, move);
        } else {
            return false;
        }
    }
}

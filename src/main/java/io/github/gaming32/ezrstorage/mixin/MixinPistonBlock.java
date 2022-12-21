package io.github.gaming32.ezrstorage.mixin;

import com.llamalad7.mixinextras.injector.WrapWithCondition;
import io.github.gaming32.ezrstorage.block.entity.RefBlockEntity;
import io.github.gaming32.ezrstorage.block.entity.StorageCoreBlockEntity;
import net.minecraft.block.PistonBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(PistonBlock.class)
public class MixinPistonBlock {
    @WrapWithCondition(
        method = "onSyncedBlockEvent",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/World;removeBlock(Lnet/minecraft/util/math/BlockPos;Z)Z"
        )
    )
    private boolean fixPistonBedrockBreaking(World instance, BlockPos pos, boolean move) {
        final BlockEntity entity = instance.getBlockEntity(pos);
        return !(entity instanceof StorageCoreBlockEntity) && !(entity instanceof RefBlockEntity);
    }
}

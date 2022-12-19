package io.github.stygigoth.ezrstorage.mixin;

import io.github.stygigoth.ezrstorage.block.entity.RefBlockEntity;
import io.github.stygigoth.ezrstorage.registry.ModBlockEntities;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.chunk.WorldChunk;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(WorldChunk.class)
public class MixinWorldChunk {
    @Inject(method = "removeBlockEntity", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/entity/BlockEntity;markRemoved()V"), locals = LocalCapture.CAPTURE_FAILEXCEPTION)
    private void onRemoveBlockEntity(BlockPos pos, CallbackInfo ci, @Nullable BlockEntity removed) {
        if (removed instanceof RefBlockEntity ref && ref.getCore() != null && ref.getWorld() != null) {
            ref.getWorld()
                .getBlockEntity(ref.getCore(), ModBlockEntities.STORAGE_CORE_BLOCK_ENTITY)
                .ifPresent(core -> core.scan(removed.getWorld()));
        }
    }
}

package io.github.stygigoth.ezrstorage.mixin;

import io.github.stygigoth.ezrstorage.HasServerPlayerOuterClass;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(targets = "net/minecraft/server/network/ServerPlayerEntity$1")
public class MixinServerPlayerEntity_1 implements HasServerPlayerOuterClass {
    @Shadow @Final ServerPlayerEntity field_29182;

    @Override
    public ServerPlayerEntity getPlayer() {
        return field_29182;
    }
}

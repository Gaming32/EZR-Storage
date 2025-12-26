package io.github.gaming32.ezrstorage.compat.create;

import com.simibubi.create.content.redstone.displayLink.AllDisplayBehaviours;
import com.simibubi.create.content.redstone.displayLink.DisplayBehaviour;
import io.github.gaming32.ezrstorage.registry.EZRBlocks;
import io.github.gaming32.ezrstorage.registry.EZRReg;

public class CreateCompat {
    public static void init() {
        final DisplayBehaviour source = AllDisplayBehaviours.register(EZRReg.id("ezrstorage_source"), new EZRStorageDisplaySource());
        AllDisplayBehaviours.assignBlock(source, EZRBlocks.STORAGE_CORE.getA());
        AllDisplayBehaviours.assignBlock(source, EZRBlocks.ACCESS_TERMINAL.getA());
    }
}

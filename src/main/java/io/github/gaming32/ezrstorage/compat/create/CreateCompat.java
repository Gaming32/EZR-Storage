package io.github.gaming32.ezrstorage.compat.create;

import com.simibubi.create.content.logistics.block.display.AllDisplayBehaviours;
import com.simibubi.create.content.logistics.block.display.DisplayBehaviour;
import io.github.gaming32.ezrstorage.registry.EZRBlocks;
import io.github.gaming32.ezrstorage.registry.EZRReg;

public class CreateCompat {
    public static void init() {
        final DisplayBehaviour source = AllDisplayBehaviours.register(EZRReg.id("ezrstorage_source"), new EZRStorageDisplaySource());
        AllDisplayBehaviours.assignBlock(source, EZRBlocks.STORAGE_CORE.getLeft());
        AllDisplayBehaviours.assignBlock(source, EZRBlocks.ACCESS_TERMINAL.getLeft());
    }
}

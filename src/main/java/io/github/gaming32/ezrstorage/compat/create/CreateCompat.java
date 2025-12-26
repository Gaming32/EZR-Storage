package io.github.gaming32.ezrstorage.compat.create;

import com.simibubi.create.foundation.data.CreateRegistrate;
import io.github.gaming32.ezrstorage.EZRStorage;
import io.github.gaming32.ezrstorage.registry.EZRBlocks;

public class CreateCompat {
    public static void init() {
        final var registrate = CreateRegistrate.create(EZRStorage.MOD_ID);
        registrate.displaySource("ezrstorage_source", EZRStorageDisplaySource::new)
            .associate(EZRBlocks.STORAGE_CORE)
            .associate(EZRBlocks.ACCESS_TERMINAL)
            .register();
        registrate.register();
    }
}

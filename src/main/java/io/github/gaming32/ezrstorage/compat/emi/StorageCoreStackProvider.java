package io.github.gaming32.ezrstorage.compat.emi;

import dev.emi.emi.api.EmiStackProvider;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.stack.EmiStackInteraction;
import io.github.gaming32.ezrstorage.client.gui.StorageCoreScreen;

public class StorageCoreStackProvider<T extends StorageCoreScreen> implements EmiStackProvider<T> {
    @Override
    public EmiStackInteraction getStackAt(T screen, int x, int y) {
        return new EmiStackInteraction(
            screen.getStack(x, y)
                .filter(s -> !s.isEmpty())
                .map(s -> EmiStack.of(s.toItemStack(), s.getCount()))
                .orElse(EmiStack.EMPTY), // Empty means check the next stack provider, which is correct for the non-special slots
            null, false
        );
    }
}

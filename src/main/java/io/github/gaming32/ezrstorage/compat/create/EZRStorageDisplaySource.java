package io.github.gaming32.ezrstorage.compat.create;

import com.simibubi.create.content.logistics.block.display.DisplayLinkContext;
import com.simibubi.create.content.logistics.block.display.source.NumericSingleLineDisplaySource;
import com.simibubi.create.content.logistics.block.display.target.DisplayTargetStats;
import com.simibubi.create.foundation.gui.ModularGuiLineBuilder;
import com.simibubi.create.foundation.utility.Lang;
import io.github.gaming32.ezrstorage.InfiniteInventory;
import io.github.gaming32.ezrstorage.block.entity.AccessTerminalBlockEntity;
import io.github.gaming32.ezrstorage.block.entity.StorageCoreBlockEntity;
import net.minecraft.text.MutableText;

public class EZRStorageDisplaySource extends NumericSingleLineDisplaySource {
    @Override
    protected MutableText provideLine(DisplayLinkContext context, DisplayTargetStats stats) {
        final StorageCoreBlockEntity core;
        if (context.getSourceTE() instanceof AccessTerminalBlockEntity accessTerminal) {
            core = accessTerminal.getCoreBlockEntity().orElse(null);
        } else if (context.getSourceTE() instanceof StorageCoreBlockEntity) {
            core = (StorageCoreBlockEntity)context.getSourceTE();
        } else {
            core = null;
        }
        if (core == null) return ZERO.copy();

        final InfiniteInventory inventory = core.getInventory();
        final long amount = switch (context.sourceConfig().getInt("Type")) {
            case 0 -> inventory.getCount();
            case 1 -> inventory.getUniqueCount();
            case 2 -> inventory.getMaxCount();
            default -> inventory.getMaxCount() - inventory.getCount();
        };
        return Lang.number(amount)
            .space()
            .translate("generic.unit.items")
            .component();
    }

    @Override
    protected String getTranslationKey() {
        return "ezrstorage";
    }

    @Override
    public void initConfigurationWidgets(DisplayLinkContext context, ModularGuiLineBuilder builder, boolean isFirstLine) {
        super.initConfigurationWidgets(context, builder, isFirstLine);
        if (isFirstLine) return;
        builder.addSelectionScrollInput(0, 95, (selectionScrollInput, label) -> {
            selectionScrollInput.forOptions(Lang.translatedOptions(
                "display_source.ezrstorage", "total", "unique", "max", "remaining"
            ));
        }, "Type");
    }

    @Override
    protected boolean allowsLabeling(DisplayLinkContext displayLinkContext) {
        return true;
    }
}
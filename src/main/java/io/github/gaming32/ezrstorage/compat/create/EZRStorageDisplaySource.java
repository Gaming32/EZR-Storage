package io.github.gaming32.ezrstorage.compat.create;

import com.simibubi.create.content.redstone.displayLink.DisplayLinkContext;
import com.simibubi.create.content.redstone.displayLink.source.NumericSingleLineDisplaySource;
import com.simibubi.create.content.redstone.displayLink.target.DisplayTargetStats;
import com.simibubi.create.foundation.gui.ModularGuiLineBuilder;
import com.simibubi.create.foundation.utility.Components;
import com.simibubi.create.foundation.utility.Lang;
import com.simibubi.create.foundation.utility.LangNumberFormat;
import io.github.gaming32.ezrstorage.InfiniteInventory;
import io.github.gaming32.ezrstorage.block.entity.AccessTerminalBlockEntity;
import io.github.gaming32.ezrstorage.block.entity.StorageCoreBlockEntity;
import net.minecraft.text.MutableText;

import java.util.Arrays;

public class EZRStorageDisplaySource extends NumericSingleLineDisplaySource {
    @Override
    protected MutableText provideLine(DisplayLinkContext context, DisplayTargetStats stats) {
        final StorageCoreBlockEntity core;
        if (context.getSourceBlockEntity() instanceof AccessTerminalBlockEntity accessTerminal) {
            core = accessTerminal.getCoreBlockEntity().orElse(null);
        } else if (context.getSourceBlockEntity() instanceof StorageCoreBlockEntity) {
            core = (StorageCoreBlockEntity) context.getSourceBlockEntity();
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
        return Lang.builder("ezrstorage")
            .text(LangNumberFormat.format(amount))
            .space()
            .translate("generic.unit.items")
            .component();
    }

    @Override
    protected String getTranslationKey() {
        return "ezrstorage";
    }

    @Override
    public void initConfigurationWidgets(
        DisplayLinkContext context,
        ModularGuiLineBuilder builder,
        boolean isFirstLine
    ) {
        super.initConfigurationWidgets(context, builder, isFirstLine);
        if (isFirstLine) return;
        builder.addSelectionScrollInput(
            0, 95, (selectionScrollInput, label) -> selectionScrollInput.forOptions(Arrays.asList(
                Components.translatable("ezrstorage.display_source.ezrstorage.total"),
                Components.translatable("ezrstorage.display_source.ezrstorage.unique"),
                Components.translatable("ezrstorage.display_source.ezrstorage.max"),
                Components.translatable("ezrstorage.display_source.ezrstorage.remaining")
            )), "Type"
        );
    }

    @Override
    protected boolean allowsLabeling(DisplayLinkContext displayLinkContext) {
        return true;
    }
}

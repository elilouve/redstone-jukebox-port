package dev.tauri.rsjukeboxes.item;

import net.minecraft.world.item.CreativeModeTab;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.Nullable;

public interface ITabbedItem {
    @Nullable
    default DeferredHolder<CreativeModeTab, CreativeModeTab> getTab() {
        return null;
    }
}

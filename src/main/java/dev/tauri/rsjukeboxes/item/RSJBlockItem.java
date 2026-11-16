package dev.tauri.rsjukeboxes.item;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.Nullable;

public class RSJBlockItem extends BlockItem implements ITabbedItem {

    private final DeferredHolder<CreativeModeTab, CreativeModeTab> tab;
    protected final Block rawBlock;

    public RSJBlockItem(Block pBlock, Properties pProperties, @Nullable DeferredHolder<CreativeModeTab, CreativeModeTab> tab) {
        super(pBlock, pProperties);
        this.tab = tab;
        this.rawBlock = pBlock;
    }

    @Override
    public DeferredHolder<CreativeModeTab, CreativeModeTab> getTab() {
        return tab;
    }

    public void addAdditional(CreativeModeTab.Output output) {
    }
}

package dev.tauri.rsjukeboxes.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import static dev.tauri.rsjukeboxes.RSJukeboxes.MOD_ID;

public class ItemRegistry {
    public static final DeferredRegister<Item> REGISTER = DeferredRegister.create(Registries.ITEM, MOD_ID);
    public static void register(IEventBus bus) {
        REGISTER.register(bus);
    }
}

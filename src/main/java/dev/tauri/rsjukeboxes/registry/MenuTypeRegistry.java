package dev.tauri.rsjukeboxes.registry;

import dev.tauri.rsjukeboxes.screen.container.TieredJukeboxContainer;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import static dev.tauri.rsjukeboxes.RSJukeboxes.MOD_ID;

public class MenuTypeRegistry {
    public static final DeferredRegister<MenuType<?>> REGISTER = DeferredRegister.create(Registries.MENU, MOD_ID);

    public static final DeferredHolder<MenuType<?>, MenuType<TieredJukeboxContainer>> TIERED_JUKEBOX_MENU_TYPE = REGISTER.register("tiered_jukebox",
            () -> IMenuTypeExtension.create(TieredJukeboxContainer::new)
    );

    public static void register(IEventBus bus) {
        REGISTER.register(bus);
    }

}

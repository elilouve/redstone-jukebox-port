package dev.tauri.rsjukeboxes;

import dev.tauri.rsjukeboxes.integration.Integrations;
import dev.tauri.rsjukeboxes.integration.cctweaked.CCIntegrationWrapper;
import dev.tauri.rsjukeboxes.packet.RSJPacketHandler;
import dev.tauri.rsjukeboxes.registry.*;
import dev.tauri.rsjukeboxes.screen.container.TieredJukeboxGui;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod(RSJukeboxes.MOD_ID)
public class RSJukeboxes {
    public static final String MOD_ID = "rsjukeboxes";
    public static final String MOD_NAME = "Redstone Jukeboxes";
    public static String MOD_VERSION = "";
    public static final String MC_VERSION = "1.21.1";
    public static Logger logger;

    public static final String CC_WRAPPER_LOADED = "dev.tauri.rsjukeboxes.integration.cctweaked.CCIntegrationLoaded";
    public static final String CC_WRAPPER_NOT_LOADED = "dev.tauri.rsjukeboxes.integration.cctweaked.CCIntegrationNotLoaded";
    public static CCIntegrationWrapper ccWrapper;

    public RSJukeboxes(IEventBus modEventBus, ModContainer container) {
        logger = LoggerFactory.getLogger(MOD_NAME);

        MOD_VERSION = MC_VERSION + "-" + container.getModInfo().getVersion().toString();
        logger.info("Loading RSJukeboxes version {}", MOD_VERSION);

        container.registerConfig(ModConfig.Type.CLIENT, RSJConfig.CLIENT_SPEC);

        Integrations.tryLoad();

        ItemRegistry.register(modEventBus);
        BlockRegistry.register(modEventBus);
        TabRegistry.register(modEventBus);
        BlockEntityRegistry.register(modEventBus);
        MenuTypeRegistry.register(modEventBus);

        modEventBus.addListener(RSJPacketHandler::register);
        modEventBus.addListener(RSJukeboxes::registerCapabilities);
    }

    // Remplace l'ancien systeme "getCapability" de Forge : on declare ici le peripherique ComputerCraft
    private static void registerCapabilities(RegisterCapabilitiesEvent event) {
        if (ccWrapper != null) {
            ccWrapper.registerCapabilities(event);
        }
    }

    @EventBusSubscriber(modid = MOD_ID, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void registerScreens(RegisterMenuScreensEvent event) {
            event.register(MenuTypeRegistry.TIERED_JUKEBOX_MENU_TYPE.get(), TieredJukeboxGui::new);
        }

        @SubscribeEvent
        public static void registerBERs(net.neoforged.neoforge.client.event.EntityRenderersEvent.RegisterRenderers event) {
            BlockEntityRegistry.registerBERs(event);
        }
    }
}

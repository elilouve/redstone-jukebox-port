package dev.tauri.rsjukeboxes.integration.cctweaked;

import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.peripheral.PeripheralCapability;
import dev.tauri.rsjukeboxes.blockentity.AbstractTieredJukeboxBE;
import dev.tauri.rsjukeboxes.integration.ComputerDeviceProvider;
import dev.tauri.rsjukeboxes.integration.cctweaked.methods.ICCDevice;
import dev.tauri.rsjukeboxes.registry.BlockEntityRegistry;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;

// Cette classe n'est chargee que si CC: Tweaked est installe (voir Integrations)
@SuppressWarnings("unused")
public class CCIntegrationLoaded implements CCIntegrationWrapper {

    @Override
    public boolean isLoaded() {
        return true;
    }

    @Override
    public ICCDevice createDevice(ComputerDeviceProvider provider, String deviceType) {
        return (ICCDevice) CCDevice.valueOf(deviceType.toUpperCase()).constructor.construct((BlockEntity) provider);
    }

    @Override
    public void registerCapabilities(RegisterCapabilitiesEvent event) {
        registerPeripheral(event, BlockEntityRegistry.TIER1_ADVANCED_JUKEBOX.get());
        registerPeripheral(event, BlockEntityRegistry.TIER2_ADVANCED_JUKEBOX.get());
        registerPeripheral(event, BlockEntityRegistry.TIER3_ADVANCED_JUKEBOX.get());
    }

    private static <T extends AbstractTieredJukeboxBE> void registerPeripheral(RegisterCapabilitiesEvent event, BlockEntityType<T> type) {
        event.registerBlockEntity(PeripheralCapability.get(), type,
                (be, side) -> be.getDeviceHolder().getOrCreateCCDevice()
                        .filter(d -> d instanceof IPeripheral)
                        .map(d -> (IPeripheral) d)
                        .orElse(null));
    }
}

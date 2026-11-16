package dev.tauri.rsjukeboxes.integration.cctweaked;

import dev.tauri.rsjukeboxes.integration.ComputerDeviceProvider;
import dev.tauri.rsjukeboxes.integration.cctweaked.methods.ICCDevice;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;

@SuppressWarnings("unused")
public class CCIntegrationNotLoaded implements CCIntegrationWrapper {
    @Override
    public boolean isLoaded() {
        return false;
    }

    @Override
    public ICCDevice createDevice(ComputerDeviceProvider tile, String deviceType) {
        return null;
    }

    @Override
    public void registerCapabilities(RegisterCapabilitiesEvent event) {
    }
}

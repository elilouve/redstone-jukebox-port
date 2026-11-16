package dev.tauri.rsjukeboxes.integration.cctweaked;

import dev.tauri.rsjukeboxes.integration.ComputerDeviceProvider;
import dev.tauri.rsjukeboxes.integration.cctweaked.methods.ICCDevice;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;

import javax.annotation.Nullable;

public interface CCIntegrationWrapper {
    boolean isLoaded();

    @Nullable
    ICCDevice createDevice(ComputerDeviceProvider tile, String deviceType);

    // NeoForge : les "capabilities" (ici le peripherique CC) sont declarees une fois au demarrage
    void registerCapabilities(RegisterCapabilitiesEvent event);
}

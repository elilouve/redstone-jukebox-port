package dev.tauri.rsjukeboxes.integration;

import dev.tauri.rsjukeboxes.RSJukeboxes;
import dev.tauri.rsjukeboxes.integration.cctweaked.methods.ICCDevice;

import java.util.Optional;

public class ComputerDeviceHolder {
    private ICCDevice ccDevice = null;

    public final ComputerDeviceProvider provider; // usually jukebox block entity

    public ComputerDeviceHolder(ComputerDeviceProvider provider) {
        this.provider = provider;
    }

    // NeoForge : remplace getOrCreateDeviceBasedOnCap / LazyOptional de Forge
    public Optional<ICCDevice> getOrCreateCCDevice() {
        if (RSJukeboxes.ccWrapper == null || !RSJukeboxes.ccWrapper.isLoaded()) return Optional.empty();
        if (ccDevice == null) {
            ccDevice = RSJukeboxes.ccWrapper.createDevice(provider, provider.getDeviceType());
        }
        return Optional.ofNullable(ccDevice);
    }


    public void sendSignal(String eventName, Object... objects) {
        sendSignalCC(eventName, objects);
    }

    public void connectToWirelessNetwork() {
        connectToWirelessNetworkCC();
    }

    public void disconnectFromWirelessNetwork() {
        disconnectFromWirelessNetworkCC();
    }


    // WIRED SIGNALS
    public void sendSignalCC(String eventName, Object... objects) {
        getOrCreateCCDevice().ifPresent(device -> device.sendSignal(eventName, objects));
    }

    // WIRELESS NETWORK CONNECTION
    public void connectToWirelessNetworkCC() {
        getOrCreateCCDevice().ifPresent(ICCDevice::connectToWirelessNetwork);
    }

    public void disconnectFromWirelessNetworkCC() {
        getOrCreateCCDevice().ifPresent(ICCDevice::disconnectFromWirelessNetwork);
    }
}

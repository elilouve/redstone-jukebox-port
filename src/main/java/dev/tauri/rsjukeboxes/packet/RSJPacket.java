package dev.tauri.rsjukeboxes.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

// NeoForge : chaque paquet est maintenant un "CustomPacketPayload"
public abstract class RSJPacket implements CustomPacketPayload {
    public RSJPacket() {
    }

    public abstract void toBytes(FriendlyByteBuf buf);

    public abstract void fromBytes(FriendlyByteBuf buf);

    public abstract void handle(IPayloadContext ctx);

    public RSJPacket(FriendlyByteBuf buf) {
        fromBytes(buf);
    }
}

package dev.tauri.rsjukeboxes.packet;

import dev.tauri.rsjukeboxes.packet.packets.JukeboxActionPacketToServer;
import dev.tauri.rsjukeboxes.packet.packets.StateUpdatePacketToClient;
import dev.tauri.rsjukeboxes.packet.packets.StateUpdateRequestToServer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

import java.util.function.Function;

public class RSJPacketHandler {
    private RSJPacketHandler() {

    }

    public static void sendToServer(RSJPacket packet) {
        PacketDistributor.sendToServer(packet);
    }

    public static void sendToClientsNear(ServerLevel level, BlockPos pos, double radius, RSJPacket packet) {
        PacketDistributor.sendToPlayersNear(level, null, pos.getX(), pos.getY(), pos.getZ(), radius, packet);
    }

    public static void sendTo(RSJPacket packet, ServerPlayer player) {
        PacketDistributor.sendToPlayer(player, packet);
    }

    public static final String NETWORK_VERSION = "1.0";

    // Appele par NeoForge au demarrage (remplace l'ancien SimpleChannel de Forge)
    public static void register(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar(NETWORK_VERSION);
        // to server
        registrar.playToServer(StateUpdateRequestToServer.TYPE, codec(StateUpdateRequestToServer::new), RSJPacket::handle);
        registrar.playToServer(JukeboxActionPacketToServer.TYPE, codec(JukeboxActionPacketToServer::new), RSJPacket::handle);

        // to client
        registrar.playToClient(StateUpdatePacketToClient.TYPE, codec(StateUpdatePacketToClient::new), RSJPacket::handle);
    }

    private static <MSG extends RSJPacket> StreamCodec<RegistryFriendlyByteBuf, MSG> codec(Function<FriendlyByteBuf, MSG> decoder) {
        return StreamCodec.of((buf, msg) -> msg.toBytes(buf), decoder::apply);
    }
}

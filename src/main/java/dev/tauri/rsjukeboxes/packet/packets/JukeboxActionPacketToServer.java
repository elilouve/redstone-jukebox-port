package dev.tauri.rsjukeboxes.packet.packets;

import dev.tauri.rsjukeboxes.RSJukeboxes;
import dev.tauri.rsjukeboxes.blockentity.AbstractTieredJukeboxBE;
import dev.tauri.rsjukeboxes.packet.PositionedPacket;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;

public class JukeboxActionPacketToServer extends PositionedPacket {
    public enum JukeboxAction {
        NONE,
        PLAY,
        STOP,
        NEXT,
        PREVIOUS
    }

    JukeboxAction action;

    public static final Type<JukeboxActionPacketToServer> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(RSJukeboxes.MOD_ID, "jukebox_action"));

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }


    public JukeboxActionPacketToServer(BlockPos pos, JukeboxAction action) {
        super(pos);
        this.action = action;
    }

    public JukeboxActionPacketToServer(FriendlyByteBuf buf) {
        super(buf);
    }

    @Override
    public void toBytes(FriendlyByteBuf buf) {
        super.toBytes(buf);
        buf.writeInt(action.ordinal());
    }

    @Override
    public void fromBytes(FriendlyByteBuf buf) {
        super.fromBytes(buf);
        action = JukeboxAction.values()[buf.readInt()];
    }

    @Override
    public void handle(IPayloadContext ctx) {
        if (ctx.player() instanceof ServerPlayer player) {
            ServerLevel level = player.serverLevel();
            ctx.enqueueWork(() -> {
                var entity = level.getBlockEntity(pos);
                if (!(entity instanceof AbstractTieredJukeboxBE jukebox)) return;
                switch (action) {
                    case PLAY:
                        if (jukebox.isPlaying()) break;
                        if (!jukebox.hasPlayableItem()) break;
                        jukebox.startPlaying();
                        break;
                    case STOP:
                        if (!jukebox.isPlaying()) break;
                        jukebox.stopPlayingAndDoNotSkip();
                        break;
                    case NEXT:
                        jukebox.selectNextTrack();
                        if (!jukebox.hasPlayableItem()) break;
                        jukebox.startPlaying();
                        break;
                    case PREVIOUS:
                        jukebox.selectPreviousTrack();
                        if (!jukebox.hasPlayableItem()) break;
                        jukebox.startPlaying();
                        break;
                    default:
                        break;
                }
            });
        }
    }
}

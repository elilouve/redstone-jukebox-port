package dev.tauri.rsjukeboxes.packet.packets;

import dev.tauri.rsjukeboxes.RSJukeboxes;
import dev.tauri.rsjukeboxes.packet.PositionedPacket;
import dev.tauri.rsjukeboxes.state.State;
import dev.tauri.rsjukeboxes.state.StateProviderInterface;
import dev.tauri.rsjukeboxes.state.StateTypeEnum;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;
import net.minecraft.world.level.Level;
import org.apache.commons.lang3.NotImplementedException;

public class StateUpdatePacketToClient extends PositionedPacket {
    private StateTypeEnum stateType;

    // Les donnees de l'etat sont copiees en octets des la creation du paquet.
    // En solo (1.21), le paquet n'est pas converti en octets entre serveur et client :
    // on garde donc toujours une copie independante, lisible des deux cotes.
    private byte[] stateBytes;

    public static final Type<StateUpdatePacketToClient> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(RSJukeboxes.MOD_ID, "state_update"));

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }


    public StateUpdatePacketToClient(BlockPos pos, StateTypeEnum stateType, State state) {
        super(pos);

        this.stateType = stateType;
        if (state == null) {
            throw new NullPointerException("State was null! (State type: " + stateType.toString() + "; Pos: " + pos.toString() + ")");
        }

        ByteBuf buf = Unpooled.buffer();
        state.toBytes(buf);
        this.stateBytes = new byte[buf.readableBytes()];
        buf.readBytes(this.stateBytes);
        buf.release();
    }

    public StateUpdatePacketToClient(FriendlyByteBuf buf) {
        super(buf);
    }

    @Override
    public void toBytes(FriendlyByteBuf buf) {
        super.toBytes(buf);
        buf.writeInt(stateType.id);
        buf.writeBytes(stateBytes);
    }

    @Override
    public void fromBytes(FriendlyByteBuf buf) {
        super.fromBytes(buf);
        stateType = StateTypeEnum.byId(buf.readInt());
        // NeoForge exige que tout le paquet soit lu : on extrait les octets restants
        stateBytes = new byte[buf.readableBytes()];
        buf.readBytes(stateBytes);
    }

    @Override
    public void handle(IPayloadContext ctx) {
        if (ctx.player() == null) return;
        Level level = ctx.player().level();
        ctx.enqueueWork(() -> {
            try {
                if (!(level.getBlockEntity(pos) instanceof StateProviderInterface te))
                    return;

                State state = te.createState(stateType);

                if (state != null) {
                    state.fromBytes(Unpooled.wrappedBuffer(stateBytes));

                    te.setState(stateType, state);
                } else {
                    throw new NotImplementedException("State not implemented on " + te.getClass().getCanonicalName());
                }
            } catch (Exception e) {
                RSJukeboxes.logger.error("Error while handling packet!", e);
            }
        });
    }
}

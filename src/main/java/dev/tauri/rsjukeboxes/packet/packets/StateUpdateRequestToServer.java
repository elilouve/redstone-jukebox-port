package dev.tauri.rsjukeboxes.packet.packets;

import dev.tauri.rsjukeboxes.RSJukeboxes;
import dev.tauri.rsjukeboxes.packet.PositionedPacket;
import dev.tauri.rsjukeboxes.packet.RSJPacketHandler;
import dev.tauri.rsjukeboxes.state.State;
import dev.tauri.rsjukeboxes.state.StateProviderInterface;
import dev.tauri.rsjukeboxes.state.StateTypeEnum;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import org.apache.commons.lang3.NotImplementedException;

public class StateUpdateRequestToServer extends PositionedPacket {
    StateTypeEnum stateType;

    public static final Type<StateUpdateRequestToServer> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(RSJukeboxes.MOD_ID, "state_update_request"));

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }


    public StateUpdateRequestToServer(BlockPos pos, StateTypeEnum stateType) {
        super(pos);
        this.stateType = stateType;
    }

    public StateUpdateRequestToServer(FriendlyByteBuf buf) {
        super(buf);
    }

    @Override
    public void toBytes(FriendlyByteBuf buf) {
        super.toBytes(buf);

        buf.writeInt(stateType.id);
    }

    @Override
    public void fromBytes(FriendlyByteBuf buf) {
        super.fromBytes(buf);
        stateType = StateTypeEnum.byId(buf.readInt());
    }

    @Override
    public void handle(IPayloadContext ctx) {
        if (ctx.player() instanceof ServerPlayer player) {
            ServerLevel level = player.serverLevel();
            ctx.enqueueWork(() -> {
                StateProviderInterface te = (StateProviderInterface) level.getBlockEntity(pos);

                if (te != null) {
                    try {
                        State state = te.getState(stateType);

                        if (state != null)
                            RSJPacketHandler.sendTo(new StateUpdatePacketToClient(pos, stateType, state), player);
                        else
                            throw new NotImplementedException("State not implemented on " + te.getClass().getCanonicalName() + " : " + stateType.toString());
                    } catch (Exception e) {
                        RSJukeboxes.logger.error("Error while handling packet!", e);
                    }
                }
            });
        }
    }
}

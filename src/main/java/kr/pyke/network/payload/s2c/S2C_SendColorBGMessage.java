package kr.pyke.network.payload.s2c;

import java.util.function.Supplier;

import kr.pyke.client.PykeLibClient;
import kr.pyke.network.CheeseBridgePacket;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;

public class S2C_SendColorBGMessage {

    private final int color;
    private final String message;

    public S2C_SendColorBGMessage(int color, String message) {
        this.color = color;
        this.message = message;
    }

    public static void encode(S2C_SendColorBGMessage packet, FriendlyByteBuf buf) {
        buf.writeVarInt(packet.color);
        buf.writeUtf(packet.message);
    }

    public static S2C_SendColorBGMessage decode(FriendlyByteBuf buf) {
        return new S2C_SendColorBGMessage(buf.readVarInt(), buf.readUtf());
    }

    public static void send(ServerPlayer player, int color, String message) {
        Packet<?> toVanillaPacket = CheeseBridgePacket.CHANNEL.toVanillaPacket(new S2C_SendColorBGMessage(color, message), NetworkDirection.PLAY_TO_CLIENT);
        player.connection.send(toVanillaPacket);
    }

    public static void handle(S2C_SendColorBGMessage packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> handleClient(packet));
        ctx.get().setPacketHandled(true);
    }

    @OnlyIn(Dist.CLIENT)
    private static void handleClient(S2C_SendColorBGMessage packet) {
        PykeLibClient.sendSystemMessage(packet.color, packet.message);
    }
}
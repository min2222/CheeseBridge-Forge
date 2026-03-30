package kr.pyke.network.payload.s2c;

import java.util.function.Supplier;

import kr.pyke.command.IntegrationCommand;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

public class S2C_AuthUrlPayload {
    private final String url;

    public S2C_AuthUrlPayload(String url) {
        this.url = url;
    }

    public String url() {
        return url;
    }

    public static void encode(S2C_AuthUrlPayload packet, FriendlyByteBuf buf) {
        buf.writeUtf(packet.url);
    }

    public static S2C_AuthUrlPayload decode(FriendlyByteBuf buf) {
        return new S2C_AuthUrlPayload(buf.readUtf());
    }

    public static void handle(S2C_AuthUrlPayload packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            IntegrationCommand.startAuthProcess(packet.url());
        });
        ctx.get().setPacketHandled(true);
    }
}
package kr.pyke.network.payload.s2c;

import java.util.function.Supplier;

import kr.pyke.CheeseBridge;
import kr.pyke.client.ChzzkManager;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

public class S2C_FinalTokenPayload {
    private final String accessToken;

    public S2C_FinalTokenPayload(String accessToken) {
        this.accessToken = accessToken;
    }

    public String accessToken() {
        return accessToken;
    }

    public static void encode(S2C_FinalTokenPayload packet, FriendlyByteBuf buf) {
        buf.writeUtf(packet.accessToken);
    }

    public static S2C_FinalTokenPayload decode(FriendlyByteBuf buf) {
        return new S2C_FinalTokenPayload(buf.readUtf());
    }

    public static void handle(S2C_FinalTokenPayload packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            CheeseBridge.LOGGER.info("[디버그] 클라이언트: 토큰 패킷 도착함! -> {}", packet.accessToken());
            ChzzkManager.getInstance().connect(packet.accessToken());
        });
        ctx.get().setPacketHandled(true);
    }
}
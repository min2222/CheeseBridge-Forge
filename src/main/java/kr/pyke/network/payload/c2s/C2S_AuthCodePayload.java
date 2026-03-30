package kr.pyke.network.payload.c2s;

import java.util.function.Supplier;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import kr.pyke.CheeseBridge;
import kr.pyke.integration.ChzzkBridge;
import kr.pyke.integration.ChzzkDataState;
import kr.pyke.network.CheeseBridgePacket;
import kr.pyke.network.payload.s2c.S2C_FinalTokenPayload;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;

public class C2S_AuthCodePayload {
    private final String code;
    private final String state;

    public C2S_AuthCodePayload(String code, String state) {
        this.code = code;
        this.state = state;
    }

    public String code() { return code; }
    public String state() { return state; }

    public static void encode(C2S_AuthCodePayload packet, FriendlyByteBuf buf) {
        buf.writeUtf(packet.code);
        buf.writeUtf(packet.state);
    }

    public static C2S_AuthCodePayload decode(FriendlyByteBuf buf) {
        return new C2S_AuthCodePayload(buf.readUtf(), buf.readUtf());
    }

    public static void handle(C2S_AuthCodePayload packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            if (player == null) return;

            String jsonResponse = ChzzkBridge.exchangeCodeForToken(packet.code(), packet.state());

            if (jsonResponse != null) {
                try {
                    JsonObject json = new Gson().fromJson(jsonResponse, JsonObject.class);
                    if (json.has("content")) { json = json.getAsJsonObject("content"); }

                    if (json.has("accessToken") && json.has("refreshToken")) {
                        String accessToken = json.get("accessToken").getAsString();
                        String refreshToken = json.get("refreshToken").getAsString();

                        ChzzkDataState state = ChzzkDataState.getServerState(player.getServer());
                        state.playerTokens.put(player.getUUID(), new ChzzkDataState.TokenInfo(accessToken, refreshToken));
                        state.setDirty();
                        
                        Packet<?> vanillaPacket = CheeseBridgePacket.CHANNEL.toVanillaPacket(new S2C_FinalTokenPayload(accessToken), NetworkDirection.PLAY_TO_CLIENT);
                        player.connection.send(vanillaPacket);
                        CheeseBridge.LOGGER.info("[인증] 토큰 발급 및 저장 완료!");
                    }
                    else { CheeseBridge.LOGGER.error("[인증] 토큰 발급 실패 (응답 내용): {}", jsonResponse); }
                }
                catch (Exception e) { CheeseBridge.LOGGER.error("[인증] 응답 파싱 실패: ", e); }
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
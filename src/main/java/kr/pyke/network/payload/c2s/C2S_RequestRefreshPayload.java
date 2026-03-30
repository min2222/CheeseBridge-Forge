package kr.pyke.network.payload.c2s;

import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import kr.pyke.CheeseBridge;
import kr.pyke.PykeLib;
import kr.pyke.config.CheeseBridgeConfig;
import kr.pyke.integration.ChzzkBridge;
import kr.pyke.integration.ChzzkDataState;
import kr.pyke.network.CheeseBridgePacket;
import kr.pyke.network.payload.s2c.S2C_AuthUrlPayload;
import kr.pyke.network.payload.s2c.S2C_FinalTokenPayload;
import kr.pyke.util.constants.COLOR;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;

public class C2S_RequestRefreshPayload {

    public static void encode(C2S_RequestRefreshPayload packet, FriendlyByteBuf buf) {
    }

    public static C2S_RequestRefreshPayload decode(FriendlyByteBuf buf) {
        return new C2S_RequestRefreshPayload();
    }

    public static void handle(C2S_RequestRefreshPayload packet, Supplier<NetworkEvent.Context> ctx) {
        ServerPlayer player = ctx.get().getSender();
        if (player == null) return;

        ctx.get().enqueueWork(() -> {
            ChzzkDataState state = ChzzkDataState.getServerState(player.getServer());
            ChzzkDataState.TokenInfo tokenInfo = state.playerTokens.get(player.getUUID());

            boolean refreshSuccess = false;

            if (tokenInfo != null && tokenInfo.refreshToken() != null) {
                CheeseBridge.LOGGER.info("[갱신] {} 님의 토큰 갱신을 시도합니다.", player.getName().getString());
                String jsonResponse = ChzzkBridge.refreshAccessToken(tokenInfo.refreshToken());

                if (jsonResponse != null) {
                    try {
                        JsonObject json = new Gson().fromJson(jsonResponse, JsonObject.class);
                        if (json.has("code") && json.get("code").getAsInt() == 200) {
                            JsonObject content = json.getAsJsonObject("content");
                            String newAccess = content.get("accessToken").getAsString();
                            String newRefresh = content.get("refreshToken").getAsString();

                            state.playerTokens.put(player.getUUID(), new ChzzkDataState.TokenInfo(newAccess, newRefresh));
                            state.setDirty();

                            Packet<?> vanillaPacket = CheeseBridgePacket.CHANNEL.toVanillaPacket(new S2C_FinalTokenPayload(newAccess), NetworkDirection.PLAY_TO_CLIENT);
                            player.connection.send(vanillaPacket);
                            CheeseBridge.LOGGER.info("[갱신] 성공! 클라이언트에 새 토큰 전송 완료.");
                            refreshSuccess = true;
                        }
                        else { CheeseBridge.LOGGER.error("[갱신] 실패 응답 수신: {}", jsonResponse); }
                    }
                    catch (Exception e) { CheeseBridge.LOGGER.error("[갱신] JSON 파싱 중 오류 발생", e); }
                }
            }

            if (!refreshSuccess) {
                CheeseBridge.LOGGER.warn("[갱신] 토큰 갱신 불가. 재인증을 요청합니다.");

                String clientId = CheeseBridgeConfig.DATA.clientID;
                String authState = UUID.randomUUID().toString();
                String authUrl = String.format("https://chzzk.naver.com/account-interlock?clientId=%s&redirectUri=%s&state=%s", clientId, "http://localhost:8080/callback", authState);

                PykeLib.sendSystemMessage(List.of(player), COLOR.RED.getColor(), "인증 세션이 만료되었습니다. 다시 로그인을 진행해주세요.");
                
                Packet<?> vanillaPacket = CheeseBridgePacket.CHANNEL.toVanillaPacket(new S2C_AuthUrlPayload(authUrl), NetworkDirection.PLAY_TO_CLIENT);
                player.connection.send(vanillaPacket);
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
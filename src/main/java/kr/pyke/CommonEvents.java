package kr.pyke;

import kr.pyke.command.DebugCommand;
import kr.pyke.command.DonationCommand;
import kr.pyke.integration.ChzzkDataState;
import kr.pyke.network.CheeseBridgePacket;
import kr.pyke.network.payload.s2c.S2C_FinalTokenPayload;
import net.minecraft.network.protocol.Packet;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.network.NetworkDirection;

@EventBusSubscriber(modid = CheeseBridge.MOD_ID)
public class CommonEvents {
    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
    	DonationCommand.register(event.getDispatcher(), event.getBuildContext(), event.getCommandSelection());
    	DebugCommand.register(event.getDispatcher(), event.getBuildContext(), event.getCommandSelection());
    }

    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerLoggedInEvent event) {
    	if (event.getEntity() instanceof ServerPlayer player) {
    		ChzzkDataState state = ChzzkDataState.getServerState(player.getServer());
    		ChzzkDataState.TokenInfo tokenInfo = state.playerTokens.get(player.getUUID());

    		if (tokenInfo != null) {
    			Packet<?> vanillaPacket = CheeseBridgePacket.CHANNEL.toVanillaPacket(new S2C_FinalTokenPayload(tokenInfo.accessToken()), NetworkDirection.PLAY_TO_CLIENT);
    			player.connection.send(vanillaPacket);
    			CheeseBridge.LOGGER.info("[인증] {} 님의 토큰을 로드하여 자동 연결합니다.", player.getName().getString());
    		}
    	}
    }
}

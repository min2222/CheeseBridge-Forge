package kr.pyke;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kr.pyke.network.payload.s2c.S2C_SendColorBGBroadcast;
import kr.pyke.network.payload.s2c.S2C_SendColorBGMessage;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

public class PykeLib {
	public static final String MOD_ID = "pykelib";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static final Component SYSTEM_PREFIX = Component.literal("§6[SYSTEM]§r ");

	public static void sendSystemMessage(List<ServerPlayer> players, int color, String message) {
		for (ServerPlayer serverPlayer : players) {
			S2C_SendColorBGMessage.send(serverPlayer, color, message);
		}
	}

	public static void sendSystemMessage(ServerPlayer player, int color, String message) {
		S2C_SendColorBGMessage.send(player, color, message);
	}

	public static void sendBroadcastMessage(List<ServerPlayer> players, int color, String message) {
		for (ServerPlayer serverPlayer : players) {
			S2C_SendColorBGBroadcast.send(serverPlayer, color, message);
		}
	}
}
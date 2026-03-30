package kr.pyke.network;

import kr.pyke.CheeseBridge;
import kr.pyke.network.payload.c2s.C2S_AuthCodePayload;
import kr.pyke.network.payload.c2s.C2S_DonationPayload;
import kr.pyke.network.payload.c2s.C2S_RequestRefreshPayload;
import kr.pyke.network.payload.s2c.S2C_AuthUrlPayload;
import kr.pyke.network.payload.s2c.S2C_FinalTokenPayload;
import kr.pyke.network.payload.s2c.S2C_SendColorBGBroadcast;
import kr.pyke.network.payload.s2c.S2C_SendColorBGMessage;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class CheeseBridgePacket {
    private CheeseBridgePacket() { }

	private static final String PROTOCOL_VERSION = "1";
	public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(ResourceLocation.fromNamespaceAndPath(CheeseBridge.MOD_ID, CheeseBridge.MOD_ID),
			() -> PROTOCOL_VERSION,
			PROTOCOL_VERSION::equals,
			PROTOCOL_VERSION::equals
	);
	
	public static int ID = 0;
	public static void registerMessages()
	{
		CHANNEL.registerMessage(ID++, S2C_AuthUrlPayload.class, S2C_AuthUrlPayload::encode, S2C_AuthUrlPayload::decode, S2C_AuthUrlPayload::handle);
		CHANNEL.registerMessage(ID++, S2C_FinalTokenPayload.class, S2C_FinalTokenPayload::encode, S2C_FinalTokenPayload::decode, S2C_FinalTokenPayload::handle);
		CHANNEL.registerMessage(ID++, S2C_SendColorBGBroadcast.class, S2C_SendColorBGBroadcast::encode, S2C_SendColorBGBroadcast::decode, S2C_SendColorBGBroadcast::handle);
		CHANNEL.registerMessage(ID++, S2C_SendColorBGMessage.class, S2C_SendColorBGMessage::encode, S2C_SendColorBGMessage::decode, S2C_SendColorBGMessage::handle);
		CHANNEL.registerMessage(ID++, C2S_DonationPayload.class, C2S_DonationPayload::encode, C2S_DonationPayload::decode, C2S_DonationPayload::handle);
		CHANNEL.registerMessage(ID++, C2S_AuthCodePayload.class, C2S_AuthCodePayload::encode, C2S_AuthCodePayload::decode, C2S_AuthCodePayload::handle);
		CHANNEL.registerMessage(ID++, C2S_RequestRefreshPayload.class, C2S_RequestRefreshPayload::encode, C2S_RequestRefreshPayload::decode, C2S_RequestRefreshPayload::handle);
	}
}

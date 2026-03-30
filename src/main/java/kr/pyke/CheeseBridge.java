package kr.pyke;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kr.pyke.config.CheeseBridgeConfig;
import kr.pyke.network.CheeseBridgePacket;
import net.minecraftforge.fml.common.Mod;

@Mod(CheeseBridge.MOD_ID)
public class CheeseBridge {
	public static final String MOD_ID = "cheese_bridge";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	
	public CheeseBridge() {
		CheeseBridgeConfig.loadConfiguration();
		CheeseBridgePacket.registerMessages();
	}
}
package kr.pyke;

import kr.pyke.client.ChzzkManager;
import kr.pyke.command.IntegrationCommand;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.client.event.RegisterClientCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber(modid = CheeseBridge.MOD_ID, value = Dist.CLIENT)
public class ClientEvents {
    @SubscribeEvent
    public static void onRegisterClientCommands(RegisterClientCommandsEvent event) {
    	IntegrationCommand.register(event.getDispatcher());
    }
    
    @SubscribeEvent
    public static void onLoggingOut(ClientPlayerNetworkEvent.LoggingOut event) {
        ChzzkManager.getInstance().disconnect();
        CheeseBridge.LOGGER.info("서버 연결 종료로 인해 치지직 소켓을 닫습니다.");
    }
}

package kr.pyke.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;

import kr.pyke.client.ChzzkAuthServer;
import kr.pyke.client.ChzzkManager;
import kr.pyke.client.PykeLibClient;
import kr.pyke.util.constants.COLOR;
import net.minecraft.Util;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

public class IntegrationCommand {
    private static final ChzzkAuthServer AUTH_SERVER = new ChzzkAuthServer();

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("연동해제")
            .executes(IntegrationCommand::integrationDisconnect)
        );
    }

    private static int integrationDisconnect(CommandContext<CommandSourceStack> ctx) {
        ChzzkManager.getInstance().disconnect();
        PykeLibClient.sendSystemMessage(COLOR.RED.getColor(), "치지직 후원 연동이 일시적으로 해제되었습니다.");
        PykeLibClient.sendSystemMessage(COLOR.RED.getColor(), "'/후원연동' 입력 시 즉시 다시 연결됩니다.");
        return 1;
    }

    public static void startAuthProcess(String url) {
        AUTH_SERVER.start();
        Util.getPlatform().openUri(url);
        PykeLibClient.sendSystemMessage(COLOR.LIME.getColor(), "브라우저에서 치지직 로그인을 진행해 주세요.");
    }
}

package kr.pyke.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import kr.pyke.PykeLib;
import kr.pyke.util.constants.COLOR;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerPlayer;

import java.util.List;

public class DebugCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext ctx, Commands.CommandSelection selection) {
        dispatcher.register(Commands.literal("디버그")
            .requires(source -> source.hasPermission(2))
            .executes(DebugCommand::sendColorBoxMessage)
        );
    }

    private static int sendColorBoxMessage(CommandContext<CommandSourceStack> ctx) {
        CommandSourceStack source = ctx.getSource();
        ServerPlayer player = source.getPlayer();
        if (player == null) { return 0; }

        List<ServerPlayer> serverPlayers = source.getServer().getPlayerList().getPlayers();
        PykeLib.sendSystemMessage(serverPlayers, COLOR.LIME.getColor(), "해당 메시지는 디버그용 테스트 메시지입니다.");

        return 1;
    }
}

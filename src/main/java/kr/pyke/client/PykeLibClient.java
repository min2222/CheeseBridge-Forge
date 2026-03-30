package kr.pyke.client;

import kr.pyke.PykeLib;
import kr.pyke.util.PykeHelper;
import net.minecraft.client.GuiMessageTag;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;

public class PykeLibClient {
    public static void sendSystemMessage(int color, String message) {
        GuiMessageTag messageTag = new GuiMessageTag(color, null, null, "color_chatbox");
        Component component = PykeLib.SYSTEM_PREFIX.copy().append(PykeHelper.parseComponent(message));

        Minecraft.getInstance().gui.getChat().addMessage(component, null, messageTag);
    }
}

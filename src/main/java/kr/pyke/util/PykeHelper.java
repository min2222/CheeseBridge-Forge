package kr.pyke.util;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

public class PykeHelper {
    private PykeHelper() { }

    public static MutableComponent parseComponent(String message) {
        if (message.isEmpty()) { return Component.empty(); }

        String formattedMessage = message.replace("&", "§");

        return Component.literal(formattedMessage).copy();
    }
}

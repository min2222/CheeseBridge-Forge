package kr.pyke.util.constants;

public enum COLOR {
    RED(0xFF5555),
    GOLD(0xFFAA00),
    YELLOW(0xFFFF55),
    LIME(0x55FF55),
    AQUA(0x55FFFF),
    DARK_AQUA(0x00AAAA),
    BLUE(0x5555FF),
    LIGHT_PURPLE(0xFF55FF),
    PURPLE(0xAA00AA);

    private final int color;

    COLOR(int color) { this.color = color; }

    public int getColor() { return color; }
}

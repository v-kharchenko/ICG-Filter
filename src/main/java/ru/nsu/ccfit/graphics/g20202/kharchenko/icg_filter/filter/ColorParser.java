package ru.nsu.ccfit.graphics.g20202.kharchenko.icg_filter.filter;

import java.util.function.Function;

public class ColorParser {

    public static int applyToComponents(int color, Function<Integer, Integer> componentFunction) {
        int red = (color >> 16) & 0xFF;
        int green = (color >> 8) & 0xFF;
        int blue = color & 0xFF;

        red = componentFunction.apply(red);
        green = componentFunction.apply(green);
        blue = componentFunction.apply(blue);

        red = Math.max(0, Math.min(red, 255));
        green = Math.max(0, Math.min(green, 255));
        blue = Math.max(0, Math.min(blue, 255));

        return (0xFF << 24) | (red << 16) | (green << 8) | (blue);
    }

}

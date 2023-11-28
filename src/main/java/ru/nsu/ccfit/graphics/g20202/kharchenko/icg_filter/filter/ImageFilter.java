package ru.nsu.ccfit.graphics.g20202.kharchenko.icg_filter.filter;

import java.awt.image.BufferedImage;
import java.util.Collection;
import java.util.HashMap;

public interface ImageFilter {

    enum ColorComponent {
        RED,
        GREEN,
        BLUE;

        int getOffset() {
            return switch (this) {
                case RED -> 16;
                case GREEN -> 8;
                case BLUE -> 0;
            };
        }
    }

    BufferedImage filterImage(BufferedImage image);

    HashMap<String, Object> getParameters();

    void updateParameters();
}

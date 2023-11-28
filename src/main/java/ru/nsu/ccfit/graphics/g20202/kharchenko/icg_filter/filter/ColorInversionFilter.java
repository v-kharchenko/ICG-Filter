package ru.nsu.ccfit.graphics.g20202.kharchenko.icg_filter.filter;

import java.awt.image.BufferedImage;
import java.util.HashMap;

public class ColorInversionFilter implements ImageFilter {

    @Override
    public BufferedImage filterImage(BufferedImage image) {
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                int rgb = image.getRGB(x, y);
                rgb = ((0xFFFFFFFF - rgb) & 0x00FFFFFF) | (rgb & 0xFF000000);
                image.setRGB(x, y, rgb);
            }
        }

        return image;
    }

    @Override
    public HashMap<String, Object> getParameters() {
        return null;
    }

    @Override
    public void updateParameters() {}
}

package ru.nsu.ccfit.graphics.g20202.kharchenko.icg_filter.filter;

import java.awt.image.BufferedImage;
import java.util.HashMap;

public class GrayscaleFilter implements ImageFilter{

    @Override
    public BufferedImage filterImage(BufferedImage image) {
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                int rgb = image.getRGB(x, y);
                int grayRGB = grayColor(rgb);
                image.setRGB(x, y, grayRGB);
            }
        }
        return image;
    }

    @Override
    public HashMap<String, Object> getParameters() {
        return null;
    }

    @Override
    public void updateParameters() {

    }

    public static int grayColor(int color) {
        int red = ((color >> 16) & 0xFF) * 30;
        int green = ((color >> 8) & 0xFF) * 59;
        int blue = ((color) & 0xFF) * 11;
        int gray = (red + green + blue) / 100;

        return (gray << 16) | (gray << 8) | gray | 0xFF000000;
    }
}

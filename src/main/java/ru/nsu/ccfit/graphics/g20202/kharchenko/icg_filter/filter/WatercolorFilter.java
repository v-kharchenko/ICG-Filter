package ru.nsu.ccfit.graphics.g20202.kharchenko.icg_filter.filter;

import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.HashMap;

public class WatercolorFilter implements ImageFilter {

    private IncreaseSharpnessFilter sharpnessFilter = new IncreaseSharpnessFilter();

    @Override
    public BufferedImage filterImage(BufferedImage image) {
        BufferedImage blurredImage = medianBlur(image);

        return sharpnessFilter.filterImage(blurredImage);
    }

    private BufferedImage medianBlur(BufferedImage image) {
        BufferedImage newImage = FilterSet.deepCopy(image);

        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                newImage.setRGB(x, y, median(image, x, y));
            }
        }

        return newImage;
    }

    private int median(BufferedImage image, int x, int y) {
        int[] redBuffer = new int[25];
        int[] greenBuffer = new int[25];
        int[] blueBuffer = new int[25];
        for (int i = -2; i <= 2; i++) {
            for (int j = -2; j <= 2; j++) {
                int rgb;

                if (x + j < 0 || x + j >= image.getWidth() || y + i < 0 || y + i >= image.getHeight())
                    rgb = image.getRGB(x, y);
                else
                    rgb = image.getRGB(x + j, y + i);

                redBuffer[(i + 2)*5 + j + 2] = (rgb >> 16) & 0xFF;
                greenBuffer[(i + 2)*5 + j + 2] = (rgb >> 8) & 0xFF;
                blueBuffer[(i + 2)*5 + j + 2] = rgb & 0xFF;
            }
        }
        Arrays.sort(redBuffer);
        Arrays.sort(greenBuffer);
        Arrays.sort(blueBuffer);

        return (0xFF << 24) | (redBuffer[12] << 16) | (greenBuffer[12] << 8) | blueBuffer[12];
    }

    @Override
    public HashMap<String, Object> getParameters() {
        return null;
    }

    @Override
    public void updateParameters() {

    }
}

package ru.nsu.ccfit.graphics.g20202.kharchenko.icg_filter.filter;

import ru.nsu.ccfit.graphics.g20202.kharchenko.icg_filter.filter.parameter.PaletteCountParameter;
import ru.nsu.ccfit.graphics.g20202.kharchenko.icg_filter.filter.parameter.RangedParameter;

import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.HashMap;

public class FloydSteinbergDitheringFilter implements ImageFilter {

    protected HashMap<String, Object> parameters = new HashMap<>() {{
        put("Palette counts", new PaletteCountParameter(2, 2, 2));
    }};
    private HashMap<ImageFilter.ColorComponent, RangedParameter> componentCount = ((PaletteCountParameter)(parameters.get("Palette counts"))).getComponentCount();
    private int[] redLookUpTable = new int[256];
    private int[] greenLookUpTable = new int[256];
    private int[] blueLookUpTable = new int[256];

    public FloydSteinbergDitheringFilter() {
        updateParameters();
    }

    @Override
    public BufferedImage filterImage(BufferedImage image) {
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                filterPixel(image, x, y);
            }
        }
        return image;
    }

    @Override
    public HashMap<String, Object> getParameters() {
        return parameters;
    }

    @Override
    public void updateParameters() {
        componentCount = ((PaletteCountParameter)(parameters.get("Palette counts"))).getComponentCount();
        int redCount = componentCount.get(ColorComponent.RED).getValue();
        int greenCount = componentCount.get(ColorComponent.GREEN).getValue();
        int blueCount = componentCount.get(ColorComponent.BLUE).getValue();
        
        // Lookup tables
        redLookUpTable = getLookUpTable(ColorComponent.RED, redCount);
        greenLookUpTable = getLookUpTable(ColorComponent.GREEN, greenCount);
        blueLookUpTable = getLookUpTable(ColorComponent.BLUE, blueCount);

        System.out.println(Arrays.toString(redLookUpTable));
    }

    private void filterPixel(BufferedImage image, int x, int y) {
        int rgb = image.getRGB(x, y);

        int red = (rgb >> 16) & 0xFF;
        int green = (rgb >> 8) & 0xFF;
        int blue = rgb & 0xFF;

        int newRed = getClosestValue(ColorComponent.RED, red);
        int newGreen = getClosestValue(ColorComponent.GREEN, green);
        int newBlue = getClosestValue(ColorComponent.BLUE, blue);

        int redError = red - newRed;
        int greenError = green - newGreen;
        int blueError = blue - newBlue;

        image.setRGB(x, y, (0xFF << 24) | (newRed << 16) | (newGreen << 8) | newBlue);

        int rgbNeighbour;
        if (y + 1 < image.getHeight()) {
            if (x - 1 > 0 && x + 1 < image.getWidth()) {
                rgbNeighbour = image.getRGB(x + 1, y);
                image.setRGB(x + 1, y, (0xFF << 24)
                        | Math.max(Math.min((((rgbNeighbour >> 16) & 0xFF) + redError * 7 / 16), 255), 0) << 16
                        | Math.max(Math.min((((rgbNeighbour >> 8) & 0xFF) + greenError * 7 / 16), 255), 0) << 8
                        | Math.max(Math.min(((rgbNeighbour & 0xFF) + blueError * 7 / 16), 255), 0));

                rgbNeighbour = image.getRGB(x - 1, y + 1);
                image.setRGB(x - 1, y + 1, (0xFF << 24)
                        | Math.max(Math.min((((rgbNeighbour >> 16) & 0xFF) + redError * 3 / 16), 255), 0) << 16
                        | Math.max(Math.min((((rgbNeighbour >> 8) & 0xFF) + greenError * 3 / 16), 255), 0) << 8
                        | Math.max(Math.min(((rgbNeighbour & 0xFF) + blueError * 3 / 16), 255), 0));

                rgbNeighbour = image.getRGB(x, y + 1);
                image.setRGB(x, y + 1, (0xFF << 24)
                        | Math.max(Math.min((((rgbNeighbour >> 16) & 0xFF) + redError * 5 / 16), 255), 0) << 16
                        | Math.max(Math.min((((rgbNeighbour >> 8) & 0xFF) + greenError * 5 / 16), 255), 0) << 8
                        | Math.max(Math.min(((rgbNeighbour & 0xFF) + blueError * 5 / 16), 255), 0));

                rgbNeighbour = image.getRGB(x + 1, y + 1);
                image.setRGB(x + 1, y + 1, (0xFF << 24)
                        | Math.max(Math.min((((rgbNeighbour >> 16) & 0xFF) + redError / 16), 255), 0) << 16
                        | Math.max(Math.min((((rgbNeighbour >> 8) & 0xFF) + greenError / 16), 255), 0) << 8
                        | Math.max(Math.min(((rgbNeighbour & 0xFF) + blueError / 16), 255), 0));
            } else if (x - 1 > 0) {
                rgbNeighbour = image.getRGB(x - 1, y + 1);
                image.setRGB(x - 1, y + 1, (0xFF << 24)
                        | Math.max(Math.min((((rgbNeighbour >> 16) & 0xFF) + redError * 3 / 8), 255), 0) << 16
                        | Math.max(Math.min((((rgbNeighbour >> 8) & 0xFF) + greenError * 3 / 8), 255), 0) << 8
                        | Math.max(Math.min(((rgbNeighbour & 0xFF) + blueError * 3 / 8), 255), 0));

                rgbNeighbour = image.getRGB(x, y + 1);
                image.setRGB(x, y + 1, (0xFF << 24)
                        | Math.max(Math.min((((rgbNeighbour >> 16) & 0xFF) + redError * 5 / 8), 255), 0) << 16
                        | Math.max(Math.min((((rgbNeighbour >> 8) & 0xFF) + greenError * 5 / 8), 255), 0) << 8
                        | Math.max(Math.min(((rgbNeighbour & 0xFF) + blueError * 5 / 8), 255), 0));
            } else if (x + 1 < image.getWidth()) {
                rgbNeighbour = image.getRGB(x + 1, y);
                image.setRGB(x + 1, y, (0xFF << 24)
                        | Math.max(Math.min((((rgbNeighbour >> 16) & 0xFF) + redError * 7 / 13), 255), 0) << 16
                        | Math.max(Math.min((((rgbNeighbour >> 8) & 0xFF) + greenError * 7 / 13), 255), 0) << 8
                        | Math.max(Math.min(((rgbNeighbour & 0xFF) + blueError * 7 / 13), 255), 0));

                rgbNeighbour = image.getRGB(x, y + 1);
                image.setRGB(x, y + 1, (0xFF << 24)
                        | Math.max(Math.min((((rgbNeighbour >> 16) & 0xFF) + redError * 5 / 13), 255), 0) << 16
                        | Math.max(Math.min((((rgbNeighbour >> 8) & 0xFF) + greenError * 5 / 13), 255), 0) << 8
                        | Math.max(Math.min(((rgbNeighbour & 0xFF) + blueError * 5 / 13), 255), 0));

                rgbNeighbour = image.getRGB(x + 1, y + 1);
                image.setRGB(x + 1, y + 1, (0xFF << 24)
                        | Math.max(Math.min((((rgbNeighbour >> 16) & 0xFF) + redError / 13), 255), 0) << 16
                        | Math.max(Math.min((((rgbNeighbour >> 8) & 0xFF) + greenError / 13), 255), 0) << 8
                        | Math.max(Math.min(((rgbNeighbour & 0xFF) + blueError / 13), 255), 0));
            }
        } else {
            if (x + 1 < image.getWidth()) {
                rgbNeighbour = image.getRGB(x + 1, y);
                image.setRGB(x + 1, y, (0xFF << 24)
                        | Math.max(Math.min((((rgbNeighbour >> 16) & 0xFF) + redError), 255), 0) << 16
                        | Math.max(Math.min((((rgbNeighbour >> 8) & 0xFF) + greenError), 255), 0) << 8
                        | Math.max(Math.min(((rgbNeighbour & 0xFF) + blueError), 255), 0));
            }
        }
    }

    private int[] getLookUpTable(ColorComponent component, int colorCount) {
        int[] componentPalette = new int[256];

        int intervalSize = 255 / (colorCount-1);
        int intervalExtra = 255 % (colorCount-1);

        int colorRange = 256 / colorCount;
        int rangeExtra = 256 % colorCount;

        int index = 0;
        int paletteColor = 0;
        while (index < 256) {
            int rangeAddon = 0;
            if (rangeExtra > 0) {
                rangeExtra--;
                rangeAddon = 1;
            }

            int intervalAddon = 0;
            if (intervalExtra > 0) {
                intervalExtra--;
                intervalAddon = 1;
            }

            for (int i = 0; i < colorRange + rangeAddon; i++) {
                componentPalette[index] = paletteColor;
                index++;
                if (index>=256)
                    break;
            }
            paletteColor += intervalSize + intervalAddon;
        }

        return componentPalette;
    }

    private int getClosestValue(ColorComponent component, int value) {
        return switch (component) {
            case RED -> redLookUpTable[value];
            case GREEN -> greenLookUpTable[value];
            case BLUE -> blueLookUpTable[value];
        };
    }

}

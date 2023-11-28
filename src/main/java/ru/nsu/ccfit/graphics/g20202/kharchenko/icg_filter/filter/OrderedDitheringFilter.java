package ru.nsu.ccfit.graphics.g20202.kharchenko.icg_filter.filter;

import ru.nsu.ccfit.graphics.g20202.kharchenko.icg_filter.filter.parameter.PaletteCountParameter;
import ru.nsu.ccfit.graphics.g20202.kharchenko.icg_filter.filter.parameter.RangedParameter;

import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.HashMap;

// ||M|| >= 256/количество_цветов
// cNew = closestColor (cOld + r * (M(i, j) - 1/2))
// сдвигаем каждый цвет на +-1/2 интервала
public class OrderedDitheringFilter implements ImageFilter {
    protected HashMap<String, Object> parameters = new HashMap<>() {{
        put("Palette counts", new PaletteCountParameter(2, 2, 2));
    }};
    private HashMap<ImageFilter.ColorComponent, RangedParameter> componentCount = ((PaletteCountParameter)(parameters.get("Palette counts"))).getComponentCount();

    private double[][] redMatrix;
    private double[][] greenMatrix;
    private double[][] blueMatrix;

    private int redMatrixSize;
    private int greenMatrixSize;
    private int blueMatrixSize;

    private int[] redLookUpTable = new int[256];
    private int[] greenLookUpTable = new int[256];
    private int[] blueLookUpTable = new int[256];

    private int redIntervalSize;
    private int greenIntervalSize;
    private int blueIntervalSize;

    @Override
    public BufferedImage filterImage(BufferedImage oldImage) {
        BufferedImage image = FilterSet.deepCopy(oldImage);
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                int rgb = image.getRGB(x, y);

                int red = (rgb >> 16) & 0xFF;
                int green = (rgb >> 8) & 0xFF;
                int blue = rgb & 0xFF;

                red = (int)(red + redIntervalSize * redMatrix[y % redMatrixSize][x % redMatrixSize]);
                green = (int)(green + greenIntervalSize * greenMatrix[y % greenMatrixSize][x % greenMatrixSize]);
                blue = (int)(blue + blueIntervalSize * blueMatrix[y % blueMatrixSize][x % blueMatrixSize]);

                red = Math.max(0, Math.min(red, 255));
                green = Math.max(0, Math.min(green, 255));
                blue = Math.max(0, Math.min(blue, 255));

                red = getClosestColor(ColorComponent.RED, red);
                green = getClosestColor(ColorComponent.GREEN, green);
                blue = getClosestColor(ColorComponent.BLUE, blue);

                image.setRGB(x, y, (0xFF << 24) | (red << 16) | (green << 8) | (blue));
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

        // Matrices
        redMatrixSize = 2;
        redMatrix = new double[][] {{0, 2}, {3, 1}};
        while (redMatrixSize*redMatrixSize < 256 / redCount) {
            redMatrixSize *= 2;
            double[][] newMatrix = new double[redMatrixSize][redMatrixSize];
            for (int i = 0; i < redMatrixSize/2; i++) {
                for (int j = 0; j < redMatrixSize/2; j++) {
                    newMatrix[i][j] = 4 * redMatrix[i][j];
                }
                for (int j = redMatrixSize/2; j < redMatrixSize; j++) {
                    newMatrix[i][j] = 4 * redMatrix[i][j - redMatrixSize/2] + 2;
                }
            }
            for (int i = redMatrixSize/2; i < redMatrixSize; i++) {
                for (int j = 0; j < redMatrixSize/2; j++) {
                    newMatrix[i][j] = 4 * redMatrix[i - redMatrixSize/2][j] + 3;
                }
                for (int j = redMatrixSize/2; j < redMatrixSize; j++) {
                    newMatrix[i][j] = 4 * redMatrix[i - redMatrixSize/2][j - redMatrixSize/2] + 1;
                }
            }
            redMatrix = newMatrix;
        }
        greenMatrixSize = 2;
        greenMatrix = new double[][] {{0, 2}, {3, 1}};
        while (greenMatrixSize*greenMatrixSize < 256 / greenCount) {
            greenMatrixSize *= 2;
            double[][] newMatrix = new double[greenMatrixSize][greenMatrixSize];
            for (int i = 0; i < greenMatrixSize/2; i++) {
                for (int j = 0; j < greenMatrixSize/2; j++) {
                    newMatrix[i][j] = 4 * greenMatrix[i][j];
                }
                for (int j = greenMatrixSize/2; j < greenMatrixSize; j++) {
                    newMatrix[i][j] = 4 * greenMatrix[i][j - greenMatrixSize/2] + 2;
                }
            }
            for (int i = greenMatrixSize/2; i < greenMatrixSize; i++) {
                for (int j = 0; j < greenMatrixSize/2; j++) {
                    newMatrix[i][j] = 4 * greenMatrix[i - greenMatrixSize/2][j] + 3;
                }
                for (int j = greenMatrixSize/2; j < greenMatrixSize; j++) {
                    newMatrix[i][j] = 4 * greenMatrix[i - greenMatrixSize/2][j - greenMatrixSize/2] + 1;
                }
            }
            greenMatrix = newMatrix;
        }
        blueMatrix = new double[][] {{0, 2}, {3, 1}};
        blueMatrixSize = 2;
        while (blueMatrixSize*blueMatrixSize < 256 / blueCount) {
            blueMatrixSize *= 2;
            double[][] newMatrix = new double[blueMatrixSize][blueMatrixSize];
            for (int i = 0; i < blueMatrixSize/2; i++) {
                for (int j = 0; j < blueMatrixSize/2; j++) {
                    newMatrix[i][j] = 4 * blueMatrix[i][j];
                }
                for (int j = blueMatrixSize/2; j < blueMatrixSize; j++) {
                    newMatrix[i][j] = 4 * blueMatrix[i][j - blueMatrixSize/2] + 2;
                }
            }
            for (int i = blueMatrixSize/2; i < blueMatrixSize; i++) {
                for (int j = 0; j < blueMatrixSize/2; j++) {
                    newMatrix[i][j] = 4 * blueMatrix[i - blueMatrixSize/2][j] + 3;
                }
                for (int j = blueMatrixSize/2; j < blueMatrixSize; j++) {
                    newMatrix[i][j] = 4 * blueMatrix[i - blueMatrixSize/2][j - blueMatrixSize/2] + 1;
                }
            }
            blueMatrix = newMatrix;
        }

        for (int i = 0; i < redMatrixSize; i++) {
            for (int j = 0; j < redMatrixSize; j++) {
                redMatrix[i][j] = redMatrix[i][j]/(redMatrixSize * redMatrixSize) -0.5;
            }
        }
        for (int i = 0; i < greenMatrixSize; i++) {
            for (int j = 0; j < greenMatrixSize; j++) {
                greenMatrix[i][j] = greenMatrix[i][j]/(greenMatrixSize * greenMatrixSize) - 0.5;
            }
        }
        for (int i = 0; i < blueMatrixSize; i++) {
            for (int j = 0; j < blueMatrixSize; j++) {
                blueMatrix[i][j] = blueMatrix[i][j]/(blueMatrixSize * blueMatrixSize) - 0.5;
            }
        }

        // Lookup tables
        redLookUpTable = getLookUpTable(ColorComponent.RED, redCount);
        greenLookUpTable = getLookUpTable(ColorComponent.GREEN, greenCount);
        blueLookUpTable = getLookUpTable(ColorComponent.BLUE, blueCount);
    }

    private int[] getLookUpTable(ColorComponent component, int colorCount) {
        int[] componentPalette = new int[256];

        int intervalSize = 255 / (colorCount-1);
        int intervalExtra = 255 % (colorCount-1);

        int colorRange = 256 / colorCount;
        switch (component) {
            case RED: redIntervalSize = intervalSize;
            case GREEN: greenIntervalSize = intervalSize;
            case BLUE:blueIntervalSize = intervalSize;
        }
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

    private int getClosestColor(ColorComponent component, int value) {
        return switch (component) {
            case RED -> redLookUpTable[value];
            case GREEN -> greenLookUpTable[value];
            case BLUE -> blueLookUpTable[value];
        };
    }
}

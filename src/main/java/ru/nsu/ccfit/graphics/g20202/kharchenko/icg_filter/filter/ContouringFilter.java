package ru.nsu.ccfit.graphics.g20202.kharchenko.icg_filter.filter;

import ru.nsu.ccfit.graphics.g20202.kharchenko.icg_filter.filter.parameter.RangedParameter;

import java.awt.image.BufferedImage;
import java.util.HashMap;

public class ContouringFilter extends MatrixFilter {

    public enum ContouringType {
        ROBERTS,
        SOBEL
    }

    protected HashMap<String, Object> parameters = new HashMap<>() {{
        put("Contouring type", ContouringType.ROBERTS);
        put("Threshold", new RangedParameter(0, 255, 1, 128));
    }};
    private ContouringType contouringType;
    private int threshold;

    private double[][] robertsMatrixX = new double[][]{{1, 0},
                                                {0, -1}};
    private double[][] robertsMatrixY = new double[][]{{0, 1},
                                                        {-1, 0}};

    private double[][] sobelsMatrixX = new double[][]{{-1, 0, 1},
                                                        {-2, 0, 2},
                                                        {-1, 0, 1}};
    private double[][] sobelsMatrixY = new double[][]{{-1, -2, -1},
                                                        {0, 0, 0},
                                                        {1, 2, 1}};

    public ContouringFilter() {
        updateParameters();
    }

    @Override
    public int applyMatrixToPixel(BufferedImage oldImage, BufferedImage image, int x, int y) {
        int red = 0, green = 0, blue = 0;
        int rgb1 = 0, rgb2 = 0;
        if (contouringType.equals(ContouringType.ROBERTS)) {
            matrix = robertsMatrixX;
            rgb1 = super.applyMatrixToPixel(oldImage, image, x, y);
            matrix = robertsMatrixY;
            rgb2 = super.applyMatrixToPixel(oldImage, image, x, y);
        } else if (contouringType.equals(ContouringType.SOBEL)) {
            matrix = sobelsMatrixX;
            rgb1 = super.applyMatrixToPixel(oldImage, image, x, y);
            matrix = sobelsMatrixY;
            rgb2 = super.applyMatrixToPixel(oldImage, image, x, y);
        }

        int red1 = (rgb1 >> 16) & 0xFF;
        int green1 = (rgb1 >> 8) & 0xFF;
        int blue1 = rgb1 & 0xFF;

        int red2 = (rgb2 >> 16) & 0xFF;
        int green2 = (rgb2 >> 8) & 0xFF;
        int blue2 = rgb2 & 0xFF;

        red = Math.abs(red1) + Math.abs(red2);
        green = Math.abs(green1) + Math.abs(green2);
        blue = Math.abs(blue1) + Math.abs(blue2);

        if (red > threshold)
            red = 255;
        else
            red = 0;

        if (green > threshold)
            green = 255;
        else
            green = 0;

        if (blue > threshold)
            blue = 255;
        else
            blue = 0;

        return getNormalizedColor(red, green, blue);
    }

    @Override
    public HashMap<String, Object> getParameters() {
        return parameters;
    }

    @Override
    public void updateParameters() {
        contouringType = (ContouringType)parameters.get("Contouring type");
        if (contouringType.equals(ContouringType.ROBERTS)) {
            size = 2;
        } else if (contouringType.equals(ContouringType.SOBEL)) {
            size = 3;
        }

        threshold = ((RangedParameter)parameters.get("Threshold")).getValue();
    }
}

package ru.nsu.ccfit.graphics.g20202.kharchenko.icg_filter.filter;

import java.awt.image.BufferedImage;

public abstract class MatrixFilter implements ImageFilter {
    protected double[][] matrix;
    protected int size;

    @Override
    public BufferedImage filterImage(BufferedImage oldImage) {
        BufferedImage image = FilterSet.deepCopy(oldImage);
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                image.setRGB(x, y, applyMatrixToPixel(oldImage, image, x, y));
            }
        }
        return image;
    }
    public int applyMatrixToPixel(BufferedImage oldImage, BufferedImage image, int x, int y) {
        if (size % 2 == 1) {
            return applyCenterMatrixToPixel(oldImage, image, x, y);
        } else {
            return applyCornerMatrixToPixel(oldImage, image, x, y);
        }
    }

    private int applyCornerMatrixToPixel(BufferedImage oldImage, BufferedImage image, int x, int y) {
        int blue = 0;
        int green = 0;
        int red = 0;

        int rgb;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (x + j >= image.getWidth() || y + i >= image.getHeight())
                    rgb = oldImage.getRGB(x, y);
                else
                    rgb = oldImage.getRGB(x + j, y + i);

                blue += ((rgb & 0xFF) * matrix[i][j]);
                green += (((rgb >> 8) & 0xFF) * matrix[i][j]);
                red += (((rgb >> 16) & 0xFF) * matrix[i][j]);
            }
        }

        return getNormalizedColor(red, green, blue);
    }

    private int applyCenterMatrixToPixel(BufferedImage oldImage, BufferedImage image, int x, int y) {
        double blue = 0;
        double green = 0;
        double red = 0;

        for (int i = -size / 2; i <= size / 2; i++) {
            for (int j = -size / 2; j <= size / 2; j++) {
                int rgb;
                if (x + j < 0 || x + j >= image.getWidth() || y + i < 0 || y + i >= image.getHeight())
                    rgb = oldImage.getRGB(x, y);
                else
                    rgb = oldImage.getRGB(x + j, y + i);

                blue += ((rgb & 0xFF) * matrix[i + size / 2][j + size / 2]);
                green += (((rgb >> 8) & 0xFF) * matrix[i + size / 2][j + size / 2]);
                red += (((rgb >> 16) & 0xFF) * matrix[i + size / 2][j + size / 2]);
            }
        }

        return getNormalizedColor((int)red, (int)green, (int)blue);
    }

    protected int getNormalizedColor(int red, int green, int blue) {
        red = Math.max(Math.min(red, 255), 0);
        green = Math.max(Math.min(green, 255), 0);
        blue = Math.max(Math.min(blue, 255), 0);

        return ((blue) | ((green) << 8) | ((red) << 16) | (0xFF << 24));
    }
}

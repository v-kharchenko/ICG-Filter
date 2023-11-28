package ru.nsu.ccfit.graphics.g20202.kharchenko.icg_filter.filter;

import ru.nsu.ccfit.graphics.g20202.kharchenko.icg_filter.filter.parameter.RangedParameter;

import java.awt.image.BufferedImage;
import java.util.HashMap;

public class EmbossFilter extends MatrixFilter {

    public EmbossFilter() {
        matrix = new double[][]{{0, 1, 0},
                            {-1, 0, 1},
                            {0, -1, 0}};
        size = 3;
    }

    @Override
    public int applyMatrixToPixel(BufferedImage oldImage, BufferedImage image, int x, int y) {
        return ColorParser.applyToComponents(super.applyMatrixToPixel(oldImage, image, x, y), c -> c + 128);
    }

    @Override
    public HashMap<String, Object> getParameters() {
        return null;
    }

    @Override
    public void updateParameters() {

    }
}

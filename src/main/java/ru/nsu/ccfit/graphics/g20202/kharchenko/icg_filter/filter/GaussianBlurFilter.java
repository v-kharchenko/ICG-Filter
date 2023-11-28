package ru.nsu.ccfit.graphics.g20202.kharchenko.icg_filter.filter;

import ru.nsu.ccfit.graphics.g20202.kharchenko.icg_filter.filter.parameter.PaletteCountParameter;
import ru.nsu.ccfit.graphics.g20202.kharchenko.icg_filter.filter.parameter.RangedParameter;

import java.util.HashMap;

public class GaussianBlurFilter extends MatrixFilter {

    protected HashMap<String, Object> parameters = new HashMap<>() {{
        put("Sigma (/10)", new RangedParameter(1, 50, 1, 10));
        put("Size", new RangedParameter(3, 11, 2, 3));
    }};
    private double sigma;

    public GaussianBlurFilter() {
        updateParameters();
    }

    @Override
    public HashMap<String, Object> getParameters() {
        return parameters;
    }

    @Override
    public void updateParameters() {
        sigma = ((double)((RangedParameter)parameters.get("Sigma (/10)")).getValue())/10.0;
        size = ((RangedParameter)parameters.get("Size")).getValue();
        matrix = new double[size][size];
        double divider = 0;
        for (int y = -size/2; y <= size/2; y++) {
            for (int x = -size / 2; x <= size / 2; x++) {
                matrix[y + size / 2][x + size / 2] = gaussianFunction(sigma, x, y);
                divider += matrix[y + size / 2][x + size / 2];
            }
        }

        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                matrix[y][x] /= divider;
            }
        }
    }

    public static double gaussianFunction(double sigma, int x, int y) {
        return Math.exp(-(x*x + y*y)/(2.0 * sigma * sigma)) / (2.0 * Math.PI * sigma * sigma);
    }
}

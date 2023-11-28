package ru.nsu.ccfit.graphics.g20202.kharchenko.icg_filter.filter;

import ru.nsu.ccfit.graphics.g20202.kharchenko.icg_filter.filter.parameter.RangedParameter;

import java.util.HashMap;

public class IncreaseSharpnessFilter extends MatrixFilter {

    public IncreaseSharpnessFilter() {
        matrix = new double[][] {{0, -1, 0},
                                {-1, 5, -1},
                                {0, -1, 0}};
        size = 3;
    }

    @Override
    public HashMap<String, Object> getParameters() {
        return null;
    }

    @Override
    public void updateParameters() {
    }
}

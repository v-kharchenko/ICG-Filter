package ru.nsu.ccfit.graphics.g20202.kharchenko.icg_filter.filter.parameter;

public class RangedParameter {
    private int lowEnd;
    private int highEnd;
    private int stepSize;
    private int value;

    public RangedParameter(int low, int high, int step, int defaultValue) {
        lowEnd = low;
        highEnd = high;
        stepSize = step;
        value = defaultValue;
    }

    public int getLowEnd() {
        return lowEnd;
    }

    public int getHighEnd() {
        return highEnd;
    }

    public int getStepSize() {
        return stepSize;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}


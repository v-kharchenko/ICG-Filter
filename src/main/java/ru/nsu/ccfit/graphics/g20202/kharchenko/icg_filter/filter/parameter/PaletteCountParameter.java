package ru.nsu.ccfit.graphics.g20202.kharchenko.icg_filter.filter.parameter;

import ru.nsu.ccfit.graphics.g20202.kharchenko.icg_filter.filter.ImageFilter;

import java.util.HashMap;

public class PaletteCountParameter {
    private HashMap<ImageFilter.ColorComponent, RangedParameter> componentCount;

    public PaletteCountParameter(int redCount, int greenCount, int blueCount) {
        componentCount = new HashMap<>() {{
            put(ImageFilter.ColorComponent.RED, new RangedParameter(2, 128, 1, 2));
            put(ImageFilter.ColorComponent.GREEN, new RangedParameter(2, 128, 1, 2));
            put(ImageFilter.ColorComponent.BLUE, new RangedParameter(2, 128, 1, 2));
        }};
    }

    public void setComponentCounts(int redCount, int greenCount, int blueCount) {
        componentCount.get(ImageFilter.ColorComponent.RED).setValue(redCount);
        componentCount.get(ImageFilter.ColorComponent.GREEN).setValue(greenCount);
        componentCount.get(ImageFilter.ColorComponent.BLUE).setValue(blueCount);
    }

    public HashMap<ImageFilter.ColorComponent, RangedParameter> getComponentCount() {
        return componentCount;
    }

}

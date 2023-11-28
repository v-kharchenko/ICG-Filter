package ru.nsu.ccfit.graphics.g20202.kharchenko.icg_filter.filter;

import ru.nsu.ccfit.graphics.g20202.kharchenko.icg_filter.filter.parameter.PaletteCountParameter;
import ru.nsu.ccfit.graphics.g20202.kharchenko.icg_filter.filter.parameter.RangedParameter;

import java.awt.image.BufferedImage;
import java.util.HashMap;

public class ThresholdFilter implements ImageFilter {
    protected HashMap<String, Object> parameters = new HashMap<>() {{
        put("Palette counts", new PaletteCountParameter(2, 2, 2));
    }};
    private HashMap<ImageFilter.ColorComponent, RangedParameter> componentCount = ((PaletteCountParameter)(parameters.get("Palette counts"))).getComponentCount();

    @Override
    public BufferedImage filterImage(BufferedImage image) {
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                int rgb = image.getRGB(x, y);

                int red = (rgb>>16) & 0xFF;
                int green = (rgb>>8) & 0xFF;
                int blue = rgb & 0xFF;

                red = getClosestValue(ColorComponent.RED, red);
                green = getClosestValue(ColorComponent.GREEN, green);
                blue = getClosestValue(ColorComponent.BLUE, blue);

                image.setRGB(x, y, (red << 16) | (green << 8) | blue | (0xFF << 24));
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
    }

    private int getClosestValue(ColorComponent component, int value) {
        int colorStep = 255 / ((int)componentCount.get(component).getValue() - 1);

        int lowEnd = value / colorStep * colorStep;

        if (value - lowEnd < lowEnd + colorStep - value) {
            return lowEnd;
        } else {
            return lowEnd + colorStep;
        }
    }
}

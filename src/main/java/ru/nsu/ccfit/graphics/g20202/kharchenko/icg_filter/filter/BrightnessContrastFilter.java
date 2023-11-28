package ru.nsu.ccfit.graphics.g20202.kharchenko.icg_filter.filter;

import ru.nsu.ccfit.graphics.g20202.kharchenko.icg_filter.filter.parameter.RangedParameter;

import java.awt.image.BufferedImage;
import java.util.HashMap;

public class BrightnessContrastFilter implements ImageFilter {

    private HashMap<String, Object> parameters = new HashMap<>() {{
        put("Brightness", new RangedParameter(-255, 255, 1, 0));
        put("Contrast", new RangedParameter(-100, 100, 1, 0));
    }};
    private int brightness;
    private double contrast;

    public BrightnessContrastFilter() {
        updateParameters();
    }

    @Override
    public BufferedImage filterImage(BufferedImage image) {

        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                image.setRGB(x, y, ColorParser.applyToComponents(image.getRGB(x,y), c -> (int) ((c - 128) * contrast + 128 + brightness)));
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
        brightness = ((RangedParameter)parameters.get("Brightness")).getValue();
        contrast = ((RangedParameter)parameters.get("Contrast")).getValue()/100.0 + 1.0;
    }
}

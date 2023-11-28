package ru.nsu.ccfit.graphics.g20202.kharchenko.icg_filter.filter;

import ru.nsu.ccfit.graphics.g20202.kharchenko.icg_filter.filter.parameter.RangedParameter;

import java.awt.image.BufferedImage;
import java.util.HashMap;

public class GammaFilter implements ImageFilter {
    private HashMap<String, Object> parameters = new HashMap<>() {{
        put("Gamma (/10)", new RangedParameter(1, 100, 1, 10));
    }};
    private double gamma;

    public GammaFilter() {
        updateParameters();
    }

    @Override
    public BufferedImage filterImage(BufferedImage image) {
        BufferedImage newImage = FilterSet.deepCopy(image);
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                int color = image.getRGB(x, y);

                int newColor = ColorParser.applyToComponents(color, c -> (int) (Math.pow((double) c / 255, gamma) * 255));

                newImage.setRGB(x, y, newColor);
            }
        }
        return newImage;
    }

    @Override
    public HashMap<String, Object> getParameters() {
        return parameters;
    }

    @Override
    public void updateParameters() {
        gamma = ((RangedParameter)parameters.get("Gamma (/10)")).getValue() / 10.0;
    }
}

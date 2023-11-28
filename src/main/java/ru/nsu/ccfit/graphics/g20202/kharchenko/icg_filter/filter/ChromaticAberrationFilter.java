package ru.nsu.ccfit.graphics.g20202.kharchenko.icg_filter.filter;

import ru.nsu.ccfit.graphics.g20202.kharchenko.icg_filter.filter.parameter.RangedParameter;

import java.awt.image.BufferedImage;
import java.util.HashMap;

public class ChromaticAberrationFilter implements ImageFilter {

    private HashMap<String, Object> parameters = new HashMap<>() {{
        put("Offset", new RangedParameter(-50, 50, 1, 10));
        put("Color component", ColorComponent.GREEN);
    }};

    private int offset = (int)((RangedParameter)parameters.get("Offset")).getValue();

    @Override
    public BufferedImage filterImage(BufferedImage image) {
        ColorComponent component = (ColorComponent)parameters.get("Color component");
        if (offset > 0) {
            for (int y = 0; y < image.getHeight(); y++) {
                for (int x = 0; x < image.getWidth() - offset; x++) {
                    int rgb = image.getRGB(x, y);

                    int newComp = image.getRGB(x + offset, y) & (0xFF << component.getOffset());
                    rgb = (rgb & ~(0xFF << component.getOffset())) | newComp;

                    image.setRGB(x, y, rgb);
                }
            }
        } else if (offset < 0) {
            for (int y = image.getHeight() - 1; y >= 0; y--) {
                for (int x = image.getWidth() - 1; x >= -offset; x--) {
                    int rgb = image.getRGB(x, y);

                    int newComp = image.getRGB(x + offset, y) & (0xFF << component.getOffset());
                    rgb = (rgb & ~(0xFF << component.getOffset())) | newComp;

                    image.setRGB(x, y, rgb);
                }
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
        offset = (int)((RangedParameter)parameters.get("Offset")).getValue();
    }
}

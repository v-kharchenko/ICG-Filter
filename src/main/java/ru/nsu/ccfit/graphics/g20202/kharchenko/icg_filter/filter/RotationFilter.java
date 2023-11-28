package ru.nsu.ccfit.graphics.g20202.kharchenko.icg_filter.filter;

import ru.nsu.ccfit.graphics.g20202.kharchenko.icg_filter.filter.parameter.RangedParameter;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;

public class RotationFilter implements ImageFilter {

    protected HashMap<String, Object> parameters = new HashMap<>() {{
        put("Angle", new RangedParameter(-180, 180, 1, 90));
    }};
    private int angle;

    public RotationFilter() {
        updateParameters();
    }

    @Override
    public BufferedImage filterImage(BufferedImage image) {
        double sin = Math.sin(Math.toRadians(angle));
        double cos = Math.cos(Math.toRadians(angle));

        if (Math.abs(sin) < 1E-10)
            sin = 0;
        if (Math.abs(cos) < 1E-10)
            cos = 0;

        int newHeight = (int) ((image.getWidth() * Math.abs(sin)) + (image.getHeight() * Math.abs(cos)));
        int newWidth = (int) ((image.getHeight() * Math.abs(sin)) + (image.getWidth() * Math.abs(cos)));

        BufferedImage newImage = new BufferedImage(newWidth, newHeight, image.getType());

        Graphics2D g = (Graphics2D)newImage.getGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, newWidth, newHeight);


        int y_lim = newHeight % 2;
        int x_lim = newWidth % 2;
        for (int y = -newHeight/2; y < newHeight/2 + y_lim; y++) {
            for (int x = -newWidth/2; x < newWidth/2 + x_lim; x++) {
                int x1 = (int)(x * cos + y * sin);
                int y1 = (int)(-x * sin + y * cos);
                if (x1 + image.getWidth()/2 < 0 || x1 + image.getWidth()/2 >= image.getWidth() || y1 + image.getHeight()/2 < 0 || y1 + image.getHeight()/2 >= image.getHeight())
                    continue;
                newImage.setRGB(x + newWidth/2, y + newHeight/2, image.getRGB(x1 + image.getWidth()/2, y1 + image.getHeight()/2));
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
        angle = ((RangedParameter)parameters.get("Angle")).getValue();
    }
}

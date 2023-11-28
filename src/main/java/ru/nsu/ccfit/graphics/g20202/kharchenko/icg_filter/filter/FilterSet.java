package ru.nsu.ccfit.graphics.g20202.kharchenko.icg_filter.filter;

import ru.nsu.ccfit.graphics.g20202.kharchenko.icg_filter.engine.ImageEngine;
import ru.nsu.ccfit.graphics.g20202.kharchenko.icg_filter.file.FilterAppliedListener;

import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.util.ArrayList;
import java.util.HashMap;

public class FilterSet {

    public enum FilterType {
        COLOR_INVERSION,
        GRAYSCALE,
        CHROMATIC_ABRR,
        THRESHOLD,
        ORDERED_DITHERING,
        FLOYD_STEINBERG,
        EMBOSS,
        INCREASE_SHARPNESS,
        GAUSSIAN_BLUR,
        ROTATE,
        BRIGHTNESS_CONTRAST,
        WATERCOLOR,
        CONTOUR,
        GAMMA;

        @Override
        public String toString() {
            switch (this) {
                case FLOYD_STEINBERG -> {
                    return "Floyd-Steinberg dithering";
                }
                case GAMMA -> {
                    return "Gamma correction";
                }
                case CHROMATIC_ABRR -> {
                    return "Chromatic aberration";
                }
                default -> {
                    String name = super.toString();
                    name = name.replace('_', ' ')
                            .toLowerCase();
                    name = name.substring(0,1).toUpperCase().concat(name.substring(1));
                    return name;
                }
            }
        }
    }

    private HashMap<FilterType, ImageFilter> typeToFilter;

    private ArrayList<FilterAppliedListener> filterAppliedListeners = new ArrayList<>();

    private ImageEngine imageEngine;

    public FilterSet(ImageEngine imageEngine) {
        this.imageEngine = imageEngine;
        typeToFilter = new HashMap<>() {{
            put(FilterType.COLOR_INVERSION, new ColorInversionFilter());
            put(FilterType.FLOYD_STEINBERG, new FloydSteinbergDitheringFilter());
            put(FilterType.GRAYSCALE, new GrayscaleFilter());
            put(FilterType.CHROMATIC_ABRR, new ChromaticAberrationFilter());
            put(FilterType.ORDERED_DITHERING, new OrderedDitheringFilter());
            put(FilterType.THRESHOLD, new ThresholdFilter());
            put(FilterType.EMBOSS, new EmbossFilter());
            put(FilterType.INCREASE_SHARPNESS, new IncreaseSharpnessFilter());
            put(FilterType.GAUSSIAN_BLUR, new GaussianBlurFilter());
            put(FilterType.ROTATE, new RotationFilter());
            put(FilterType.BRIGHTNESS_CONTRAST, new BrightnessContrastFilter());
            put(FilterType.WATERCOLOR, new WatercolorFilter());
            put(FilterType.GAMMA, new GammaFilter());
            put(FilterType.CONTOUR, new ContouringFilter());
        }};
    }

    public void applyFilter(FilterType filterType) {
        // Get last image in engine -> Get required filter -> Apply filter to image -> Add new image to engine
        imageEngine.addImage(typeToFilter.get(filterType).filterImage(deepCopy(imageEngine.getCurrentImage())));

        for (var l: filterAppliedListeners) {
            l.filterApplied(filterType);
        }
    }

    public HashMap<String, Object> getParameters(FilterType filterType) {
        return typeToFilter.get(filterType).getParameters();
    }


    public void updateParameters(FilterType type) {
        typeToFilter.get(type).updateParameters();
    }

    /**
     * This function is used to return copies of images. This way, filters will not rewrite engine's history.
     * @param bi image to be copied.
     * @return copy of bi
     */
    static BufferedImage deepCopy(BufferedImage bi) {
        ColorModel cm = bi.getColorModel();
        boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
        WritableRaster raster = bi.copyData(bi.getRaster().createCompatibleWritableRaster());
        return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
    }

    public void addFilterAppliedListener(FilterAppliedListener listener) {
        filterAppliedListeners.add(listener);
    }
}

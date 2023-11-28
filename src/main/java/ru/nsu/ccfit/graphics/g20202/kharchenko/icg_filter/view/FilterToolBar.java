package ru.nsu.ccfit.graphics.g20202.kharchenko.icg_filter.view;

import ru.nsu.ccfit.graphics.g20202.kharchenko.icg_filter.filter.FilterSet;
import ru.nsu.ccfit.graphics.g20202.kharchenko.icg_filter.filter.RotationFilter;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class FilterToolBar extends JToolBar {

    FilterAppFrame parentFrame;
    ImageFileChooser imageFileChooser;

    HashMap<FilterSet.FilterType, String> filterIcons = new HashMap<>() {{
        put(FilterSet.FilterType.COLOR_INVERSION, "src/main/resources/invert_color_icon.png");
        put(FilterSet.FilterType.GRAYSCALE, "src/main/resources/grayscale_icon.png");
        put(FilterSet.FilterType.CHROMATIC_ABRR, "src/main/resources/chromatic_abbr_icon.png");
        put(FilterSet.FilterType.THRESHOLD, "src/main/resources/threshold_icon.png");
        put(FilterSet.FilterType.FLOYD_STEINBERG, "src/main/resources/floyd_steinberg_icon.png");
        put(FilterSet.FilterType.ORDERED_DITHERING, "src/main/resources/ordered_dithering_icon.png");
        put(FilterSet.FilterType.EMBOSS, "src/main/resources/high_pass_icon.png");
        put(FilterSet.FilterType.INCREASE_SHARPNESS, "src/main/resources/sharpness_icon.png");
        put(FilterSet.FilterType.GAUSSIAN_BLUR, "src/main/resources/blur_icon.png");
        put(FilterSet.FilterType.ROTATE, "src/main/resources/rotate_icon.png");
        put(FilterSet.FilterType.BRIGHTNESS_CONTRAST, "src/main/resources/brightness_contrast_icon.png");
        put(FilterSet.FilterType.WATERCOLOR, "src/main/resources/watercolor_icon.png");
        put(FilterSet.FilterType.GAMMA, "src/main/resources/gamma_icon.png");
        put(FilterSet.FilterType.CONTOUR, "src/main/resources/contour_icon.png");
    }};
    private ArrayList<JComponent> imageDependantComponents = new ArrayList<>();

    public FilterToolBar(FilterAppFrame parentFrame, ImageFileChooser imageFileChooser, FilterSet filterSet, ParameterDialogFactory parameterDialogFactory) {
        super();
        this.setFloatable(false);

        this.parentFrame = parentFrame;
        this.imageFileChooser = imageFileChooser;

        // Open item
        addButton("Open", "src/main/resources/open_file_icon.png").addActionListener(e -> parentFrame.imageOpened(imageFileChooser.showOpenDialog()));

        // Save item
        addImageDependantButton("Save", "src/main/resources/save_file_icon.png")
                .addActionListener(e -> parentFrame.imageSaved(imageFileChooser.showSaveDialog()));

        addCategorySeparator();

        for (var type: FilterSet.FilterType.values()) {
            addImageDependantButton(type.toString(), filterIcons.get(type))
                    .addActionListener(e -> parameterDialogFactory.showParameterDialog(type));
        }
    }

    private void addCategorySeparator() {
        this.addSeparator(new Dimension(10, 10));
    }


    private JButton addImageDependantButton(String itemName, String iconPath) {
        JButton button = addButton(itemName, iconPath);
        button.setEnabled(false);
        imageDependantComponents.add(button);
        return button;
    }

    private JButton addButton(String itemName, String iconPath) {
        JButton button;
        try {
            button = new JButton(new ImageIcon(ImageIO.read(new File(iconPath))));
        } catch (IOException e) {
            button = new JButton(itemName);
        }
        button.setToolTipText(itemName);

        this.add(button);
        return button;
    }

    public void imageOpened() {
        for (var c: imageDependantComponents) {
            c.setEnabled(true);
        }
    }

    public void imageClosed() {
        for (var c: imageDependantComponents) {
            c.setEnabled(false);
        }
    }
}

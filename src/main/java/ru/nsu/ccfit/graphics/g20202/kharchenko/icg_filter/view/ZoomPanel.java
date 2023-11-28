package ru.nsu.ccfit.graphics.g20202.kharchenko.icg_filter.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.util.HashMap;

public class ZoomPanel extends JPanel {
    static final double MIN_ZOOM = 5.0;
    static final double MAX_ZOOM = 1000.0;

    HashMap<String, Object> resizeStyles = new HashMap<>() {{
        put("Bilinear", RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        put("Bicubic", RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        put("Nearest neighbor", RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
    }};

    JComboBox<String> resizeStyleBox;
    JButton fitToScreenButton;
    JButton originalSizeButton;
    ImageNavigator imageNavigator;
    SpinnerModel zoomSpinnerModel;
    JSpinner zoomSpinner;


    public ZoomPanel(ImageNavigator imageNavigator) {
        super();
        this.setLayout(new FlowLayout(FlowLayout.RIGHT));

        this.imageNavigator = imageNavigator;

        // Resizing type
        resizeStyleBox = new JComboBox<>(resizeStyles.keySet().toArray(new String[0]));
        resizeStyleBox.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                imageNavigator.setZoomInterpolation(resizeStyles.get((String)e.getItem()));
                imageNavigator.repaint();
            }
        });
        resizeStyleBox.setSelectedItem("Bilinear");
        this.add(resizeStyleBox);

        // Original size
        originalSizeButton = new JButton("Full size");
        originalSizeButton.setEnabled(false);
        originalSizeButton.addActionListener(e -> imageNavigator.setZoom(100.0));
        this.add(originalSizeButton);

        // Fit to screen
        fitToScreenButton = new JButton("Fit to screen");
        fitToScreenButton.setEnabled(false);
        this.add(fitToScreenButton);

        // Zoom spinner
        zoomSpinnerModel = new SpinnerNumberModel(100.0f, MIN_ZOOM, MAX_ZOOM, 10.0);
        zoomSpinner = new JSpinner(zoomSpinnerModel);
        zoomSpinner.addChangeListener(e -> imageNavigator.setZoom((double)zoomSpinnerModel.getValue()));
        fitToScreenButton.addActionListener(e -> {
            double zoom = imageNavigator.fitToScreenZoom();
            if (zoom != -1) {
                zoomSpinnerModel.setValue(zoom);
            }
        });
        originalSizeButton.addActionListener(e -> {
            zoomSpinnerModel.setValue(100.0);
        });
        zoomSpinner.setEnabled(false);
        this.add(zoomSpinner);
        this.add(new JLabel("%"));
    }
    public void imageOpened() {
        zoomSpinnerModel.setValue(100.0);
        zoomSpinner.setEnabled(true);
        fitToScreenButton.setEnabled(true);
        originalSizeButton.setEnabled(true);
    }

    public void imageClosed() {
        zoomSpinner.setEnabled(false);
        fitToScreenButton.setEnabled(false);
        originalSizeButton.setEnabled(false);
    }

    public HashMap<String, Object> getResizeStyles() {
        return resizeStyles;
    }

    public void setZoomInterpolation(String resizeStyleName, Object resizeStyle) {
        resizeStyleBox.setSelectedItem(resizeStyleName);
        imageNavigator.setZoomInterpolation(resizeStyle);
        imageNavigator.repaint();
    }

    public void setFullZoom() {
        imageNavigator.setZoom(100.0);
        zoomSpinnerModel.setValue(100.0);
    }

    public void fitToScreenZoom() {
        double zoom = imageNavigator.fitToScreenZoom();
        if (zoom != -1)
            zoomSpinnerModel.setValue(zoom);
    }

    public JComboBox<String> getResizeStyleBox() {
        return resizeStyleBox;
    }
}
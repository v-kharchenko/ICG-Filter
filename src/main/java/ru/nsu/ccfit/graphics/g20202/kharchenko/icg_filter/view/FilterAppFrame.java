package ru.nsu.ccfit.graphics.g20202.kharchenko.icg_filter.view;

import com.formdev.flatlaf.FlatDarculaLaf;
import ru.nsu.ccfit.graphics.g20202.kharchenko.icg_filter.engine.ImageEngine;
import ru.nsu.ccfit.graphics.g20202.kharchenko.icg_filter.file.ImageFileManager;
import ru.nsu.ccfit.graphics.g20202.kharchenko.icg_filter.filter.FilterSet;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class FilterAppFrame extends JFrame {

    final static public String FILTER_APP_ICON = "src/main/resources/filter_app_icon.png";

    private ImageEngine imageEngine;
    private ImageNavigator imageNavigator;

    private ImageFileManager imageFileManager;
    private ImageFileChooser imageFileChooser;

    private FilterMenuBar menuBar;
    private FilterToolBar toolBar;
    private ZoomPanel zoomPanel;

    public FilterAppFrame(ImageEngine imageEngine, ImageFileManager imageFileManager, FilterSet filterSet) {
        // Basic frame settings
        super("ICGFilter");
        setPreferredSize(new Dimension(800, 600));
        setMinimumSize(new Dimension(640, 480));
        setSize(new Dimension(800, 600));
        setResizable(true);
        setLocation(300, 100);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Setup window theme
        FlatDarculaLaf.setup();
        SwingUtilities.updateComponentTreeUI(this);

        try {
            setIconImage(ImageIO.read(new File(FILTER_APP_ICON)));
        } catch (IOException ignored) {}

        // Fields
        this.imageEngine = imageEngine;
        this.imageFileManager = imageFileManager;
        this.imageFileChooser = new ImageFileChooser(this, imageFileManager);

        // Image navigator
        imageNavigator = new ImageNavigator(imageEngine);
        this.add(imageNavigator, BorderLayout.CENTER);

        // Zoom panel
        zoomPanel = new ZoomPanel(imageNavigator);
        this.add(zoomPanel, BorderLayout.PAGE_END);

        // Dialog factory
        ParameterDialogFactory parameterDialogFactory = new ParameterDialogFactory(this, filterSet);

        // Menu bar
        menuBar = new FilterMenuBar(this, imageFileChooser, parameterDialogFactory, zoomPanel, imageNavigator);
        this.setJMenuBar(menuBar);

        // Toolbar
        toolBar = new FilterToolBar(this, imageFileChooser, filterSet, parameterDialogFactory);
        this.add(toolBar, BorderLayout.PAGE_START);

        // History list
        HistoryList history = new HistoryList(imageEngine, filterSet, imageNavigator);
        this.add(history, BorderLayout.EAST);
    }

    public void imageOpened(File file) {
        if (file != null) {
            this.setTitle("ICGFilter - " + file.getName());
            toolBar.imageOpened();
            menuBar.imageOpened();
            zoomPanel.imageOpened();
            imageNavigator.repaint();
        }
    }

    public void imageSaved(File file) {
        if (file != null) {
            this.setTitle("ICGFilter - " + file.getName());
        }
    }

    public void imageClosed() {
        this.setTitle("ICGFilter");
        toolBar.imageClosed();
        menuBar.imageClosed();
        zoomPanel.imageClosed();
        imageNavigator.repaint();
    }
}

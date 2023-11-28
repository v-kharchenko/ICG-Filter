package ru.nsu.ccfit.graphics.g20202.kharchenko.icg_filter.view;

import com.formdev.flatlaf.FlatDarculaLaf;
import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.themes.FlatMacDarkLaf;
import ru.nsu.ccfit.graphics.g20202.kharchenko.icg_filter.filter.FilterSet;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;

public class FilterMenuBar extends JMenuBar {

    private FilterAppFrame parentFrame;
    private ImageFileChooser imageFileChooser;
    private ParameterDialogFactory parameterDialogFactory;
    private ImageNavigator imageNavigator;

    private ArrayList<JComponent> imageDependantComponents = new ArrayList<>();

    public FilterMenuBar(FilterAppFrame parentFrame, ImageFileChooser imageFileChooser, ParameterDialogFactory parameterDialogFactory, ZoomPanel zoomPanel, ImageNavigator imageNavigator) {
        super();

        this.parentFrame = parentFrame;
        this.imageFileChooser = imageFileChooser;
        this.imageNavigator = imageNavigator;
        this.parameterDialogFactory = parameterDialogFactory;

        // File menu
        this.add(createFileMenu());

        // Filter menu
        this.add(createFilterMenu());

        // View menu
        this.add(createViewMenu(zoomPanel));

        // Help menu
        this.add(createHelpMenu());
    }

    private JMenu createFilterMenu() {
        JMenu filterMenu = new JMenu("Filters");
        imageDependantComponents.add(filterMenu);
        filterMenu.setEnabled(false);

        for (var type: FilterSet.FilterType.values()) {
            JMenuItem filterItem = new JMenuItem(type.toString());
            filterItem.addActionListener(e -> parameterDialogFactory.showParameterDialog(type));
            filterMenu.add(filterItem);
        }

        return filterMenu;
    }

    private JMenu createViewMenu(ZoomPanel zoomPanel) {
        JMenu viewMenu = new JMenu("View");

        // Full size
        JMenuItem fullSizeItem = new JMenuItem("Full size");
        fullSizeItem.setEnabled(false);
        imageDependantComponents.add(fullSizeItem);
        fullSizeItem.addActionListener(e -> zoomPanel.setFullZoom());
        viewMenu.add(fullSizeItem);

        // Fit to screen
        JMenuItem fitToScreenItem = new JMenuItem("Fit to screen");
        fitToScreenItem.setEnabled(false);
        imageDependantComponents.add(fitToScreenItem);
        fitToScreenItem.addActionListener(e -> zoomPanel.fitToScreenZoom());
        viewMenu.add(fitToScreenItem);

        // Interpolation mode
        JMenu interpolationMenu = new JMenu("Interpolation mode");
        viewMenu.add(interpolationMenu);

        HashMap<String, Object> resizeStyles = zoomPanel.getResizeStyles();
        HashMap<String, JRadioButtonMenuItem> resizeStyleButtons = new HashMap<>();
        ButtonGroup resizeStylesGroup = new ButtonGroup();
        for (var entry: resizeStyles.entrySet()) {
            JRadioButtonMenuItem resizeStyle = new JRadioButtonMenuItem(entry.getKey());
            resizeStylesGroup.add(resizeStyle);
            interpolationMenu.add(resizeStyle);
            resizeStyle.addActionListener(e -> zoomPanel.setZoomInterpolation(entry.getKey(), entry.getValue()));
            if (entry.getKey().equalsIgnoreCase("Bilinear")) {
                resizeStyle.setSelected(true);
            }
            resizeStyleButtons.put(entry.getKey(), resizeStyle);
        }
        zoomPanel.getResizeStyleBox().addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                resizeStylesGroup.setSelected(resizeStyleButtons.get((String)e.getItem()).getModel(), true);
            }
        });

        // Viewing mode
        JRadioButtonMenuItem originalViewItem = new JRadioButtonMenuItem("View original image");
        imageDependantComponents.add(originalViewItem);
        originalViewItem.setSelected(false);
        originalViewItem.addActionListener(e -> {
            imageNavigator.changeViewMode();
        });
        imageNavigator.addViewChangeListener(() -> {
            originalViewItem.setSelected(!originalViewItem.isSelected());
        });
        viewMenu.add(originalViewItem);

        viewMenu.addSeparator();

        // Theme menu
        JMenu themeMenu = new JMenu("Theme");
        viewMenu.add(themeMenu);

        ButtonGroup themeChooser = new ButtonGroup();

        // Dark theme
        JRadioButtonMenuItem darculaThemeItem = new JRadioButtonMenuItem("Darcula");
        darculaThemeItem.addActionListener(e -> {
            FlatDarculaLaf.setup();
            SwingUtilities.updateComponentTreeUI(parentFrame);
        });
        darculaThemeItem.setSelected(true);
        themeMenu.add(darculaThemeItem);
        themeChooser.add(darculaThemeItem);

        // Light theme
        JRadioButtonMenuItem lightThemeItem = new JRadioButtonMenuItem("Light");
        lightThemeItem.addActionListener(e -> {
            FlatLightLaf.setup();
            SwingUtilities.updateComponentTreeUI(parentFrame);
        });
        themeMenu.add(lightThemeItem);
        themeChooser.add(lightThemeItem);

        // Dark theme
        JRadioButtonMenuItem darkThemeItem = new JRadioButtonMenuItem("Dark");
        darkThemeItem.addActionListener(e -> {
            FlatMacDarkLaf.setup();
            SwingUtilities.updateComponentTreeUI(parentFrame);
        });
        themeMenu.add(darkThemeItem);
        themeChooser.add(darkThemeItem);

        return viewMenu;
    }

    private JMenu createHelpMenu() {
        JMenu helpMenu = new JMenu("Help");

        // About menu
        JMenuItem aboutItem = new JMenuItem("About");
        aboutItem.addActionListener(e -> showAboutMessage());
        helpMenu.add(aboutItem);

        return helpMenu;
    }

    private void showAboutMessage() {
        JTextArea aboutText = new JTextArea();

        aboutText.setEditable(false);
        aboutText.setLineWrap(true);
        aboutText.setWrapStyleWord(true);

        aboutText.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        try {
            InputStreamReader aboutFile = new InputStreamReader(new FileInputStream("src/main/resources/about.txt"), StandardCharsets.UTF_8);
            aboutText.read(aboutFile, "txt");
        } catch (IOException e) {
            aboutText.setText("Failed to load about.txt");
        }

        JScrollPane aboutTextScroll = new JScrollPane(aboutText);
        aboutTextScroll.setPreferredSize(new Dimension(400, 200));

        JOptionPane.showMessageDialog(this.getParent(), aboutTextScroll, "About", JOptionPane.INFORMATION_MESSAGE);
    }

    private JMenu createFileMenu() {
        JMenu fileMenu = new JMenu("File");

        // Open file
        JMenuItem openItem = new JMenuItem("Open");
        openItem.addActionListener(e -> parentFrame.imageOpened(imageFileChooser.showOpenDialog()));
        fileMenu.add(openItem);

        // Save file
        JMenuItem saveItem = new JMenuItem("Save");
        saveItem.addActionListener(e -> parentFrame.imageSaved(imageFileChooser.showSaveDialog()));
        saveItem.setEnabled(false);
        imageDependantComponents.add(saveItem);
        fileMenu.add(saveItem);

        // Close file
        JMenuItem closeItem = new JMenuItem("Close");
        closeItem.addActionListener(e -> {
            imageFileChooser.closeImage();
            parentFrame.imageClosed();
        });
        closeItem.setEnabled(false);
        imageDependantComponents.add(closeItem);
        fileMenu.add(closeItem);

        return fileMenu;
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

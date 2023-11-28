package ru.nsu.ccfit.graphics.g20202.kharchenko.icg_filter;

import com.formdev.flatlaf.FlatDarculaLaf;
import com.formdev.flatlaf.FlatLightLaf;
import ru.nsu.ccfit.graphics.g20202.kharchenko.icg_filter.file.ImageFileManager;
import ru.nsu.ccfit.graphics.g20202.kharchenko.icg_filter.engine.ImageEngine;
import ru.nsu.ccfit.graphics.g20202.kharchenko.icg_filter.filter.FilterSet;
import ru.nsu.ccfit.graphics.g20202.kharchenko.icg_filter.view.FilterAppFrame;

/**
 * This class is used to manage all the major modules of the ICGFilter app.
 * It manages the engine, the filters, the view and the file manager.
 */
public class FilterApp {
    public static void main(String[] args) {
        // Image (with history)
        ImageEngine imageEngine = new ImageEngine(10);

        // Image loader/saver
        ImageFileManager imageFileManager = new ImageFileManager(imageEngine);

        // Set of filters
        FilterSet filterSet = new FilterSet(imageEngine);

        // App Frame
        FilterAppFrame appFrame = new FilterAppFrame(imageEngine, imageFileManager, filterSet);
        appFrame.setVisible(true);
    }
}
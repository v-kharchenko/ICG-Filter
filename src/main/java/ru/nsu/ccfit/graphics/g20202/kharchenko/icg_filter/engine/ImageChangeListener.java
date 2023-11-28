package ru.nsu.ccfit.graphics.g20202.kharchenko.icg_filter.engine;

import java.awt.image.BufferedImage;

public interface ImageChangeListener {
    void newImageAdded(BufferedImage image);
    void newImageSet(BufferedImage image);
    void historyCleared(int fromIndex);
    void imageSelected(int index);
}

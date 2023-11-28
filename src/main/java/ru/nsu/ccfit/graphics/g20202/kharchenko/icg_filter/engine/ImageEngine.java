package ru.nsu.ccfit.graphics.g20202.kharchenko.icg_filter.engine;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.LinkedList;

public class ImageEngine {
    private LinkedList<BufferedImage> history = new LinkedList<>();
    private ArrayList<ImageChangeListener> imageChangeListeners = new ArrayList<>();
    private int currentImageIndex;
    private int historyLimit;

    public ImageEngine(int historyLimit) {
        this.historyLimit = historyLimit;
    }

    /**
     * Sets new image as an origin. Previous history gets deleted.
     * @param image an image to be set as origin.
     */
    public void setImage(BufferedImage image) {
        history.clear();
        history.add(image);
        currentImageIndex = 0;
        for (var l: imageChangeListeners) {
            l.newImageSet(image);
        }
    }

    /**
     * Adds new image to the history. If the currently selected image isn't
     * at the end, the history from this point on gets deleted, and the new image is set as
     * the next entry.
     * @param image an image to be added after the selected history entry.
     */
    public void addImage(BufferedImage image) {
        if (currentImageIndex + 1 < history.size()) {
            history.subList(currentImageIndex + 1, history.size()).clear();
            for (var l: imageChangeListeners) {
                l.historyCleared(currentImageIndex + 1);
            }
        }
        history.add(image);
        currentImageIndex++;

        for (var l: imageChangeListeners) {
            l.newImageAdded(image);
        }
    }

    public BufferedImage getOriginalImage() {
        if (!history.isEmpty()) {
            currentImageIndex = 0;

            for (var l: imageChangeListeners) {
                l.imageSelected(0);
            }

            return history.get(0);
        }


        return null;
    }

    /**
     * Get last image in engine's history.
     * @return the last image in engine's history.
     */
    public BufferedImage getLastImage() {
        if (!history.isEmpty()) {
            currentImageIndex = history.size() - 1;

            for (var l: imageChangeListeners) {
                l.imageSelected(currentImageIndex);
            }

            return history.get(currentImageIndex);
        }


        return null;
    }

    /**
     * Get image by its index in history.
     * @param index index of the image in history.
     * @return the image at specified index.
     */
    public BufferedImage getImageAt(int index) {
        if (0 <= index && index < history.size()) {
            currentImageIndex = index;

            for (var l: imageChangeListeners) {
                l.imageSelected(index);
            }

            return history.get(index);
        }

        return null;
    }

    public void clearHistory() {
        history.clear();
        currentImageIndex = -1;

        for (var l: imageChangeListeners) {
            l.historyCleared(0);
        }
    }

    public int getHistoryLength() {
        return history.size();
    }

    public void addImageChangeListener(ImageChangeListener listener) {
        imageChangeListeners.add(listener);
    }

    public int getLastIndex() {
        return currentImageIndex;
    }

    public BufferedImage getCurrentImage() {
        return history.get(currentImageIndex);
    }

    public int getCurrentIndex() {
        return currentImageIndex;
    }
}

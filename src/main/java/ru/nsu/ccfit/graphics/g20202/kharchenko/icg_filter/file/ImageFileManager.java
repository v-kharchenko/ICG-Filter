package ru.nsu.ccfit.graphics.g20202.kharchenko.icg_filter.file;

import ru.nsu.ccfit.graphics.g20202.kharchenko.icg_filter.engine.ImageEngine;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * A tunnel between image files and engine's image history.
 */
public class ImageFileManager {
    private ImageEngine imageEngine;

    /**
     * The basic constructor for ImageFileManager. The imageEngine is the engine that will receive
     * BufferedImage read from file, as well as the engine that the file will be saved from.
     * @param imageEngine the engine that the file manager will work with.
     */
    public ImageFileManager(ImageEngine imageEngine) {
        this.imageEngine = imageEngine;
    }

    /**
     * Loads an image into the ImageFileManager's ImageEngine.
     * @param file file to retrieve the image from.
     * @return true if the image was successfully loaded, false otherwise.
     *
     */
    public boolean loadImage(File file) {
        try {
            imageEngine.setImage(ImageIO.read(file));
            return true;
        } catch (IOException e) {
            return false;
        }
    }


    /**
     * Saves the last image in ImageEngine to the specified file.
     * @param file file to save the image to.
     * @return true if the image was successfully loaded, false otherwise.
     */
    public boolean saveFile(File file, String format) {
        try {
            BufferedImage image = imageEngine.getLastImage();

            if (image == null)
                return false;

            if (file.getPath().endsWith("." + format.toLowerCase())) {
                ImageIO.write(image, format, new File(file.getPath()));
            } else {
                ImageIO.write(image, format, new File(file.getPath() + "." + format.toLowerCase()));
            }
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * Used to reach the engine to clear its history.
     */
    public void closeImage() {
        imageEngine.clearHistory();
    }
}

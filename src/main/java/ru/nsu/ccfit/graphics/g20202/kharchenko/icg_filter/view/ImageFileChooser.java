package ru.nsu.ccfit.graphics.g20202.kharchenko.icg_filter.view;

import ru.nsu.ccfit.graphics.g20202.kharchenko.icg_filter.file.ImageFileManager;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class ImageFileChooser {

    private JFrame parentFrame;
    private ImageFileManager imageFileManager;

    public ImageFileChooser(JFrame parentFrame, ImageFileManager imageFileManager) {
        this.imageFileManager = imageFileManager;
        this.parentFrame = parentFrame;
    }

    public File showOpenDialog() {
        FileDialog fileDialog = new FileDialog(parentFrame, "Open image", FileDialog.LOAD);
        fileDialog.setFile("*.png; *.jpg; *.jpeg; *.gif; *.bmp");
        fileDialog.setVisible(true);
        if (fileDialog.getFile() != null) {
            imageFileManager.loadImage(fileDialog.getFiles()[0]);
            return fileDialog.getFiles()[0];
        }
        return null;
    }

    public File showSaveDialog() {
        FileDialog fileDialog = new FileDialog(parentFrame, "Save image", FileDialog.SAVE);
        fileDialog.setFile("*.png");
        fileDialog.setVisible(true);
        if (fileDialog.getFile() != null) {
            imageFileManager.saveFile(fileDialog.getFiles()[0], "png");
            return fileDialog.getFiles()[0];
        }
        return null;
    }


    public void closeImage() {
        imageFileManager.closeImage();
    }
}

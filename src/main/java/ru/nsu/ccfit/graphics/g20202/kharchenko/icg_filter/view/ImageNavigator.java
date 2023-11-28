package ru.nsu.ccfit.graphics.g20202.kharchenko.icg_filter.view;

import ru.nsu.ccfit.graphics.g20202.kharchenko.icg_filter.engine.ImageChangeListener;
import ru.nsu.ccfit.graphics.g20202.kharchenko.icg_filter.engine.ImageEngine;

import javax.swing.*;
import javax.swing.text.View;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class ImageNavigator extends JScrollPane implements MouseMotionListener, MouseListener, ImageChangeListener {


    private final int scrollSpeed = 16;
    private ImageEngine imageEngine;
    private ArrayList<ViewChangeListener> viewChangeListeners = new ArrayList<>();

    private ImagePane imagePane;
    private Point scrollOrigin;
    private int returnIndex = 0;

    private class ImagePane extends JPanel {

        public static final JLabel startText = new JLabel("Open an existing file to start working.");

        private BufferedImage image;
        private int width;
        private int height;
        private double zoom = 100;
        private Object resizeStyle = RenderingHints.VALUE_INTERPOLATION_BILINEAR;

        public ImagePane() {
            super();

            this.setLayout(new GridLayout(1, 1));
            startText.setHorizontalAlignment(SwingConstants.CENTER);
            this.add(startText);
        }

        @Override
        public void paintComponent(Graphics g) {
            if (!resizeStyle.equals(((Graphics2D)g).getRenderingHint(RenderingHints.KEY_INTERPOLATION)))
                ((Graphics2D)g).setRenderingHint(RenderingHints.KEY_INTERPOLATION, resizeStyle);

            if (image != null) {
                this.remove(startText);
                super.paintComponent(g);
                g.drawImage(image, this.getWidth()/2 - width/2, this.getHeight()/2 - height/2, width, height, this);
                this.setPreferredSize(new Dimension(width, height));
            } else {
                this.add(startText);
                super.paintComponent(g);
                this.setPreferredSize(this.getMinimumSize());
            }
        }

        public double setZoom(double zoom) {
            if (zoom < ZoomPanel.MIN_ZOOM || zoom > ZoomPanel.MAX_ZOOM || image == null)
                return -1;
            width = (int)(image.getWidth() * zoom / 100);
            height = (int)(image.getHeight() * zoom / 100);
            this.zoom = zoom;
            this.setPreferredSize(new Dimension(width, height));
            this.revalidate();
            return zoom;
        }
    }

    public ImageNavigator(ImageEngine imageEngine) {
        super();

        this.imageEngine = imageEngine;
        imagePane = new ImagePane();
        this.setViewportView(imagePane);

        this.setViewportBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(4, 4, 4, 4),
                BorderFactory.createDashedBorder(null, 4, 4)
                )
        );

        this.getVerticalScrollBar().setUnitIncrement(scrollSpeed);
        this.getHorizontalScrollBar().setUnitIncrement(scrollSpeed);

        this.addMouseListener(this);
        this.addMouseMotionListener(this);
        imageEngine.addImageChangeListener(this);
    }

    public void setZoomInterpolation(Object hintValue) {
        imagePane.resizeStyle = hintValue;
        this.repaint();
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        imagePane.repaint();
        updateUI();
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (scrollOrigin != null) {
            JViewport viewPort = this.getViewport();
            if (viewPort != null) {
                int deltaX = scrollOrigin.x - e.getX();
                int deltaY = scrollOrigin.y - e.getY();

                Rectangle view = viewPort.getViewRect();
                view.x += deltaX;
                view.y += deltaY;

                imagePane.scrollRectToVisible(view);
                scrollOrigin = new Point(e.getPoint());
            }
        }
    }

    public double setZoom(double zoom) {
        zoom = imagePane.setZoom(zoom);

        this.updateUI();

        return zoom;
    }

    public double fitToScreenZoom() {
        double zoom;

        // image_width / image_height < pane_width / pane_height
        if (this.getViewport().getWidth() * imagePane.image.getHeight() < this.getViewport().getHeight() * imagePane.image.getWidth()) {
            zoom = (double)this.getViewport().getWidth() * 100 / imagePane.image.getWidth();
        } else {
            zoom = (double)this.getViewport().getHeight() * 100 / imagePane.image.getHeight();
        }
        return setZoom(zoom);
    }

    @Override
    public void newImageAdded(BufferedImage image) {
        imagePane.image = image;
        imagePane.setZoom(imagePane.zoom);
        imagePane.repaint();
        returnIndex = 0;
    }

    @Override
    public void newImageSet(BufferedImage image) {
        imagePane.image = image;
        imagePane.width = image.getWidth();
        imagePane.height = image.getHeight();
        returnIndex = 0;
        imagePane.repaint();
    }

    public void setImageAt(int index) {
        imagePane.image = imageEngine.getImageAt(index);
        returnIndex = 0;
        imagePane.setZoom(imagePane.zoom);
        imagePane.repaint();
    }


    public void changeViewMode() {
        if (returnIndex == 0) {
            returnIndex = imageEngine.getCurrentIndex();
            imagePane.image = imageEngine.getOriginalImage();
        } else {
            imagePane.image = imageEngine.getImageAt(returnIndex);
            returnIndex = 0;
        }
        imagePane.setZoom(imagePane.zoom);
    }

    public void addViewChangeListener(ViewChangeListener listener) {
        viewChangeListeners.add(listener);
    }

    @Override
    public void historyCleared(int fromIndex) {
        imagePane.image = imageEngine.getLastImage();
    }

    @Override
    public void imageSelected(int index) {}

    @Override
    public void mouseMoved(MouseEvent e) {}

    @Override
    public void mouseClicked(MouseEvent e) {
        changeViewMode();
        for (var l: viewChangeListeners){
            l.viewChanged();
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        scrollOrigin = new Point(e.getPoint());
    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {
        scrollOrigin = null;
    }
}

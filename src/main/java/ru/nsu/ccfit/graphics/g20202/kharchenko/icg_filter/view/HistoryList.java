package ru.nsu.ccfit.graphics.g20202.kharchenko.icg_filter.view;

import ru.nsu.ccfit.graphics.g20202.kharchenko.icg_filter.engine.ImageChangeListener;
import ru.nsu.ccfit.graphics.g20202.kharchenko.icg_filter.engine.ImageEngine;
import ru.nsu.ccfit.graphics.g20202.kharchenko.icg_filter.file.FilterAppliedListener;
import ru.nsu.ccfit.graphics.g20202.kharchenko.icg_filter.filter.FilterSet;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.image.BufferedImage;

public class HistoryList extends JPanel implements ImageChangeListener, ListSelectionListener, FilterAppliedListener {

    DefaultListModel<String> historyListModel;
    JList<String> historyList;
    ImageEngine imageEngine;
    ImageNavigator imageNavigator;

    HistoryList(ImageEngine imageEngine, FilterSet filterSet, ImageNavigator imageNavigator) {
        super();
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setMinimumSize(new Dimension(170, 20));
        this.setPreferredSize(new Dimension(170, 50));

        JLabel historyName = new JLabel("History");
        historyName.setAlignmentX(CENTER_ALIGNMENT);
        this.add(historyName);

        this.imageEngine = imageEngine;
        this.imageNavigator = imageNavigator;

        historyListModel = new DefaultListModel<>();
        historyList = new JList<>(historyListModel);
        JScrollPane scrollHistoryList = new JScrollPane(historyList);
        historyList.setFocusable(false);
        this.add(scrollHistoryList);

        imageEngine.addImageChangeListener(this);
        filterSet.addFilterAppliedListener(this);

        historyList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        historyList.addListSelectionListener(this);
    }

    @Override
    public void newImageAdded(BufferedImage image) {

    }

    @Override
    public void newImageSet(BufferedImage image) {
        historyListModel.clear();
        historyListModel.addElement(imageEngine.getLastIndex() + ": Image opened");
        historyList.setSelectedIndex(0);
    }

    @Override
    public void historyCleared(int fromIndex) {
        historyListModel.removeRange(0, historyListModel.size() - fromIndex - 1);
    }

    @Override
    public void imageSelected(int index) {
       historyList.setSelectedIndex(historyListModel.size() - index - 1);
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        if (e.getValueIsAdjusting()) {
            imageNavigator.setImageAt(imageEngine.getHistoryLength() - 1 - historyList.getMinSelectionIndex());
        }
    }

    @Override
    public void filterApplied(FilterSet.FilterType filterType) {
        historyListModel.add(0, imageEngine.getLastIndex() + ": " + filterType.toString());
        historyList.setSelectedIndex(0);
    }
}

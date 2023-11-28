package ru.nsu.ccfit.graphics.g20202.kharchenko.icg_filter.view;

import ru.nsu.ccfit.graphics.g20202.kharchenko.icg_filter.FilterApp;
import ru.nsu.ccfit.graphics.g20202.kharchenko.icg_filter.filter.ContouringFilter;
import ru.nsu.ccfit.graphics.g20202.kharchenko.icg_filter.filter.FilterSet;
import ru.nsu.ccfit.graphics.g20202.kharchenko.icg_filter.filter.ImageFilter;
import ru.nsu.ccfit.graphics.g20202.kharchenko.icg_filter.filter.parameter.PaletteCountParameter;
import ru.nsu.ccfit.graphics.g20202.kharchenko.icg_filter.filter.parameter.RangedParameter;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

public class ParameterDialogFactory {

    private FilterSet filterSet;
    private FilterAppFrame parentFrame;
    HashMap<String, Object> pendingValues;

    public ParameterDialogFactory(FilterAppFrame parentFrame, FilterSet filterSet) {
        this.filterSet = filterSet;
        this.parentFrame = parentFrame;
    }

    public void showParameterDialog(FilterSet.FilterType type) {
        HashMap<String, Object> parameters = filterSet.getParameters(type);

        if (parameters == null) {
            filterSet.applyFilter(type);
            return;
        }

        JPanel parametersPane = new JPanel();
        parametersPane.setLayout(new BoxLayout(parametersPane, BoxLayout.Y_AXIS));

        pendingValues = new HashMap<>();
        for (var entry: parameters.entrySet()) {
            if (entry.getValue().getClass().equals(RangedParameter.class)) {
                parametersPane.add(getRangedParameterPane(entry.getKey(), (RangedParameter)entry.getValue()));
            } else if (entry.getValue().getClass().equals(PaletteCountParameter.class)) {
                parametersPane.add(getPaletteCountPane(entry.getKey(), (PaletteCountParameter)entry.getValue()));
            } else if (entry.getValue().getClass().equals(ImageFilter.ColorComponent.class)) {
                parametersPane.add(getColorComponentPane(entry.getKey(), (ImageFilter.ColorComponent)entry.getValue()));
            } else if (entry.getValue().getClass().equals(ContouringFilter.ContouringType.class)) {
                parametersPane.add(getContouringTypePane(entry.getKey(), (ContouringFilter.ContouringType)entry.getValue()));
            }
        }

        int res = JOptionPane.showConfirmDialog(parentFrame, parametersPane, type + " parameters", JOptionPane.OK_CANCEL_OPTION);
        if (res != JOptionPane.OK_OPTION) {
            return;
        }

        for (var entry: parameters.entrySet()) {
            if (entry.getValue().getClass().equals(RangedParameter.class)) {
                RangedParameter param = (RangedParameter)entry.getValue();
                Object newValue = pendingValues.get(entry.getKey());
                param.setValue((int)newValue);
            } else if (entry.getValue().getClass().equals(PaletteCountParameter.class)) {
                PaletteCountParameter param = (PaletteCountParameter) entry.getValue();
                param.setComponentCounts((int)pendingValues.get(entry.getKey() + " Red"), (int)pendingValues.get(entry.getKey() + " Green"), (int)pendingValues.get(entry.getKey() + " Blue"));
            } else if (entry.getValue().getClass().equals(ImageFilter.ColorComponent.class)) {
                entry.setValue(pendingValues.get(entry.getKey()));
            } else if (entry.getValue().getClass().equals(ContouringFilter.ContouringType.class)) {
                entry.setValue(pendingValues.get(entry.getKey()));
            }
        }

        filterSet.updateParameters(type);
        filterSet.applyFilter(type);

    }

    private JPanel getContouringTypePane(String parameterName, ContouringFilter.ContouringType contouringType) {
        JPanel contouringTypePane = new JPanel();
        contouringTypePane.add(new JLabel(parameterName));

        JComboBox<String> contouringTypeBox = new JComboBox<>();
        for (var c: ContouringFilter.ContouringType.values()) {
            contouringTypeBox.addItem(c.toString());
        }
        contouringTypePane.add(contouringTypeBox);

        contouringTypeBox.setSelectedItem(contouringType.toString());
        pendingValues.put(parameterName, contouringType);

        contouringTypeBox.addItemListener(e -> {
            String componentName = (String)e.getItem();
            for (var c: ContouringFilter.ContouringType.values()) {
                if (c.toString().equals(componentName)) {
                    pendingValues.put(parameterName, c);
                }
            }
        });

        return contouringTypePane;
    }

    private JPanel getColorComponentPane(String parameterName, ImageFilter.ColorComponent colorComponent) {
        JPanel colorComponentPane = new JPanel();
        colorComponentPane.add(new JLabel(parameterName));

        JComboBox<String> colorComponentBox = new JComboBox<>();
        for (var color: ImageFilter.ColorComponent.values()) {
            colorComponentBox.addItem(color.toString());
        }
        colorComponentPane.add(colorComponentBox);

        colorComponentBox.setSelectedItem(colorComponent.toString());
        pendingValues.put(parameterName, colorComponent);

        colorComponentBox.addItemListener(e -> {
            String componentName = (String)e.getItem();
            for (var comp: ImageFilter.ColorComponent.values()) {
                if (comp.toString().equals(componentName)) {
                    pendingValues.put(parameterName, comp);
                }
            }
        });

        return colorComponentPane;
    }

    private JPanel getPaletteCountPane(String parameterName, PaletteCountParameter paletteCountParameter) {
        JPanel palettePane = new JPanel();
        palettePane.setLayout(new BoxLayout(palettePane, BoxLayout.Y_AXIS));
        palettePane.add(new JLabel(parameterName));

        HashMap<ImageFilter.ColorComponent, RangedParameter> componentCount = paletteCountParameter.getComponentCount();

        JPanel redPane = getRangedParameterPane(parameterName + " Red", componentCount.get(ImageFilter.ColorComponent.RED));
        JPanel greenPane = getRangedParameterPane(parameterName + " Green", componentCount.get(ImageFilter.ColorComponent.GREEN));
        JPanel bluePane = getRangedParameterPane(parameterName + " Blue", componentCount.get(ImageFilter.ColorComponent.BLUE));
        palettePane.add(redPane);
        palettePane.add(greenPane);
        palettePane.add(bluePane);

        return palettePane;
    }

    private JPanel getRangedParameterPane(String parameterName, RangedParameter parameterValue) {
        JPanel rangePane = new JPanel();
        rangePane.add(new JLabel(parameterName));

        JSlider rangedSlider = new JSlider();
        rangedSlider.setMinimum(parameterValue.getLowEnd());
        rangedSlider.setMaximum(parameterValue.getHighEnd());
        rangedSlider.setValue(parameterValue.getValue());
        if (parameterValue.getStepSize() != 1) {
            rangedSlider.setMajorTickSpacing(parameterValue.getStepSize());
            rangedSlider.setSnapToTicks(true);
            rangedSlider.setPaintTicks(true);
        }
        rangePane.add(rangedSlider);

        SpinnerNumberModel rangedSpinnerModel = new SpinnerNumberModel(parameterValue.getValue(), parameterValue.getLowEnd(), parameterValue.getHighEnd(), parameterValue.getStepSize());
        JSpinner rangedSpinner = new JSpinner(rangedSpinnerModel);
        pendingValues.put(parameterName, rangedSpinnerModel.getValue());
        rangePane.add(rangedSpinner);

        rangedSpinnerModel.addChangeListener(l -> {
            rangedSlider.setValue((int)rangedSpinnerModel.getValue());
            pendingValues.put(parameterName, rangedSpinnerModel.getValue());
        });

        rangedSlider.addChangeListener(l -> {
            rangedSpinnerModel.setValue(rangedSlider.getValue());
            pendingValues.put(parameterName, rangedSlider.getValue());
        });

        return rangePane;
    }
}

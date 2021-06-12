package nl.rug.oop.flaps.aircraft_editor.view.panels.aircraft_info.interaction_panels;

import lombok.Getter;
import lombok.Setter;
import nl.rug.oop.flaps.aircraft_editor.controller.listeners.infopanel_listeners.ConfirmChangeButtonListener;
import nl.rug.oop.flaps.aircraft_editor.controller.listeners.infopanel_listeners.fuel.FuelSliderListener;
import nl.rug.oop.flaps.aircraft_editor.model.blueprint.BluePrintModel;
import nl.rug.oop.flaps.aircraft_editor.view.frame.EditorFrame;
import nl.rug.oop.flaps.aircraft_editor.view.panels.aircraft_info.InfoPanel;
import nl.rug.oop.flaps.simulation.model.aircraft.Aircraft;
import nl.rug.oop.flaps.simulation.model.aircraft.FuelTank;

import javax.swing.*;
import javax.swing.undo.AbstractUndoableEdit;
import java.awt.*;

/**
 * this config panel gets presented when the user selects a fuel tank of the aircraft.
 * This panel then lets the user interact with a {@link JSlider} to fill the fuel tanks of the aircraft
 * */
@Getter
public class FuelConfigPanel extends AbstractUndoableEdit  {
    private final BluePrintModel bluePrintModel;
    @Setter
    private Aircraft aircraft;
    @Setter
    private FuelTank selectedTank;

    private final JPanel displayPanel;
    private final JLabel tankWeightLabel;
    private final JSlider infoSlider;

    @Getter
    private final JButton confirmButton;


    /**
     * Constructor for the fuel configuration panel
     *
     * @param displayPanel the panel which is to be displayed
     * @param bluePrintModel the bluePrintModel which we contains the data about the aircraft
     */
    public FuelConfigPanel(JPanel displayPanel, BluePrintModel bluePrintModel) {
        this.bluePrintModel = bluePrintModel;
        this.aircraft = bluePrintModel.getAircraft();
        this.selectedTank = bluePrintModel.getSelectedTank();

        this.displayPanel = displayPanel;
        this.tankWeightLabel = new JLabel();
        this.infoSlider = new JSlider();
        this.infoSlider.addChangeListener(new FuelSliderListener(this));
        confirmButton = new JButton();
        confirmButton.addActionListener(new ConfirmChangeButtonListener(this));

        initConfigPanelFuel();
    }

    /**
     * initialises components and adds then to the display panel
     * */
    public void initConfigPanelFuel() {
        initSlider();
        tankWeightLabel.setText("<html><h2>Current Fuel: " + (int)aircraft.getFuelAmountForFuelTank(selectedTank) + " Kg</h2></html>");
        confirmButton.setText("Confirm Fuel Changes");

        //setting up grid layout for displayPanel components
        GridBagConstraints c = new GridBagConstraints();
        setLayoutOfDisplayPanel(displayPanel, c);
        displayPanel.add(new JLabel("<html><h1>Updating " + selectedTank.getName() + "</h1></html>"), c);

        c.ipady = 0; //resetting padding
        c.insets= new Insets(0,20, -15, 0);
        c.gridx = 0;
        c.gridy = 1;
        displayPanel.add(tankWeightLabel, c);

        c.insets= new Insets(0,0, 0, 0);
        c.gridx = 0;
        c.gridy = 2;
        displayPanel.add(infoSlider, c);

        c.ipady = 20;
        c.gridx = 0;
        c.gridy = 3;
        displayPanel.add(confirmButton, c);

        //resetting divider location at InfoPanel
        InfoPanel.getInfoSplitPane().setDividerLocation(EditorFrame.getHEIGHT()/2);
    }

    /**
     * sets layout for display panel
     * @param displayPanel displayPanel to add layout to
     * @param c the GridBagConstraints to be modified
     * */
    public static void setLayoutOfDisplayPanel(JPanel displayPanel, GridBagConstraints c) {
        displayPanel.setLayout(new GridBagLayout());
        c.weightx = 1;
        c.weighty = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.ipadx = 0;

        c.ipady = 30;
        c.gridx = 0;
        c.gridy = 0;
    }

    /**
     * adds slider component to update fuel
     * */
    private void initSlider() {
        infoSlider.setOrientation(JSlider.HORIZONTAL);
        infoSlider.setMinimum(0);
        infoSlider.setMaximum(selectedTank.getCapacity());
        infoSlider.setValue((int)aircraft.getFuelAmountForFuelTank(selectedTank));
        infoSlider.setMinorTickSpacing(selectedTank.getCapacity()/10);
        infoSlider.setMajorTickSpacing(selectedTank.getCapacity()/10);
        infoSlider.setFont(new Font("MV Boli", Font.PLAIN, 15));
        infoSlider.setPaintTicks(true);
        infoSlider.setPaintLabels(true);
        infoSlider.setPaintTrack(true);
    }

    /**
     * updates the weight label {@link FuelConfigPanel#tankWeightLabel} when the slider
     * {@link FuelConfigPanel#infoSlider} value changes
     * */
    public void updateWeightLabel() {
        tankWeightLabel.setText("<html><h2>Current Fuel: " + infoSlider.getValue() + " Kg</h2></html>");
    }
}
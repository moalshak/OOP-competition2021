package nl.rug.oop.flaps.aircraft_editor.controller.listeners.infopanel_listeners;

import lombok.extern.java.Log;
import nl.rug.oop.flaps.aircraft_editor.util.AddEdit;
import nl.rug.oop.flaps.aircraft_editor.view.frame.EditMenu;
import nl.rug.oop.flaps.aircraft_editor.view.panels.aircraft_info.InfoPanel;
import nl.rug.oop.flaps.aircraft_editor.view.panels.aircraft_info.interaction_panels.CargoConfigPanel;
import nl.rug.oop.flaps.aircraft_editor.view.panels.aircraft_info.interaction_panels.FuelConfigPanel;
import nl.rug.oop.flaps.aircraft_editor.view.panels.aircraft_info.interaction_panels.PassengersConfigPanel;
import nl.rug.oop.flaps.simulation.model.cargo.CargoUnit;

import javax.swing.undo.UndoableEditSupport;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

/**
 * this class uses {@link ActionListener} to listen to an action for the confirm button to confirm a change made by the user\
 * either for {@link PassengersConfigPanel} or {@link CargoConfigPanel} and {@link FuelConfigPanel}
 * */
@Log
public class ConfirmChangeButtonListener implements ActionListener {
    private FuelConfigPanel fuelConfigPanel;
    private CargoConfigPanel cargoConfigPanel;
    private PassengersConfigPanel passengersConfigPanel;
    private final UndoableEditSupport undoSupport = EditMenu.getUndoSupport();

    public ConfirmChangeButtonListener(FuelConfigPanel fuelConfigPanel) {
        this.fuelConfigPanel = fuelConfigPanel;

    }

    public ConfirmChangeButtonListener(CargoConfigPanel cargoConfigPanel) {
        this.cargoConfigPanel = cargoConfigPanel;

    }

    public ConfirmChangeButtonListener(PassengersConfigPanel passengersConfigPanel) {
        this.passengersConfigPanel = passengersConfigPanel;

    }

    /**
     * updates the panels for fuel, cargo or passenger when the confirm button is pressed
     * */
    @Override
    public void actionPerformed(ActionEvent e) {
        updateFuel(e);
        updateCargo(e);
        updatePassengers(e);
    }

    /**
     * Creates a backup of the undo/redo data and updates the the passengers
     * @param e the button event, check if its from the cargoConfigPanel
     */
    private void updatePassengers(ActionEvent e) {
        if (passengersConfigPanel != null && e.getSource() == passengersConfigPanel.getConfirmButton()) {
            HashMap<String, Integer> undoPassengers = new HashMap<>(passengersConfigPanel.getModel().getPassengers());
            passengersConfigPanel.updateConfig();
            HashMap<String, Integer> redoPassengers = new HashMap<>(passengersConfigPanel.getModel().getPassengers());
            undoSupport.postEdit(new AddEdit(passengersConfigPanel, undoPassengers, redoPassengers));
            InfoPanel.updateResultInfo();
            log.info("Updated Passengers");
        }
    }

    /**
     * Creates a backup of the undo/redo data and updates the the cargoArea
     * @param e the button event, check if its from the cargoConfigPanel
     */
    private void updateCargo(ActionEvent e) {
        if (cargoConfigPanel != null && e.getSource() == cargoConfigPanel.getConfirmButton()) {
                CargoUnit undoCargoUnit = new CargoUnit(cargoConfigPanel.getSelectedCargoUnit().getCargoType(),
                        cargoConfigPanel.getSelectedCargoUnit().getWeight());
                cargoConfigPanel.getSelectedCargoUnit().setWeight(cargoConfigPanel.getInfoSlider().getValue());
                CargoUnit redoCargoUnit = new CargoUnit(cargoConfigPanel.getSelectedCargoUnit().getCargoType(),
                        cargoConfigPanel.getSelectedCargoUnit().getWeight());
                undoSupport.postEdit(new AddEdit(cargoConfigPanel, undoCargoUnit, redoCargoUnit));
                cargoConfigPanel.getAircraft().addToCargoArea(cargoConfigPanel.getSelectedCargoArea(), cargoConfigPanel.getSelectedCargoUnit());
                cargoConfigPanel.updateCargoAreaWeightLabel();
                cargoConfigPanel.getBluePrintModel().getBluePrintPanel().repaint();
                InfoPanel.updateResultInfo();
                log.info("Updated Cargo");
            }
    }

    /**
     * Creates a backup of the undo/redo data and updates the the fuelTank
     * @param e the button event, check if its from the fuelConfigPanel
     */
    private void updateFuel(ActionEvent e) {
        if (fuelConfigPanel != null && e.getSource() == fuelConfigPanel.getConfirmButton()) {
            double undoFuelTankAmount = fuelConfigPanel.getAircraft().getFuelAmountForFuelTank(fuelConfigPanel.getSelectedTank());
            double redoFuelTankAmount = fuelConfigPanel.getInfoSlider().getValue();
            undoSupport.postEdit(new AddEdit(fuelConfigPanel,undoFuelTankAmount, redoFuelTankAmount));
            fuelConfigPanel.getAircraft().setFuelAmountForFuelTank(fuelConfigPanel.getSelectedTank(), fuelConfigPanel.getInfoSlider().getValue());
            fuelConfigPanel.getDisplayPanel().removeAll();
            fuelConfigPanel.initConfigPanelFuel();
            fuelConfigPanel.getBluePrintModel().getBluePrintPanel().repaint();
            InfoPanel.updateResultInfo();
            log.info("Updated Fuel");
        }
    }

}

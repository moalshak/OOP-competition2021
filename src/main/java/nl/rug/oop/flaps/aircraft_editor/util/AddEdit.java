package nl.rug.oop.flaps.aircraft_editor.util;

import lombok.extern.java.Log;
import nl.rug.oop.flaps.aircraft_editor.model.blueprint.BluePrintModel;
import nl.rug.oop.flaps.aircraft_editor.model.config_models.passenger.PassengersModelListener;
import nl.rug.oop.flaps.aircraft_editor.view.panels.aircraft_info.InfoPanel;
import nl.rug.oop.flaps.aircraft_editor.view.panels.aircraft_info.interaction_panels.CargoConfigPanel;
import nl.rug.oop.flaps.aircraft_editor.view.panels.aircraft_info.interaction_panels.FuelConfigPanel;
import nl.rug.oop.flaps.aircraft_editor.view.panels.aircraft_info.interaction_panels.InteractionPanel;
import nl.rug.oop.flaps.aircraft_editor.view.panels.aircraft_info.interaction_panels.PassengersConfigPanel;
import nl.rug.oop.flaps.simulation.model.cargo.CargoUnit;

import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import java.util.HashMap;

/**
 * This class is to provide undo/redo support for cargo, fuel (and to-do passenger) changes
 */
@Log
public class AddEdit extends AbstractUndoableEdit {
    private FuelConfigPanel fuelConfigPanel;
    private double undoFuelTankAmount;
    private double redoFuelTankAmount;

    private CargoConfigPanel cargoConfigPanel;
    private CargoUnit undoCargoUnit;
    private CargoUnit redoCargoUnit;

    private PassengersConfigPanel passengersConfigPanel;
    private HashMap<String, Integer> undoPassengers;
    private HashMap<String, Integer> redoPassengers;


    //creating overloaded constructor for each undo/redo instances
    public AddEdit(FuelConfigPanel fuelConfigPanel, double undoFuelTankAmount, double redoFuelTankAmount) {
        this.fuelConfigPanel = fuelConfigPanel;
        this.undoFuelTankAmount = undoFuelTankAmount;
        this.redoFuelTankAmount = redoFuelTankAmount;
    }

    public AddEdit(CargoConfigPanel cargoConfigPanel, CargoUnit undoCargoUnit, CargoUnit redoCargoUnit) {
        this.cargoConfigPanel = cargoConfigPanel;
        this.undoCargoUnit = undoCargoUnit;
        this.redoCargoUnit = redoCargoUnit;
    }

    public AddEdit(PassengersConfigPanel passengersConfigPanel, HashMap<String, Integer> undoPassengers, HashMap<String, Integer> redoPassengers) {
        this.passengersConfigPanel = passengersConfigPanel;
        this.undoPassengers = undoPassengers;
        this.redoPassengers = redoPassengers;
    }

    /**
     * Updates the fuel tank with the given fuel tank amount
     * @param fuelTankAmount the fuel amount u want to update the fuel tank with
     */
    private void updateFuelTankInfo(double fuelTankAmount){
        if (fuelConfigPanel != null) {
            fuelConfigPanel = InteractionPanel.getFuelConfigPanel();
            fuelConfigPanel.getBluePrintModel().setSelectedCompartment(BluePrintModel.FUEL);
            fuelConfigPanel.getAircraft().setFuelAmountForFuelTank(fuelConfigPanel.getSelectedTank(), fuelTankAmount);
            fuelConfigPanel.getDisplayPanel().removeAll();
            fuelConfigPanel.initConfigPanelFuel();
            InfoPanel.updateResultInfo();
            log.info("undo/redo Fuel");
        }
    }

    /**
     * Updates the current cargo unit with the given cargo unit
     * @param cargoUnit the cargo unit that will be used to update the current cargo unit
     */
    private void updateCargoUnit(CargoUnit cargoUnit){
        if (cargoConfigPanel != null) {
            cargoConfigPanel = InteractionPanel.getCargoConfigPanel();
            cargoConfigPanel.getBluePrintModel().setSelectedCompartment(BluePrintModel.CARGO);
            cargoConfigPanel.getAircraft().addToCargoArea(cargoConfigPanel.getSelectedCargoArea(), cargoUnit);
            cargoConfigPanel.getDisplayPanel().removeAll();
            cargoConfigPanel.initConfigCargoPanel();
            cargoConfigPanel.getCargoList().setSelectedValue(cargoUnit.getCargoType().getName(), true);
            InfoPanel.updateResultInfo();
            log.info("undo/redo Cargo");
        }
    }

    /**
     * Updates the current passengers with the given passengers
     * @param passengers the passengers that will be used to update the current passengers
     */
    private void updatePassengers(HashMap<String, Integer> passengers){
        if (passengersConfigPanel != null) {
            passengersConfigPanel = InteractionPanel.getPassengersConfigPanel();
            passengersConfigPanel.getBluePrintModel().setSelectedCompartment(BluePrintModel.ENTRY);
            passengersConfigPanel.getModel().setPassengers(passengers);
            passengersConfigPanel.getModel().setPassengersWeight();
            passengersConfigPanel.initPassengersConfig();
            passengersConfigPanel.getModel().updatePassengerSumPassenger();
            passengersConfigPanel.getModel().getListeners().forEach(PassengersModelListener::updatePassengersConfiguration);
            passengersConfigPanel.updatePassengerLabel();
            passengersConfigPanel.updateConfig();
            InfoPanel.updateResultInfo();
            log.info("undo/redo Passengers");
        }
    }

    @Override
    public void undo() {
        try {
            updateFuelTankInfo(undoFuelTankAmount);
            updateCargoUnit(undoCargoUnit);
            updatePassengers(undoPassengers);
        } catch (CannotUndoException error) {
            log.info("Nothing to undo");
        }
    }

    @Override
    public void redo() {
        try {
            updateFuelTankInfo(redoFuelTankAmount);
            updateCargoUnit(redoCargoUnit);
            updatePassengers(redoPassengers);
        } catch (CannotRedoException error) {
            log.info("Nothing to redo");
        }
    }

}

package nl.rug.oop.flaps.aircraft_editor.util;

import lombok.extern.java.Log;
import nl.rug.oop.flaps.aircraft_editor.model.blueprint.BluePrintModel;
import nl.rug.oop.flaps.aircraft_editor.model.blueprint.BluePrintModelListener;
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
import java.awt.geom.Point2D;
import java.util.HashMap;

/**
 * This class is to provide undo/redo support for cargo, fuel (and to-do passenger) changes
 */
@Log
public class AddEdit extends AbstractUndoableEdit {
    private final Point2D selectedPoint;

    private FuelConfigPanel fuelConfigPanel;
    private double undoFuelTankAmount;
    private double redoFuelTankAmount;

    private CargoConfigPanel cargoConfigPanel;
    private CargoUnit undoCargoUnit;
    private CargoUnit redoCargoUnit;

    private PassengersConfigPanel passengersConfigPanel;
    private HashMap<String, Integer> undoPassengers;
    private HashMap<String, Integer> redoPassengers;


    /**
     * creates an instance for the un-re do app for the fuel configuration panel
     * @param fuelConfigPanel the fuel configuration panel
     * @param undoFuelTankAmount the old fuel tank amount
     * @param redoFuelTankAmount the new / redo fuel tank amount
     * */
    public AddEdit(FuelConfigPanel fuelConfigPanel, double undoFuelTankAmount, double redoFuelTankAmount) {
        this.fuelConfigPanel = fuelConfigPanel;
        this.selectedPoint = (Point2D) fuelConfigPanel.getBluePrintModel().getSelectedPoint().clone();
        this.undoFuelTankAmount = undoFuelTankAmount;
        this.redoFuelTankAmount = redoFuelTankAmount;
    }

    /**
     * creates an instance for the un-re do app for the cargo configuration panel
     * @param cargoConfigPanel the cargo configuration panel
     * @param undoCargoUnit the unit for the undo
     * @param redoCargoUnit the unit or the redo
     * */
    public AddEdit(CargoConfigPanel cargoConfigPanel, CargoUnit undoCargoUnit, CargoUnit redoCargoUnit) {
        this.cargoConfigPanel = cargoConfigPanel;
        this.selectedPoint = (Point2D) cargoConfigPanel.getBluePrintModel().getSelectedPoint().clone();
        this.undoCargoUnit = undoCargoUnit;
        this.redoCargoUnit = redoCargoUnit;
    }

    /**
     * creates an instance for the un-re do app for the passengers configuration panel
     * @param passengersConfigPanel the passengers configuration panel
     * @param undoPassengers the old passengers map
     * @param redoPassengers the new passengers map
     * */
    public AddEdit(PassengersConfigPanel passengersConfigPanel, HashMap<String, Integer> undoPassengers, HashMap<String, Integer> redoPassengers) {
        this.passengersConfigPanel = passengersConfigPanel;
        this.selectedPoint = (Point2D) passengersConfigPanel.getBluePrintModel().getSelectedPoint().clone();
        this.undoPassengers = undoPassengers;
        this.redoPassengers = redoPassengers;
    }

    /**
     * Updates the fuel tank with the given fuel tank amount
     * @param fuelTankAmount the fuel amount u want to update the fuel tank with
     */
    private void updateFuelTankInfo(double fuelTankAmount){
        if (fuelConfigPanel != null) {
            BluePrintModel bluePrintModel = InteractionPanel.getBluePrintModelStatic();
            bluePrintModel.setSelectedPoint(this.selectedPoint);
            bluePrintModel.selectPointByCoords(this.selectedPoint, 34);
            bluePrintModel.getListeners().forEach(BluePrintModelListener::pointSelectedUpdater);
            fuelConfigPanel = InteractionPanel.getFuelConfigPanel();
            InteractionPanel.getFuelConfigPanel().getDisplayPanel().updateUI();
            fuelConfigPanel.setSelectedTank(fuelConfigPanel.getBluePrintModel().getSelectedTank());
            fuelConfigPanel.getAircraft().setFuelAmountForFuelTank(fuelConfigPanel.getSelectedTank(), fuelTankAmount);
            InteractionPanel.getInteractionPanel().updateInfo();
            InfoPanel.updateResultInfo();
        }
    }

    /**
     * Updates the current cargo unit with the given cargo unit
     * @param cargoUnit the cargo unit that will be used to update the current cargo unit
     */
    private void updateCargoUnit(CargoUnit cargoUnit){
        if (cargoConfigPanel != null) {
            BluePrintModel bluePrintModel = InteractionPanel.getBluePrintModelStatic();
            bluePrintModel.setSelectedPoint(this.selectedPoint);
            bluePrintModel.selectPointByCoords(this.selectedPoint, 34);
            bluePrintModel.getListeners().forEach(BluePrintModelListener::pointSelectedUpdater);
            cargoConfigPanel = InteractionPanel.getCargoConfigPanel();
            cargoConfigPanel.getBluePrintModel().setSelectedCompartment(BluePrintModel.CARGO);
            cargoConfigPanel.getAircraft().addToCargoArea(cargoConfigPanel.getSelectedCargoArea(), cargoUnit);
            cargoConfigPanel.getDisplayPanel().removeAll();
            cargoConfigPanel.initConfigCargoPanel();
            cargoConfigPanel.getCargoList().setSelectedValue(cargoUnit.getCargoType().getName(), true);
            cargoConfigPanel.getDisplayPanel().updateUI();
            InfoPanel.updateResultInfo();
        }
    }

    /**
     * Updates the current passengers with the given passengers
     * @param passengers the passengers that will be used to update the current passengers
     */
    private void updatePassengers(HashMap<String, Integer> passengers){
        if (passengersConfigPanel != null) {
            BluePrintModel bluePrintModel = InteractionPanel.getBluePrintModelStatic();
            bluePrintModel.setSelectedPoint(this.selectedPoint);
            bluePrintModel.selectPointByCoords(this.selectedPoint, 12);
            bluePrintModel.getListeners().forEach(BluePrintModelListener::pointSelectedUpdater);
            passengersConfigPanel = InteractionPanel.getPassengersConfigPanel();
            passengersConfigPanel.getModel().setPassengers(passengers);
            passengersConfigPanel.getModel().setPassengersWeight();
            passengersConfigPanel.initPassengersConfig();
            passengersConfigPanel.getModel().updatePassengerSumPassenger();
            passengersConfigPanel.getModel().getListeners().forEach(PassengersModelListener::updatePassengersConfiguration);
            passengersConfigPanel.updatePassengerLabel();
            passengersConfigPanel.updateConfig();
            InfoPanel.updateResultInfo();
        }
    }

    @Override
    public void undo() {
        try {
            updateFuelTankInfo(undoFuelTankAmount);
            updateCargoUnit(undoCargoUnit);
            updatePassengers(undoPassengers);
            log.info("Undid Action");
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
            log.info("Redid action");
        } catch (CannotRedoException error) {
            log.info("Nothing to redo");
        }
    }

}

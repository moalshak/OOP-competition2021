package nl.rug.oop.flaps.aircraft_editor.model.config_models.aircraft;

import lombok.Getter;
import lombok.extern.java.Log;
import nl.rug.oop.flaps.aircraft_editor.controller.listeners.infopanel_listeners.ConfirmChangeButtonListener;
import nl.rug.oop.flaps.aircraft_editor.model.config_models.InfoPanelModel;
import nl.rug.oop.flaps.aircraft_editor.model.config_models.InfoPanelModelListener;
import nl.rug.oop.flaps.aircraft_editor.model.config_models.passenger.PassengersModel;
import nl.rug.oop.flaps.simulation.model.aircraft.Aircraft;

/**
 * this class calculates the weight of the aircraft
 * This class updates each time the {@link ConfirmChangeButtonListener} is in action.
 * */
@Log
public class AircraftWeightModel implements InfoPanelModelListener {
    @Getter
    private double aircraftWeight;

    private final Aircraft aircraft;

    public AircraftWeightModel(InfoPanelModel infoPanelModel) {
        infoPanelModel.addListener(this);
        this.aircraft = infoPanelModel.getAircraft();
        calcAircraftWeight();
    }

    /**
     * calculates aircraft weight and sets it in the {@link InfoPanelModel}
     * */
    private void calcAircraftWeight() {
        aircraftWeight = aircraft.getType().getEmptyWeight();
        aircraftWeight += aircraft.getTotalFuel();
        aircraftWeight += aircraft.getTotalCargoWeight();
        aircraftWeight += PassengersModel.getPassengersWeight();
        InfoPanelModel.setAircraftWeight(aircraftWeight);
    }

    @Override
    public void updateConfig() {
        log.info("Updating the weight");
        calcAircraftWeight();
    }

}

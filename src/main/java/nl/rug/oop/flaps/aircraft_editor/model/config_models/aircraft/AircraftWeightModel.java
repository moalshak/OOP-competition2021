package nl.rug.oop.flaps.aircraft_editor.model.config_models.aircraft;

import lombok.Getter;
import lombok.extern.java.Log;
import nl.rug.oop.flaps.aircraft_editor.controller.listeners.infopanel_listeners.ConfirmChangeButtonListener;
import nl.rug.oop.flaps.aircraft_editor.model.config_models.InfoPanelModel;
import nl.rug.oop.flaps.aircraft_editor.model.config_models.InfoPanelModelListener;
import nl.rug.oop.flaps.simulation.model.aircraft.Aircraft;

/**
 * this class calculates the weight of the aircraft
 * This class updates each time the {@link ConfirmChangeButtonListener} is in action.
 * */
@Log
public class AircraftWeightModel implements InfoPanelModelListener {
    @Getter
    private double aircraftWeight;

    private Aircraft aircraft;
    private final InfoPanelModel infoPanelModel;
    public AircraftWeightModel(InfoPanelModel infoPanelModel) {
        this.infoPanelModel = infoPanelModel;
        infoPanelModel.addListener(this);
        this.aircraft = infoPanelModel.getAircraft();
        calcAircraftWeight();
    }

    /**
     * calculates aircraft weight and sets it in the {@link InfoPanelModel}
     * */
    private void calcAircraftWeight() {
        aircraft = infoPanelModel.getAircraft();
        aircraftWeight = aircraft.getType().getEmptyWeight();
        aircraftWeight += aircraft.getTotalFuel();
        aircraftWeight += aircraft.getTotalCargoWeight();
        aircraftWeight += aircraft.getPassengersWeight();
        infoPanelModel.setAircraftWeight(aircraftWeight);
    }

    @Override
    public void updateConfig() {
        log.info("Updating the weight");
        calcAircraftWeight();
    }

}

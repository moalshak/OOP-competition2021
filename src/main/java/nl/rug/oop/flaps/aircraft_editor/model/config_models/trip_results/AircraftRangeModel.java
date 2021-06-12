package nl.rug.oop.flaps.aircraft_editor.model.config_models.trip_results;

import lombok.extern.java.Log;
import nl.rug.oop.flaps.aircraft_editor.controller.listeners.infopanel_listeners.ConfirmChangeButtonListener;
import nl.rug.oop.flaps.aircraft_editor.model.config_models.InfoPanelModel;
import nl.rug.oop.flaps.aircraft_editor.model.config_models.InfoPanelModelListener;
import nl.rug.oop.flaps.simulation.model.aircraft.Aircraft;

/**
 * class to calculate the range of the aircraft and sets value in {@link InfoPanelModel}
 * This class updates each time the {@link ConfirmChangeButtonListener} is in action.
 * */
@Log
public class AircraftRangeModel implements InfoPanelModelListener {
    private final  Aircraft aircraft;

    public AircraftRangeModel(InfoPanelModel infoPanelModel) {
        infoPanelModel.addListener(this);

        this.aircraft = infoPanelModel.getAircraft();

        calcRange();
    }

    /**
     * calculates the range of the {@link Aircraft} dependent on the fuelAmount, the fuel consumption and the cruise speed
     * sets values in {@link InfoPanelModel}
     * */
    private void calcRange() {
            double totalFuel = this.aircraft.getTotalFuel();
            double fuelConsumption = this.aircraft.getType().getFuelConsumption();
            double cruiseSpeed = this.aircraft.getType().getCruiseSpeed();
           InfoPanelModel.setAircraftRange( ( totalFuel / fuelConsumption) * cruiseSpeed);
    }

    @Override
    public void updateConfig() {
        log.info("Updating the range");
        calcRange();
    }
}


package nl.rug.oop.flaps.aircraft_editor.util;

import nl.rug.oop.flaps.simulation.model.aircraft.Aircraft;
import nl.rug.oop.flaps.simulation.model.aircraft.FuelTank;
import nl.rug.oop.flaps.simulation.model.world.WorldSelectionModel;
/**
 * this class is a utility method to calculate, when choosing an aircraft, if the aircraft can reach its destination.
 * */
public class IsDestinationReachable {
    /**
     * calculates of the destination of the aircraft can reach its destination
     *
     * @param aircraft the aircraft chosen
     * @param model the world selection model
     * @return boolean ( the destination is reachable or not )
     * */
    public static boolean isDestinationReachable(Aircraft aircraft, WorldSelectionModel model) {
        var distance = model.getSelectedAirport().getLocation().distanceTo(
                model.getSelectedDestinationAirport().getLocation());
        return (distance/1000) < aircraft.getType().getRange();
    }
}

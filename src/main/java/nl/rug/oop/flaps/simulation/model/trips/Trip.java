package nl.rug.oop.flaps.simulation.model.trips;

import lombok.Getter;
import nl.rug.oop.flaps.aircraft_editor.view.panels.aircraft_info.interaction_panels.InteractionPanel;
import nl.rug.oop.flaps.simulation.model.aircraft.Aircraft;
import nl.rug.oop.flaps.simulation.model.airport.Airport;
import nl.rug.oop.flaps.simulation.model.map.coordinates.ProjectionMapping;
import nl.rug.oop.flaps.simulation.model.world.World;
import nl.rug.oop.flaps.simulation.model.world.WorldSelectionModel;
import nl.rug.oop.flaps.simulation.view.panels.WorldPanel;

import java.awt.geom.Point2D;

public class Trip {
    private static final double INDICATOR_SIZE = 8;
    private final Point2D destinationAirportLocation;

    private final Airport originAirport;
    private final Airport destAirport;
    @Getter
    private final Aircraft aircraft;
    @Getter
    private final Point2D currentPosition;
    @Getter
    boolean reachedDestination = false;

    private final WorldSelectionModel sm;
    private static final double velocity = 0.1;
    /**
     * creates a new instance of the Trip after departure
     * */
    public Trip(WorldSelectionModel sm) {
        this.sm = sm;
        originAirport = sm.getSelectedAirport();
        destAirport = sm.getSelectedDestinationAirport();
        aircraft = sm.getSelectedAircraft();
        currentPosition = getAirportAsPoint(sm.getSelectedAirport());
        destinationAirportLocation = getAirportAsPoint(sm.getSelectedDestinationAirport());
        WorldPanel.getWorldPanel().addTrip(this);
    }

    /**
     * Moves the plane forward
     * */
    public void cruise () {
        // update position
        if (currentPosition.getX() < destinationAirportLocation.getX()) {
            currentPosition.setLocation(currentPosition.getX()+velocity, currentPosition.getY());
        } else if (currentPosition.getX() >= destinationAirportLocation.getX()) {
            currentPosition.setLocation(currentPosition.getX()-velocity, currentPosition.getY());
        }
        if (currentPosition.getY() < destinationAirportLocation.getY()) {
            currentPosition.setLocation(currentPosition.getX(), currentPosition.getY() + velocity);
        } else if (currentPosition.getY() >= destinationAirportLocation.getY()) {
            currentPosition.setLocation(currentPosition.getX(), currentPosition.getY() - velocity);
        }
        // update of checked destination
        reachedDestination = currentPosition.distance(destinationAirportLocation) < INDICATOR_SIZE;
        // repaint
        if(reachedDestination) aircraftArrived();
        WorldPanel.getWorldPanel().repaint();
    }

    /**
     * The trip has arrived, remove cargo, fuel consumed and passengers.
     * Also removed the trip from the trip list
     * */
    private void aircraftArrived() {
        destAirport.addAircraft(aircraft);
        aircraft.removeFuel(aircraft.getFuelConsumption(originAirport, destAirport));
        aircraft.emptyCargo();
        /* escort passengers if any were added */
        if(InteractionPanel.getPassengersConfigPanel() != null) {
            InteractionPanel.getPassengersConfigPanel().getModel().escortPassengers();
        }
        if(sm.getSelectedAirport() != null && sm.getSelectedAirport() == destAirport) {
            sm.setSelectedAirport(destAirport);
        }
        WorldPanel.getWorldPanel().removeTrip(this);
    }

    /**
     * gets the mapped airport
     * @param airport the airport to map on the world map
     * @return the airport as a 2D point mapped on the world map
     * */
    private Point2D getAirportAsPoint(Airport airport) {
        return ProjectionMapping.mercatorToWorld(World.getStaticDimensions())
                .map(airport.getGeographicCoordinates()).asPoint();
    }
}

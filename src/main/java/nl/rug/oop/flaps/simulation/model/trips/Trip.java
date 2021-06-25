package nl.rug.oop.flaps.simulation.model.trips;

import lombok.Getter;
import lombok.SneakyThrows;
import nl.rug.oop.flaps.aircraft_editor.view.panels.aircraft_info.interaction_panels.InteractionPanel;
import nl.rug.oop.flaps.simulation.model.aircraft.Aircraft;
import nl.rug.oop.flaps.simulation.model.airport.Airport;
import nl.rug.oop.flaps.simulation.model.map.coordinates.GeographicCoordinates;
import nl.rug.oop.flaps.simulation.model.map.coordinates.PointProvider;
import nl.rug.oop.flaps.simulation.model.map.coordinates.ProjectionMapping;
import nl.rug.oop.flaps.simulation.model.world.World;
import nl.rug.oop.flaps.simulation.model.world.WorldSelectionModel;
import nl.rug.oop.flaps.simulation.view.panels.WorldPanel;
import nl.rug.oop.flaps.simulation.view.panels.aircraft.AircraftPanel;
import nl.rug.oop.flaps.simulation.view.panels.airport.AirportPanel;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Random;

@Getter
public class Trip {
    private static final double INDICATOR_SIZE = 8;
    private final Point2D originAirportLocation;
    private final Point2D destinationAirportLocation;
    private final StringBuilder flightsId = generateId();
    private Image bannerInAir;

    private final Airport originAirport;
    private final Airport destAirport;

    private final Aircraft aircraft;
    private final Point2D currentPosition;
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
        originAirportLocation = getAirportAsPoint(originAirport);
        currentPosition = originAirportLocation;
        destinationAirportLocation = getAirportAsPoint(destAirport);
        setBannerImage();
        WorldPanel.getWorldPanel().addTrip(this);
    }

    /**
     * sets the banner image of the aircraft flying according to the type
     * */
    @SneakyThrows
    private void setBannerImage() {
        String aircraftType = aircraft.getType().getName();
        if (aircraftType.equals("Boeing 747-400F")) {
            bannerInAir = ImageIO.read(Path.of("data/aircraft_types/jets/747", "clouds747.jpg").toFile());
        } else if (aircraftType.equals("Boeing 737-800BCF Freighter")) {
            bannerInAir = ImageIO.read(Path.of("data/aircraft_types/jets/737", "clouds737.jpg").toFile());
        } else {
            bannerInAir = ImageIO.read(Path.of("data/aircraft_types/general_aviation/grand_caravan", "cloudsGrandCarvan.jpg").toFile());
        }
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
        // update of checked destination (trip arrived when in range of the airport )
        reachedDestination = currentPosition.distance(destinationAirportLocation) < INDICATOR_SIZE;
        // repaint
        if(reachedDestination) aircraftArrived();
        // todo: make the fuel removed according to the place of the plane on the map
        WorldPanel.getWorldPanel().repaint();
    }

    /**
     * The trip has arrived, remove cargo, fuel consumed and passengers.
     * Also removed the trip from the trip list
     * */
    private void aircraftArrived() {
        //GeographicCoordinates place = new GeographicCoordinates(0,0);
        //place.setLongitude(currentPosition.getX() * originAirport.getLocation().getLongitude() / originAirportLocation.getX());
        //place.setLongitude(currentPosition.getY() * originAirport.getLocation().getLatitude() / originAirportLocation.getY());
        //aircraft.removeFuel(aircraft.getFuelConsumption(originAirport, place));
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
     * generates a random ID
     * */
    private StringBuilder generateId() {
        Random r = new Random();
        StringBuilder id = new StringBuilder();
        for (int i = 0; i < 3; i++) {
            id.append((char) (r.nextInt(26) + 'a'));
        }
        for (int i = 0; i < 3; i++) {
            id.append(Math.floor(Math.random()*(10 +1)+0));
        }
        return id;
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

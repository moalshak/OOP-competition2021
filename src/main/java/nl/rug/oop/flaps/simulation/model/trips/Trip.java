package nl.rug.oop.flaps.simulation.model.trips;

import lombok.Getter;
import lombok.SneakyThrows;
import nl.rug.oop.flaps.aircraft_editor.model.config_models.InfoPanelModel;
import nl.rug.oop.flaps.aircraft_editor.view.panels.aircraft_info.interaction_panels.InteractionPanel;
import nl.rug.oop.flaps.simulation.model.aircraft.Aircraft;
import nl.rug.oop.flaps.simulation.model.aircraft.FuelTank;
import nl.rug.oop.flaps.simulation.model.airport.Airport;
import nl.rug.oop.flaps.simulation.model.map.coordinates.GeographicCoordinates;
import nl.rug.oop.flaps.simulation.model.map.coordinates.PointProvider;
import nl.rug.oop.flaps.simulation.model.map.coordinates.ProjectionMapping;
import nl.rug.oop.flaps.simulation.model.world.World;
import nl.rug.oop.flaps.simulation.model.world.WorldSelectionModel;
import nl.rug.oop.flaps.simulation.view.panels.WorldPanel;
import nl.rug.oop.flaps.simulation.view.panels.trip.TripsInfo;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Point2D;
import java.lang.reflect.Field;
import java.nio.file.Path;
import java.util.*;
import java.util.List;

@Getter
public class Trip {
    private static final double INDICATOR_SIZE = 8;
    private final Point2D originAirportLocation;
    private final Point2D destinationAirportLocation;
    @Getter
    private final static int revenue = (int) InfoPanelModel.getRevenue();

    private final StringBuilder flightsId = generateId();
    private Image bannerInAir;
    private final List<Point2D> steps = new ArrayList<>();

    private final Airport originAirport;
    private final Airport destAirport;

    private final Aircraft aircraft;
    private final Point2D currentPosition;
    boolean reachedDestination = false;

    private final WorldSelectionModel sm;
    private static final double VELOCITY = 0.1;

    private String distanceLeft;

    Map<FuelTank, Double> fuelTankFillStatuses;
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
        this.fuelTankFillStatuses = new HashMap<>(aircraft.getFuelTankFillStatuses());

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
            int nr = (int)(Math.random()*(4-1+1)+1);
            switch (nr) {
                case 1 : bannerInAir = ImageIO.read(Path.of("data/aircraft_types/jets/747", "clouds747.jpg").toFile());
                break;
                case 2 : bannerInAir = ImageIO.read(Path.of("data/aircraft_types/jets/747", "clouds747_2.jpg").toFile());
                break;
                case 3 : bannerInAir = ImageIO.read(Path.of("data/aircraft_types/jets/747", "clouds747_3.jpg").toFile());
                break;
                case 4 : bannerInAir = ImageIO.read(Path.of("data/aircraft_types/jets/747", "clouds747_4.jpg").toFile());
                break;
            }
        } else if (aircraftType.equals("Boeing 737-800BCF Freighter")) {
            int nr = (int)(Math.random()*(4-1+1)+1);
            switch (nr) {
                case 1 : bannerInAir = ImageIO.read(Path.of("data/aircraft_types/jets/737", "clouds737.jpg").toFile());
                    break;
                case 2 : bannerInAir = ImageIO.read(Path.of("data/aircraft_types/jets/737", "clouds737_2.jpg").toFile());
                    break;
                case 3 : bannerInAir = ImageIO.read(Path.of("data/aircraft_types/jets/737", "clouds737_3.jpg").toFile());
                    break;
                case 4 : bannerInAir = ImageIO.read(Path.of("data/aircraft_types/jets/737", "clouds737_4.jpg").toFile());
                    break;
            }
        } else {
            bannerInAir = ImageIO.read(Path.of("data/aircraft_types/general_aviation/grand_caravan", "cloudsGrandCarvan.jpg").toFile());
        }
    }
    /**
     * Moves the plane forward
     * */
    public void cruise () {
        // update position

        GeographicCoordinates start =  getGeoPosition(currentPosition);
        double xVelocity = currentPosition.getX();
        double yVelocity = currentPosition.getY();

        if (currentPosition.getX() < destinationAirportLocation.getX()) {
            xVelocity += VELOCITY;
        } else if (currentPosition.getX() >= destinationAirportLocation.getX()) {
            xVelocity -= VELOCITY;
        }
        if (currentPosition.getY() < destinationAirportLocation.getY()) {
            yVelocity += VELOCITY;
        } else if (currentPosition.getY() >= destinationAirportLocation.getY()) {
            yVelocity -= VELOCITY;
        }
        currentPosition.setLocation(xVelocity, yVelocity);

        steps.add(new Point2D.Double(currentPosition.getX(), currentPosition.getY()));
        // update of checked destination (trip arrived when in range of the airport )
        reachedDestination = currentPosition.distance(destinationAirportLocation) < INDICATOR_SIZE/10;
        GeographicCoordinates end = getGeoPosition(currentPosition);

        setDistanceLeft(end);
        removedAndUpdateFuel(end);
        // repaint
        WorldPanel.getWorldPanel().repaint();

        if(reachedDestination) aircraftArrived();
    }

    /**
     * sets and loads the distance left progress bar
     * */
    private void setDistanceLeft(GeographicCoordinates end) {
        StringBuilder stringBuilder = new StringBuilder();

        int percentage = (int) (Math.round(50.0 * originAirport.getGeographicCoordinates().distanceTo(end) /
                originAirport.getGeographicCoordinates().distanceTo(destAirport.getGeographicCoordinates())));

        stringBuilder.append("Progress: 🏢");
        stringBuilder.append("-".repeat(Math.max(0,percentage)));
        if (percentage > 0) {
            stringBuilder.append("✈");
        }
        stringBuilder.append("-".repeat(Math.max(0, 50 - percentage)));
        stringBuilder.append("🏢");

        if(reachedDestination) stringBuilder.append("   Arrived 🛬 ✅");

        distanceLeft = String.valueOf(stringBuilder);
    }

    /**
     * removes the fuel from the aircraft and updates the trip's info panel
     * */
    private void removedAndUpdateFuel(GeographicCoordinates end) {
        for (FuelTank fuelTank : fuelTankFillStatuses.keySet()) {
            aircraft.setFuelAmountForFuelTank(fuelTank, fuelTankFillStatuses.get(fuelTank));
        }

        aircraft.removeFuel(aircraft.getFuelConsumption(originAirport, end));

        if (TripsInfo.getTripsInfo() != null) {
            TripsInfo.getTripsInfo().updateFuelLabel();
            TripsInfo.getTripsInfo().updateDistanceMeter();
        }
    }

    /**
     * @return the the geo position of a point drawn on the world map
     * */
    private GeographicCoordinates getGeoPosition(Point2D currentPosition) {
        var geo = ProjectionMapping.worldToMercator(World.getStaticDimensions()).
                map(PointProvider.ofPoint(new Point2D.Double(currentPosition.getX(), currentPosition.getY())));
        return new GeographicCoordinates(geo.getPointX(),geo.getPointY());
    }

    /**
     * The trip has arrived, remove cargo, fuel consumed and passengers.
     * Also removed the trip from the trip list
     * */
    private void aircraftArrived() {
        destAirport.addAircraft(aircraft);
        aircraft.emptyCargo();
        /* escort passengers if any were added */
        if(InteractionPanel.getPassengersConfigPanel() != null) {
            InteractionPanel.getPassengersConfigPanel().getModel().escortPassengers(this);
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

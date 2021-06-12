package nl.rug.oop.flaps.simulation.model.airport;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import nl.rug.oop.flaps.simulation.model.aircraft.Aircraft;
import nl.rug.oop.flaps.simulation.model.aircraft.FuelType;
import nl.rug.oop.flaps.simulation.model.cargo.CargoType;
import nl.rug.oop.flaps.simulation.model.map.coordinates.GeographicCoordinates;

import java.awt.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Represents an airport in the world, with some metadata, aircraft
 *
 * @author T.O.W.E.R.
 */
@Getter
public class Airport implements Comparable<Airport> {
    /**
     * The name of the airport
     */
    private String name;

    /**
     * The description of the airport
     */
    private String description;

    /**
     * The maximum number of aircraft this airport can have
     */
    private int aircraftCapacity;

    /**
     * The geographic coordinates of this airport on the world map
     */
    private GeographicCoordinates geographicCoordinates;

    /**
     * The aircraft that are currently on this airport
     */
    @JsonIgnore
    private final Set<Aircraft> aircraft;

    /**
     * A map that contains, for each fuel type, its price in euros at this airport
     */
    @JsonIgnore
    private final Map<FuelType, Double> fuelPrices;

    /**
     * A map that contains, for each cargo type, its price in euros at this airport
     */
    @JsonIgnore
    private final Map<CargoType, Double> cargoImportDemands;

    /**
     * The banner image of this airport
     */
    @Setter
    private Image bannerImage;

    /**
     * Creates a new airport
     */
    public Airport() {
        aircraft = new HashSet<>();
        fuelPrices = new HashMap<>();
        cargoImportDemands = new HashMap<>();
    }

    /**
     * Indicates whether this airport has enough capacity to accept an incoming aircraft
     *
     * @return True if there is space left for an aircraft to arrive here; false otherwise
     */
    public boolean canAcceptIncomingAircraft() {
        return aircraftCapacity > aircraft.size();
    }

    /**
     * Adds an aircraft to this airport
     *
     * @param incomingAircraft The aircraft that is arriving at this airport
     */
    public void addAircraft(Aircraft incomingAircraft) {
        aircraft.add(incomingAircraft);
    }

    /**
     * Removes an aircraft from this airport
     *
     * @param departingAircraft The aircraft that is leaving the airport
     */
    public void removeAircraft(Aircraft departingAircraft) {
        aircraft.remove(departingAircraft);
    }

    /**
     * Retrieves the location of the airport
     *
     * @return The geographic location of this airport
     */
    public GeographicCoordinates getLocation() {
        return this.geographicCoordinates;
    }

    /**
     * Retrieve the price of the specified cargo type at this airport
     *
     * @param cargoType The cargo type of which the price should be retrieved
     * @return The price of the cargo type in euros per kg
     */
    public double getCargoPriceByType(CargoType cargoType) {
        return cargoImportDemands.getOrDefault(cargoType, 0d);
    }

    /**
     * Retrieve the price of the specified fuel type at this airport
     *
     * @param fuelType The fuel type of which the price should be retrieved
     * @return The price of the fuel type in euros per kg
     */
    public double getFuelPriceByType(FuelType fuelType) {
        return fuelPrices.getOrDefault(fuelType, 0d);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Airport)) return false;
        Airport a = (Airport) o;
        return this.getName().equals(a.getName());
    }

    @Override
    public int compareTo(Airport o) {
        return this.getName().compareTo(o.getName());
    }

    @Override
    public String toString() {
        return "Airport{name=\"" + name + "\"}";
    }
}

package nl.rug.oop.flaps.simulation.model.world;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.java.Log;
import nl.rug.oop.flaps.simulation.model.aircraft.AircraftType;
import nl.rug.oop.flaps.simulation.model.aircraft.FuelType;
import nl.rug.oop.flaps.simulation.model.airport.Airport;
import nl.rug.oop.flaps.simulation.model.cargo.CargoType;
import nl.rug.oop.flaps.simulation.model.map.WorldDimensions;
import nl.rug.oop.flaps.simulation.model.map.coordinates.GeographicCoordinates;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

/**
 * The main model for the simulation, containing the set of airports in the
 * world, and the state of the application.
 *
 * @author T.O.W.E.R.
 */
@Log
public class World {
    private static final double NEARBY_AIRPORT_RANGE = 500.0;

    private final Map<String, Airport> airports;
    private final Map<String, AircraftType> aircraftTypes;
    private final Map<String, FuelType> fuelTypes;
    private final Map<String, CargoType> cargoTypes;

    @Getter
    @Setter
    private WorldDimensions dimensions;

    @Getter
    private final WorldSelectionModel selectionModel;

    public World(Set<Airport> airports, Set<AircraftType> aircraftTypes, Set<FuelType> fuelTypes, Set<CargoType> cargoTypes, WorldDimensions dimensions) {
        this.airports = setToMap(airports, Airport::getName);
        this.aircraftTypes = setToMap(aircraftTypes, AircraftType::getName);
        this.fuelTypes = setToMap(fuelTypes, FuelType::getName);
        this.cargoTypes = setToMap(cargoTypes, CargoType::getName);
        this.dimensions = dimensions;
        this.selectionModel = new WorldSelectionModel();
    }

    public Map<String, Airport> getAirports() {
        return Collections.unmodifiableMap(this.airports);
    }

    public Map<String, AircraftType> getAircraftTypes() {
        return Collections.unmodifiableMap(this.aircraftTypes);
    }

    public Map<String, FuelType> getFuelTypes() {
        return Collections.unmodifiableMap(this.fuelTypes);
    }

    public Map<String, CargoType> getCargoTypes() {
        return Collections.unmodifiableMap(this.cargoTypes);
    }

    /**
     * Finds the nearest airport to the given position, within the specified
     * tolerance range.
     *
     * @param geoPosition The geographic coordinates around which to search.
     * @param tolerance   The range to look for an airport, in kilometers.
     *
     * @return The airport that was found, or none if none could be found.
     */
    public Optional<Airport> getNearestAirport(GeographicCoordinates geoPosition, double tolerance) {
        return this.airports.values().stream()
                .filter(airport -> airport.getLocation().distanceTo(geoPosition) / NEARBY_AIRPORT_RANGE < tolerance)
                .findAny();
    }

    /**
     * Utility method to convert a set of objects to a map, using a unique field
     * as the key.
     *
     * @param set            The set of objects.
     * @param uniqueProperty A getter method for a unique key value.
     * @param <P>            The type of the key.
     * @param <O>            The type of the object.
     *
     * @return A map of objects, mapped by their unique property.
     */
    private static <P, O> Map<P, O> setToMap(Set<O> set, Function<O, P> uniqueProperty) {
        Map<P, O> map = new ConcurrentHashMap<>(set.size());
        set.forEach(o -> map.put(uniqueProperty.apply(o), o));
        return map;
    }
}

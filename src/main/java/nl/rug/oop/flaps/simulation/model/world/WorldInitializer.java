package nl.rug.oop.flaps.simulation.model.world;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import lombok.extern.java.Log;
import nl.rug.oop.flaps.simulation.model.aircraft.Aircraft;
import nl.rug.oop.flaps.simulation.model.aircraft.AircraftType;
import nl.rug.oop.flaps.simulation.model.airport.Airport;
import nl.rug.oop.flaps.simulation.model.loaders.AircraftTypeLoader;
import nl.rug.oop.flaps.simulation.model.loaders.AirportLoader;
import nl.rug.oop.flaps.simulation.model.loaders.CargoLoader;
import nl.rug.oop.flaps.simulation.model.map.WorldDimensions;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Very simple initializer class that initializes the world
 *
 * @author T.O.W.E.R.
 */
@Log
public class WorldInitializer {
    /**
     * Initializes the world
     *
     * @return The default world
     */
    public World initializeWorld() throws IOException {
        var mapper = new ObjectMapper(new YAMLFactory());
        var aircraftTypeLoader = new AircraftTypeLoader(mapper);
        WorldDimensions dimensions;
        File dimensionsFile = Path.of("config", "map.yaml").toFile();
        dimensions = new ObjectMapper(new YAMLFactory()).readValue(dimensionsFile, WorldDimensions.class);
        World world = new World(
                new AirportLoader(mapper).loadAirports(),
                aircraftTypeLoader.loadTypes(),
                new HashSet<>(aircraftTypeLoader.getFuelTypes().values()),
                new CargoLoader(mapper).loadTypes(),
                dimensions
        );
        this.populateWorld(world);
        return world;
    }

    private void populateWorld(World world) {
        world.getAirports().forEach((name, airport) -> {
            this.generateRandomAircraft(airport, world);
            this.generateFuelPrices(airport, world);
            this.generateCargoDemands(airport, world);
        });
    }

    private void generateRandomAircraft(Airport airport, World world) {
        Random r = ThreadLocalRandom.current();
        List<AircraftType> typesList = new ArrayList<>(world.getAircraftTypes().values());
        int aircraftCount = r.nextInt(airport.getAircraftCapacity() + 1) + 1;
        for (int i = 0; i < aircraftCount; i++) {
            String identifier = UUID.randomUUID().toString().substring(0, 8);
            var type = typesList.get(r.nextInt(typesList.size()));
            airport.addAircraft(new Aircraft(identifier, type, world));
        }
    }

    private void generateFuelPrices(Airport airport, World world) {
        world.getFuelTypes().values().forEach(fuelType -> {
            double basePrice = fuelType.getBasePricePerKg();
            airport.getFuelPrices().put(fuelType, ThreadLocalRandom.current().nextDouble(basePrice * 0.5, basePrice * 1.5));
        });
    }

    private void generateCargoDemands(Airport airport, World world) {
        world.getCargoTypes().values().forEach(cargoType ->
                airport.getCargoImportDemands().put(cargoType, ThreadLocalRandom.current().nextDouble(0.1, 1.0))
        );
    }
}

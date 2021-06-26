package nl.rug.oop.flaps.simulation.model.aircraft;

import lombok.Getter;
import lombok.Setter;
import nl.rug.oop.flaps.aircraft_editor.model.blueprint.BluePrintModel;
import nl.rug.oop.flaps.simulation.model.airport.Airport;
import nl.rug.oop.flaps.simulation.model.cargo.CargoType; //!!Don't remove this!!, if you do it causes some weird bug
import nl.rug.oop.flaps.simulation.model.cargo.CargoUnit;
import nl.rug.oop.flaps.simulation.model.map.coordinates.GeographicCoordinates;
import nl.rug.oop.flaps.simulation.model.world.World;

import java.awt.geom.Point2D;
import java.util.*;

/**
 * Represents a single aircraft of a particular type.
 *
 * @author T.O.W.E.R.
 */
@Getter
public class Aircraft implements Comparable<Aircraft>, Cloneable {
    /**
     * A unique identifier for this aircraft, like a call-sign.
     */
    private final String identifier;

    /**
     * The type of this aircraft.
     */
    private final AircraftType type;

    /**
     * The world that this aircraft exists in.
     */
    private final World world;

    /**
     * A map that contains information for each fuel tank of how much fuel is in there
     * The key is the name of the fuel tank and the Double is the amount of fuel in kg
     */
    private final Map<FuelTank, Double> fuelTankFillStatuses;

    /**
     * A map that contains information for each cargo area what the cargo contents are
     * The key is the name of the cargo area
     */
    private final Map<CargoArea, Set<CargoUnit>> cargoAreaContents;

    /**
     * holds the passengers on board
     * */
    @Setter
    private HashMap<String, Integer> passengers;

    /**
     * number of crew on board
     * */
    @Setter
    private int crewOnBoard;
    private int nrOfSeats;
    private Point2D geoEnt;

    public Aircraft(String identifier, AircraftType type, World world) {
        this.identifier = identifier;
        this.type = type;
        this.world = world;
        fuelTankFillStatuses = new HashMap<>();
        cargoAreaContents = new HashMap<>();

        passengers = new HashMap<>();
        passengers.put("adults", 0);
        passengers.put("kidsTo12", 0);
        passengers.put("kidsUnder12", 0);

        typeMapper();
    }

    public void typeMapper() {
        AircraftType type = this.getType();
        geoEnt = new Point2D.Double();

        if(type.getName().equals("Boeing 747-400F")) {
            geoEnt.setLocation(4, 18);
            nrOfSeats = 366;
            crewOnBoard = (int)(Math.random()*(10-2+1)+2);
        } else if (type.getName().equals("Boeing 737-800BCF Freighter")) {
            geoEnt.setLocation(3, 10);
            nrOfSeats = 120;
            crewOnBoard = (int)(Math.random()*(5-2+1)+2);
        } else {
            geoEnt.setLocation(0.5, 2.1);
            nrOfSeats = 9;
            crewOnBoard = 2;
        }
    }



    /**
     * Retrieves the amount of fuel that is consumed when flying between two airports in kg
     *
     * @param origin The airport the aircraft departs from
     * @param destination The airport the aircraft flies to
     * @return The amount of fuel in kg consumed in the journey
     */
    public double getFuelConsumption(Airport origin, Airport destination) {
        /* calculate distance to destination airport */
        double distance = origin.getGeographicCoordinates().distanceTo(destination.getGeographicCoordinates());

        double cruiseSpeed = this.getType().getCruiseSpeed();
        /* flightDuration = the distance in Km then device by the cruiseSpeed*/
        double flightDuration =  (distance / 1000) / cruiseSpeed;
        /* the rate at which the aircraft uses fuel */
        double fuelConsumptionRate = this.getType().getFuelConsumption();
        /* fuel consumption */
        return flightDuration * fuelConsumptionRate;
    }

    /**
     * Retrieves the amount of fuel that is consumed when flying between origin airport and a point on map in kg
     *
     * @param origin The airport the aircraft departs from
     * @param destination The airport the aircraft flies to
     * @return The amount of fuel in kg consumed in the journey
     */
    public double getFuelConsumption(GeographicCoordinates origin, GeographicCoordinates destination) {
        /* calculate distance to destination airport */
        double distance = origin.distanceTo(destination);

        double cruiseSpeed = this.getType().getCruiseSpeed();
        /* flightDuration = the distance in Km then device by the cruiseSpeed*/
        double flightDuration =  (distance / 1000) / cruiseSpeed;
        /* the rate at which the aircraft uses fuel */
        double fuelConsumptionRate = this.getType().getFuelConsumption();
        /* fuel consumption */
        return flightDuration * fuelConsumptionRate;
    }

    /**
     * Progressively removes fuel from tanks until consumedFuel kg has been removed
     *
     * @param consumedFuel The amount of fuel that should be removed in total
     */
    public void removeFuel(double consumedFuel) {
        for (FuelTank fuelTank : fuelTankFillStatuses.keySet()) {
            double fuelInTank = getFuelAmountForFuelTank(fuelTank);
            if (consumedFuel > fuelInTank) {
                setFuelAmountForFuelTank(fuelTank, 0);
                consumedFuel -= fuelInTank;
            } else {
                setFuelAmountForFuelTank(fuelTank, fuelInTank - consumedFuel);
                return;
            }
        }
    }

    /**
     * Retrieves the total amount of fuel (from all fuel tanks) in the aircraft in kg
     *
     * @return The total amount of fuel in the aircraft in kg
     */
    public double getTotalFuel() {
        return fuelTankFillStatuses.keySet().stream().mapToDouble(fuelTankFillStatuses::get).sum();
    }

    /**
     * Sets the amount of fuel in kg for a specific fuel tank
     *
     * @param fuelTank The fuel tank
     * @param fuelAmount   The amount of fuel in kg this aircraft
     */
    public void setFuelAmountForFuelTank(FuelTank fuelTank, double fuelAmount) {
        fuelTankFillStatuses.put(fuelTank, fuelAmount);
    }

    /**
     * Retrieves the amount of fuel in kg for a specific fuel tank
     *
     * @param fuelTank The fuel tank you want the contents of
     *
     * @return The amount of fuel in kg in the fuel tank with the provided name
     */
    public double getFuelAmountForFuelTank(FuelTank fuelTank) {
        return fuelTankFillStatuses.getOrDefault(fuelTank, 0.0);
    }

    /**
     * Retrieves the contents of a cargo area
     *
     * @param cargoArea The cargo area the contents should be retrieved from
     *
     * @return A list of cargo units representing the contents of the cargo area
     */
    public Set<CargoUnit> getCargoAreaContents(CargoArea cargoArea) {
        return cargoAreaContents.getOrDefault(cargoArea, new HashSet<>());
    }

    /**
     * Adds a cargo unit to the specified cargo area
     *
     * @param cargoArea the cargo area in which you want to add cargo too
     * @param newCargoUnit the unit of cargo you are adding
     */
    public void addToCargoArea(CargoArea cargoArea, CargoUnit newCargoUnit){
        //initialises the cargo area if it is empty
        if (this.getCargoAreaContents(cargoArea).isEmpty()) cargoAreaContents.put(cargoArea, new HashSet<>());

        //Checks to see if the cargoUnit already exits in the aircraft, if it does we update the unit instead of adding another copy
        CargoUnit foundUnit = existsInCargoArea(cargoArea, newCargoUnit);
        if (foundUnit != null) this.getCargoAreaContents(cargoArea).remove(foundUnit);
        if (newCargoUnit.getWeight() > 0.0) this.getCargoAreaContents(cargoArea).add(newCargoUnit);
    }

    public void resetCargoArea(CargoArea cargoArea) {
        this.getCargoAreaContents().remove(cargoArea);
        this.getCargoAreaContents(cargoArea).clear();
        cargoAreaContents.put(cargoArea, new HashSet<>());
    }


    /**
     * Checks if a cargoType already exists in the the cargo area
     *
     * @param cargoArea the cargoArea in which you want to search
     * @param sample the sample cargoUnit to search the cargoArea for
     * @return the found cargoUnit or null when nothing is found
     */
    public CargoUnit existsInCargoArea(CargoArea cargoArea, CargoUnit sample) {
        for (CargoUnit existingUnit : this.getCargoAreaContents(cargoArea)) {
            if (existingUnit.getCargoType().equals(sample.getCargoType())) {
                return existingUnit;
            }
        }
        return null;
    }

    /**
     * Gets tha total weight of all the cargoUnits in the specified cargo area
     *
     * @param cargoArea the cargoArea which u want to know the weight of
     * @return the weight of the cargoArea
     */
    public double getCargoAreaWeight(CargoArea cargoArea) {
        return getCargoAreaContents(cargoArea).stream().mapToDouble(CargoUnit::getWeight).sum();
    }

    /**
     * Gets the total weight of all the cargoAreas in the aircraft
     *
     * @return  the weight of all the cargoAreas
     */
    public double getTotalCargoWeight() {
        return getCargoAreaContents().keySet().stream().mapToDouble(this::getCargoAreaWeight).sum();
    }

    /**
     * Finds the revenue based on the the weight of all the cargo in the aircraft
     *
     * @return the revenue from the the cargo
     */
    public double getCargoRevenue() {
        Airport destinationAirport = this.getWorld().getSelectionModel().getSelectedDestinationAirport();
        double totalRevenue = 0;
        for (CargoArea cargoArea : this.getCargoAreaContents().keySet()) {
            for(CargoUnit cargoUnit: this.getCargoAreaContents(cargoArea)){
                totalRevenue = totalRevenue + (cargoUnit.getWeight() * destinationAirport.getCargoPriceByType(cargoUnit.getCargoType()));
            }
        }
        return totalRevenue;
    }

    /**
     * empties all the cargo areas of the air craft
     */
    public void emptyCargo() { getCargoAreaContents().clear(); }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Aircraft aircraft = (Aircraft) o;
        return getIdentifier().equals(aircraft.getIdentifier()) && getType().equals(aircraft.getType());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getIdentifier(), getType());
    }

    @Override
    public int compareTo(Aircraft o) {
        int typeComparison = this.getType().compareTo(o.getType());
        if (typeComparison != 0) return typeComparison;
        return this.getIdentifier().compareTo(o.getIdentifier());
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
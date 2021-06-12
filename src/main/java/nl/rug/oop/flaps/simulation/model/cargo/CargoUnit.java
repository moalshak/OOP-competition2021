package nl.rug.oop.flaps.simulation.model.cargo;

import lombok.Getter;
import lombok.Setter;

/**
 * Represents a unit of cargo
 *
 * @author T.O.W.E.R.
 */
@Getter
public class CargoUnit {
    /**
     * The type that this cargo unit has
     */
    private final CargoType cargoType;
    /**
     * The weight in kg of this cargo unit
     */
    @Setter
    private double weight;

    public CargoUnit(CargoType cargoType) {
        this.cargoType = cargoType;
    }

    public CargoUnit(CargoType cargoType, double weight) {
        this.cargoType = cargoType;
        this.weight = weight;
    }
}

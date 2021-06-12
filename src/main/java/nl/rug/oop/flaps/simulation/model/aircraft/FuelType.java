package nl.rug.oop.flaps.simulation.model.aircraft;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

/**
 * Represents a particular type of fuel. Note that this does not represent a unit of fuel; it only represents the type
 * itself and the base properties of this type. These properties are the same for all classes that have this type.
 *
 * @author T.O.W.E.R.
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FuelType implements Comparable<FuelType> {
	/**
	 * The name of the fuel type
	 */
	private String name;

	/**
	 * The description of this fuel type
	 */
	private String description;

	/**
	 * The density of this fuel type in kg/l
	 */
	private double density;

	/**
	 * The specific energy of this fuel type in J/kg
	 */
	private double specificEnergy;

	/**
	 * The price of this fuel type per kg
	 */
	private double basePricePerKg;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		FuelType fuelType = (FuelType) o;
		return getName().equals(fuelType.getName());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getName());
	}

	@Override
	public int compareTo(FuelType o) {
		return this.getName().compareTo(o.getName());
	}

	@Override
	public String toString() {
		return "FuelType{" +
				"name='" + name + '\'' +
				'}';
	}
}

package nl.rug.oop.flaps.simulation.model.cargo;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

/**
 * Represents a type of cargo. Note that this does not represent a unit of cargo; it only represents the type itself and
 * the base properties of this type. These properties are the same for all classes that have this type.
 *
 * @author T.O.W.E.R.
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CargoType implements Comparable<CargoType> {
	/**
	 * The name of the cargo type
	 */
	private String name;

	/**
	 * The price per kg in euros
	 */
	private float basePricePerKg;

	/**
	 * The sprite of this type
	 */
	@Setter
	private Image image;

	public ImageIcon getIcon() {
		ImageIcon icon;
		try {
			icon = new ImageIcon(this.getImage().getScaledInstance(28, 28,Image.SCALE_DEFAULT));
		} catch (Exception e) {
			icon = null;
		}
		return icon;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		CargoType cargoType = (CargoType) o;
		return getName().equals(cargoType.getName());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getName());
	}

	@Override
	public String toString() {
		return "CargoType{" +
				"name='" + name + '\'' +
				", basePricePerKg=" + basePricePerKg +
				'}';
	}

	@Override
	public int compareTo(CargoType o) {
		return this.getName().compareTo(o.getName());
	}
}

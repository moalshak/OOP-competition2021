package nl.rug.oop.flaps.simulation.model.aircraft;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.awt.geom.Point2D;
import java.util.Objects;

/**
 * Represents a cargo area. Note that this class does not contain any information about the actual contents of the cargo
 * area. This is done in the aircraft itself.
 *
 * @author T.O.W.E.R.
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CargoArea {
    /**
     * The name of this cargo area
     */
    private String name;

    /**
     * The maximum dimension in meters that this cargo area can contain. You do not have to use this
     */
    private double maxSize;

    /**
     * The maximum weight in kg that the contents of this cargo may weigh
     */
    private double maxWeight;

    /**
     * The x-coordinate of this cargo area
     */
    private double x;

    /**
     * The y-coordinate of this cargo area
     */
    private double y;

    /**
     * Retrieves the coordinates of this cargo area
     *
     * @return The coordinates of this cargo area
     */
    public Point2D getCoords() {
        return new Point2D.Double(x, y);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CargoArea cargoArea = (CargoArea) o;
        return Objects.equals(name, cargoArea.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}

package nl.rug.oop.flaps.simulation.model.aircraft;


import lombok.Getter;
import lombok.NoArgsConstructor;

import java.awt.geom.Point2D;
import java.util.Objects;

/**
 * Represents a fuel tank. Note that this class does not contain any information about the actual contents of the fuel
 * tank. This is done in the aircraft itself.
 *
 * @author T.O.W.E.R.
 */
@Getter
@NoArgsConstructor//(access = AccessLevel.PROTECTED)
public class FuelTank {
    /**
     * The name of this fuel tank.
     */
    private String name;

    /**
     * The maximum amount of fuel this tank can contain in kg
     */
    private int capacity;

    /**
     * The x-coordinate of this fuel tank
     */
    private double x;

    /**
     * The y-coordinate of this fuel tank
     */
    private double y;

    /**
     * Retrieves the coordinates of this fuel tank
     *
     * @return The coordinates of this fuel tank
     */
    public Point2D getCoords() {
        return new Point2D.Double(x, y);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FuelTank fuelTank = (FuelTank) o;
        return Objects.equals(name, fuelTank.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}

package nl.rug.oop.flaps.simulation.model.aircraft;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nl.rug.oop.flaps.simulation.model.world.World;

import java.awt.*;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

/**
 * Models a particular type of aircraft.
 *
 * @author T.O.W.E.R.
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AircraftType implements Comparable<AircraftType> {
    /**
     * The unique name of this aircraft type.
     */
    private String name;

    /**
     * A longer description of this aircraft.
     */
    private String description;

    /**
     * The weight of this aircraft, in Kg, when empty (no fuel or cargo).
     */
    private double emptyWeight;

    /**
     * The maximum allowable weight of the aircraft, in Kg, for takeoff. The
     * aircraft cannot be heavier than this at takeoff.
     */
    private double maxTakeoffWeight;

    /**
     * The maximum allowable weight of the aircraft, in Kg, for landing. The
     * aircraft cannot be heavier than this during landing. This is usually
     * slightly less than the maximum takeoff weight.
     */
    private double maxLandingWeight;

    /**
     * The cruise speed, in Km/h, of this aircraft.
     */
    private double cruiseSpeed;

    /**
     * The default range of this aircraft. <em>Do not use this value when
     * determining the range of an aircraft with cargo and fuel.</em>
     */
    private double range;

    /**
     * The length of this aircraft from nose to tail, in meters.
     */
    private double length;

    /**
     * The x-coordinate of the aircraft's center of gravity, where 0 is at the
     * center line, and negative values move towards the left wing, and positive
     * values move towards the right wing.
     */
    private double emptyCgX;

    /**
     * The y-coordinate of the aircraft's center of gravity, where 0 is at the
     * tip of the aircraft's nose, and positive values move towards the tail.
     */
    private double emptyCgY;

    /**
     * The aircraft's tolerance for off-center weight distribution, as a percent
     * of the aircraft's length. The aircraft cannot take off if the computed
     * CoG is further than (cgTolerance * length) meters from the empty CoG.
     */
    private double cgTolerance;

    /**
     * The aircraft's rate of fuel consumption, in Kg per hour. It is assumed
     * that during flight, all fuel tanks are drained evenly.
     */
    private double fuelConsumption;

    /**
     * The list of cargo areas in this type of aircraft.
     */
    private List<CargoArea> cargoAreas;

    /**
     * The list of fuel tanks in this type of aircraft.
     */
    private List<FuelTank> fuelTanks;

    /**
     * The name of the type of fuel that this type of aircraft uses. It should
     * correspond to the name of a {@link FuelType} in {@link World#getFuelTypes()}.
     */
    private String fuelTypeName;

    /**
     * The fuel type that this type of aircraft consumes.
     */
    @Setter
    private FuelType fuelType;

    /**
     * A path to a WAV file that contains a sound clip to play during takeoff.
     */
    @Setter
    private Path takeoffClipPath;

    /**
     * The banner image of this type of aircraft.
     */
    @Setter
    private Image bannerImage;

    /**
     * The blueprint image for this aircraft, as a top-down view.
     */
    @Setter
    private Image blueprintImage;

    /**
     * A small sprite to use in menus for selecting aircraft of this type.
     */
    @Setter
    private Image spriteImage;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AircraftType that = (AircraftType) o;
        return getName().equals(that.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName());
    }

    @Override
    public int compareTo(AircraftType o) {
        return this.getName().compareTo(o.getName());
    }

    @Override
    public String toString() {
        return "AircraftType{name=\"" + this.getName() + "\"}";
    }
}

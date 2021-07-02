package nl.rug.oop.flaps.simulation.model.aircraft;


import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Entrance {

    /**
     * The x-coordinate of this cargo area
     */
    private double x;

    /**
     * The y-coordinate of this cargo area
     */
    private double y;



}

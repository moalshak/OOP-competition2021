package nl.rug.oop.flaps.simulation.model.map;

import lombok.Data;
import nl.rug.oop.flaps.simulation.model.map.coordinates.GeographicCoordinates;

/**
 * Contains a bunch of info about the dimensions (both in terms of pixels as in geographical coordinates) of the map
 * Basically just a data holder
 *
 * @author T.O.W.E.R.
 */
@Data
public class WorldDimensions {
    private int mapWidth;
    private int mapHeight;
    private GeographicCoordinates mapStartCoordinates;
    private GeographicCoordinates mapEndCoordinates;
}

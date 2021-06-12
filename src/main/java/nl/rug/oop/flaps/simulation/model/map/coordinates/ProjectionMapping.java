package nl.rug.oop.flaps.simulation.model.map.coordinates;

import nl.rug.oop.flaps.simulation.model.map.WorldDimensions;

/**
 * Simple interface that defines a method that maps from one 2D coordinate space
 * to another.
 *
 * @author T.O.W.E.R.
 */
@FunctionalInterface
public interface ProjectionMapping {
    PointProvider map(PointProvider source);

    static ProjectionMapping mercatorToWorld(WorldDimensions worldDimensions) {
        return new MercatorToWorldMapping(worldDimensions);
    }

    static ProjectionMapping worldToMercator(WorldDimensions worldDimensions) {
        return new WorldToMercatorMapping(worldDimensions);
    }
}

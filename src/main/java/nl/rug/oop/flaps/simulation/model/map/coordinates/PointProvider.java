package nl.rug.oop.flaps.simulation.model.map.coordinates;

import java.awt.geom.Point2D;

/**
 * Simple interface for declaring other data structures as being able to be
 * implicitly interpreted as a 2D point.
 *
 * @author T.O.W.E.R.
 */
public interface PointProvider {
    Point2D.Double asPoint();

    default double getPointX() {
        return asPoint().x;
    }

    default double getPointY() {
        return asPoint().y;
    }

    /**
     * Helper method to wrap concrete point objects into a point provider.
     *
     * @param point The point to use.
     *
     * @return The point provider implementation.
     */
    static PointProvider ofPoint(Point2D.Double point) {
        return () -> point;
    }
}

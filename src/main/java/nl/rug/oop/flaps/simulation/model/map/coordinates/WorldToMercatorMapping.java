package nl.rug.oop.flaps.simulation.model.map.coordinates;

import nl.rug.oop.flaps.simulation.model.map.WorldDimensions;

import java.awt.geom.Point2D;

/**
 * Maps mercator map coordinates to geographic coordinates
 *
 * @author T.O.W.E.R.
 */
public class WorldToMercatorMapping implements ProjectionMapping {
    private final WorldDimensions worldDimensions;

    public WorldToMercatorMapping(WorldDimensions worldDimensions) {
        this.worldDimensions = worldDimensions;
    }

    @Override
    public PointProvider map(PointProvider source) {
        double w = worldDimensions.getMapWidth();
        double h = worldDimensions.getMapHeight();
        double west = Math.toRadians(worldDimensions.getMapStartCoordinates().getLongitude());
        double east = Math.toRadians(worldDimensions.getMapEndCoordinates().getLongitude());
        double south = Math.toRadians(worldDimensions.getMapStartCoordinates().getLatitude());
        double north = Math.toRadians(worldDimensions.getMapEndCoordinates().getLatitude());

        double x = Math.toDegrees((source.getPointX() / (w / (east - west))) + west);
        double myN = toMercatorY(north);
        double myS = toMercatorY(south);
        double v = (-source.getPointY() / (h / (myN - myS))) + myN;
        double y = Math.toDegrees(fromMercatorY(v));
        return PointProvider.ofPoint(new Point2D.Double(x, y));
    }

    private double toMercatorY(double latitude) {
        return Math.log(Math.tan(latitude / 2.0 + Math.PI / 4.0));
    }

    private double fromMercatorY(double y) {
        return 2 * (Math.atan(Math.pow(Math.E, y)) - Math.PI / 4);
    }
}

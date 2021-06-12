package nl.rug.oop.flaps.simulation.model.map.coordinates;

import nl.rug.oop.flaps.simulation.model.map.WorldDimensions;

import java.awt.geom.Point2D;

/**
 * A mapping that maps mercator longitude and latitude to world map screen
 * coordinates.
 *
 * Taken from Niels' original implementation which referenced calculations on
 * the GIS stack exchange <a href="https://gis.stackexchange.com/questions/71643/map-projection-lat-lon-to-pixel/72050#72050">here</a>.
 *
 * @author T.O.W.E.R.
 */
public class MercatorToWorldMapping implements ProjectionMapping {
    private final WorldDimensions worldDimensions;

    public MercatorToWorldMapping(WorldDimensions worldDimensions) {
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

        double yMin = toMercatorY(south);
        double yMax = toMercatorY(north);

        double x = (Math.toRadians(source.getPointX()) - west) * (w / (east - west));
        double yRad = toMercatorY(Math.toRadians(source.getPointY()));
        double y = (yMax - yRad) * (h / (yMax - yMin));

        return PointProvider.ofPoint(new Point2D.Double(x, y));
    }

    /**
     * The Mercator projection is not a simple scale map, since a sphere is being projected on a rectangle
     * As such, our latitude needs a specific conversion calculation
     *
     * @param latitude The geographical latitude calculation
     *
     * @return The conversion of the latitude calculation
     */
    private double toMercatorY(double latitude) {
        return Math.log(Math.tan(latitude / 2.0 + Math.PI / 4.0));
    }
}

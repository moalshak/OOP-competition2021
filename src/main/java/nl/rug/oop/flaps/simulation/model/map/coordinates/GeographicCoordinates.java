package nl.rug.oop.flaps.simulation.model.map.coordinates;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.awt.geom.Point2D;

/**
 * Class that contains the geographical coordinates of a point
 *
 * @author T.O.W.E.R.
 */
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GeographicCoordinates implements PointProvider {
    private static final double EARTH_RADIUS = 6371e3;

    private double latitude;
    private double longitude;

    public GeographicCoordinates(double longitude, double latitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    /**
     * Computes the distance between two geographic coordinates, using the
     * <a href="https://en.wikipedia.org/wiki/Haversine_formula">haversine formula</a>.
     * <p>
     * See <a href="https://www.movable-type.co.uk/scripts/latlong.html">this website</a>
     * for a more digestible explanation of the mathematics.
     * </p>
     *
     * @param other The other coordinates to use.
     *
     * @return The distance between the coordinates, in meters.
     */
    public double distanceTo(GeographicCoordinates other) {
        final double theta1 = this.latitude * Math.PI / 180.0;
        final double theta2 = other.latitude * Math.PI / 180.0;
        final double dTheta = (other.latitude - this.latitude) * Math.PI / 180.0;
        final double dLambda = (other.longitude - this.longitude) * Math.PI / 180.0;
        final double haversineValue = (
                Math.pow(Math.sin(dTheta / 2), 2) +
                        Math.cos(theta1) * Math.cos(theta2) * Math.pow(Math.sin(dLambda / 2), 2)
        );
        final double c = 2 * Math.atan2(Math.sqrt(haversineValue), Math.sqrt(1 - haversineValue));
        return EARTH_RADIUS * c;
    }

    /**
     * @return These coordinates represented as a Point2D, with longitude on the
     * x-axis and latitude on the y-axis.
     */
    @Override
    public Point2D.Double asPoint() {
        return new Point2D.Double(this.longitude, this.latitude);
    }

    @Override
    public String toString() {
        return "GeographicCoordinates{" +
                "latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
}

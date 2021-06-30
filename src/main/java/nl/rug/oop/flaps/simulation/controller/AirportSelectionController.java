package nl.rug.oop.flaps.simulation.controller;

import nl.rug.oop.flaps.simulation.model.map.coordinates.GeographicCoordinates;
import nl.rug.oop.flaps.simulation.model.map.coordinates.PointProvider;
import nl.rug.oop.flaps.simulation.model.map.coordinates.ProjectionMapping;
import nl.rug.oop.flaps.simulation.model.trips.Trip;
import nl.rug.oop.flaps.simulation.model.world.World;
import nl.rug.oop.flaps.simulation.view.panels.WorldPanel;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;

/**
 * Listens to mouse events on the world map
 * Used for selecting airports
 *
 * @author T.O.W.E.R.
 */
public class AirportSelectionController extends MouseAdapter {
    private static final int SELECTION_TOLERANCE = 1000;
    private static final int RADIUS  = 8;
    private final World world;


    public AirportSelectionController(World world) {
        this.world = world;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        SwingUtilities.invokeLater(() -> {
            var geo = ProjectionMapping.worldToMercator(this.world.getDimensions())
                    .map(PointProvider.ofPoint(new Point2D.Double(e.getX(), e.getY())));
            var list = WorldPanel.getWorldPanel().getCurrentTrips();
            if (list != null) {
                for (Trip trip : list) {
                    if ((new Point2D.Double(e.getX(), e.getY())).distance(trip.getCurrentPosition()) < RADIUS) {
                        this.world.getSelectionModel().setSelectedTrip(trip);
                        return;
                    }
                }
            }

            var airport = this.world.getNearestAirport(new GeographicCoordinates(geo.getPointX(), geo.getPointY()), SELECTION_TOLERANCE);
            if (airport.isEmpty()) {
                this.world.getSelectionModel().setSelectedAirport(null);
                this.world.getSelectionModel().setSelectedAircraft(null);
            }
            airport.ifPresent(a -> {
                if (this.world.getSelectionModel().isSelectingDestination()) {
                    this.world.getSelectionModel().setSelectedDestinationAirport(a);
                } else {
                    this.world.getSelectionModel().setSelectedAirport(a);
                }
            });
        });
    }

    @Override
    public void mouseExited(MouseEvent e) {
        SwingUtilities.invokeLater(() -> {
            if(this.world.getSelectionModel().isSelectingDestination()) {
                var airportCoords = ProjectionMapping.mercatorToWorld(this.world.getDimensions())
                        .map(this.world.getSelectionModel().getSelectedAirport().getLocation());
                this.world.getSelectionModel().setDestinationSelectionCursorPosition((int) airportCoords.getPointX(), (int) airportCoords.getPointY());
            }
        });
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        SwingUtilities.invokeLater(() -> {
            if(this.world.getSelectionModel().isSelectingDestination()) {
                this.world.getSelectionModel().setDestinationSelectionCursorPosition(e.getX(), e.getY());
            }
        });
    }
}

package nl.rug.oop.flaps.simulation.view.panels;

import lombok.extern.java.Log;
import nl.rug.oop.flaps.simulation.controller.AirportSelectionController;
import nl.rug.oop.flaps.simulation.model.airport.Airport;
import nl.rug.oop.flaps.simulation.model.world.World;
import nl.rug.oop.flaps.simulation.model.world.WorldSelectionModelListener;
import nl.rug.oop.flaps.simulation.model.map.coordinates.GeographicCoordinates;
import nl.rug.oop.flaps.simulation.model.map.coordinates.PointProvider;
import nl.rug.oop.flaps.simulation.model.map.coordinates.ProjectionMapping;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Path;

/**
 * Displays the world map and the airport indicators
 *
 * @author T.O.W.E.R.
 */
@Log
public class WorldPanel extends JPanel implements WorldSelectionModelListener {
    public static final double INDICATOR_SIZE = 8;

    private final BufferedImage worldMapImage;
    private final World world;

    private Image cachedWorldMapImage;

    public WorldPanel(World world) {
        this.world = world;
        try {
            worldMapImage = ImageIO.read(Path.of("images", "map", "world_map_satellite.jpg").toFile());
        } catch (IOException e) {
            log.severe("Could not load world map image.");
            throw new IllegalStateException(e);
        }
        AirportSelectionController selectionController = new AirportSelectionController(world);
        addMouseMotionListener(selectionController);
        addMouseListener(selectionController);
        this.world.getSelectionModel().addListener(this);
    }

    private void drawAirportIndicator(Graphics2D g, Airport airport) {
        double s = INDICATOR_SIZE;
        Color c = Color.RED;
        var sm = this.world.getSelectionModel();
        if (sm.getSelectedAirport() != null && sm.getSelectedAirport().equals(airport)) {
            c = Color.CYAN;
            s *= 2;
        } else if (sm.getSelectedDestinationAirport() != null && sm.getSelectedDestinationAirport().equals(airport)) {
            c = Color.GREEN;
            s *= 1.5;
        }
        var p = ProjectionMapping.mercatorToWorld(this.world.getDimensions())
                .map(airport.getGeographicCoordinates()).asPoint();
        g.setColor(c);
        Shape marker = new Ellipse2D.Double(p.x - s/2, p.y - s/2, s, s);
        g.fill(marker);
    }

    private void drawTrajectory(Graphics2D g) {
        g.setColor(Color.WHITE);
        var sm = this.world.getSelectionModel();
        var start = ProjectionMapping.mercatorToWorld(this.world.getDimensions())
                .map(sm.getSelectedAirport().getLocation());
        var end = new Point2D.Double(sm.getDestinationSelectionCursorX(), sm.getDestinationSelectionCursorY());
        var endM = ProjectionMapping.worldToMercator(this.world.getDimensions())
                .map(PointProvider.ofPoint(end));
        var endGeo = new GeographicCoordinates(endM.getPointX(), endM.getPointY());
        double distance = sm.getSelectedAirport().getLocation().distanceTo(endGeo);
        Color c = distance / 1000 > sm.getSelectedAircraft().getType().getRange() ? Color.RED : Color.WHITE;
        g.setColor(c);
        Line2D.Double line = new Line2D.Double(start.getPointX(), start.getPointY(), end.x, end.y);
        g.draw(line);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(Color.WHITE);
        g2d.setStroke(new BasicStroke(3));
        if (this.cachedWorldMapImage == null) { // Use cached world map image to avoid expensive scaling with each repaint.
            this.cachedWorldMapImage = this.worldMapImage.getScaledInstance(
                    this.world.getDimensions().getMapWidth(),
                    this.world.getDimensions().getMapHeight(),
                    Image.SCALE_SMOOTH
            );
        }
        g2d.drawImage(this.cachedWorldMapImage, 0, 0, null);
        var sm = this.world.getSelectionModel();
        if (sm.isSelectingDestination()) {
            drawTrajectory(g2d);
        }
        if (sm.getSelectedDestinationAirport() != null && sm.getSelectedAirport() != null && sm.getSelectedAircraft() != null) {
            drawPlannedRoute(g2d, sm.getSelectedAirport(), sm.getSelectedDestinationAirport());
        }
        this.world.getAirports().values().forEach(airport -> drawAirportIndicator(g2d, airport));
    }

    private void drawPlannedRoute(Graphics2D g, Airport selectedAirport, Airport selectedDestinationAirport) {
        var projectionMapping = ProjectionMapping.mercatorToWorld(this.world.getDimensions());
        var start = projectionMapping.map(selectedAirport.getGeographicCoordinates()).asPoint();
        var end = projectionMapping.map(selectedDestinationAirport.getGeographicCoordinates()).asPoint();
        g.setColor(Color.WHITE);
        g.draw(new Line2D.Double(start, end));
    }

    @Override
    public void airportSelected(Airport selectedAirport) {
        this.repaint();
    }

    @Override
    public void destinationAirportSelected(Airport destinationAirport) {
        this.repaint();
    }

    @Override
    public void destinationSelectionUpdated() {
        this.repaint();
    }
}

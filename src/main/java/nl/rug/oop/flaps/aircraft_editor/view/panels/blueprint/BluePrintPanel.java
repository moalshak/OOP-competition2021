package nl.rug.oop.flaps.aircraft_editor.view.panels.blueprint;

import lombok.Getter;
import lombok.extern.java.Log;
import nl.rug.oop.flaps.aircraft_editor.controller.listeners.blueprint_listeners.PointSelectionListener;
import nl.rug.oop.flaps.aircraft_editor.model.blueprint.BluePrintModel;
import nl.rug.oop.flaps.aircraft_editor.model.blueprint.BluePrintModelListener;
import nl.rug.oop.flaps.aircraft_editor.model.config_models.InfoPanelModelListener;
import nl.rug.oop.flaps.aircraft_editor.view.frame.EditorFrame;
import nl.rug.oop.flaps.aircraft_editor.view.panels.aircraft_info.interaction_panels.InteractionPanel;
import nl.rug.oop.flaps.simulation.model.aircraft.Aircraft;
import nl.rug.oop.flaps.simulation.model.aircraft.CargoArea;
import nl.rug.oop.flaps.simulation.model.aircraft.FuelTank;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

/**
 * this class displays the blue print panel
 * */
@Log
public class BluePrintPanel extends JPanel implements BluePrintModelListener, InfoPanelModelListener {
    @Getter
    private static final int INDICATOR_SIZE = 33;
    @Getter
    private final BluePrintModel model;
    @Getter
    private static final int WIDTH = EditorFrame.getWIDTH()/2;
    private static final int HEIGHT = EditorFrame.getHEIGHT();

    @Getter
    private static final Point2D originOfBluePrint = new Point2D.Double(0 , 100);

    @Getter
    private final JScrollPane infoScrollPane;

    @Getter
    private final JScrollPane bluePrintScrollPane;

    @Getter
    private static JPanel bluePrintPanel;

    private Graphics2D g2d;
    private static final int KEY_WIDTH = 200, KEY_HEIGHT = 250;

    public BluePrintPanel(Aircraft aircraft) {
        // make blue print model
        model = new BluePrintModel(aircraft, this);
        // add listener for the dots
        model.addListener(this);
        this.addMouseListener(new PointSelectionListener(model));
        setPreferredSize(new Dimension(WIDTH, (int) (HEIGHT-originOfBluePrint.getY())));

        /* we have two parts the blueprint part and the info about aircraft part */
        bluePrintScrollPane = new JScrollPane(this);
        JPanel infoPanel = new JPanel();
        infoScrollPane = new JScrollPane(infoPanel);

        bluePrintPanel = this;

        this.setLayout(new BorderLayout());
        this.setToolTipText("");
    }

    /**
     *  draws the blueprint key. The key is pre-made with photoshop, any new icon should be added in the
     *  <b>.psd</b> file.
     * */
    private void drawBlueprintKey() {
        try {
            Image keyImage = ImageIO.read(Path.of("data", "blueprintKeyDark.PNG").toFile());
            keyImage = keyImage.getScaledInstance(KEY_WIDTH, KEY_HEIGHT, Image.SCALE_SMOOTH);
            g2d.drawImage(keyImage, 0, 0, null);
        } catch (IOException e) {
            log.info("Key image not found in data folder... not drawing it..");
        }
    }

    /**
     * paints the components (icons,cg..) of the blueprint
     * */
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g2d = (Graphics2D) g;
        g2d.setColor(Color.WHITE);

        drawBluePrint(g2d);

        /* draw each point  */
        for(Point2D point : model.getFuelTanksPos()) {
            drawIndicators(g2d, point, Color.RED, "fuelTank");
        }

        for(Point2D point : model.getCargoPos()) {
            drawIndicators(g2d, point, Color.BLUE, "cargoArea");
        }

        drawEntry();
        drawCg();
        drawBlueprintKey();
    }

    /**
     * draws the center of gravity on top of the blueprint
     * */
    private void drawCg() {
        double x =  BluePrintModel.getCenterOfGravity().getX();
        double y =  BluePrintModel.getCenterOfGravity().getY();
        drawIcons(g2d, "cg", INDICATOR_SIZE, (int) x, (int) y);
    }

    /**
     * draws the entrance of the passengers
     * */
    private void drawEntry() {
        double s = INDICATOR_SIZE;
        Point2D selectedPoint = model.getSelectedPoint();
        double x = model.getPassengersEntrance().getX();
        double y = model.getPassengersEntrance().getY();
        if(selectedPoint!= null && selectedPoint.equals(model.getPassengersEntrance())) {
            s *= 1.6;
            /* the position of the dot changes when it gets bigger */
            x -= s/15;
            y -= s/10;
        }
        drawIcons(g2d, "entry", (int) s, (int) x, (int) y);
    }
    /**
     * draws the indicators of the fuel tanks and the cargo areas on top of the blueprint
     *
     * @param g the graphic of the {@link Panel} see {@link Graphics}
     * @param coords the coordinates to paint the indicator
     * @param color the color to paint the point in
     * @param type the type of the indicator
     * */
    private void drawIndicators(Graphics2D g, Point2D coords, Color color, String type) {
        double s = INDICATOR_SIZE;
        Point2D selectedPoint = model.getSelectedPoint();
        double x = coords.getX();
        double y = coords.getY();
        /* if a point is selected draw its properties */
        if (selectedPoint != null && selectedPoint.equals(coords)) {
            g.setColor(Color.CYAN);
            s *= 1.6;
            /* the position of the dot changes when it gets bigger */
            x -= s/15;
            y -= s/10;
        } else {
            g.setColor(color);
        }

        drawIcons(g, type, (int) s, (int) x, (int) y);
    }

    /**
     * retrieves the icons needed for the indicators and draws them
     *
     * @param g the graphic of the {@link Panel} see {@link Graphics}
     * @param s the size of the indicator
     * @param x the x coordinate of the indicator
     * @param y the y coordinate of the indicator
     * @param type the type of the indicator
     * */
    private void drawIcons(Graphics2D g, String type, int s, int x, int y) {
        try {
            switch (type) {
                case "fuelTank":
                    Image fuelIcon = ImageIO.read(Path.of("data/fuel", "fuelTankIcon3.png").toFile());
                    fuelIcon = fuelIcon.getScaledInstance(s, s, Image.SCALE_SMOOTH);
                    g.drawImage(fuelIcon, x, y, null);
                    break;
                case "entry": {
                    Image cgIcon = ImageIO.read(Path.of("data", "entry.png").toFile());
                    cgIcon = cgIcon.getScaledInstance(s, s, Image.SCALE_SMOOTH);
                    g.drawImage(cgIcon, x, y, null);
                    break;
                }
                case "cg": {
                    Image cgIcon = ImageIO.read(Path.of("data", "cg.png").toFile());
                    cgIcon = cgIcon.getScaledInstance(INDICATOR_SIZE, INDICATOR_SIZE, Image.SCALE_SMOOTH);
                    g2d.drawImage(cgIcon, x, y, null);
                    break;
                }
                case "cargoArea" :
                    Image cargoIcon = ImageIO.read(Path.of("data/cargo", "cargoIcon.png").toFile());
                    cargoIcon = cargoIcon.getScaledInstance(s, s, Image.SCALE_SMOOTH);
                    g.drawImage(cargoIcon, x, y, null);
                    break;
                default:
                    break;
            }
        } catch (IOException e) {
            if (type.equals("entry")) {
                log.info("No icon for cg");
                Shape marker = new Ellipse2D.Double((x),(y),INDICATOR_SIZE,INDICATOR_SIZE);
                g.setColor(Color.GREEN);
                g.fill(marker);
            } else if (type.equals("cg")) {
                log.info("No icon for cg");
                Shape marker = new Ellipse2D.Double((x),(y),INDICATOR_SIZE,INDICATOR_SIZE);
                g2d.setColor(Color.BLUE);
                g2d.fill(marker);
            } else {
                log.info("Oil or Cargo icons not found in data folder...replacing with dots..");
                Shape marker = new Ellipse2D.Double(x, y, s, s);
                g.fill(marker);
            }
        }
    }

    /**
     * draws the image of the blueprint with the origin being the point <b>originOfBluePrint</b>
     * */
    private void drawBluePrint(Graphics2D g2d) {
        g2d.drawImage(model.getBluePrintImage(), (int)originOfBluePrint.getX(), (int)originOfBluePrint.getY(), null);
    }

    /**
     * @param event the area mouse hovers over
     * @return the tooltip text that is to be displayed
     */
    @Override
    public String getToolTipText(MouseEvent event) {
        ToolTipManager.sharedInstance().setInitialDelay(250); //setting delay to display toolTipText
        //checking mouse is at FuelTank
        List<Point2D> fuelTankPos = model.getFuelTanksPos();
        for (Point2D point : fuelTankPos) {
            if (event.getPoint().distance(point) < INDICATOR_SIZE) {
                FuelTank fuelTank = model.getAircraft().getType().getFuelTanks().get(fuelTankPos.indexOf(point));
                return model.getHoverAreaText(fuelTank);
            }
        }
        //checking if mouse is at CargoArea
        List<Point2D> cargoPos = model.getCargoPos();
        for (Point2D point : cargoPos) {
            if (event.getPoint().distance(point) < INDICATOR_SIZE) {
                CargoArea cargoArea = model.getAircraft().getType().getCargoAreas().get(cargoPos.indexOf(point));
                return model.getHoverAreaText(cargoArea);
            }
        }
        //checking if mouse is at Passengers
        Point2D entrancePos = model.getPassengersEntrance();
        if (event.getPoint().distance(entrancePos) < INDICATOR_SIZE) {
            return model.getHoverAreaText(InteractionPanel.getPassengersModel());
        }
        return null;
    }

    @Override
    public void pointSelectedUpdater() {
        repaint();
    }

    @Override
    public void updateConfig() {
        model.updateInfo();
        repaint();
    }

}

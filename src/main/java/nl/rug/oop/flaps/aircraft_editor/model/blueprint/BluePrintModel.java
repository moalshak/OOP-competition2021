package nl.rug.oop.flaps.aircraft_editor.model.blueprint;

import lombok.Getter;
import lombok.Setter;
import nl.rug.oop.flaps.aircraft_editor.model.config_models.aircraft.GravityCenterModel;
import nl.rug.oop.flaps.aircraft_editor.model.config_models.passenger.PassengersModel;
import nl.rug.oop.flaps.aircraft_editor.view.panels.blueprint.BluePrintPanel;
import nl.rug.oop.flaps.simulation.model.aircraft.Aircraft;
import nl.rug.oop.flaps.simulation.model.aircraft.CargoArea;
import nl.rug.oop.flaps.simulation.model.aircraft.FuelTank;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * this class holds the data necessary for the {@link BluePrintPanel}
 * */

@Getter
public class BluePrintModel {
    private final int spacing = 10;
    private final int WIDTH = 800-spacing, HEIGHT = 600-spacing;

    private Image bluePrintImage;
    private final Aircraft aircraft;
    @Getter @Setter
    private Point2D selectedPoint;
    @Getter
    public static Point2D selectedPointS;
    @Getter
    private final List<Point2D> fuelTanksPos = new ArrayList<>();
    @Getter
    private final List<Point2D> cargoPos = new ArrayList<>();
    @Getter @Setter
    private static Point2D centerOfGravity = new Point2D.Double();
    @Getter @Setter
    private static Point2D realCG = new Point2D.Double();
    @Getter @Setter
    private Point2D passengersEntrance = new Point2D.Double();

    private double scale, indicatorOffset, offsetX, offsetY;
    private final Set<BluePrintModelListener> listeners;

    public static final int NOTHING = 0;

    public static final int FUEL = 1;
    private FuelTank selectedTank = null;

    public static final int CARGO = 2;
    private CargoArea selectedCargo = null;

    public static final int ENTRY = 3;

    @Setter
    private int selectedCompartment = NOTHING;

    @Getter
    private final BluePrintPanel bluePrintPanel;

    public BluePrintModel(Aircraft aircraft, BluePrintPanel bluePrintPanel) {
        this.bluePrintPanel = bluePrintPanel;
        this.aircraft = aircraft;
        listeners = new HashSet<>();


        setBluePrintImage();
        setPoints();
        setCenterOfGravity();

        PassengersModel.typeMapper(this);
        this.passengersEntrance = setOnBluePrint(passengersEntrance);
        System.out.println(passengersEntrance);
    }
    /**
     * sets the center of gravity in the {@link GravityCenterModel}
     * and updates the Info
     * */
    private void setCenterOfGravity() {
        GravityCenterModel.setAircraft(aircraft);
        updateInfo();
    }

    /**
     * assigns the x and y coordinates to the points to be displayed
     * */
    private void setPoints() {
        List<FuelTank> fuelTankList = aircraft.getType().getFuelTanks();
        List<CargoArea> cargoAreaList = aircraft.getType().getCargoAreas();
        calcScale();
        setCargoPos(cargoAreaList);
        setFuelTanksPos(fuelTankList);
    }

    /**
     * calculates the scale of the pic. Which is dependent on the height and the length of the airplane
     * */
    private void calcScale() {
        double picWidth = getBluePrintImage().getWidth(null);
        double picHeight = getBluePrintImage().getHeight(null);
        double airPlaneLength = aircraft.getType().getLength();

        // calculate scale based on the picture height in relation to the airplane length
        scale =  picHeight / airPlaneLength;
        // compensate for the top left corner position
        Point2D paintingOrigin = BluePrintPanel.getOriginOfBluePrint();
        indicatorOffset = BluePrintPanel.getINDICATOR_SIZE() / 2.0;
        /* to paint the dots correctly we need to account on where the origin is of the painting */
        offsetX =  picWidth / 2 + paintingOrigin.getX();
        offsetY = paintingOrigin.getY();
    }

    /**
     * sets the coordinates of the fuel tanks
     * */
    private void setFuelTanksPos(List<FuelTank> fuelTankList) {
        for(FuelTank tank : fuelTankList) {
            Point2D point = setOnBluePrint(tank.getCoords());
            this.fuelTanksPos.add(point);
        }
    }

    /**
     * sets the coordinates of the cargo
     * */
    private void setCargoPos(List<CargoArea> cargoAreaList) {
        for(CargoArea cargo : cargoAreaList) {
            Point2D point = setOnBluePrint(cargo.getCoords());
            this.cargoPos.add(point);
        }
    }

    /**
     * modifies the points up to the scale of the blueprint
     * @param point the point that needs to be mapped on top of the {@link BluePrintPanel}
     * @return the point with pixel coordinates (ready for drawing)
     * */
    private Point2D setOnBluePrint(Point2D point) {
        /* I am making a copy because later on we might need the original point coordinates in meters */
        Point2D temp = new Point2D.Double(point.getX(), point.getY());

        // make x and y in pixels and also getting rid of the offset
        double x = point.getX() * scale - indicatorOffset + offsetX ; /* starting x index is in the middle*/
        double y = point.getY() * scale - indicatorOffset + offsetY;

        temp.setLocation(x, y);
        return temp;
    }

    /**
     * sets the blueprint image in the field bluePrintImage
     * */
    private void setBluePrintImage() {
        this.bluePrintImage = aircraft.getType().getBlueprintImage().getScaledInstance(WIDTH, HEIGHT, Image.SCALE_SMOOTH);
    }

    /**
     * helper for method {@link BluePrintModel#pointIsSelected(Point2D, int, Point2D)} (Point2D, int)}
     * here we pass all indicators we have through {@link BluePrintModel#pointIsSelected(Point2D, int, Point2D)} to check
     * if that point is selected.
     * */
    public void selectPointByCoords(Point2D clickPosition, int radius) {
        if (pointIsSelected(clickPosition, radius, fuelTanksPos)) {
            selectedCompartment = FUEL;
            selectedTank = aircraft.getType().getFuelTanks().get(fuelTanksPos.indexOf(selectedPoint));

        } else if (pointIsSelected(clickPosition, radius, cargoPos)) {
            selectedCompartment = CARGO;
            /* This is what is needed to display (except current weight) but for now in console*/
            selectedCargo = aircraft.getType().getCargoAreas().get(cargoPos.indexOf(selectedPoint));

        } else if (pointIsSelected(clickPosition, radius, getPassengersEntrance())) {
            selectedCompartment = ENTRY;
        }
        else {
            selectedCompartment = NOTHING;
            selectedPoint = null;
        }
    }

    /**
     * method that checks if a point is selected.
     *
     * @param clickPosition the position clicked on screen
     * @param radius the radius of the painted indicators
     * @param point the position of an indicator
     *
     * @return a boolean (point is either selected or not)
     * */
    private boolean pointIsSelected(Point2D clickPosition, int radius, Point2D point) {
        if (point.distance(clickPosition) < radius) {
            selectedPoint = point;
            selectedPointS = point;
            return true;
        }
        return false;
    }

    /**
     * This one is made for {@link BluePrintModel#cargoPos}
     * sets the selected point in the field {@link BluePrintModel#selectedPoint}
     *
     * @param clickPosition the position clicked on screen
     * @param radius the radius of the painted indicators
     * @param positions the positions in the cargo list
     *
     * @return boolean (a cargo is selected or not)
     * */
    private boolean pointIsSelected(Point2D clickPosition, int radius, List<Point2D> positions) {
        for (Point2D point : positions) {
            if (point.distance(clickPosition) < radius) {
                selectedPoint = point;
                return true;
            }
        }
        return false;
    }

    /**
     * initialises the tool tip text look for the area the mouse hovers over
     *
     * @param hoverArea the object that the mouse is hovering over
     * @return tooltip text for the hoverArea
     */
    public String getHoverAreaText(Object hoverArea) {
        StringBuilder displayText;
        int percentage;

        if (hoverArea instanceof FuelTank) {
            FuelTank hoveredTank = (FuelTank) hoverArea;
            percentage = (int) Math.round(50.0 * aircraft.getFuelAmountForFuelTank(hoveredTank) / hoveredTank.getCapacity());
            displayText = new StringBuilder(hoveredTank.getName() + " Capacity: \n");
        } else if (hoverArea instanceof CargoArea) {
            CargoArea hoveredCargoArea = (CargoArea) hoverArea;
            percentage = (int) Math.round(50.0 * aircraft.getCargoAreaWeight(hoveredCargoArea) / hoveredCargoArea.getMaxWeight());
            displayText = new StringBuilder(hoveredCargoArea.getName() + " Capacity: \n");
        } else {
            PassengersModel hoveredEntry = (PassengersModel) hoverArea;
            percentage = (int) Math.round(50.0 * hoveredEntry.getPassengersSum() / PassengersModel.getNrOfSeats());
            displayText = new StringBuilder("Passenger" + " Capacity: \n");
        }
        displayText.append("█".repeat(Math.max(0, percentage)));
        displayText.append("░".repeat(Math.max(0, 50 - percentage)));
        displayText.append(" ").append(percentage * 2).append("%");
        return String.valueOf(displayText);
    }

    /**
     * uses the {@link GravityCenterModel} to calculate the new position
     * of the center of gravity then maps it on top of the blueprint
     * */
    public void updateInfo() {
        GravityCenterModel.calcCenterOfGravity();
        centerOfGravity = setOnBluePrint(centerOfGravity);
    }

    /**
     * adds a listener
     *
     * @param listener the listener to be added
     * */
    public void addListener(BluePrintModelListener listener) {
        listeners.add(listener);
    }

    /**
     * @return the set of listeners
     * */
    public Set<BluePrintModelListener> getListeners() {
        return listeners;
    }
}

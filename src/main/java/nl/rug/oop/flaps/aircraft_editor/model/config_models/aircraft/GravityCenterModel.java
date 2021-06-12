package nl.rug.oop.flaps.aircraft_editor.model.config_models.aircraft;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.java.Log;
import nl.rug.oop.flaps.aircraft_editor.controller.listeners.infopanel_listeners.ConfirmChangeButtonListener;
import nl.rug.oop.flaps.aircraft_editor.model.blueprint.BluePrintModel;
import nl.rug.oop.flaps.aircraft_editor.model.config_models.InfoPanelModel;
import nl.rug.oop.flaps.simulation.model.aircraft.Aircraft;
import nl.rug.oop.flaps.simulation.model.aircraft.CargoArea;
import nl.rug.oop.flaps.simulation.model.aircraft.FuelTank;

import java.awt.geom.Point2D;

/**
 * This class calculates the center of gravity of the aircraft with cargo and fuel
 * This class updates each time the {@link ConfirmChangeButtonListener} is in action.
 *
 * <b> The center of gravity is taken care of by the {@link BluePrintModel} because the cg needs to be put on the blueprint  </b>
 * */
@Log
public class GravityCenterModel {
    @Setter
    private static Aircraft aircraft;

    @Getter
    private static Point2D cg;

    @Getter
    private static boolean AircraftOffCenter;

    /**
     * calculates the center of gravity and sets value in {@link InfoPanelModel}
     * also save the real center of gravity to the user
     * */
    public static void calcCenterOfGravity() {
        double x = 0.0;
        double y = 0.0;
        double totalWeight = 0.0;

        x += aircraft.getType().getEmptyWeight() * aircraft.getType().getEmptyCgX();
        y += aircraft.getType().getEmptyWeight() * aircraft.getType().getEmptyCgY();
        totalWeight += aircraft.getType().getEmptyWeight();

        /* cg for cargo areas */
        for(CargoArea cargo : aircraft.getCargoAreaContents().keySet()) {
            x += aircraft.getCargoAreaWeight(cargo) * cargo.getX();
            y += aircraft.getCargoAreaWeight(cargo) * cargo.getY();
        }
        totalWeight += aircraft.getTotalCargoWeight();

        /* cg for fuel tanks*/
        for(FuelTank tank : aircraft.getType().getFuelTanks()) {
            if(aircraft.getFuelTankFillStatuses().get(tank) != null) {
                x += (aircraft.getFuelTankFillStatuses().get(tank) * aircraft.getType().getFuelType().getDensity()) * tank.getX();
                y += (aircraft.getFuelTankFillStatuses().get(tank) * aircraft.getType().getFuelType().getDensity()) * tank.getY();
            }
        }
        totalWeight += aircraft.getTotalFuel();

        x = x / totalWeight;
        y = y / totalWeight;
        cg = new Point2D.Double(x, y);


        double tolerance = aircraft.getType().getCgTolerance();

        double x1, y1;
        x1 = aircraft.getType().getEmptyCgX(); y1 = aircraft.getType().getEmptyCgY();

        double offset = Math.sqrt( (Math.pow(x1- cg.getX(), 2) + Math.pow(y1 - cg.getY(), 2)) ) /
                aircraft.getType().getLength();
        AircraftOffCenter = offset > tolerance;

        BluePrintModel.setRealCG(cg);
        BluePrintModel.setCenterOfGravity(cg);
    }

}

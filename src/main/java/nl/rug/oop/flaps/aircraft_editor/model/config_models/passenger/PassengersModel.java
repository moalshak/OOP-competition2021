package nl.rug.oop.flaps.aircraft_editor.model.config_models.passenger;

import lombok.Getter;
import lombok.Setter;
import nl.rug.oop.flaps.aircraft_editor.controller.listeners.infopanel_listeners.ConfirmChangeButtonListener;
import nl.rug.oop.flaps.aircraft_editor.model.blueprint.BluePrintModel;
import nl.rug.oop.flaps.aircraft_editor.view.panels.aircraft_info.interaction_panels.PassengersConfigPanel;
import nl.rug.oop.flaps.simulation.model.aircraft.AircraftType;
import nl.rug.oop.flaps.simulation.model.world.WorldSelectionModel;

import java.awt.geom.Point2D;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * this class holds information needed for the aircraft
 *  this model is used by {@link PassengersConfigPanel} and updates through {@link javax.swing.JSpinner} and {@link ConfirmChangeButtonListener}.
 * */
@Getter
public class PassengersModel {
    private static int crewOnBoard = 0;
    private final static int avgAdultWeight = 83;
    private final static int avgKidTo12Weight = 42;
    private final static int avgKidUnder12Weight = 22;
    @Setter
    private PassengersConfigPanel panel;

    @Getter @Setter
    Set<PassengersModelListener> listeners = new HashSet<>();

    @Getter
    private static int passengersWeight;
    @Getter @Setter
    private int passengersSum;

    private final Point2D entrance = new Point2D.Double();
    @Getter
    private static int nrOfSeats;

    @Setter
    private HashMap<String, Integer> passengers;

    private final WorldSelectionModel selectionModel;

    public PassengersModel(WorldSelectionModel selectionModel) {
        this.selectionModel = selectionModel;
        passengers = new HashMap<>();
        passengers.put("adults", 0);
        passengers.put("kidsTo12", 0);
        passengers.put("kidsUnder12", 0);
        selectionModel.getSelectedAircraft().setPassengers(passengers);
    }

    /**
     * Maps particular properties to the aircraft and sets that in the {@link BluePrintModel}
     * @param bluePrintModel the blueprintModel for the particular aircraft
     * */
    public static void typeMapper(BluePrintModel bluePrintModel) {
        AircraftType type = bluePrintModel.getAircraft().getType();
        Point2D geo = new Point2D.Double();

        if(type.getName().equals("Boeing 747-400F")) {
            geo.setLocation(4, 18);
            nrOfSeats = 366;
            crewOnBoard = (int)(Math.random()*(10-2+1)+2);
        } else if (type.getName().equals("Boeing 737-800BCF Freighter")) {
            geo.setLocation(3, 10);
            nrOfSeats = 120;
            crewOnBoard = (int)(Math.random()*(5-2+1)+2);
        } else {
            geo.setLocation(0.5, 2.1);
            nrOfSeats = 9;
            crewOnBoard = 2;
        }
        bluePrintModel.setPassengersEntrance(geo);
    }

    /**
     * updates the {@link PassengersModel#passengersSum} so the value in the hashmap {@link PassengersModel#passengers}
     * are up to date with the latest user configuration through the {@link javax.swing.JSpinner} in {@link PassengersConfigPanel}
     * */
    public void updatePassengersSum() {
        this.passengersSum = 0;
        this.passengersSum += (int) panel.getAdults().getValue();
        this.passengersSum += (int) panel.getKidsTo12().getValue();
        this.passengersSum += (int) panel.getKidsUnder12().getValue();
    }

    /**
     * seats passengers in the aircraft aka saves the values gotten from the {@link javax.swing.JSpinner} in {@link PassengersConfigPanel}
     * to the map {@link PassengersModel#passengers} .
     * */
    public void seatPassengers() {
        passengers.put("adults", (int) panel.getAdults().getValue());
        passengers.put("kidsTo12", (int) panel.getKidsTo12().getValue());
        passengers.put("kidsUnder12", (int) panel.getKidsUnder12().getValue());
        selectionModel.getSelectedAircraft().setPassengers(passengers);
    }

    /**
     * sets the passengers weight in the {@link PassengersModel#passengersWeight} with addition to the {@link PassengersModel#crewOnBoard}
     *
     * */
    public void setPassengersWeight() {
        passengersWeight = 0;
        passengersWeight += ( (passengers.get("adults") + crewOnBoard) * avgAdultWeight );
        passengersWeight += ( passengers.get("kidsTo12") * avgKidTo12Weight );
        passengersWeight += (passengers.get("kidsUnder12") * avgKidUnder12Weight);
    }

    public void updatePassengerSumPassenger() {
        passengersSum = 0;
        passengersSum += (passengers.get("adults"));
        passengersSum += (passengers.get("kidsTo12"));
        passengersSum += (passengers.get("kidsUnder12"));
    }

    /**
     * escorts the passengers from the aircraft by resetting values in {@link PassengersModel#passengersWeight} and
     * {@link PassengersModel#passengersSum} and resetting all values in {@link PassengersModel#passengers}
     * */
    public void escortPassengers() {
        passengersWeight = 0;
        passengersSum = 0;
        passengers.put("adults", 0);
        passengers.put("kidsTo12", 0);
        passengers.put("kidsUnder12", 0);
        selectionModel.getSelectedAircraft().setPassengers(passengers);
    }

    /**
     * Adds a listener to the set {@link PassengersModel#listeners}
     * */
    public void addListener(PassengersModelListener listener) {
        listeners.add(listener);
    }
}

package nl.rug.oop.flaps.aircraft_editor.model.config_models.passenger;

import lombok.Getter;
import lombok.Setter;
import nl.rug.oop.flaps.aircraft_editor.controller.listeners.infopanel_listeners.ConfirmChangeButtonListener;
import nl.rug.oop.flaps.aircraft_editor.view.panels.aircraft_info.interaction_panels.PassengersConfigPanel;
import nl.rug.oop.flaps.simulation.model.trips.Trip;
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
    @Getter
    private int ticketAdults;
    @Getter
    private int ticketKids12;
    @Getter
    private int ticketKids;

    @Setter
    private PassengersConfigPanel panel;

    @Getter @Setter
    Set<PassengersModelListener> listeners = new HashSet<>();

    @Getter
    private static int passengersWeight;
    @Getter @Setter
    private int passengersSum;

    @Getter
    private static int nrOfSeats;

    @Setter
    private HashMap<String, Integer> passengers;

    private final WorldSelectionModel selectionModel;

    public PassengersModel(WorldSelectionModel selectionModel) {
        this.selectionModel = selectionModel;
        passengers = selectionModel.getSelectedAircraft().getPassengers();
        nrOfSeats = selectionModel.getSelectedAircraft().getType().getNrOfSeats();
        crewOnBoard = selectionModel.getSelectedAircraft().getCrewOnBoard();

        if (selectionModel.getSelectedAircraft().getTicketAdults() == 0) {
            ticketAdults = (int) (Math.random()*(900-300+1)+300);
            ticketKids12 = (int) (Math.random()*(300-200+1)+200);
            ticketKids = (int) (Math.random()*(200-50+1)+50);
            selectionModel.getSelectedAircraft().setTicketAdults(ticketAdults);
            selectionModel.getSelectedAircraft().setTicketKids12(ticketKids12);
            selectionModel.getSelectedAircraft().setTicketKids(ticketKids);
        } else {
            ticketAdults = selectionModel.getSelectedAircraft().getTicketAdults();
            ticketKids12 = selectionModel.getSelectedAircraft().getTicketKids12();
            ticketKids = selectionModel.getSelectedAircraft().getTicketKids();
        }
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
        selectionModel.getSelectedAircraft().setPassengersWeight(passengersWeight);
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
    public void escortPassengers(Trip trip) {
        passengersWeight = 0;
        passengersSum = 0;
        passengers.put("adults", 0);
        passengers.put("kidsTo12", 0);
        passengers.put("kidsUnder12", 0);
        trip.getAircraft().setPassengers(passengers);
        trip.getAircraft().setCrewOnBoard(crewOnBoard);
        trip.getAircraft().setTicketAdults(0);
        trip.getAircraft().setTicketKids12(0);
        trip.getAircraft().setTicketKids(0);
    }

    /**
     * Adds a listener to the set {@link PassengersModel#listeners}
     * */
    public void addListener(PassengersModelListener listener) {
        listeners.add(listener);
    }
}

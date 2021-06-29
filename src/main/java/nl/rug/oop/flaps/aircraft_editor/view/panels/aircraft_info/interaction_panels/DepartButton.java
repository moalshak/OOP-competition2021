package nl.rug.oop.flaps.aircraft_editor.view.panels.aircraft_info.interaction_panels;

import nl.rug.oop.flaps.aircraft_editor.controller.listeners.infopanel_listeners.DepartButtonListener;
import nl.rug.oop.flaps.aircraft_editor.model.config_models.aircraft.GravityCenterModel;
import nl.rug.oop.flaps.aircraft_editor.model.config_models.InfoPanelModel;
import nl.rug.oop.flaps.aircraft_editor.view.panels.aircraft_info.InfoPanel;
import nl.rug.oop.flaps.simulation.model.world.WorldSelectionModel;

import javax.swing.*;

/**
 * This class holds the button for departure info and its actions. The depart button can hold text it self.
 * Which looks cleaner than and more user friendly than a pop up message.
 *
 * */

public class DepartButton extends JButton {
    private final WorldSelectionModel selectionModel;

    private static final String NO_DESTINATION = "Cannot depart: No destination airport selected...";
    private static final String NO_SPACE = "Cannot depart: Destination airport can not accept incoming aircraft at this moment...";
    private static final String NOT_ENOUGH_FUEL = "Cannot depart: fuel amount is not sufficient...";
    private static final String OFF_CENTER = "Cannot depart: Aircraft center of gravity is problematic...";
    private static final String TOO_HEAVY = "Cannot depart: Max takeoff weight exceeded...";

    private final InfoPanelModel model;
    public DepartButton(InfoPanelModel model) {
        this.model = model;
        this.selectionModel = InfoPanel.getWorldSelectionModel();
        makeDepartButton();
    }

    /**
     * Makes the departure button. While making it, the button holds information on weather the aircraft
     * can or cannot depart
     * */
    private void makeDepartButton() {
        new JButton();

        DepartButtonListener listener = new DepartButtonListener(selectionModel);

        /* this is as far as we know is not possible, but you never when a user gets crazy and finds a new glitch... 😐*/
        if (selectionModel.getSelectedDestinationAirport() == null) {
            this.setText(NO_DESTINATION);
            return;
        }

        boolean offCenter = GravityCenterModel.isAircraftOffCenter();
        boolean canAcceptIncomingAircraft = selectionModel.getSelectedDestinationAirport().canAcceptIncomingAircraft();
        boolean sufficientWeight = model.getAircraftWeight() > selectionModel.getSelectedAircraft().getType().getMaxTakeoffWeight();
        boolean notEnoughFuel = model.getAircraftRange() < model.getTripDistance();

        if (offCenter) {
            this.setText(OFF_CENTER);
        } else if (!canAcceptIncomingAircraft) {
            this.setText(NO_SPACE);
        } else if (sufficientWeight) {
            this.setText(TOO_HEAVY + "Max weight = " +
                    (int) selectionModel.getSelectedAircraft().getType().getMaxTakeoffWeight() + " Kg");
        } else if (notEnoughFuel) {
            this.setText(NOT_ENOUGH_FUEL + " Distance = " + (int) model.getTripDistance() + " Km");
        } else {
            this.setText("Depart");
            addActionListener(listener);
        }
    }
}

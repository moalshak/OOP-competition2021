package nl.rug.oop.flaps.aircraft_editor.controller.listeners.blueprint_listeners;

import nl.rug.oop.flaps.aircraft_editor.model.config_models.passenger.PassengersModel;
import nl.rug.oop.flaps.aircraft_editor.model.config_models.passenger.PassengersModelListener;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class PassengersChangeListener implements ChangeListener {
    private final PassengersModel model ;

    public PassengersChangeListener(PassengersModel model) {
        this.model =model;
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        model.updatePassengersSum();
        model.getListeners().forEach(PassengersModelListener::updatePassengersConfiguration);
    }
}

package nl.rug.oop.flaps.aircraft_editor.controller.listeners.infopanel_listeners.cargo;

import nl.rug.oop.flaps.aircraft_editor.view.panels.aircraft_info.interaction_panels.CargoConfigPanel;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 *
 * listens to change in the slider in the {@link CargoConfigPanel}
 * if the value changes the cargoUnitWeightLabel in {@link CargoConfigPanel}
 *
 * */
public class CargoSliderListener implements ChangeListener {
    private final CargoConfigPanel panel;
    public CargoSliderListener(CargoConfigPanel panel) {
        this.panel = panel;
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        if(e.getSource() == panel.getInfoSlider()) {
            panel.updateCargoUnitWeightLabel();
        }
    }
}

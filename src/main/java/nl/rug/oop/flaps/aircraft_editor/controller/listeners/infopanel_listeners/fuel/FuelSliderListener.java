package nl.rug.oop.flaps.aircraft_editor.controller.listeners.infopanel_listeners.fuel;

import nl.rug.oop.flaps.aircraft_editor.view.panels.aircraft_info.interaction_panels.FuelConfigPanel;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
/**
 * listens to a value change in the {@link FuelConfigPanel} infoSlider
 * */
public class FuelSliderListener implements ChangeListener {
    private final FuelConfigPanel panel;
    public FuelSliderListener(FuelConfigPanel panel) {
        this.panel = panel;
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        panel.updateWeightLabel();
    }
}

package nl.rug.oop.flaps.aircraft_editor.controller.listeners.infopanel_listeners.cargo;

import nl.rug.oop.flaps.aircraft_editor.view.panels.aircraft_info.interaction_panels.CargoConfigPanel;
import nl.rug.oop.flaps.simulation.model.cargo.CargoUnit;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 * this class uses a {@link ListSelectionListener} to listen to changes on the list of cargos.
 *
 * */
public class CargoListChangeListener implements ListSelectionListener {
    private final CargoConfigPanel cargoConfigPanel;

    public CargoListChangeListener(CargoConfigPanel cargoConfigPanel) {
        this.cargoConfigPanel = cargoConfigPanel;
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        if (e.getValueIsAdjusting()) return; //Prevents bug where value is clicked twice
        JList<Object> list = cargoConfigPanel.getCargoList();
        cargoConfigPanel.setSelectedCargoUnit(
                new CargoUnit(cargoConfigPanel.getCargoType().get(list.getSelectedIndex()))
        );

        cargoConfigPanel.initCargoUnitLabel();
        cargoConfigPanel.initSlider();
    }
}

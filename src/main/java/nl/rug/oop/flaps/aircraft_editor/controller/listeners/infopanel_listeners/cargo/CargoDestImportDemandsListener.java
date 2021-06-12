package nl.rug.oop.flaps.aircraft_editor.controller.listeners.infopanel_listeners.cargo;

import nl.rug.oop.flaps.aircraft_editor.view.panels.aircraft_info.interaction_panels.CargoConfigPanel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * listens to action performed on the button to view import demands of the destination airport.
 * @see CargoConfigPanel
 * */
public class CargoDestImportDemandsListener implements ActionListener {
    private final CargoConfigPanel panel;
    public CargoDestImportDemandsListener(CargoConfigPanel panel) {
        this.panel = panel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == panel.getViewImportButton()) {
            panel.viewDestinationImportDemands();
        }
    }
}

package nl.rug.oop.flaps.simulation.controller.actions;

import lombok.Getter;
import nl.rug.oop.flaps.aircraft_editor.view.frame.EditorFrame;
import nl.rug.oop.flaps.simulation.model.aircraft.Aircraft;
import nl.rug.oop.flaps.simulation.model.world.WorldSelectionModel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 *
 * @author T.O.W.E.R.
 */
public class OpenAircraftConfigurer extends AbstractAction implements PropertyChangeListener {
    @Getter
    private final Aircraft aircraft;
    private final WorldSelectionModel selectionModel;

    public OpenAircraftConfigurer(Aircraft aircraft, WorldSelectionModel selectionModel) {
        super("Configure");
        this.aircraft = aircraft;
        this.selectionModel = selectionModel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (selectionModel.getSelectedDestinationAirport() != null) {
            SwingUtilities.invokeLater(() -> new EditorFrame(aircraft, selectionModel));
        } else {
            JOptionPane.showMessageDialog(null,
                    "Select a destination before configuring",
                    "No destination selected",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
    }
}

package nl.rug.oop.flaps.simulation.view.panels.aircraft;

import nl.rug.oop.flaps.simulation.controller.actions.SelectDestinationAction;
import nl.rug.oop.flaps.simulation.model.aircraft.Aircraft;
import nl.rug.oop.flaps.simulation.model.world.WorldSelectionModel;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 *
 * @author T.O.W.E.R.
 */
public class AircraftInfoPanel extends JPanel implements PropertyChangeListener {

    private final Aircraft aircraft;
    private final WorldSelectionModel selectionModel;
    private final GridBagConstraints gbc;


    public AircraftInfoPanel(Aircraft aircraft, WorldSelectionModel selectionModel) {
        super(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 50, 5, 50);
        gbc.anchor = GridBagConstraints.WEST;
        this.aircraft = aircraft;
        this.selectionModel = selectionModel;
        displayInfoPanel();
    }

    private void displayInfoPanel() {
        this.removeAll();
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(buildHeaderLabel("ID: "), gbc);
        gbc.gridx++;
        add(buildHeaderLabel(aircraft.getIdentifier()), gbc);
        gbc.gridy++;
        gbc.gridx = 0;
        add(buildHeaderLabel("Type: "), gbc);
        gbc.gridx++;
        add(buildHeaderLabel(aircraft.getType().getName()), gbc);
        gbc.gridy = 0;
        gbc.gridx = 2;
        gbc.gridheight = 2;
        add(new JButton(new SelectDestinationAction(this.selectionModel)), gbc);
        gbc.gridheight = 1;
        revalidate();
        repaint();
    }

    private JLabel buildHeaderLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font(Font.SANS_SERIF, Font.BOLD, label.getFont().getSize() + 5));
        return label;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        displayInfoPanel();
    }
}

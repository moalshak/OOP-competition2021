package nl.rug.oop.flaps.simulation.view.panels.airport;

import nl.rug.oop.flaps.simulation.model.airport.Airport;
import nl.rug.oop.flaps.simulation.model.world.World;
import nl.rug.oop.flaps.simulation.model.world.WorldSelectionModelListener;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;

/**
 * Displays all information of a single airport
 *
 * @author T.O.W.E.R.
 */
public class AirportPanel extends JPanel implements WorldSelectionModelListener {

    private final World world;

    private final GridBagConstraints gbc;

    private final AircraftAreaPanel aircraftAreaPanel;

    public AirportPanel(World world) {
        setLayout(new GridBagLayout());
        this.aircraftAreaPanel = new AircraftAreaPanel(null, world);
        gbc = new GridBagConstraints();
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weighty = 1;
        gbc.anchor = GridBagConstraints.NORTH;
        this.world = world;
        world.getSelectionModel().addListener(this);
        displayAirport();
    }

    private void pad() {
        gbc.insets = new Insets(0, 20, 0, 20);

    }

    private void resetPad() {
        gbc.insets = new Insets(0, 0, 0, 0);

    }

    private void displayAirport() {
        this.removeAll();
        Airport airport = world.getSelectionModel().getSelectedAirport();
        gbc.gridx = 0;
        gbc.gridy = 0;
        if(airport != null) {
            gbc.gridwidth = 2;
            ImageIcon bannerImage = new ImageIcon(airport.getBannerImage().getScaledInstance(this.getWidth(), this.getWidth() / 3, Image.SCALE_SMOOTH));
            add(new JLabel(bannerImage), gbc);
            pad();
            gbc.gridy++;
            JLabel nameLabel = new JLabel(airport.getName());
            nameLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 30));
            add(nameLabel, gbc);
            gbc.gridy++;
            gbc.weightx = 1;
            JTextArea descriptionArea = new JTextArea(airport.getDescription(), 5, 300);
            descriptionArea.setEditable(false);
            descriptionArea.setWrapStyleWord(true);
            descriptionArea.setLineWrap(true);
            descriptionArea.setMinimumSize(new Dimension(780, 80));
            descriptionArea.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
            JScrollPane scrollableDescription = new JScrollPane(descriptionArea);
            scrollableDescription.setMinimumSize(new Dimension(getWidth(), 80));
            add(scrollableDescription, gbc);
            gbc.gridwidth = 1;
            gbc.gridy++;
            add(buildValuePanel("Longitude:", Double.toString(airport.getLocation().getLongitude())), gbc);
            gbc.gridx++;
            add(buildValuePanel("Latitude:", Double.toString(airport.getLocation().getLatitude())), gbc);
            gbc.gridx = 0;
            gbc.gridy++;
            gbc.gridwidth = 2;
            JLabel airplaneLabel = new JLabel("Aircraft");
            airplaneLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));
            add(airplaneLabel, gbc);
            gbc.gridy++;

            JScrollPane aircraftAreaPane = new JScrollPane(aircraftAreaPanel, JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
            aircraftAreaPane.setMinimumSize(new Dimension(this.getWidth(), 100));
            add(aircraftAreaPane, gbc);

            gbc.gridwidth = 1;
            gbc.gridy++;
            add(new CargoPanel(airport.getCargoImportDemands()), gbc);
            gbc.gridx++;
            add(new FuelPanel(airport.getFuelPrices()), gbc);
        } else {
            JLabel emptyLabel = new JLabel("No airport selected..");
            emptyLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 30));
            add(emptyLabel, gbc);
        }
        resetPad();
        revalidate();
        repaint();
    }

    private JPanel buildValuePanel(String label, String value) {
        JTextField valueField = new JTextField(value);
        valueField.setEditable(false);
        return this.buildValuePanel(label, valueField);
    }

    private JPanel buildValuePanel(String label, Component component) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JLabel(label), BorderLayout.NORTH);
        panel.add(component, BorderLayout.CENTER);
        return panel;
    }

    @Override
    public void airportSelected(Airport selectedAirport) {
        this.displayAirport();
    }
}

package nl.rug.oop.flaps.simulation.view.panels.trip;

import lombok.Getter;
import nl.rug.oop.flaps.simulation.model.trips.Trip;
import nl.rug.oop.flaps.simulation.model.world.WorldSelectionModel;

import javax.swing.*;
import java.awt.*;

/**
 * shows information about the flight when clicked
 * */
public class TripsInfo extends JPanel {
    private final WorldSelectionModel sm;
    private final GridBagConstraints gbc = new GridBagConstraints();
    private final int WIDTH;
    @Getter
    private static TripsInfo tripsInfo;

    private Trip selectedTrip;
    private final JPanel infoPanel = new JPanel();
    private final Font infoTextFont = new Font(Font.SANS_SERIF, Font.BOLD, 18);
    private JLabel fuelLabel;
    private JLabel distanceLabel;

    public TripsInfo (WorldSelectionModel sm, int width) {
        this.sm = sm;
        this.WIDTH = width;
        setLayout(new BorderLayout());
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.ipady = 3;

        JLabel title = new JLabel("Flights info || Flight id:  \t" + sm.getSelectedTrip().getFlightsId());
        title.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 30));

        JSplitPane splitPane = new JSplitPane (JSplitPane.VERTICAL_SPLIT ,new JScrollPane(title), new JScrollPane(viewInfo()));
        splitPane.setDividerLocation(60);
        splitPane.setDividerSize(0);

        add(splitPane);

        setVisible(true);
        tripsInfo = this;
    }
    // y -> row ; x -> col
    private JPanel viewInfo() {

        selectedTrip = sm.getSelectedTrip();
        infoPanel.setLayout(new GridBagLayout());

        JLabel label = new JLabel("Origin Airport: " + selectedTrip.getOriginAirport().getName());
        gbc.gridy = 0; gbc.gridx = 0;
        infoPanel.add(label, gbc);
        label.setFont(infoTextFont);

        label = new JLabel("Destination Airport: " + selectedTrip.getDestAirport().getName());
        gbc.gridy = 1; gbc.gridx = 0;
        infoPanel.add(label, gbc);
        label.setFont(infoTextFont);

        updateFuelLabel();
        updateDistanceMeter();

        label = new JLabel("Aircraft's cargo weight: " + selectedTrip.getAircraft().getTotalCargoWeight() + " Kg");
        gbc.gridy = 3; gbc.gridx = 0;
        infoPanel.add(label, gbc);
        label.setFont(infoTextFont);

        label = new JLabel("Aircraft's speed: " + selectedTrip.getAircraft().getType().getCruiseSpeed() + " Km/h");
        gbc.gridy = 4; gbc.gridx = 0;
        infoPanel.add(label, gbc);
        label.setFont(infoTextFont);

        label = new JLabel("Aircraft's Revenue: € " + Trip.getRevenue());
        gbc.gridy = 5; gbc.gridx = 0;
        infoPanel.add(label, gbc);
        label.setFont(infoTextFont);

        label = new JLabel();
        ImageIcon banner = new ImageIcon(selectedTrip.getBannerInAir().getScaledInstance(WIDTH-15, WIDTH/2-50, Image.SCALE_SMOOTH));
        label.setIcon(banner);
        gbc.gridy = 7; gbc.gridx = 0;
        gbc.gridwidth = 2; gbc.fill = GridBagConstraints.HORIZONTAL;
        infoPanel.add(label, gbc);
        label.setFont(infoTextFont);
        //todo: add distance remaining and thus the time

        return infoPanel;
    }


    /**
     * updates the fuel label when the aircraft is flying
     * */
    public void updateFuelLabel() {
        if (fuelLabel == null) {
            fuelLabel = new JLabel("Aircraft's fuel: " + (int) selectedTrip.getAircraft().getTotalFuel() + " Kg");
            gbc.gridy = 2; gbc.gridx = 0;
            infoPanel.add(fuelLabel, gbc);
            fuelLabel.setFont(infoTextFont);
        } else {
            fuelLabel.setText("Aircraft's fuel: " + (int) selectedTrip.getAircraft().getTotalFuel() + " Kg");
        }
        infoPanel.updateUI();
    }

    /**
     * updates the distance meter
     * */
    public void updateDistanceMeter() {
        if (distanceLabel == null) {
            distanceLabel = new JLabel(selectedTrip.getDistanceLeft());
            gbc.gridy = 6; gbc.gridx = 0;
            distanceLabel.setFont(infoTextFont);
            infoPanel.add(distanceLabel, gbc);
        } else {
            distanceLabel.setText(selectedTrip.getDistanceLeft());
        }
        infoPanel.updateUI();
    }
}

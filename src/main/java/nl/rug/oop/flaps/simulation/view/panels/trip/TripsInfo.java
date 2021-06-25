package nl.rug.oop.flaps.simulation.view.panels.trip;

import lombok.Getter;
import nl.rug.oop.flaps.simulation.model.aircraft.CargoArea;
import nl.rug.oop.flaps.simulation.model.airport.Airport;
import nl.rug.oop.flaps.simulation.model.cargo.CargoUnit;
import nl.rug.oop.flaps.simulation.model.trips.Trip;
import nl.rug.oop.flaps.simulation.model.world.WorldSelectionModel;
import nl.rug.oop.flaps.simulation.view.panels.airport.AirportPanel;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * shows information about the flight when clicked
 * */
public class TripsInfo extends JPanel {
    private WorldSelectionModel sm;
    private GridBagConstraints gbc = new GridBagConstraints();
    private final int WIDTH;

    public TripsInfo (WorldSelectionModel sm, int width) {
        this.sm = sm;
        this.WIDTH = width;
        setLayout(new BorderLayout());
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel title = new JLabel("Flights info. Flight id:\t" + sm.getSelectedTrip().getFlightsId());
        title.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 30));
        add(title, BorderLayout.NORTH);

        add(viewInfo(), BorderLayout.CENTER);
        setVisible(true);
    }
    // y -> row ; x -> col
    private JPanel viewInfo() {
        JPanel infoPanel = new JPanel();
        Font infoTextFont = new Font(Font.SANS_SERIF, Font.BOLD, 18);

        Trip selectedTrip = sm.getSelectedTrip();
        infoPanel.setLayout(new GridBagLayout());

        JLabel label = new JLabel("Origin Airport: " + selectedTrip.getOriginAirport().getName());
        gbc.gridy = 0; gbc.gridx = 0;
        infoPanel.add(label, gbc);
        label.setFont(infoTextFont);

        label = new JLabel("Destination Airport: " + selectedTrip.getDestAirport().getName());
        gbc.gridy = 1; gbc.gridx = 0;
        infoPanel.add(label, gbc);
        label.setFont(infoTextFont);

        label = new JLabel("Aircraft's fuel: " + selectedTrip.getAircraft().getTotalFuel() + " Kg");
        gbc.gridy = 2; gbc.gridx = 0;
        infoPanel.add(label, gbc);
        label.setFont(infoTextFont);

        label = new JLabel("Aircraft's cargo: " + selectedTrip.getAircraft().getTotalCargoWeight() + "Kg");
        gbc.gridy = 2; gbc.gridx = 1;
        infoPanel.add(label, gbc);
        label.setFont(infoTextFont);

        label = new JLabel("Aircraft's speed: " + selectedTrip.getAircraft().getType().getCruiseSpeed() + " Km/h");
        gbc.gridy = 3; gbc.gridx = 0;
        infoPanel.add(label, gbc);
        label.setFont(infoTextFont);

        label = new JLabel();
        ImageIcon banner = new ImageIcon(selectedTrip.getBannerInAir().getScaledInstance(WIDTH, WIDTH/2, Image.SCALE_SMOOTH));
        label.setIcon(banner);
        gbc.gridy = 4; gbc.gridx = 0;
        gbc.gridwidth = 3; gbc.fill = GridBagConstraints.HORIZONTAL;
        infoPanel.add(label, gbc);
        label.setFont(infoTextFont);
        //todo: add distance remaining and thus the time

        return infoPanel;
    }
}

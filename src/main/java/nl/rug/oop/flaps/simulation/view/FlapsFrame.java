package nl.rug.oop.flaps.simulation.view;

import lombok.Getter;
import nl.rug.oop.flaps.simulation.model.world.World;
import nl.rug.oop.flaps.simulation.view.panels.WorldPanel;
import nl.rug.oop.flaps.simulation.view.panels.aircraft.AircraftPanel;
import nl.rug.oop.flaps.simulation.view.panels.airport.AirportPanel;

import javax.swing.*;
import java.awt.*;

/**
 * The frame of the main application
 *
 * @author T.O.W.E.R.
 */
public class FlapsFrame extends JFrame {
    private static final int WIDTH = 1620;
    @Getter
    private static final int HEIGHT = 920;
    @Getter
    private static JFrame frame = null;

    @Getter
    private static JSplitPane worldPlaneSplit;
    @Getter
    private static JSplitPane leftRightSplit;

    public FlapsFrame(World world) {
        super("Flight Logistics Aviation Planning Simulation");
        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        initPanels(world);

        frame = this;

        pack();

        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void initPanels(World world) {
        WorldPanel worldPanel = new WorldPanel(world);
        worldPanel.setPreferredSize(new Dimension(WIDTH/2, HEIGHT/2));
        JScrollPane worldPanelScrollPane = new JScrollPane(worldPanel);

        AircraftPanel aircraftPanel = new AircraftPanel(world);
        AirportPanel airportPanel = new AirportPanel(world);
        airportPanel.setPreferredSize(new Dimension(WIDTH/2, HEIGHT/2));

        worldPlaneSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT, worldPanelScrollPane, aircraftPanel);
        worldPlaneSplit.setDividerLocation(HEIGHT/2+20);
        //worldPlaneSplit.setDividerSize(0);

        leftRightSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, airportPanel, worldPlaneSplit);
        leftRightSplit.setDividerLocation(WIDTH/2+280);
        //leftRightSplit.setDividerSize(0);
        add(leftRightSplit);
    }
}

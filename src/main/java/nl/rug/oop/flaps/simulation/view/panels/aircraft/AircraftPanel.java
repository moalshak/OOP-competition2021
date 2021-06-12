package nl.rug.oop.flaps.simulation.view.panels.aircraft;

import lombok.extern.java.Log;
import nl.rug.oop.flaps.simulation.controller.actions.OpenAircraftConfigurer;
import nl.rug.oop.flaps.simulation.model.aircraft.Aircraft;
import nl.rug.oop.flaps.simulation.model.world.World;
import nl.rug.oop.flaps.simulation.model.world.WorldSelectionModelListener;

import javax.swing.*;
import java.awt.*;

/**
 *
 * @author T.O.W.E.R.
 */
@Log
public class AircraftPanel extends JPanel implements WorldSelectionModelListener {
    private final World world;

    public AircraftPanel(World world) {
        super(new BorderLayout());
        this.world = world;
        this.world.getSelectionModel().addListener(this);
        displayAircraft(null);
    }

    private void displayAircraft(Aircraft aircraft) {
        this.removeAll();
        if(aircraft == null) {
            JLabel emptyLabel = new JLabel("No aircraft selected..", JLabel.CENTER);
            emptyLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 30));
            add(emptyLabel, BorderLayout.CENTER);
            revalidate();
            repaint();
            return;
        }
        add(new AircraftInfoPanel(aircraft, this.world.getSelectionModel()), BorderLayout.NORTH);
        ImageIcon blueprintIcon = new ImageIcon(aircraft.getType().getBannerImage().getScaledInstance(this.getWidth(), this.getWidth() / 3, Image.SCALE_SMOOTH));
        add(new JLabel(blueprintIcon), BorderLayout.CENTER);
        var sm = this.world.getSelectionModel();
        add(new JButton(new OpenAircraftConfigurer(sm.getSelectedAircraft(), sm)), BorderLayout.SOUTH);
        revalidate();
        repaint();
    }

    @Override
    public void aircraftSelected(Aircraft aircraft) {
        this.displayAircraft(aircraft);
    }
}

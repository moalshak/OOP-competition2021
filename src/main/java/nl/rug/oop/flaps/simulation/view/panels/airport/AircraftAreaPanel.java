package nl.rug.oop.flaps.simulation.view.panels.airport;

import nl.rug.oop.flaps.simulation.model.aircraft.Aircraft;
import nl.rug.oop.flaps.simulation.model.airport.Airport;
import nl.rug.oop.flaps.simulation.model.world.World;
import nl.rug.oop.flaps.simulation.model.world.WorldSelectionModelListener;

import javax.swing.*;
import java.awt.*;

/**
 * Displays all the different aircraft of a given airport
 *
 * @author T.O.W.E.R.
 */
public class AircraftAreaPanel extends JPanel implements WorldSelectionModelListener {
    private Airport airport;
    private final World world;

    public AircraftAreaPanel(Airport airport, World world) {
        super(new FlowLayout());
        this.airport = airport;
        this.world = world;
        world.getSelectionModel().addListener(this);
        drawAircraft();
        setBorder(BorderFactory.createEtchedBorder());
    }

    private void drawAircraft() {
        removeAll();
        if (this.airport != null) {
            var sm = this.world.getSelectionModel();
            airport.getAircraft().forEach(aircraft -> {
                AircraftIconLabel aircraftIconLabel = new AircraftIconLabel(aircraft, world);
                if (sm.getSelectedAircraft() != null && sm.getSelectedAircraft().equals(aircraft)) {
                    aircraftIconLabel.select();
                }
                add(aircraftIconLabel);
                add(Box.createHorizontalStrut(10));
            });
        }
        revalidate();
    }

    @Override
    public void airportSelected(Airport selectedAirport) {
        this.airport = selectedAirport;
        this.drawAircraft();
    }

    @Override
    public void aircraftSelected(Aircraft aircraft) {
        this.drawAircraft();
    }
}

package nl.rug.oop.flaps.aircraft_editor.controller.listeners.infopanel_listeners;

import lombok.extern.java.Log;
import nl.rug.oop.flaps.aircraft_editor.view.frame.EditorFrame;
import nl.rug.oop.flaps.simulation.model.aircraft.AircraftType;
import nl.rug.oop.flaps.simulation.model.trips.Trip;
import nl.rug.oop.flaps.simulation.model.trips.TripsThread;
import nl.rug.oop.flaps.simulation.model.world.WorldSelectionModel;
import nl.rug.oop.flaps.simulation.view.FlapsFrame;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * This action is invoked to perform the actual departure of an aircraft from an
 * airport.
 *
 * Use the setEnabled() method to enable/disable the corresponding button
 *
 * @author T.O.W.E.R.
 */
@Log
public class DepartButtonListener extends AbstractAction {
    private final WorldSelectionModel selectionModel;

    public DepartButtonListener(WorldSelectionModel selectionModel) {
        super("Depart");
        this.selectionModel = selectionModel;
        setEnabled(false);
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        startNewTrip();

        var sm = this.selectionModel; // Just to keep things succinct.
        if (sm.getSelectedAirport() != null && sm.getSelectedAircraft() != null && sm.getSelectedDestinationAirport() != null) {
            var aircraft = sm.getSelectedAircraft();
            aircraft.setEditMenu(null);
            if (aircraft.getType().getTakeoffClipPath() != null) {
                this.playTakeoffClip(aircraft.getType());
            }
            sm.getSelectedAirport().removeAircraft(aircraft);
            sm.setSelectedAirport(sm.getSelectedAirport()); // this line is to refresh the airport viewer panel
        }
        closeFrame();
    }

    /**
     *  start a trip from origin airport to destination airport
     *  */
    private void startNewTrip() {
        Trip newTrip = new Trip(selectionModel);
        TripsThread tripsThread = new TripsThread(newTrip);
        tripsThread.start();
    }

    /**
     * closes the frame after departing and shows a message
     * */
    private void closeFrame() {
        JFrame frame = EditorFrame.getFrame();
        frame.setVisible(false);
        frame.dispose();
        JOptionPane.showMessageDialog(FlapsFrame.getFrame(),"Aircraft departed successfully");
    }

    private void playTakeoffClip(AircraftType type) {
        new Thread(() -> {
            try (Clip clip = AudioSystem.getClip()) {
                AudioInputStream ais = AudioSystem.getAudioInputStream(type.getTakeoffClipPath().toFile());
                clip.open(ais);
                clip.start();
                Thread.sleep((long) (ais.getFrameLength() / ais.getFormat().getFrameRate()) * 1000);
            } catch (Exception e) {
                log.warning("Could not play takeoff clip: " + e.getMessage());
            }
        }).start();
    }
}

package nl.rug.oop.flaps.simulation.controller;

import lombok.Getter;
import nl.rug.oop.flaps.simulation.view.panels.WorldPanel;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class SpeedRateUp implements ChangeListener {
    private final WorldPanel worldPanel;
    @Getter
    private static double RATE;

    /**
     * controllers the thread at which the thread sleep thus the speed at which the planes move on the world map
     * */
    public SpeedRateUp(WorldPanel worldPanel) {
        this.worldPanel = worldPanel;
        RATE = 1;
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        RATE = (double) worldPanel.getSpeedUpRate().getValue();
        worldPanel.updateUI();
    }
}

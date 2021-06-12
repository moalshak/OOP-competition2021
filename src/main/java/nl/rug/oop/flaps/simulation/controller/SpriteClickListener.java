package nl.rug.oop.flaps.simulation.controller;

import nl.rug.oop.flaps.simulation.model.aircraft.Aircraft;
import nl.rug.oop.flaps.simulation.model.world.World;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Listeners that listens for clicks
 * While this can also be done with a lambda expression, this way we have it nicely separated into our controller
 *
 * @author T.O.W.E.R.
 */
public class SpriteClickListener extends MouseAdapter {
    /**
     * The aircraft that belongs to the sprite
     */
    private final Aircraft aircraft;
    /**
     * The world context
     */
    private final World world;

    /**
     * Creates a new listener
     *
     * @param aircraft The aircraft that is belongs to this sprite
     * @param world The world context
     */
    public SpriteClickListener(Aircraft aircraft, World world) {
        this.aircraft = aircraft;
        this.world = world;
    }

    /**
     * Executed when the user clicks on the sprite
     * Sets the current aircraft as the selected aircraft
     *
     * @param e The mouse event
     */
    @Override
    public void mouseReleased(MouseEvent e) {
        world.getSelectionModel().setSelectedAircraft(aircraft);
    }
}

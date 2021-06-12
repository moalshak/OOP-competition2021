package nl.rug.oop.flaps.simulation.controller.actions;

import nl.rug.oop.flaps.simulation.model.world.WorldSelectionModel;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 *
 * @author T.O.W.E.R.
 */
public class SelectDestinationAction extends AbstractAction {

    private final WorldSelectionModel selectionModel;

    public SelectDestinationAction(WorldSelectionModel selectionModel) {
        super("Select Destination");
        this.selectionModel = selectionModel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        this.selectionModel.startSelectingDestination();
    }
}

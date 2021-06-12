package nl.rug.oop.flaps.aircraft_editor.controller.listeners.blueprint_listeners;

import nl.rug.oop.flaps.aircraft_editor.model.blueprint.BluePrintModel;
import nl.rug.oop.flaps.aircraft_editor.model.blueprint.BluePrintModelListener;
import nl.rug.oop.flaps.aircraft_editor.view.panels.blueprint.BluePrintPanel;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * This class uses {@link MouseListener} to listen to mouse actions. If the mouse is clicked we pass the point where
 * the mouse was clicked and pass it to {@link BluePrintModel} to check if any of the indicators are selected.
 * for difference events of the mouse a different action
 * */

public class PointSelectionListener implements MouseListener {
    private static final int CLICK_RADIUS = BluePrintPanel.getINDICATOR_SIZE()+1;
    private final BluePrintModel bluePrintModel;

    public PointSelectionListener(BluePrintModel bluePrintModel) {
        this.bluePrintModel = bluePrintModel;

    }

    @Override
    public void mouseClicked(MouseEvent e) {
        bluePrintModel.selectPointByCoords(e.getPoint(), CLICK_RADIUS);
        bluePrintModel.getListeners().forEach(BluePrintModelListener::pointSelectedUpdater);

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}

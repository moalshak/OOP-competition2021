package nl.rug.oop.flaps.aircraft_editor.view.panels.aircraft_info;


import lombok.Getter;
import nl.rug.oop.flaps.aircraft_editor.model.blueprint.BluePrintModel;
import nl.rug.oop.flaps.aircraft_editor.model.blueprint.BluePrintModelListener;
import nl.rug.oop.flaps.aircraft_editor.model.config_models.InfoPanelModel;
import nl.rug.oop.flaps.aircraft_editor.model.config_models.InfoPanelModelListener;
import nl.rug.oop.flaps.aircraft_editor.view.frame.EditorFrame;
import nl.rug.oop.flaps.aircraft_editor.view.panels.aircraft_info.interaction_panels.InteractionPanel;
import nl.rug.oop.flaps.aircraft_editor.view.panels.blueprint.BluePrintPanel;
import nl.rug.oop.flaps.simulation.model.aircraft.Aircraft;
import nl.rug.oop.flaps.simulation.model.world.WorldSelectionModel;

import javax.swing.*;
import java.awt.*;

/**
 * this class holds the panel of the information that has to be presented to the user
 *
 * holds :
 * <ol>
 *     <li>{@link BluePrintModel}</li>
 *     <li>{@link InfoPanelModel}</li>
 *     <li>{@link ResultPanel}</li>
 *     <li>{@link InteractionPanel}</li>
 *
 * </ol>
 * */

public class InfoPanel extends JPanel implements BluePrintModelListener {
    private static final int DIV_LOCATION = EditorFrame.getHEIGHT()/2;
    @Getter
    private static WorldSelectionModel worldSelectionModel;

    @Getter
    private static JSplitPane infoSplitPane;

    /**
     *  makes an instance for the information panel for the aircraft
     * @param aircraft the aircraft to display the information about
     * @param bluePrintPanel the blueprint panel of the aircraft
     * @param selectionModel the world selection model of the world
     * */
    public InfoPanel(Aircraft aircraft, WorldSelectionModel selectionModel, BluePrintPanel bluePrintPanel) {
        BluePrintModel model = bluePrintPanel.getModel();
        worldSelectionModel = selectionModel;
        InfoPanelModel infoPanelModel = new InfoPanelModel(aircraft, selectionModel);
        infoPanelModel.addListener((InfoPanelModelListener) BluePrintPanel.getBluePrintPanel());
        ResultPanel resultPanel = new ResultPanel(infoPanelModel);
        InteractionPanel interactionPanel = new InteractionPanel(selectionModel,model, infoPanelModel);
        this.add(interactionPanel.getInteractionScrollPane(), BorderLayout.NORTH);
        this.add(resultPanel.getResultPartScrollPane(), BorderLayout.SOUTH);
        infoSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, interactionPanel, resultPanel);
        infoSplitPane.setOneTouchExpandable(false);
        infoSplitPane.setDividerLocation(DIV_LOCATION);
    }

    /**
     * Updates the information in the result panel
     */
    public static void updateResultInfo() {
        InfoPanelModel.getListeners().forEach(InfoPanelModelListener::updateConfig);
        /* the result panel must be updated as the last member */
        InfoPanelModelListener result = (InfoPanelModelListener) (ResultPanel.getMainPanel());
        InfoPanelModelListener bluePrint = (InfoPanelModelListener) (BluePrintPanel.getBluePrintPanel());
        bluePrint.updateConfig();
        result.updateConfig();
    }
}


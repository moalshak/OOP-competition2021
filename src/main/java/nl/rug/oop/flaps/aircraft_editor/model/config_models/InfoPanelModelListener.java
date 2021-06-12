package nl.rug.oop.flaps.aircraft_editor.model.config_models;

import nl.rug.oop.flaps.aircraft_editor.view.panels.aircraft_info.interaction_panels.InteractionPanel;
import nl.rug.oop.flaps.aircraft_editor.view.panels.aircraft_info.ResultPanel;

/**
 * listens to changes inf the {@link InfoPanelModel} and updates the list of listeners. Which leads to update the info
 * int the {@link ResultPanel} and {@link InteractionPanel}
 * */
public interface InfoPanelModelListener {
    /**
     * updates the configuration to the latest chosen by the user
     * */
    default void updateConfig() {}
}

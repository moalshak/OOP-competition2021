package nl.rug.oop.flaps.aircraft_editor.view.panels.aircraft_info.interaction_panels;

import lombok.Getter;
import nl.rug.oop.flaps.aircraft_editor.model.blueprint.BluePrintModel;
import nl.rug.oop.flaps.aircraft_editor.model.blueprint.BluePrintModelListener;
import nl.rug.oop.flaps.aircraft_editor.model.config_models.passenger.PassengersModel;
import nl.rug.oop.flaps.aircraft_editor.model.config_models.InfoPanelModel;
import nl.rug.oop.flaps.aircraft_editor.view.frame.EditorFrame;
import nl.rug.oop.flaps.aircraft_editor.view.panels.aircraft_info.InfoPanel;

import javax.swing.*;
import java.awt.*;

/**
 * This class is for the interaction part of modifying the fuel tanks and cargo areas of the airplane
 * after confirmation of the user the data gets updated and the ResultPanel gets updated
 * */
public class InteractionPanel extends JPanel implements BluePrintModelListener {
    private final BluePrintModel bluePrintModel;
    @Getter
    private final JScrollPane interactionScrollPane;
    @Getter
    private final JPanel displayPanel;  //Contains the information displayed for the selected areas
    private final JLabel nothingSelectedLabel = new JLabel(
            "<html> <h2> Nothing Selected... Select: </h2> \n" +
            "\t <h2>Entrance: to add / remove passengers</h2>\n" +
            "<h2>Fuel Tank: to add / remove fuel </h2>\n" +
            "<h2>Cargo Area: to add / remove cargo</h2> </html>");

    private final InfoPanelModel infoPanelModel;
    @Getter
    private static PassengersModel passengersModel;
    @Getter
    private static PassengersConfigPanel passengersConfigPanel;
    /**
     * Constructor for the InteractionPanel
     *
     * @param infoPanelModel the model with the info about the aircraft
     * @param bluePrintModel the bluePrintModel which we contains the data about the aircraft
     */
    public InteractionPanel(BluePrintModel bluePrintModel, InfoPanelModel infoPanelModel) {
        this.bluePrintModel = bluePrintModel;
        this.infoPanelModel = infoPanelModel;
        bluePrintModel.addListener(this);

        this.setLayout(new BorderLayout());
        displayPanel = new JPanel();
        this.add(nothingSelectedLabel, BorderLayout.NORTH);
        interactionScrollPane = new JScrollPane(this);

        passengersModel = new PassengersModel();
    }

    /**
     * Initialises the information panel used to interact with the aircraft's cargo and fuel areas
     */
    private void initInfoPanel() {
        displayPanel.removeAll();
        this.removeAll();
        //checks to see which compartment is selected, and displays the appropriate information
        switch (bluePrintModel.getSelectedCompartment()) {
            case BluePrintModel.FUEL:
                FuelConfigPanel fuelConfigPanel = new FuelConfigPanel(displayPanel, bluePrintModel);
                this.add(fuelConfigPanel.getDisplayPanel());
                InfoPanel.getInfoSplitPane().setDividerLocation(EditorFrame.getHEIGHT()/2);
                break;
            case BluePrintModel.CARGO:
                CargoConfigPanel cargoConfigPanel = new CargoConfigPanel(displayPanel, bluePrintModel);
                this.add(cargoConfigPanel.getDisplayPanel());
                break;
            case BluePrintModel.ENTRY:
                passengersConfigPanel = new PassengersConfigPanel(displayPanel, passengersModel, bluePrintModel);
                this.infoPanelModel.addListener(passengersConfigPanel);
                this.add(passengersConfigPanel.getDisplayPanel());
                InfoPanel.getInfoSplitPane().setDividerLocation(EditorFrame.getHEIGHT()/2);
                break;
            default:
                this.add(nothingSelectedLabel, BorderLayout.NORTH);
                InfoPanel.getInfoSplitPane().setDividerLocation(EditorFrame.getHEIGHT()/2);
                break;
        }
    }

    @Override
    public void pointSelectedUpdater() {
        initInfoPanel();
        this.updateUI();
    }

}

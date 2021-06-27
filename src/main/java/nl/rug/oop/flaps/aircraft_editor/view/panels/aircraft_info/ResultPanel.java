package nl.rug.oop.flaps.aircraft_editor.view.panels.aircraft_info;

import lombok.Getter;
import nl.rug.oop.flaps.aircraft_editor.model.config_models.InfoPanelModelListener;
import nl.rug.oop.flaps.aircraft_editor.model.config_models.InfoPanelModel;
import nl.rug.oop.flaps.aircraft_editor.model.blueprint.BluePrintModel;
import nl.rug.oop.flaps.aircraft_editor.view.panels.aircraft_info.interaction_panels.DepartButton;

import javax.swing.*;
import java.awt.*;

/**
 * this class is for the resulting status of the aircraft after user confirmation of modification
 * to the {@link nl.rug.oop.flaps.simulation.model.aircraft.Aircraft}
 * */
@Getter
public class ResultPanel extends JPanel implements InfoPanelModelListener {
    @Getter
    private JScrollPane resultPartScrollPane;

    @Getter
    private static JPanel mainPanel;

    private final InfoPanelModel model;
    private final JPanel bottomPanel;
    private JButton departButton;

    public ResultPanel(InfoPanelModel infoPanelModel) {
        this.model = infoPanelModel;
        mainPanel = this;
        this.setLayout(new BorderLayout());
        bottomPanel = new JPanel();
        departButton = null;
        bottomPanel.setLayout(new GridLayout(4,2));
        initResultPanel();
        updateConfig();
    }

    /**
     * initializes the result panel holding the resulting info of the configuration of the user
     * */
    private void initResultPanel() {
        JLabel title = new JLabel("<html> <h2> Current Aircraft configuration ✈ </h2> </html>");
        this.add(title, BorderLayout.NORTH);
        resultPartScrollPane = new JScrollPane(this);
    }

    /**
     * updates the results in the result panel holding the information about the aircraft and its current configuration
     * */
    private void updateLabels() {
        bottomPanel.removeAll();
        updateRangeLabel();
        updateProfitRevAndCost();
        updateCenterOfGravityLabel();
        updateConfirmButton();
        this.add(bottomPanel, BorderLayout.CENTER);
        this.add(departButton, BorderLayout.SOUTH);
        bottomPanel.updateUI();
        this.updateUI();
    }

    /**
     * updates the profit and cost labels puts them in the {@link ResultPanel#bottomPanel}
     * */
    private void updateProfitRevAndCost() {
        JLabel text; double val;
        val = model.getCost();
        text = new JLabel("<html> <h3>Trip's cost: € "+ (int) val + "</h3> </html>");
        bottomPanel.add(text);
        val = roundTwoDec(model.getRevenue());
        text = new JLabel("<html> <h3>Trip's revenue: € "+ (int) val + "</h3> </html>");
        bottomPanel.add(text);
        val = roundTwoDec(model.getProfitEstimation());
        text = new JLabel("<html> <h3>Trip's profit: € "+ (int) val + "</h3> </html>");
        bottomPanel.add(text);
        val = roundTwoDec(model.getTripDistance()/model.getAircraft().getType().getCruiseSpeed());
        text = new JLabel("<html> <h3>Estimated flight time: "+ (int) val + " Hours </h3> </html>");
        bottomPanel.add(text);
    }

    /**
     * updates the confirm button and adds it to the {@link ResultPanel#bottomPanel}
     * */
    private void updateConfirmButton() {
        if(departButton != null) {
            this.remove(departButton);
        }
        departButton = new DepartButton(model);
    }

    /**
     * updates the center of gravity label and adds it to the {@link ResultPanel#bottomPanel}
     * */
    private void updateCenterOfGravityLabel() {
        JLabel text;
        int x = (int) model.getAircraft().getType().getEmptyCgX();
        int y = (int) model.getAircraft().getType().getEmptyCgY();
        text = new JLabel("<html> <h3>Aircraft's original center of gravity: "+ x + "  "+ y + "</h3> </html>");
        bottomPanel.add(text);

        x = (int) BluePrintModel.getRealCG().getX();
        y = (int) BluePrintModel.getRealCG().getY();
        text = new JLabel("<html> <h3>Current aircraft's center of gravity: "+ x + "  "+ y + "</h3> </html>");
        bottomPanel.add(text);
    }

    /**
     * updates the range label and adds it to the {@link ResultPanel#bottomPanel}
     * */
    private void updateRangeLabel() {
        JLabel text;
        text = new JLabel("<html> <h3>Aircraft's range: "+ (int) model.getAircraftRange() + " Km </h3> </html>");
        bottomPanel.add(text);
        text = new JLabel("<html> <h3>Aircraft's weight: "+ (int) model.getAircraftWeight() + " Kg </h3> </html>");
        bottomPanel.add(text);
    }

    /**
     * rounds up to 2 decimals
     * @param value the value to round up
     * @return the value rounded up
     * */
    private double roundTwoDec(double value) {
        return ( Math.round(value * 100.0) / 100.0 );
    }

    @Override
    public void updateConfig() {
        updateLabels();
    }


}

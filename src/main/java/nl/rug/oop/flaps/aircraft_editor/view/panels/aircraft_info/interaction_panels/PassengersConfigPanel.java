package nl.rug.oop.flaps.aircraft_editor.view.panels.aircraft_info.interaction_panels;

import lombok.Getter;
import lombok.Setter;
import nl.rug.oop.flaps.aircraft_editor.controller.errors.MaxSeatsExceeded;
import nl.rug.oop.flaps.aircraft_editor.controller.listeners.infopanel_listeners.ConfirmChangeButtonListener;
import nl.rug.oop.flaps.aircraft_editor.controller.listeners.blueprint_listeners.PassengersChangeListener;
import nl.rug.oop.flaps.aircraft_editor.model.blueprint.BluePrintModel;
import nl.rug.oop.flaps.aircraft_editor.model.config_models.InfoPanelModelListener;
import nl.rug.oop.flaps.aircraft_editor.model.config_models.passenger.PassengersModel;
import nl.rug.oop.flaps.aircraft_editor.model.config_models.passenger.PassengersModelListener;
import nl.rug.oop.flaps.aircraft_editor.view.frame.EditorFrame;
import nl.rug.oop.flaps.aircraft_editor.view.panels.aircraft_info.InfoPanel;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * this class holds the configuration panel of the passengers once the entrance is clicked
 * */
@Getter @Setter
public class PassengersConfigPanel implements InfoPanelModelListener, PassengersModelListener {

    private JPanel displayPanel;
    private PassengersModel model;
    private BluePrintModel bluePrintModel;
    private JPanel passengerInteraction = new JPanel();

    private JLabel nrPassengersLabel;
    private JLabel seatsLabel;
    private JButton confirmButton;

    List<JSpinner> spinnerList = new ArrayList<>();

    JSpinner adults = new JSpinner();
    JSpinner kidsTo12 = new JSpinner();
    JSpinner kidsUnder12 = new JSpinner();

    private SpinnerNumberModel adultNumberModel ;
    private SpinnerNumberModel to12NumberModel ;
    private SpinnerNumberModel under12NumberModel;

    private ConfirmChangeButtonListener listener;
    public PassengersConfigPanel(JPanel displayPanel, PassengersModel passengersModel, BluePrintModel bluePrintModel) {
        this.model = passengersModel;
        this.bluePrintModel = bluePrintModel;
        model.addListener(this);
        model.setPanel(this);

        this.displayPanel = displayPanel;

        nrPassengersLabel = new JLabel();
        seatsLabel = new JLabel();
        confirmButton = new JButton();

        listener = new ConfirmChangeButtonListener(this);

        initPassengersConfig();

    }

    /**
     * initializes the panel the calls {@link PassengersConfigPanel#intiLabels()} and {@link PassengersConfigPanel#initModels()}
     * */
    public void initPassengersConfig() {
        passengerInteraction.removeAll();
        displayPanel.removeAll();
        displayPanel.setLayout(new BorderLayout());
        passengerInteraction.setLayout(new GridLayout(4, 2));
        this.displayPanel.add(passengerInteraction, BorderLayout.CENTER);
        this.displayPanel.add(new JLabel("<html><h1> Adding passengers aboard </h1></html>"), BorderLayout.NORTH);

        adultNumberModel = new SpinnerNumberModel(0, 0, bluePrintModel.getAircraft().getNrOfSeats(), 1);
        to12NumberModel = new SpinnerNumberModel(0, 0, bluePrintModel.getAircraft().getNrOfSeats(), 1);
        under12NumberModel = new SpinnerNumberModel(0, 0, bluePrintModel.getAircraft().getNrOfSeats(), 1);

        initModels();
        intiLabels();

        PassengersChangeListener action = new PassengersChangeListener(this.model);
        for (JSpinner spinner : spinnerList) {
            spinner.addChangeListener(action);
        }

        InfoPanel.getInfoSplitPane().setDividerLocation(EditorFrame.getHEIGHT()/2);
    }

    /**
     * initializes the models for the {@link JSpinner}
     * <ol>
     *     <li>{@link PassengersConfigPanel#adults}</li>
     *     <li>{@link PassengersConfigPanel#kidsTo12}</li>
     *     <li>{@link PassengersConfigPanel#kidsUnder12}</li>
     * </ol>
     * */
    public void initModels() {
        if(model.getPassengers().get("adults")!=null) {
            int startVal = model.getPassengers().get("adults");
            adultNumberModel.setValue(startVal);
            startVal = model.getPassengers().get("kidsTo12");
            to12NumberModel.setValue(startVal);
            startVal = model.getPassengers().get("kidsUnder12");
            under12NumberModel.setValue(startVal);
        }
    }

    /**
     * initializes the labels before the {@link JSpinner}
     * <ol>
     *     <li>{@link PassengersConfigPanel#adults}</li>
     *     <li>{@link PassengersConfigPanel#kidsTo12}</li>
     *     <li>{@link PassengersConfigPanel#kidsUnder12}</li>
     * </ol>
     * */
    public void intiLabels() {
        if(adults == null) {
            adults = new JSpinner();
            kidsTo12 = new JSpinner();
            kidsUnder12 = new JSpinner();
        }

        passengerInteraction.add(new JLabel("<html><h1>Adults: ( "+ PassengersModel.getTicketAdults()+ " Euro)</h1></html>"));
        passengerInteraction.add(adults);

        passengerInteraction.add(new JLabel("<html><h1>Kids older than 12: ( "+ PassengersModel.getTicketKids12()+ " Euro)</h1></html>"));
        passengerInteraction.add(kidsTo12);

        passengerInteraction.add(new JLabel("<html><h1>Kids younger than 12: ( "+ PassengersModel.getTicketKids()+ " Euro)</h1></html>"));
        passengerInteraction.add(kidsUnder12);

        adults.setModel(adultNumberModel);
        kidsTo12.setModel(to12NumberModel);
        kidsUnder12.setModel(under12NumberModel);

        spinnerList.add(adults);
        spinnerList.add(kidsTo12);
        spinnerList.add(kidsUnder12);

        passengerInteraction.add(nrPassengersLabel);
        passengerInteraction.add(seatsLabel);
        this.displayPanel.add(confirmButton, BorderLayout.SOUTH);

        updatePassengerLabel();
    }

    /**
     * updates the passengers label
     *  the one holding the {@link PassengersConfigPanel#nrPassengersLabel}
     *  and {@link PassengersConfigPanel#seatsLabel}
     * */
    public void updatePassengerLabel() {
        int remainingSeats = (bluePrintModel.getAircraft().getNrOfSeats() - model.getPassengersSum());

        nrPassengersLabel.setText("<html><h1>Number of seats occupied: " + model.getPassengersSum() +" </h1></html>");
        nrPassengersLabel.updateUI();
        seatsLabel.setText("<html><h3>Max number of seats: " + bluePrintModel.getAircraft().getNrOfSeats() + " || \t Free seats: " + remainingSeats +" </h3></html>");
        seatsLabel.updateUI();

        if(remainingSeats >= 0) {
            this.confirmButton.setText("Confirm passengers configuration");
            this.confirmButton.updateUI();
            passengerInteraction.updateUI();
            MaxSeatsExceeded.setWarned(false);
            displayPanel.updateUI();
            this.confirmButton.removeActionListener(listener);
            this.confirmButton.addActionListener(listener);
        } else  {
            this.confirmButton.setText("Kick " + remainingSeats*-1 + " passengers out of the aircraft");
            this.confirmButton.updateUI();
            passengerInteraction.updateUI();
            displayPanel.updateUI();
            this.confirmButton.removeActionListener(listener);
            if (!MaxSeatsExceeded.isWarned()) new MaxSeatsExceeded();
        }
    }

    @Override
    public void updatePassengersConfiguration() {
        updatePassengerLabel();
    }

    @Override
    public void updateConfig() {
        model.seatPassengers();
        model.setPassengersWeight();
    }
}

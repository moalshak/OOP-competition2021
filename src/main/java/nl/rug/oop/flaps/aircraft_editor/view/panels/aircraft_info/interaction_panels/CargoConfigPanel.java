package nl.rug.oop.flaps.aircraft_editor.view.panels.aircraft_info.interaction_panels;

import lombok.Getter;
import lombok.Setter;
import nl.rug.oop.flaps.aircraft_editor.controller.listeners.infopanel_listeners.cargo.CargoDestImportDemandsListener;
import nl.rug.oop.flaps.aircraft_editor.controller.listeners.infopanel_listeners.cargo.CargoListChangeListener;
import nl.rug.oop.flaps.aircraft_editor.controller.listeners.infopanel_listeners.cargo.CargoSliderListener;
import nl.rug.oop.flaps.aircraft_editor.controller.listeners.infopanel_listeners.ConfirmChangeButtonListener;
import nl.rug.oop.flaps.aircraft_editor.model.blueprint.BluePrintModel;
import nl.rug.oop.flaps.aircraft_editor.view.frame.EditorFrame;
import nl.rug.oop.flaps.aircraft_editor.view.panels.aircraft_info.InfoPanel;
import nl.rug.oop.flaps.simulation.model.aircraft.Aircraft;
import nl.rug.oop.flaps.simulation.model.aircraft.CargoArea;
import nl.rug.oop.flaps.simulation.model.airport.Airport;
import nl.rug.oop.flaps.simulation.model.cargo.CargoType;
import nl.rug.oop.flaps.simulation.model.cargo.CargoUnit;
import nl.rug.oop.flaps.simulation.view.panels.airport.CargoPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Getter
public class CargoConfigPanel {
    private final BluePrintModel bluePrintModel;
    private final Aircraft aircraft;
    private final CargoArea selectedCargoArea;
    @Setter
    private CargoUnit selectedCargoUnit;
    @Getter
    private final List<CargoType> cargoType = new ArrayList<>();

    private final JPanel displayPanel;
    private JButton viewImportButton;
    private JList<Object> cargoList;
    private JLabel selectedCargoLabel;
    private final JLabel cargoAreaWeightLabel;
    private final JLabel cargoUnitWeightLabel;
    private final JSlider infoSlider;
    @Getter
    private final JButton confirmButton;

    /**
     * Constructor for the cargo configuration panel
     *
     * @param displayPanel the panel which is to be displayed
     * @param bluePrintModel the bluePrintModel which we contains the data about the aircraft
     */
    public CargoConfigPanel(JPanel displayPanel, BluePrintModel bluePrintModel) {
        this.bluePrintModel = bluePrintModel;
        this.aircraft = bluePrintModel.getAircraft();
        this.selectedCargoArea = bluePrintModel.getSelectedCargo();

        this.displayPanel = displayPanel;
        this.cargoAreaWeightLabel = new JLabel();
        this.cargoUnitWeightLabel = new JLabel();
        this.infoSlider = new JSlider();
        this.infoSlider.addChangeListener(new CargoSliderListener(this));

        this.confirmButton = new JButton();
        confirmButton.addActionListener(new ConfirmChangeButtonListener(this, aircraft));

        initConfigCargoPanel();
    }

    /**
     * Initialises the configuration panel for the cargo area
     */
    public void initConfigCargoPanel() {
        //Initialising pane components
        initImportButton();
        JSplitPane cargoSplitPane = initCargoSplitPane();
        updateCargoAreaWeightLabel();

        infoSlider.setVisible(false);
        confirmButton.setVisible(false);

        //setting up grid layout for displayPanel components
        GridBagConstraints c = new GridBagConstraints();
        FuelConfigPanel.setLayoutOfDisplayPanel(displayPanel, c);
        displayPanel.add(new JLabel("<html><h1>Updating</b> "+ selectedCargoArea.getName() +"</h1></html>"), c);

        //import demands button
        c.ipady = 5;
        c.gridx = 0;
        c.gridy = 1;
        displayPanel.add(viewImportButton, c);

        //split pane
        c.ipady = 0; //resetting padding
        c.gridx = 0;
        c.gridy = 2;
        displayPanel.add(cargoSplitPane, c);

        //cargo area label
        c.gridx = 0;
        c.gridy = 3;
        displayPanel.add(cargoAreaWeightLabel, c);

        //cargo unit label
        c.insets= new Insets(0,20, -15, 0);
        c.gridx = 0;
        c.gridy = 4;
        displayPanel.add(cargoUnitWeightLabel, c);

        //slider
        c.insets= new Insets(0,15, 0, 0);
        c.gridx = 0;
        c.gridy = 5;
        displayPanel.add(infoSlider, c);

        //confirm button
        c.insets= new Insets(20,0, 0, 0);
        c.ipady = 20;
        c.gridx = 0;
        c.gridy = 6;
        displayPanel.add(confirmButton, c);

        //resetting divider location at InfoPanel
        InfoPanel.getInfoSplitPane().setDividerLocation(EditorFrame.getHEIGHT()/2+180);
    }

    /**
     * initialises a button when clicked it displays information about import demands
     */
    private void initImportButton() {
        viewImportButton = new JButton("View Import Demands for destination airport");
        viewImportButton.setVerticalTextPosition(AbstractButton.CENTER);
        viewImportButton.setHorizontalTextPosition(AbstractButton.LEADING);
        viewImportButton.setMnemonic(KeyEvent.VK_D);
        viewImportButton.addActionListener(new CargoDestImportDemandsListener(this));
    }

    /**
     * Creates a splitPane for the cargoList and selectedCargoLabel
     * @return the splitPane containing the cargoList and selectedCargoLabel
     */
    private JSplitPane initCargoSplitPane() {
        //Creates a JList of all the available cargo the user can add to the cargo area
        Map<String, CargoType> cargoTypeMap = aircraft.getWorld().getCargoTypes();
        List<String> cargoTypeNames = new ArrayList<>();
        cargoTypeMap.values().stream().sorted().forEach(cargo -> {
            cargoTypeNames.add(cargo.getName());
            cargoType.add(cargo);
        });

        cargoList = new JList<>(cargoTypeNames.toArray());
        cargoList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        cargoList.addListSelectionListener(new CargoListChangeListener(this));
        JScrollPane cargoListScrollPane = new JScrollPane(cargoList);

        //Label updates and notifies the user when they select different cargo in the list
        selectedCargoLabel = new JLabel();
        selectedCargoLabel.setFont(selectedCargoLabel.getFont().deriveFont(Font.ITALIC));
        selectedCargoLabel.setHorizontalAlignment(JLabel.CENTER);

        return new JSplitPane(JSplitPane.VERTICAL_SPLIT, cargoListScrollPane, selectedCargoLabel);
    }

    /**
     * updates the cargo area weight label
     */
    public void updateCargoAreaWeightLabel(){
        cargoAreaWeightLabel.setText("<html><h2> Cargo Area Weight: " + ((int) aircraft.getCargoAreaWeight(selectedCargoArea)) + " Kg</h2></html>");
    }

    /**
     * Initialises the slider that enables the user to add or remove cargo
     */
    public void initSlider() {
        //Updating information based on the slider status
        selectedCargoLabel.setText("<html>Selected <b>" + selectedCargoUnit.getCargoType().getName() + "</b> to add in cargo area</b></html>");
        ImageIcon icon = selectedCargoUnit.getCargoType().getIcon();
        if (icon != null) selectedCargoLabel.setIcon(icon);
        confirmButton.setVisible(true);
        confirmButton.setText("Confirm Cargo Changes");

        //Slider logic to show different information depending on whether the cargo has been added before
        int remainingWeight = (int) (selectedCargoArea.getMaxWeight() - aircraft.getCargoAreaWeight(selectedCargoArea));
        infoSlider.setMaximum(remainingWeight);
        infoSlider.setVisible(true);
        CargoUnit foundUnit = aircraft.existsInCargoArea(selectedCargoArea, selectedCargoUnit);
        if (foundUnit == null) {
            infoSlider.setValue(0);
            if (remainingWeight == 0) {
                selectedCargoLabel.setText("<html>Cargo full cannot add <b>" + selectedCargoUnit.getCargoType().getName() + "</b> to cargo area.</html>");
                confirmButton.setText("Remove some cargo to add " + selectedCargoUnit.getCargoType().getName());
                infoSlider.setVisible(false);
            }
        } else {
            infoSlider.setMaximum((int) (remainingWeight+foundUnit.getWeight()));
            infoSlider.setValue((int) foundUnit.getWeight());
            if (remainingWeight == 0) {
                infoSlider.setMaximum((int) foundUnit.getWeight());
                infoSlider.setValue((int) foundUnit.getWeight());
            }
        }

        //Adjusting Look and feel of slider
        infoSlider.setMinorTickSpacing((int)selectedCargoArea.getMaxWeight()/10);
        infoSlider.setMajorTickSpacing((int)selectedCargoArea.getMaxWeight()/10);
        infoSlider.setFont(new Font("MV Boli", Font.PLAIN, 15));
        infoSlider.setPaintTicks(true);
        infoSlider.setPaintLabels(true);
        infoSlider.setPaintTrack(true);
    }

    /**
     * Initialises the cargo unit wight labels
     */
    public void initCargoUnitLabel() {
        //updating the weight labels for the selected cargo type
        CargoUnit foundUnit = aircraft.existsInCargoArea(selectedCargoArea, selectedCargoUnit);
        if (foundUnit == null) {
            cargoUnitWeightLabel.setText("<html><h3>"+ selectedCargoUnit.getCargoType().getName() + " Weight: 0 Kg </h3></html>");
        } else {
            cargoUnitWeightLabel.setText("<html><h3>"+ foundUnit.getCargoType().getName() + " Weight: " + (int) foundUnit.getWeight() +" Kg </h3></html>");
            selectedCargoUnit.setWeight(foundUnit.getWeight());
        }
    }

    /**
     * updates the {@link CargoConfigPanel#cargoUnitWeightLabel}
     * */
    public void updateCargoUnitWeightLabel() {
        //updates cargo unit information
        cargoUnitWeightLabel.setText("<html><h3>"+selectedCargoUnit.getCargoType().getName() + " Weight: " + infoSlider.getValue() +" Kg</h3></html>");
    }

    /**
     * opens a new frame showing the import demands in the destination import
     * */
    public void viewDestinationImportDemands() {
            //creates and displays frame for import demands
            Airport destinationAirport = aircraft.getWorld().getSelectionModel().getSelectedDestinationAirport();
            JFrame frame = new JFrame("Destination Import Demands");
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.getContentPane().add(new CargoPanel(destinationAirport.getCargoImportDemands(),"Destination Import Demands"));
            frame.setLocationRelativeTo(null);
            frame.pack();
            frame.setVisible(true);
    }

}

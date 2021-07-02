package nl.rug.oop.flaps.simulation.view.panels.trip;

import nl.rug.oop.flaps.aircraft_editor.model.config_models.passenger.PassengersModel;
import nl.rug.oop.flaps.simulation.model.aircraft.Aircraft;
import nl.rug.oop.flaps.simulation.model.aircraft.CargoArea;
import nl.rug.oop.flaps.simulation.model.cargo.CargoUnit;
import nl.rug.oop.flaps.simulation.model.trips.Trip;
import nl.rug.oop.flaps.simulation.model.world.WorldSelectionModel;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.util.List;

public class AircraftContents extends JPanel {
    private final WorldSelectionModel sm;
    private final Aircraft aircraft;

    public AircraftContents(WorldSelectionModel selectionModel) {
        this.sm = selectionModel;
        this.aircraft = sm.getSelectedAircraft();

        setLayout(new BorderLayout());
        JLabel title = new JLabel("Aircraft's content:");
        title.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 30));
        add( title, BorderLayout.NORTH);
        add(displayContent(), BorderLayout.CENTER);
    }

    /**
     * displays the content of the two tables
     * */
    private JSplitPane displayContent() {
        Trip selectedTrip = sm.getSelectedTrip();

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment( JLabel.CENTER );

        JScrollPane panel = new JScrollPane(this.makePassengersTable(centerRenderer));
        JScrollPane panel1 = new JScrollPane(makeCargoTable(selectedTrip, centerRenderer));

        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, panel,panel1);
        splitPane.setDividerLocation(100);
        splitPane.setDividerSize(0);
        return splitPane;
    }

    /**
     * makes the table for passengers
     * @return the passengers info table
     * */
    private JTable makePassengersTable(DefaultTableCellRenderer centerRenderer) {
        String[] colNames1 = {"Adults", "Kids 12+", "Kids Under 12", "Crew"};
        Object[][] data1 = {
                {aircraft.getPassengers().get("adults") + " on board || Ticket: € " + aircraft.getTicketAdults(),
                        aircraft.getPassengers().get("kidsTo12") + " on board || Ticket: € " + aircraft.getTicketKids12(),
                        aircraft.getPassengers().get("kidsUnder12")+ " on board || Ticket: € " + aircraft.getTicketKids(),
                        aircraft.getCrewOnBoard() + " on board exc. (co-)pilot"
                }
        };
        JTable table = new JTable(data1, colNames1);
        centerColumns(table, centerRenderer, colNames1.length);
        table.setEnabled(false);
        return table;
    }

    /** makes the table for cargo
     * @return the cargo info table
     * */
    private JTable makeCargoTable(Trip selectedTrip, DefaultTableCellRenderer centerRenderer) {
        List<CargoArea> list = selectedTrip.getAircraft().getType().getCargoAreas();
        String[] colNames = new String[list.size()]; int i = 0; int j = 0;
        Object[][] data = new Object[10][list.size()];
        for (CargoArea cargoArea : list) {
            colNames[i] = cargoArea.getName();
            i++;
        }

        for (CargoArea cargoArea : list) {
            i = 0;
            for (CargoUnit unit : selectedTrip.getAircraft().getCargoAreaContents(cargoArea)) {
                data[i][j] = unit.getCargoType().getName() + " || " + unit.getWeight() + " Kg";
                i++;
            }
            j++;
        }
        JTable table = new JTable(data, colNames);
        centerColumns(table, centerRenderer, list.size());
        table.setEnabled(false);
        return table;
    }

    /**
     * centers the text in the columns of the table
     * */
    private void centerColumns(JTable table, DefaultTableCellRenderer centerRenderer, int size) {
        for(int x=0;x<size;x++){
            table.getColumnModel().getColumn(x).setCellRenderer( centerRenderer );
        }
    }

}

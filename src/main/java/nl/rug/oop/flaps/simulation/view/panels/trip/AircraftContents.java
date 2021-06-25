package nl.rug.oop.flaps.simulation.view.panels.trip;

import nl.rug.oop.flaps.simulation.model.aircraft.Aircraft;
import nl.rug.oop.flaps.simulation.model.aircraft.CargoArea;
import nl.rug.oop.flaps.simulation.model.cargo.CargoUnit;
import nl.rug.oop.flaps.simulation.model.trips.Trip;
import nl.rug.oop.flaps.simulation.model.world.WorldSelectionModel;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class AircraftContents extends JPanel {
    private WorldSelectionModel sm;
    private int WIDTH;
    private GridBagConstraints gbc = new GridBagConstraints();

    public AircraftContents(WorldSelectionModel selectionModel, int width) {
        this.sm = selectionModel;
        this.WIDTH = width;
        setLayout(new BorderLayout());

        add(new JLabel("Aircraft's content:"), BorderLayout.NORTH);
        add(displayContent(), BorderLayout.CENTER);
    }

    private JSplitPane displayContent() {
        Trip selectedTrip = sm.getSelectedTrip();
        Aircraft aircraft = selectedTrip.getAircraft();

        String[] colNames1 = {"Adults", "Kids 12+", "Kids Under 12"};
        Object[][] data1 = {
                {aircraft.getPassengers().get("adults"), aircraft.getPassengers().get("kidsTo12"), aircraft.getPassengers().get("kidsUnder12")}
        };
        JTable table = new JTable(data1, colNames1);
        JScrollPane panel = new JScrollPane(table);

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
        JTable table1 = new JTable(data, colNames);
        JScrollPane panel1 = new JScrollPane(table1);

        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, panel,panel1);
        splitPane.setDividerLocation(100);
        return splitPane;
    }

}

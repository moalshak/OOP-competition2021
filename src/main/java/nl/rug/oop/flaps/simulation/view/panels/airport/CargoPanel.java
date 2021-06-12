package nl.rug.oop.flaps.simulation.view.panels.airport;

import nl.rug.oop.flaps.simulation.model.cargo.CargoType;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

/**
 * Displays a list of cargo types and their demands
 *
 * @author T.O.W.E.R.
 */
public class CargoPanel extends JPanel {
    private final static int ICON_SIZE = 40;
    private final static int CARGO_LIST_HEIGHT = 200;
    private final static int PADDING = 5;

    private final String name;

    private final Map<CargoType, Double> cargoDemands;

    public CargoPanel(Map<CargoType, Double> cargoDemands) {
        super(new BorderLayout());
        this.name = "Import Demands";
        this.cargoDemands = cargoDemands;
        displayStorageUnit();
    }

    public CargoPanel(Map<CargoType, Double> cargoDemands, String name) {
        super(new BorderLayout());
        this.name = name;
        this.cargoDemands = cargoDemands;
        displayStorageUnit();
    }

    private void displayStorageUnit() {
        JLabel nameLabel = new JLabel(name);
        nameLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));
        add(nameLabel, BorderLayout.NORTH);

        JPanel cargoList = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;

        cargoDemands.keySet().stream().sorted().forEach(k -> addCargoField(k, cargoDemands.get(k), cargoList, gbc));
        JScrollPane scrollPane = new JScrollPane(buildFillPanel(cargoList),
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(5);
        scrollPane.setMinimumSize(new Dimension(getWidth(), CARGO_LIST_HEIGHT));
        add(scrollPane, BorderLayout.CENTER);
    }

    private JPanel buildFillPanel(JComponent component) {
        GridBagConstraints fillConstraints = new GridBagConstraints();
        fillConstraints.gridwidth = GridBagConstraints.REMAINDER;
        fillConstraints.anchor = GridBagConstraints.NORTHWEST;
        fillConstraints.weighty = 1;
        fillConstraints.weightx = 1;

        JPanel fillPanel = new JPanel(new GridBagLayout());
        fillPanel.setOpaque(false);
        fillPanel.add(component, fillConstraints);
        return fillPanel;
    }

    private void addCargoField(CargoType cargo, double demand, JPanel panel, GridBagConstraints panelConstraints) {
        panelConstraints.gridx = 0;
        panelConstraints.anchor = GridBagConstraints.LINE_START;
        panelConstraints.insets = new Insets(0, PADDING, 0, PADDING);
        Image image = cargo.getImage();
        int width = image.getWidth(null);
        int height = image.getHeight(null);
        double scaleFactor = ICON_SIZE / (double) height;
        panel.add(new JLabel(new ImageIcon(image.getScaledInstance((int) (width * scaleFactor), ICON_SIZE, Image.SCALE_SMOOTH))), panelConstraints);
        panelConstraints.gridx++;
        panel.add(new JLabel(cargo.getName()), panelConstraints);
        panelConstraints.gridx++;
        JTextField valueField = new JTextField(String.format("%.2f", demand));
        valueField.setEditable(false);
        panel.add(valueField, panelConstraints);
        panelConstraints.gridy++;
    }
}

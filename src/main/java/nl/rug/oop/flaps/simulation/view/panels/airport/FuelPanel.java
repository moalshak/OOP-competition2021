package nl.rug.oop.flaps.simulation.view.panels.airport;

import nl.rug.oop.flaps.simulation.model.aircraft.FuelType;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

/**
 * Displays a list of fuel types and their prices
 *
 * @author T.O.W.E.R.
 */
public class FuelPanel extends JPanel {
	private final static int FUEL_LIST_HEIGHT_LIST_HEIGHT = 200;
	private final static int PADDING = 5;

	private final String name;

	private final Map<FuelType, Double> fuelPrices;

	public FuelPanel(Map<FuelType, Double> fuelPrices) {
		super(new BorderLayout());
		this.name = "Fuel Prices per kg in €";
		this.fuelPrices = fuelPrices;
		displayStorageUnit();
	}

	private void displayStorageUnit() {
		JLabel nameLabel = new JLabel(name);
		nameLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));
		add(nameLabel, BorderLayout.NORTH);

		JPanel fuelList = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;

		fuelPrices.keySet().stream().sorted().forEach(k -> addCargoField(k, fuelPrices.get(k), fuelList, gbc));
		JScrollPane scrollPane = new JScrollPane(buildFillPanel(fuelList),
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.getVerticalScrollBar().setUnitIncrement(5);
		scrollPane.setMinimumSize(new Dimension(getWidth(), FUEL_LIST_HEIGHT_LIST_HEIGHT));
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

	private void addCargoField(FuelType fuelType, double price, JPanel panel, GridBagConstraints panelConstraints) {
		panelConstraints.gridx = 0;
		panelConstraints.anchor = GridBagConstraints.LINE_START;
		panelConstraints.insets = new Insets(0, PADDING, 0, PADDING);
		panel.add(new JLabel(fuelType.getName()), panelConstraints);
		panelConstraints.gridx++;
		JTextField valueField = new JTextField(String.format("%.2f", price));
		valueField.setEditable(false);
		panel.add(valueField, panelConstraints);
		panelConstraints.gridy++;
	}
}

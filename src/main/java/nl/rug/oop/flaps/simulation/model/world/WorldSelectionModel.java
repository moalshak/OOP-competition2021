package nl.rug.oop.flaps.simulation.model.world;

import lombok.Getter;
import lombok.extern.java.Log;
import nl.rug.oop.flaps.simulation.model.aircraft.Aircraft;
import nl.rug.oop.flaps.simulation.model.airport.Airport;

import java.util.HashSet;
import java.util.Set;

/**
 * Model that keeps track of the state of user interaction in the application.
 * <p>
 *     Using the listener pattern, classes which implement {@link WorldSelectionModelListener}
 *     can <em>subscribe</em> to certain kinds of updates for when this model
 *     changes, by implementing certain methods.
 * </p>
 *
 * @author T.O.W.E.R.
 */
@Log
public class WorldSelectionModel {
	@Getter
	private Aircraft selectedAircraft = null;
	@Getter
	private Airport selectedAirport = null;
	@Getter
	private Airport selectedDestinationAirport = null;

	@Getter
	private boolean selectingDestination = false;
	@Getter
	private int destinationSelectionCursorX;
	@Getter
	private int destinationSelectionCursorY;

	private final Set<WorldSelectionModelListener> listeners;

	public WorldSelectionModel() {
		listeners = new HashSet<>();
	}

	public void addListener(WorldSelectionModelListener listener) {
		log.info("Adding selection listener to world.");
		this.listeners.add(listener);
	}

	public void setSelectedAirport(Airport airport) {
		this.selectedAirport = airport;
		this.selectedAircraft = null;
		this.selectedDestinationAirport = null;
		this.listeners.forEach(lst -> {
			lst.airportSelected(airport);
			lst.aircraftSelected(null);
			lst.destinationAirportSelected(null);
		});
	}

	public void setSelectedDestinationAirport(Airport airport) {
		this.selectedDestinationAirport = airport;
		this.selectingDestination = false;
		this.listeners.forEach(lst -> {
			lst.destinationAirportSelected(airport);
			lst.destinationSelectionUpdated();
		});
	}

	public void setSelectedAircraft(Aircraft aircraft) {
		this.selectedAircraft = aircraft;
		this.selectedDestinationAirport = null;
		this.listeners.forEach(lst -> {
			lst.aircraftSelected(aircraft);
			lst.destinationAirportSelected(null);
		});
	}

	public void startSelectingDestination() {
		this.selectingDestination = true;
		this.listeners.forEach(WorldSelectionModelListener::destinationSelectionUpdated);
	}

	public void setDestinationSelectionCursorPosition(int x, int y) {
		this.destinationSelectionCursorX = x;
		this.destinationSelectionCursorY = y;
		this.listeners.forEach(WorldSelectionModelListener::destinationSelectionUpdated);
	}
}

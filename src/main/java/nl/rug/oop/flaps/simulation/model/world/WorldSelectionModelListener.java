package nl.rug.oop.flaps.simulation.model.world;

import nl.rug.oop.flaps.simulation.model.aircraft.Aircraft;
import nl.rug.oop.flaps.simulation.model.airport.Airport;

/**
 * A world selection model listener is any object that should react to changes
 * to the world's selection information.
 * <p>
 *     Implementations can choose to implement only certain methods of this
 *     interface, depending on what sort of events they'd like to be notified of.
 * </p>
 *
 * @author T.O.W.E.R.
 */
public interface WorldSelectionModelListener {
	/**
	 * Called when the selected airport changes.
	 * @param selectedAirport The airport that is now selected; might be null.
	 */
	default void airportSelected(Airport selectedAirport) {}

	/**
	 * Called when the selected destination airport changes.
	 * @param destinationAirport The airport that is now selected as the
	 *                           destination; might be null.
	 */
	default void destinationAirportSelected(Airport destinationAirport) {}

	/**
	 * Called when an aircraft is selected.
	 * @param aircraft The aircraft that was selected; might be null.
	 */
	default void aircraftSelected(Aircraft aircraft) {}

	/**
	 * Called when the state of destination selection has updated in some way,
	 * such as the user moving their mouse while selecting a destination.
	 */
	default void destinationSelectionUpdated() {}
}

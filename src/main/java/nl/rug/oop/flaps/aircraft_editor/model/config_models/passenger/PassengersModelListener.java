package nl.rug.oop.flaps.aircraft_editor.model.config_models.passenger;

/**
 * interface for {@link PassengersModel}
 * */
public interface PassengersModelListener {
    /**
     * updates the passengers configuration
     * */
    default void updatePassengersConfiguration(){}
}

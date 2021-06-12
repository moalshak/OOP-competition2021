package nl.rug.oop.flaps.aircraft_editor.model.blueprint;
/**
 * interface for the model the has the {@link BluePrintModelListener#pointSelectedUpdater()}
 * so when a point is selected the points are redrawn with the selected one bigger.
 * */
public interface BluePrintModelListener {
    /**
     * updates the points
     * so when a point is selected the points are redrawn with the selected one bigger.
     * */
    default void pointSelectedUpdater() {}
}

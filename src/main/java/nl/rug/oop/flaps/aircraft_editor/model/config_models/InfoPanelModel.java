package nl.rug.oop.flaps.aircraft_editor.model.config_models;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.java.Log;
import nl.rug.oop.flaps.aircraft_editor.model.config_models.trip_results.ProfitEstimationModel;
import nl.rug.oop.flaps.aircraft_editor.model.config_models.trip_results.AircraftRangeModel;
import nl.rug.oop.flaps.aircraft_editor.model.config_models.aircraft.AircraftWeightModel;
import nl.rug.oop.flaps.aircraft_editor.view.panels.aircraft_info.InfoPanel;
import nl.rug.oop.flaps.aircraft_editor.view.panels.aircraft_info.ResultPanel;
import nl.rug.oop.flaps.simulation.model.aircraft.Aircraft;
import nl.rug.oop.flaps.simulation.model.airport.Airport;
import nl.rug.oop.flaps.simulation.model.world.WorldSelectionModel;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

/** <p>
 * this class is a model that holds all the info needed for the {@link InfoPanel}
 * and therefore has list of listeners to update. </p>
 * <p>
 * Helper models are: </p>
 * <ol>
 *     <li> {@link AircraftRangeModel} </li>
 *     <li> {@link AircraftWeightModel} </li>
 *     <li> {@link ProfitEstimationModel} </li>
 *
 * </ol>
 *
 * */
@Getter @Log
public class InfoPanelModel  {
    private final Aircraft aircraft;
    private final WorldSelectionModel selectionModel;
    private final Airport originAirport;
    private final Airport destinationAirport;

    @Setter
    private double aircraftRange;
    @Getter @Setter
    private static Point2D centerOfGravity;
    @Setter
    private double profitEstimation;
    @Setter
    private double cost;
    @Setter
    private double Revenue;
    @Setter
    private double aircraftWeight;
    @Setter
    private double tripDistance;
    @Getter
    private static List<InfoPanelModelListener> listeners;
    @Getter
    private static ResultPanel resultPanel;
    @Getter
    private static InfoPanelModel infoPanelModel;
    public InfoPanelModel(Aircraft aircraft, WorldSelectionModel selectionModel) {
        this.aircraft = aircraft;
        this.selectionModel = selectionModel;
        this.originAirport = selectionModel.getSelectedAirport();
        this.destinationAirport = selectionModel.getSelectedDestinationAirport();
        listeners = new ArrayList<>();
        resultPanel = getResultPanel();

        new AircraftRangeModel(this);

        new ProfitEstimationModel(this);

        calcAircraftWeight(selectionModel);
        infoPanelModel = this;
    }

    /**
     * calculates the aircraft's weight through {@link AircraftWeightModel}
     * */
    private void calcAircraftWeight(WorldSelectionModel selectionModel) {
        AircraftWeightModel aircraftWeightModel = new AircraftWeightModel(this);
        aircraftWeight = aircraftWeightModel.getAircraftWeight();
        /* get distance to airport then convert to Km */
        tripDistance = Math.round(selectionModel.getSelectedAirport().getGeographicCoordinates().
                distanceTo(selectionModel.getSelectedDestinationAirport().getGeographicCoordinates()) * 100.0) / 100.0;
        tripDistance /= 1000;
    }

    /**
     * adds listener to listeners list
     * */
    public void addListener(InfoPanelModelListener listener) {
        listeners.add(listener);
    }

}

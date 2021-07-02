package nl.rug.oop.flaps.aircraft_editor.model.config_models.trip_results;

import lombok.extern.java.Log;
import nl.rug.oop.flaps.aircraft_editor.controller.listeners.infopanel_listeners.ConfirmChangeButtonListener;
import nl.rug.oop.flaps.aircraft_editor.model.config_models.InfoPanelModel;
import nl.rug.oop.flaps.aircraft_editor.model.config_models.InfoPanelModelListener;
import nl.rug.oop.flaps.aircraft_editor.model.config_models.passenger.PassengersModel;
import nl.rug.oop.flaps.simulation.model.aircraft.Aircraft;
import nl.rug.oop.flaps.simulation.model.aircraft.FuelType;
import nl.rug.oop.flaps.simulation.model.airport.Airport;

/**
 * this class calculates the ProfitEstimationModel for the aircraft's trip.
 * This class updates each time the {@link ConfirmChangeButtonListener} is in action.
 * */
@Log
public class ProfitEstimationModel implements InfoPanelModelListener {
    private final Aircraft aircraft;
    private static Airport originAirport = null;
    private static Airport destinationAirport = null;
    private final FuelType fuelType;
    private final InfoPanelModel infoPanelModel;

    public ProfitEstimationModel(InfoPanelModel infoPanelModel) {
        infoPanelModel.addListener(this);
        this.infoPanelModel = infoPanelModel;

        this.aircraft = infoPanelModel.getAircraft();
        originAirport = infoPanelModel.getOriginAirport();
        destinationAirport = infoPanelModel.getDestinationAirport();
        this.fuelType = infoPanelModel.getAircraft().getType().getFuelType();
        Airport destinationAirport = aircraft.getWorld().getSelectionModel().getSelectedDestinationAirport();
        if (destinationAirport != null) {
            calcProfit();
        }
    }

    /**
     * Calculates the profit of of trip using cost of fuel and revenue form cargo
     * the updates values in the {@link InfoPanelModel}
     */
    private void calcProfit() {
        /* the cost does not go higher than the total needed fuel for the trip */
        double cost;
        if (aircraft.getTotalFuel() >= aircraft.getFuelConsumption(originAirport, destinationAirport)) {
            cost = originAirport.getFuelPriceByType(fuelType) * aircraft.getFuelConsumption(originAirport, destinationAirport);
        } else {
            cost = originAirport.getFuelPriceByType(fuelType) * aircraft.getTotalFuel();
        }
        double cargoRevenue = aircraft.getCargoRevenue();
        int ticketsRev = 0;
        var passengers = aircraft.getPassengers();
        ticketsRev += passengers.get("adults") * aircraft.getTicketAdults();
        ticketsRev += passengers.get("kidsTo12") * aircraft.getTicketKids12();
        ticketsRev += passengers.get("kidsUnder12") * aircraft.getTicketKids();

        double totRev = ticketsRev + cargoRevenue;

        infoPanelModel.setCost(cost);
        infoPanelModel.setRevenue(totRev);
        infoPanelModel.setProfitEstimation(totRev - cost);
    }

    @Override
    public void updateConfig() {
        log.info("Updating the profit");
        calcProfit();
    }
}

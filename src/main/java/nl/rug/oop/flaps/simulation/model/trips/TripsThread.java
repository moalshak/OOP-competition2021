package nl.rug.oop.flaps.simulation.model.trips;

import lombok.SneakyThrows;
import nl.rug.oop.flaps.simulation.controller.SpeedRateUp;

public class TripsThread extends Thread{

    private final Trip newTrip;
    private final long RATE;

    /**
     * creates a new thread to process the trip
     * */
    public TripsThread(Trip newTrip) {
        this.newTrip = newTrip;
        String type = newTrip.getAircraft().getType().getName();
        if(type.equals("Boeing 747-400F")) {
            this.RATE = 100;
        } else if (type.equals("Boeing 737-800BCF Freighter")) {
            this.RATE = 150;
        } else {
            this.RATE = 200;
        }
    }

    @SneakyThrows
    @Override
    public void run() {
        while (!newTrip.isReachedDestination()) {
            newTrip.cruise();
            long newRate = RATE;
            if (SpeedRateUp.getRATE() == 0.5) {
                newRate = (long) (newRate * 1.5);
            }
            for (double i = (SpeedRateUp.getRATE()/0.5-2); i > 0; i--) {
                newRate = newRate / 2;
            }
            Thread.sleep(newRate);
        }
    }

}

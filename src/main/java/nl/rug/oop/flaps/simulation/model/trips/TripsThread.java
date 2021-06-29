package nl.rug.oop.flaps.simulation.model.trips;

import lombok.SneakyThrows;

public class TripsThread extends Thread{

    private final Trip newTrip;
    private static final int RATE = 500;

    /**
     * creates a new thread to process the trip
     * */
    public TripsThread(Trip newTrip) {
        this.newTrip = newTrip;
    }

    @SneakyThrows
    @Override
    public void run() {
        while (!newTrip.isReachedDestination()) {
            newTrip.cruise();
            Thread.sleep(RATE);
        }
    }

}

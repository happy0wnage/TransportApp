package ua.petrov.transport.simulation.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.petrov.transport.core.entity.Bus;
import ua.petrov.transport.core.entity.Passenger;
import ua.petrov.transport.core.entity.Route;
import ua.petrov.transport.core.entity.Station;
import ua.petrov.transport.core.util.RouteFactory;
import ua.petrov.transport.simulation.generator.PassengerGenerator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Владислав on 03.01.2016.
 */
public class PassengerController {

    private static final Logger LOGGER = LoggerFactory.getLogger(PassengerController.class);

   private PassengerGenerator passengerGenerator;



    public static int boarding(List<Route> routes, List<Passenger> passengers, Bus bus, List<Passenger> statisticsGoAway, List<Double> prices) {
        int amountOfPassengers = 0;
        List<Passenger> allPassengers = new ArrayList<>(passengers);
        long minTime = bus.getTravelTimeLong() + bus.getStartTimeLong();
        long maxTime = minTime + bus.getCurrentStation().getStopTimeLong();

        for (Passenger passenger : allPassengers) {
            if (isPassengerCanBoard(passenger, bus, minTime, maxTime)) {
                Route route = bus.getRoute();
                Station currentStation = passenger.getCurrentStation();
                Station destination = passenger.getDestination();

                if (RouteFactory.routesByTwoStations(routes, currentStation, destination, bus.getDirection()).contains(route)) {
                    if (isPassengerReady(passenger, bus, maxTime, prices)) {
                        bus.addToPassengerList(passenger);
                        passengers.remove(passenger);
                        amountOfPassengers++;
                    }
                }
            } else if (passenger.getTimeOfTheDay() < minTime && passenger.getCurrentStation().equals(bus.getCurrentStation())) {
                statisticsGoAway.add(passenger);
                passengers.remove(passenger);
            }
        }

        LOGGER.info("Got on the bus:        " + amountOfPassengers);
        LOGGER.info("Now In the bus:        " + bus.getPassengerList().size());
        return amountOfPassengers;
    }

    public static int landing(Bus bus, List<Passenger> statisticsCome) {
        int amountOfPassengers = 0;
        List<Passenger> passengersInTheBus = new ArrayList<>(bus.getPassengerList());
        for (Passenger passenger : passengersInTheBus) {
            if (passenger.getDestination().equals(bus.getCurrentStation())) {
                bus.removeFromPassengersList(passenger);
                statisticsCome.add(passenger);
                amountOfPassengers++;
            }
        }
        LOGGER.info("Left the bus:          " + amountOfPassengers);
        return amountOfPassengers;
    }

    private static boolean isPassengerReady(Passenger passenger, Bus bus, long busMaxTime, List<Double> prices) {
        List<Double> allPrices = new ArrayList<>(prices);
        Collections.sort(allPrices);

        if (Collections.min(allPrices) == bus.getRoute().getPrice()) {
            return true;
        }

        long timeOfTheDay = passenger.getTimeOfTheDay();
        long waitingTime = passenger.getWaitingTime();
        long maxTimeOfTheDay = timeOfTheDay + waitingTime;

        if (maxTimeOfTheDay > busMaxTime && maxTimeOfTheDay - busMaxTime > 600) {
            return false;
        }

        return true;

    }

    private static boolean isPassengerCanBoard(Passenger passenger, Bus bus, long minTime, long maxTime) {
        long timeOfTheDay = passenger.getTimeOfTheDay();
        long waitingTime = passenger.getWaitingTime();

        Station passengerStation = passenger.getCurrentStation();
        int currentSize = bus.getPassengerList().size();

        if (passengerStation.equals(bus.getCurrentStation())) {
            if (isOnLimit(timeOfTheDay, minTime, maxTime, waitingTime) && currentSize< bus.getSeat()){
                return true;
            }
        }
        return false;
    }

    private static boolean isOnLimit(long timeOfTheDay, long minTime, long maxTime, long waitingTime) {
        if (((timeOfTheDay >= minTime && timeOfTheDay <= maxTime) || (timeOfTheDay + waitingTime >= minTime))) {
            return true;
        }
        return false;
    }

}

package ua.petrov.transport.simulation.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.petrov.transport.core.entity.Bus;
import ua.petrov.transport.core.entity.Passenger;
import ua.petrov.transport.core.entity.Route;
import ua.petrov.transport.core.entity.Station;
import ua.petrov.transport.core.util.RouteFactory;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Владислав on 03.01.2016.
 */
public class PassengerController {

    private static final Logger LOGGER = LoggerFactory.getLogger(PassengerController.class);

    private List<Route> routes;

    private List<Passenger> allPassengers;

    private int statisticsGoAway;

    private int statisticsCome;

    private List<Integer> statisticsPercent;

    public PassengerController(List<Route> routes, List<Passenger> passengers) {
        this.routes = routes;
        this.allPassengers = passengers;
        statisticsPercent = new ArrayList<>();
    }

    public int getStatisticsCome() {
        return statisticsCome;
    }

    public int getStatisticsGoAway() {
        return statisticsGoAway;
    }

    public List<Integer> getStatisticsPercent() {
        return statisticsPercent;
    }

    public void setStatisticsPercent(List<Integer> statisticsPercent) {
        this.statisticsPercent = statisticsPercent;
    }

    public void boarding(Bus bus, List<Double> prices) {
        int amountOfPassengers = 0;
        List<Passenger> passengers = new ArrayList<>(allPassengers);
        long busMinTime = bus.getTravelTimeLong() + bus.getStartTimeLong();
        long busMaxTime = busMinTime + bus.getCurrentStation().getStopTimeLong();

        for (Passenger passenger : passengers) {
            long passengerMinTime = passenger.getTimeOfTheDay();
            long passengerMaxTime = passenger.getWaitingTime() + passengerMinTime;

            if (passengerMaxTime < busMinTime) {
                allPassengers.remove(passenger);
                statisticsGoAway++;
                if(bus.getCurrentStation().equals(passenger.getCurrentStation())) {
                    LOGGER.error("!!!Не успел ни на один автобус: " + passenger.getCurrentStation().getName() + "\t" + LocalTime.ofSecondOfDay(passenger.getTimeOfTheDay()) +
                            ". Время ожидения" + LocalTime.ofSecondOfDay(passenger.getWaitingTime()));
                }
                continue;
            }

            if (isPassengerCanBoard(passenger, bus, busMinTime, busMaxTime)) {
                Route route = bus.getRoute();
                Station currentStation = passenger.getCurrentStation();
                Station destination = passenger.getDestination();

                if (RouteFactory.routesByTwoStations(routes, currentStation, destination, bus.getDirection()).contains(route)) {
                    if(isPassengerReady(passenger, bus, busMaxTime, prices)) {
                        bus.addToPassengerList(passenger);
                        allPassengers.remove(passenger);
                        amountOfPassengers++;
                    } else {
                        passenger.setWaitingTime(passengerMaxTime - busMaxTime);
//                        LOGGER.error("Подождет другого автобуса: " + LocalTime.ofSecondOfDay(passenger.getTimeOfTheDay()) +
//                                ". Время ожидения" + LocalTime.ofSecondOfDay(passenger.getWaitingTime()));
                    }
                } else {
//                    LOGGER.error("Нет подходящих автобусов без пересадок");
                }
            }
        }
        LOGGER.info("Got on the bus:        " + amountOfPassengers);
        LOGGER.info("Now In the bus:        " + bus.getPassengerList().size());
        statisticsPercent.add((amountOfPassengers * 100) / bus.getSeat());
    }

    public void landing(Bus bus) {
        int amountOfPassengers = 0;
        List<Passenger> passengersInTheBus = new ArrayList<>(bus.getPassengerList());
        for (Passenger passenger : passengersInTheBus) {
            if (passenger.getDestination().equals(bus.getCurrentStation())) {
                bus.removeFromPassengersList(passenger);
                amountOfPassengers++;
            }
        }
        statisticsCome += amountOfPassengers;
        LOGGER.info("Left the bus:          " + amountOfPassengers);
    }

    private boolean isPassengerReady(Passenger passenger, Bus bus, long busMaxTime, List<Double> prices) {
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

    private boolean isPassengerCanBoard(Passenger passenger, Bus bus, long minTime, long maxTime) {
        long timeOfTheDay = passenger.getTimeOfTheDay();
        long waitingTime = passenger.getWaitingTime();

        Station passengerStation = passenger.getCurrentStation();
        int currentSize = bus.getPassengerList().size();

        if (passengerStation.equals(bus.getCurrentStation())) {
            if (isOnLimit(timeOfTheDay, minTime, maxTime, waitingTime) && currentSize < bus.getSeat()) {
                return true;
            }
        }
        return false;
    }

    private boolean isOnLimit(long timeOfTheDay, long minTime, long maxTime, long waitingTime) {
        if (((timeOfTheDay >= minTime || timeOfTheDay + waitingTime >= minTime) && timeOfTheDay <= maxTime)) {
            return true;
        }
        return false;
    }

    public double getStatisticPercent() {
        int sum = 0;
        for (Integer s : statisticsPercent) {
            sum += s;
        }
        return sum / statisticsPercent.size();
    }

}

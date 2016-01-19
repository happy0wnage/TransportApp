package ua.petrov.transport.simulation.generator;

import ua.petrov.transport.core.entity.Passenger;
import ua.petrov.transport.core.entity.Route;
import ua.petrov.transport.core.entity.Station;
import ua.petrov.transport.core.JAXB.passengers.ArrivalPeriod;
import ua.petrov.transport.core.JAXB.passengers.DailyFlow;
import ua.petrov.transport.core.util.random.Randomize;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class PassengerGenerator {

    private static final int DISPERSION = 5;

    private List<Route> routes;

    private List<Station> stations;

    private DailyFlow flow;

    public PassengerGenerator(List<Route> routes, List<Station> stations, DailyFlow flow) {
        this.routes = routes;
        this.stations = stations;
        this.flow = flow;
    }

    public List<Passenger> generatePassengerPerDay() {
        List<Passenger> passengers = new ArrayList<>();
        Random rand = new Random();

        List<ArrivalPeriod> arrivalPeriods = flow.getPeriods();
        for (ArrivalPeriod period : arrivalPeriods) {
            for (Station station : stations) {
                double val = rand.nextGaussian() * DISPERSION + (period.getPassengersCount() * 10);
                int passengerCount = (int) Math.round(val);
                for (int j = 0; j < passengerCount; j++) {
                    Passenger passenger = new Passenger();
                    passenger.setWaitingTime(Randomize.randomWaitingTime());
                    passenger.setCurrentStation(station);
                    passenger.setDestination(StationGenerator.getRandomStationTo(station, routes));
                    passenger.setTimeOfTheDay(Randomize.randomTime(period.getTimeFrom(), period.getTimeTo()));
                    passengers.add(passenger);
                }
            }
        }
        Collections.sort(passengers);
        return passengers;
    }
}

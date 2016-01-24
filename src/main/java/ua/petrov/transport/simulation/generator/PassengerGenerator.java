package ua.petrov.transport.simulation.generator;

import ua.petrov.transport.core.JAXB.passengers.ArrivalPeriod;
import ua.petrov.transport.core.JAXB.passengers.DailyFlow;
import ua.petrov.transport.core.entity.Passenger;
import ua.petrov.transport.core.entity.Route;
import ua.petrov.transport.core.entity.Station;
import ua.petrov.transport.core.sorter.RouteSorter;
import ua.petrov.transport.core.util.random.Randomize;

import java.util.*;

public class PassengerGenerator {

    private static final int DISPERSION = 5;

    private List<Route> routes;

    private DailyFlow flow;

    private Set<Station> stations = new HashSet<>();

    private static final Random rand = new Random();

    public PassengerGenerator(List<Route> routes, DailyFlow flow) {
        this.routes = routes;
        this.flow = flow;
        initStations();
    }

    private void initStations() {
        for (Route route : routes) {
            stations.addAll(route.getStations());
        }
    }

    public List<Passenger> generatePassengerPerDay() {
        List<Passenger> passengers = new ArrayList<>();
        for (Station station : stations) {
            passengers.addAll(generatePassengersForStation(station));
        }
        Collections.sort(passengers);
        return passengers;
    }

    public List<Passenger> generatePassengersForStation(Station station) {
        long modelTime = Collections.min(routes, RouteSorter.SORT_BY_START_TIME).getFirstBusTimeLong();
        return generatePassengersNowForStation(station, modelTime);
    }

    public List<Passenger> generatePassengersNowForStation(Station station, long modelTime) {
        List<Passenger> passengers = new ArrayList<>();
        boolean flag = false;
        for (ArrivalPeriod period : flow.getPeriods()) {
            if(period.getTimeFrom().toSecondOfDay() <= modelTime && period.getTimeTo().toSecondOfDay() >= modelTime) {
                flag = true;
            }
            if (period.getTimeFrom().toSecondOfDay() > modelTime || flag) {
                double val = rand.nextGaussian() * DISPERSION + (period.getPassengersCount() * 20);
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

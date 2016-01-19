package ua.petrov.transport.simulation.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.petrov.transport.core.JAXB.event.ResultEvent;
import ua.petrov.transport.core.entity.*;
import ua.petrov.transport.core.entity.util.Direction;
import ua.petrov.transport.core.sorter.BusSorter;
import ua.petrov.transport.core.util.RouteFactory;
import ua.petrov.transport.simulation.Event;
import ua.petrov.transport.simulation.print.SimulationPrint;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Владислав on 18.12.2015.
 */
public class Simulation {

    private static final Logger LOGGER = LoggerFactory.getLogger(Simulation.class);

    private boolean pauseFlag = false;

    //MODEL
    private List<Bus> allBuses;
    private List<Passenger> allPassengers;
    private List<Route> allRoutes;

    protected List<Bus> nextBuses;
    protected List<Bus> busesOnRoute;

    private Event timeToNextBus = new Event(1);
    private Event timeToStation = new Event(2);

    private Bus fasterBus;

    private List<Passenger> statisticsGoAway = new ArrayList<>();
    private List<Passenger> statisticsCome = new ArrayList<>();
    private List<Integer> statisticsPercent = new ArrayList<>();

    private ResultEvent eventLogger = new ResultEvent();

    private long modelTime;

    public Simulation(List<Bus> buses, List<Route> allRoutes, List<Passenger> passengers) {
        this.allRoutes = allRoutes;
        this.allPassengers = passengers;
        this.allBuses = RouteFactory.fillBuses(buses, this.allRoutes);
        nextBuses = new ArrayList<>(this.allBuses);
        busesOnRoute = new ArrayList<>();
        LOGGER.info("-----------------------------------------[ " + LocalDateTime.now() + " ]-----------------------------------------");
    }

    public List<Bus> getBusesOnRoute() {
        return busesOnRoute;
    }

    public void setBusesOnRoute(List<Bus> busesOnRoute) {
        this.busesOnRoute = busesOnRoute;
    }

    public ResultEvent getEventLogger() {
        return eventLogger;
    }

    public void setEventLogger(ResultEvent eventLogger) {
        this.eventLogger = eventLogger;
    }

    public void setPauseFlag() {
        if (pauseFlag) {
            pauseFlag = false;
        } else {
            pauseFlag = true;
        }
    }

    public boolean isPauseFlag() {
        return pauseFlag;
    }

    public void addBus(Bus bus) {
        final long defaultWaitingTime = 60;
        Bus newBus = new Bus();
        newBus.setId(0);
        newBus.setStartTime(modelTime + defaultWaitingTime);
        newBus.setDirection(Direction.STRAIGHT);
        newBus.setCurrentStation(bus.getCurrentStation());
        newBus.setRoute(bus.getRoute());
        newBus.setTravelTime(0);
        newBus.setWaitingTime(defaultWaitingTime);
        newBus.setTimeToStation(getTimeToNextStation(newBus));
        newBus.setSeat(bus.getSeat());

        nextBuses.add(newBus);
        setMinTimeToNextBus();
    }

    private long getTimeToNextStation(Bus bus) {
        Station currentStation = bus.getCurrentStation();
        Station nextStation = bus.next(currentStation);
        Arc arc = bus.getRoute().arcBetweenTwoStations(currentStation, nextStation);
        return arc.getTravelTimeLong();
    }

    private void fillFirstBuses() {
        Bus firstBus = Collections.min(allBuses, BusSorter.SORT_BY_START_TIME);
        for (Bus bus : allBuses) {
            long timeToNextStation = getTimeToNextStation(bus);
            bus.setTimeToStation(timeToNextStation);

            if (bus.getStartTime() == firstBus.getStartTime()) {
                nextBuses.remove(bus);
                busesOnRoute.add(bus);
                SimulationPrint.newBus(bus);
                boarding(bus);
            }
        }

        fasterBus = Collections.min(busesOnRoute, BusSorter.SORT_BY_TIME_TO_STATION);
        for (Bus bus : nextBuses) {
            bus.setWaitingTime(bus.getStartTimeLong() - fasterBus.getStartTimeLong());
        }

        timeToStation.setTime(fasterBus.getTimeToStationLong());
        setMinTimeToNextBus();
    }

    private void boarding(Bus bus) {
        PassengerController.boarding(allRoutes, allPassengers, bus, statisticsGoAway, RouteFactory.getPrices(allRoutes));
        int count = bus.getPassengerList().size();
        statisticsPercent.add((count * 100) / bus.getSeat());
    }

    private void initEvent(int speed) {
        try {
            if (!pauseFlag) {
                long eventTime = eventDetection();
                modelTime += eventTime;
                Thread.sleep(speed);
            } else {
                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            LOGGER.error(e.getMessage());
        }
    }

    public void start(long maxTime, int speed) {
        eventLogger.setStartDate(Timestamp.valueOf(LocalDateTime.now()));
        eventLogger.setRoutes(RouteFactory.printRoutingNumbers(allRoutes));

        fillFirstBuses();
        modelTime = fasterBus.getStartTimeLong();
        while (modelTime <= maxTime) {
            initEvent(speed);
        }

        while (busesOnRoute.size() != 0) {
            List<Bus> busesOnRouteTemp = new ArrayList<>(busesOnRoute);
            for (Bus bus : busesOnRouteTemp) {
                long currTime = bus.getTravelTimeLong() + bus.getStartTimeLong();
                Station current = bus.getCurrentStation();
                Station first = bus.getRoute().getStartStation();
                if ((currTime > maxTime) && current.equals(first)) {
                    SimulationPrint.lastBus(bus);
                    busesOnRoute.remove(bus);
                }
            }
            if (busesOnRoute.size() != 0) {
                initEvent(speed);
            }
        }

        eventLogger.setLoadingPercent(getStatisticPercent());
        eventLogger.setSatisfiedCount(statisticsCome.size());
        eventLogger.setDissatisfiedCount(statisticsGoAway.size());
        eventLogger.setEndDate(Timestamp.valueOf(LocalDateTime.now()));
    }


    private long eventDetection() {
        List<Event> events = new ArrayList<>();
        if(nextBuses.size() > 0) events.add(timeToNextBus);
        if(busesOnRoute.size() > 0) events.add(timeToStation);
        Event fasterEvent = Collections.min(events);
        long time = fasterEvent.getTime();

        if (fasterEvent.equals(timeToStation)) {
            minusTimeToNextBus(time);
            stationEventProcess(time);
        } else if (fasterEvent.equals(timeToNextBus)) {
            minusTimeToStation(time);
            nextBusProcess(time);
        }

        return time;
    }

    private void minusTimeToNextBus(long time) {
        if (nextBuses.size() > 0) {
            for (Bus bus : nextBuses) {
                bus.minusWaitingTime(time);
            }
            setMinTimeToNextBus();
        }
    }

    private void minusTimeToStation(long time) {
        for (Bus bus : busesOnRoute) {
            bus.addTravelTime(time);
            bus.minusTimeToStation(time);
        }
        setMinTimeToStation();
    }

    private void nextBusProcess(long modelTime) {
        if (nextBuses.size() > 0) {
            List<Bus> nextBusesOnRoute = new ArrayList<>(nextBuses);

            for (Bus bus : nextBusesOnRoute) {
                bus.minusWaitingTime(modelTime);
                if (bus.getWaitingTimeLong() == 0) {
                    busesOnRoute.add(bus);
                    nextBuses.remove(bus);
                    SimulationPrint.leftTheDepot(bus);
                    boarding(bus);
                }
            }
            setMinTimeToNextBus();
        }
    }

    private void setMinTimeToNextBus() {
        if (nextBuses.size() > 0) {
            timeToNextBus.setTime(Collections.min(nextBuses, BusSorter.SORT_BY_WAITING_TIME).getWaitingTimeLong());
        } else {
            timeToNextBus.setEmpty();
        }
    }

    private void setMinTimeToStation() {
        if (busesOnRoute.size() > 0) {
            timeToStation.setTime(Collections.min(busesOnRoute, BusSorter.SORT_BY_TIME_TO_STATION).getTimeToStationLong());
        } else {
            timeToStation.setEmpty();
        }
    }


    private void stationEventProcess(long modelTime) {
        List<Bus> buss = new ArrayList<>(busesOnRoute);
        for (Bus bus : buss) {
            bus.addTravelTime(modelTime);
            long difference = bus.getTimeToStationLong() - modelTime;
            bus.setTimeToStation(difference);

            if (difference == 0) {
                long timeToNextStation = getTimeToNextStation(bus);
                bus.setTimeToStation(timeToNextStation);
                Station newCurrentStation = bus.next(bus.getCurrentStation());
                Station nextStation = bus.next(newCurrentStation);
                Arc arc = bus.getRoute().arcBetweenTwoStations(newCurrentStation, nextStation);
                bus.setCurrentStation(newCurrentStation);
                bus.setWaitingTime(bus.getCurrentStation().getStopTime());
                bus.setTimeToStation(arc.getTravelTime());

                //Station event logger
                SimulationPrint.stationEvent(bus, nextStation);

                PassengerController.landing(bus, statisticsCome);
                boarding(bus);
            }
        }

        setMinTimeToStation();
    }

    private double getStatisticPercent() {
        int sum = 0;
        for (Integer s : statisticsPercent) {
            sum += s;
        }
        return sum / statisticsPercent.size();
    }
}

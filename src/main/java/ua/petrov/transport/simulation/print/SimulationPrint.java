package ua.petrov.transport.simulation.print;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.petrov.transport.core.entity.Bus;
import ua.petrov.transport.core.entity.Station;
import ua.petrov.transport.core.entity.util.Direction;
import ua.petrov.transport.core.util.TimeUtil;
import ua.petrov.transport.simulation.controller.Simulation;

/**
 * Created by Владислав on 04.01.2016.
 */
public class SimulationPrint {

    private static final Logger LOGGER = LoggerFactory.getLogger(Simulation.class);

    public static void newBus(Bus bus) {
        LOGGER.info("======Bus №[" + bus.getId() + "]========[" + bus.getCurrentStation().getName().toUpperCase()
                + "]============Time: [" + TimeUtil.getSqlTime(bus.getTravelTimeLong() + bus.getStartTimeLong()) + "]");
        LOGGER.info("Next station:          " + bus.getNext(bus.getCurrentStation()).getName().toUpperCase());
    }

    public static void lastBus(Bus bus) {
        long currTime = bus.getTravelTimeLong() + bus.getStartTimeLong();
        LOGGER.info("==================Bus №[" + bus.getId() +
                "]===Route №[" + bus.getRoute().getRoutingNumber().toUpperCase() +
                "]=== arrived the depot at " + TimeUtil.getSqlTime(currTime));
    }

    public static void leftTheDepot(Bus bus) {
        LOGGER.info("[ ====================> ] Bus №[" + bus.getId() +
                "]===Route №[" + bus.getRoute().getRoutingNumber().toUpperCase() + "]===left the depot at: " +
                TimeUtil.getSqlTime(bus.getStartTimeLong()) +
                " on station: " + bus.getCurrentStation().getName());
        LOGGER.info("Time to getNext station:  " + TimeUtil.getSqlTime(bus.getTimeToStationLong()));
        LOGGER.info("Next station:          " + bus.getNext(bus.getCurrentStation()).getName());
    }

    public static void stationEvent(Bus bus, Station nextStation) {

        LOGGER.info("======Bus №[" + bus.getId() + "]===Route №[" + bus.getRoute().getRoutingNumber().toUpperCase() +
                "]===[" + bus.getCurrentStation().getName().toUpperCase() + "]============Time: [" +
                TimeUtil.getSqlTime(bus.getTravelTimeLong() + bus.getStartTimeLong()) + "]");
        LOGGER.info("Bus will wait for:     " + TimeUtil.getSqlTime(bus.getWaitingTimeLong()) + " minutes");
        LOGGER.info("Time to Next station:  " + TimeUtil.getSqlTime(bus.getTimeToStationLong()));
        LOGGER.info("Next station:          " + nextStation.getName().toUpperCase());
        LOGGER.info(getDirection(bus.getDirection()));
        LOGGER.info("In the bus:            " + bus.getPassengerList().size());
    }

    public static void waitingEvent(Bus bus, Station nextStation) {
        LOGGER.info("======Bus №[" + bus.getId() + "]===Route №[" + bus.getRoute().getRoutingNumber().toUpperCase() +
                "]===[" + bus.getCurrentStation().getName().toUpperCase() + "]============Time: [" +
                TimeUtil.getSqlTime(bus.getTravelTimeLong() + bus.getStartTimeLong()) + "]");
        LOGGER.info("Bus will wait on the depot for:     " + TimeUtil.getSqlTime(bus.getWaitingTimeLong()) + " minutes");
        LOGGER.info("Next station:          " + nextStation.getName().toUpperCase());
        LOGGER.info(getDirection(bus.getDirection()));
    }

    private static String getDirection(Direction direction) {
        if(direction == Direction.STRAIGHT) {
            return "Direction:             --->---->--->";
        } else {
            return "Direction:             <---<----<---";
        }
    }
}

package ua.petrov.transport.simulation.generator;

import ua.petrov.transport.core.entity.Arc;
import ua.petrov.transport.core.entity.Route;
import ua.petrov.transport.core.entity.Station;
import ua.petrov.transport.core.util.RouteFactory;

import java.util.List;
import java.util.Random;

public class StationGenerator {

    public static Station getRandomStationTo(Station stationFrom, List<Route> routes) {
        Random rand = new Random();

        List<Route> passingRoutes = RouteFactory.getRouteByStation(routes, stationFrom);
        Route randomRoute = passingRoutes.get(rand.nextInt(passingRoutes.size()));

        List<Arc> availableArcs;
        switch (randomRoute.getType()) {
            case CIRCLE:
                availableArcs = randomRoute.getArcsForStationForCycle(stationFrom);
                return availableArcs.get(rand.nextInt(availableArcs.size())).getToStation();
            case DIRECT:
                availableArcs = randomRoute.getArcList();
                Arc randomArc = availableArcs.get(rand.nextInt(availableArcs.size()));
                if(randomArc.getToStation().equals(stationFrom)) return randomArc.getFromStation();
                if(randomArc.getFromStation().equals(stationFrom)) return randomArc.getToStation();
                if (rand.nextBoolean()) {
                    return randomArc.getToStation();
                }
                else {
                    return randomArc.getFromStation();
                }
            default:
                return null;
        }
    }
}

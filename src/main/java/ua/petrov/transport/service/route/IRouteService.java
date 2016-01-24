package ua.petrov.transport.service.route;

import org.springframework.stereotype.Service;
import ua.petrov.transport.core.entity.Arc;
import ua.petrov.transport.core.entity.Route;

import java.util.List;

@Service
public interface IRouteService {

    List<Route> getAll();

    Route getRouteById(int id);

    List<Arc> getArcByIdRoute(int id);

    void add(Route route);

    void update(Route route);

    void delete(int id);

}

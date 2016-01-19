package ua.petrov.transport.db.dao.route;

import org.springframework.stereotype.Repository;
import ua.petrov.transport.core.entity.Arc;
import ua.petrov.transport.core.entity.Route;

import java.util.List;

/**
 * Created by ��������� on 25 ��� 2015 �..
 */
@Repository
public interface IRouteDAO {

    List<Route> getAll();

    Route getRouteById(int id);

    List<Arc> getArcByIdRoute(int id);

    void add(Route route);

    void delete(int id);
}

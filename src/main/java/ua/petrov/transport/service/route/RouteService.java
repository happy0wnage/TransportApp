package ua.petrov.transport.service.route;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ua.petrov.transport.core.entity.Arc;
import ua.petrov.transport.core.entity.Route;
import ua.petrov.transport.db.dao.route.IRouteDAO;
import ua.petrov.transport.db.transaction.TransactionManager;

import java.util.List;

/**
 * Created by Владислав on 24.01.2016.
 */
@Service
public class RouteService implements IRouteService {

    @Autowired
    private IRouteDAO routeDAO;

    @Qualifier("mySqlTransactionManager")
    @Autowired
    private TransactionManager manager;

    @Override
    public List<Route> getAll() {
        return manager.doInTransaction(() -> routeDAO.getAll());
    }

    @Override
    public Route getRouteById(int id) {
        return manager.doInTransaction(() -> routeDAO.getRouteById(id));
    }

    @Override
    public List<Arc> getArcByIdRoute(int id) {
        return manager.doInTransaction(() -> routeDAO.getArcByIdRoute(id));
    }

    @Override
    public void add(Route route) {
        manager.doInTransaction(() -> {
            routeDAO.add(route);
            return null;
        });
    }

    @Override
    public void update(Route route) {
        manager.doInTransaction(() -> {
            routeDAO.update(route);
            return null;
        });
    }

    @Override
    public void delete(int id) {
        manager.doInTransaction(() -> {
            routeDAO.delete(id);
            return null;
        });
    }
}

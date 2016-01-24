package ua.petrov.transport.db.dao.route;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import ua.petrov.transport.core.entity.Arc;
import ua.petrov.transport.core.entity.Route;
import ua.petrov.transport.db.connection.IConnectionPool;
import ua.petrov.transport.db.constants.DbTables.RouteFields;
import ua.petrov.transport.db.dao.Extractor;
import ua.petrov.transport.exception.DBLayerException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ��������� on 25 ��� 2015 �..
 */
@Repository
public class RouteDAO implements IRouteDAO {

    @Autowired
    private Extractor extractor;

    @Qualifier("connectionPool")
    @Autowired
    private IConnectionPool<Connection> connectionPool;

    private static final String GET_ALL_ROUTES = "SELECT * FROM route;";
    private static final String GET_ARCS_BY_ID_ROUTE = "SELECT * FROM arc WHERE id_arc IN (SELECT id_arc FROM route_arc WHERE id_route = ?);";
    private static final String GET_ROUTE_BY_ID = "SELECT * FROM route WHERE id_route = ?;";
    private static final String ADD_ROUTE = "INSERT INTO route (routing_number, price, depot_stop_time, last_bus_time, first_bus_time, id_start_station, id_end_station) VALUES (?,?,?,?,?,?,?);";
    private static final String UPDATE_ROUTE = "UPDATE route set routing_number = ?, price = ?, depot_stop_time = ?, last_bus_time = ?, first_bus_time = ?, id_start_station = ?, id_end_station = ? WHERE id_route = ?;";
    private static final String ADD_ROUTE_ARC = "INSERT INTO route_arc (id_route, id_arc) VALUES (?,?);";
    private static final String DELETE_ROUTE_ARC = "DELETE FROM route_arc WHERE id_route = ?;";
    private static final String DELETE_ROUTE = "DELETE FROM route WHERE id_route = ?;";

    @Override
    public List<Route> getAll() {
        List<Route> routes = new ArrayList<>();
        Connection con = connectionPool.get();
        try (PreparedStatement pst = con.prepareStatement(GET_ALL_ROUTES)) {
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                Route route = extractor.extractRoute(rs);
                route.setArcList(getArcByIdRoute(route.getId()));
                routes.add(route);
            }
            return routes;
        } catch (SQLException ex) {
            throw new DBLayerException("Failed to get route" + routes, ex);
        }
    }

    public List<Arc> getArcByIdRoute(int id) {
        List<Arc> arcs = new ArrayList<>();
        Connection con = connectionPool.get();
        try (PreparedStatement pst = con.prepareStatement(GET_ARCS_BY_ID_ROUTE)) {
            int k = 1;
            pst.setInt(k, id);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                arcs.add(extractor.extractArc(rs));
            }
            return arcs;
        } catch (SQLException ex) {
            throw new DBLayerException("Failed to get arcs for route" + arcs, ex);
        }
    }

    @Override
    public Route getRouteById(int id) {
        Connection con = connectionPool.get();
        try (PreparedStatement pst = con.prepareStatement(GET_ROUTE_BY_ID)) {
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();
            rs.relative(1);
            Route route = extractor.extractRoute(rs);
            route.setArcList(getArcByIdRoute(route.getId()));
            return route;

        } catch (SQLException ex) {
            throw new DBLayerException("Failed to get route by id=" + id, ex);
        }
    }

    @Override
    public void add(Route route) {
        Connection con = connectionPool.get();
        try {
            String generatedColumns[] = {RouteFields.ID_ROUTE};
            PreparedStatement pst = con.prepareStatement(ADD_ROUTE, generatedColumns);
            int k = 1;
            pst.setString(k++, route.getRoutingNumber());
            pst.setDouble(k++, route.getPrice());
            pst.setTime(k++, route.getDepotStopTime());
            pst.setTime(k++, route.getLastBusTime());
            pst.setTime(k++, route.getFirstBusTime());
            pst.setInt(k++, route.getStartStation().getId());
            pst.setInt(k, route.getEndStation().getId());
            pst.executeUpdate();

            ResultSet rs = pst.getGeneratedKeys();
            rs.next();
            int idRoute = rs.getInt(1);

            List<Arc> arcList = route.getArcList();
            for (Arc arc : arcList) {
                pst = con.prepareStatement(ADD_ROUTE_ARC);
                k = 1;
                pst.setInt(k++, idRoute);
                pst.setInt(k, arc.getId());
                pst.executeUpdate();
            }
        } catch (SQLException ex) {
            throw new DBLayerException("Failed to add route" + route, ex);
        }
    }

    @Override
    public void update(Route route) {
        Connection con = connectionPool.get();
        try {
            PreparedStatement pst = con.prepareStatement(UPDATE_ROUTE);
            int k = 1;
            pst.setString(k++, route.getRoutingNumber());
            pst.setDouble(k++, route.getPrice());
            pst.setTime(k++, route.getDepotStopTime());
            pst.setTime(k++, route.getLastBusTime());
            pst.setTime(k++, route.getFirstBusTime());
            pst.setInt(k++, route.getStartStation().getId());
            pst.setInt(k++, route.getEndStation().getId());
            pst.setInt(k, route.getId());
            pst.executeUpdate();

            pst = con.prepareStatement(DELETE_ROUTE_ARC);
            pst.setInt(1, route.getId());
            pst.executeUpdate();

            List<Arc> arcList = route.getArcList();
            for (Arc arc : arcList) {
                pst = con.prepareStatement(ADD_ROUTE_ARC);
                k = 1;
                pst.setInt(k++, route.getId());
                pst.setInt(k, arc.getId());
                pst.executeUpdate();
            }
        } catch (SQLException ex) {
            throw new DBLayerException("Failed to add route" + route, ex);
        }
    }

    @Override
    public void delete(int id) {
        Connection con = connectionPool.get();
        try {
            PreparedStatement pst = con.prepareStatement(DELETE_ROUTE_ARC);
            pst.setInt(1, id);
            pst.executeUpdate();

            pst = con.prepareStatement(DELETE_ROUTE);
            pst.setInt(1, id);
            pst.executeUpdate();

        } catch (SQLException e) {
            throw new DBLayerException("Failed to delete route", e);
        }
    }
}

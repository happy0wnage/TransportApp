package ua.petrov.transport.db.dao.route;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ua.petrov.transport.core.entity.Arc;
import ua.petrov.transport.core.entity.Route;
import ua.petrov.transport.db.connection.MySQLConnection;
import ua.petrov.transport.db.constants.DbTables.RouteFields;
import ua.petrov.transport.db.dao.Dao;
import ua.petrov.transport.db.dao.Extractor;
import ua.petrov.transport.db.dao.arc.IArcDAO;
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
public class RouteDAO extends Dao implements IRouteDAO {

    @Autowired
    private Extractor extractor;

    @Autowired
    private IArcDAO arcDAO;

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
        Connection con = MySQLConnection.getWebInstance();
        try (PreparedStatement pstm = con.prepareStatement(GET_ALL_ROUTES)) {
            ResultSet rs = pstm.executeQuery();
            while (rs.next()) {
                Route route = extractor.extractRoute(rs);
                route.setArcList(getArcByIdRoute(route.getId()));
                routes.add(route);
            }
            return routes;
        } catch (SQLException ex) {
            rollback(con);
            throw new DBLayerException("Failed to get route" + routes, ex);
        } finally {
            commit(con);
        }
    }

    public List<Arc> getArcByIdRoute(int id) {
        List<Arc> arcs = new ArrayList<>();
        Connection con = MySQLConnection.getWebInstance();
        try (PreparedStatement pstm = con.prepareStatement(GET_ARCS_BY_ID_ROUTE)) {
            int k = 1;
            pstm.setInt(k, id);
            ResultSet rs = pstm.executeQuery();
            while (rs.next()) {
                arcs.add(extractor.extractArc(rs));
            }
            return arcs;
        } catch (SQLException ex) {
            rollback(con);
            throw new DBLayerException("Failed to get arcs for route" + arcs, ex);
        } finally {
            commit(con);
        }
    }

    @Override
    public Route getRouteById(int id) {
        Connection con = MySQLConnection.getWebInstance();
        try (PreparedStatement pstm = con.prepareStatement(GET_ROUTE_BY_ID)) {
            pstm.setInt(1, id);
            ResultSet rs = pstm.executeQuery();
            rs.relative(1);
            Route route = extractor.extractRoute(rs);
            route.setArcList(getArcByIdRoute(route.getId()));
            return route;

        } catch (SQLException ex) {
            rollback(con);
            throw new DBLayerException("Failed to get route by id=" + id, ex);
        } finally {
            commit(con);
        }
    }

    @Override
    public void add(Route route) {
        Connection con = MySQLConnection.getWebInstance();
        try {
            String generatedColumns[] = {RouteFields.ID_ROUTE};
            PreparedStatement pstm = con.prepareStatement(ADD_ROUTE, generatedColumns);
            int k = 1;
            pstm.setString(k++, route.getRoutingNumber());
            pstm.setDouble(k++, route.getPrice());
            pstm.setTime(k++, route.getDepotStopTime());
            pstm.setTime(k++, route.getLastBusTime());
            pstm.setTime(k++, route.getFirstBusTime());
            pstm.setInt(k++, route.getStartStation().getId());
            pstm.setInt(k, route.getEndStation().getId());
            pstm.executeUpdate();

            ResultSet rs = pstm.getGeneratedKeys();
            rs.next();
            int idRoute = rs.getInt(1);

            List<Arc> arcList = route.getArcList();
            for (Arc arc : arcList) {
                pstm = con.prepareStatement(ADD_ROUTE_ARC);
                k = 1;
                pstm.setInt(k++, idRoute);
                pstm.setInt(k, arc.getId());
                pstm.executeUpdate();
            }
        } catch (SQLException ex) {
            rollback(con);
            throw new DBLayerException("Failed to add route" + route, ex);
        } finally {
            commit(con);
        }
    }

    @Override
    public void update(Route route) {
        Connection con = MySQLConnection.getWebInstance();
        try {
            PreparedStatement pstm = con.prepareStatement(UPDATE_ROUTE);
            int k = 1;
            pstm.setString(k++, route.getRoutingNumber());
            pstm.setDouble(k++, route.getPrice());
            pstm.setTime(k++, route.getDepotStopTime());
            pstm.setTime(k++, route.getLastBusTime());
            pstm.setTime(k++, route.getFirstBusTime());
            pstm.setInt(k++, route.getStartStation().getId());
            pstm.setInt(k++, route.getEndStation().getId());
            pstm.setInt(k, route.getId());
            pstm.executeUpdate();

            pstm = con.prepareStatement(DELETE_ROUTE_ARC);
            pstm.setInt(1, route.getId());
            pstm.executeUpdate();

            List<Arc> arcList = route.getArcList();
            for (Arc arc : arcList) {
                pstm = con.prepareStatement(ADD_ROUTE_ARC);
                k = 1;
                pstm.setInt(k++, route.getId());
                pstm.setInt(k, arc.getId());
                pstm.executeUpdate();
            }
        } catch (SQLException ex) {
            rollback(con);
            throw new DBLayerException("Failed to add route" + route, ex);
        } finally {
            commit(con);
        }
    }

    @Override
    public void delete(int id) {
        Connection con = MySQLConnection.getWebInstance();
        try {
            PreparedStatement pstm = con.prepareStatement(DELETE_ROUTE_ARC);
            pstm.setInt(1, id);
            pstm.executeUpdate();

            pstm = con.prepareStatement(DELETE_ROUTE);
            pstm.setInt(1, id);
            pstm.executeUpdate();

        } catch (SQLException e) {
            rollback(con);
            throw new DBLayerException("Failed to delete route", e);
        } finally {
            commit(con);
        }
    }
}

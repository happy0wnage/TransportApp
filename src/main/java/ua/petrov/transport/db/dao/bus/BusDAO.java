package ua.petrov.transport.db.dao.bus;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import ua.petrov.transport.core.entity.Bus;
import ua.petrov.transport.db.connection.IConnectionPool;
import ua.petrov.transport.db.dao.Extractor;
import ua.petrov.transport.exception.DBLayerException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Владислав on 25.11.2015.
 */
@Repository
public class BusDAO implements IBusDAO {

    @Autowired
    private Extractor extractor;

    @Qualifier("connectionPool")
    @Autowired
    private IConnectionPool<Connection> connectionPool;
    
    private static final String GET_ALL_BUSES = "SELECT * FROM bus;";
    private static final String GET_BUS_BY_ID = "SELECT * FROM bus WHERE id_bus = ?;";
    private static final String ADD_BUS = "INSERT INTO Bus (id_route, seat) VALUES (?,?);";
    private static final String UPDATE_BUS = "UPDATE bus set id_route = ?, seat = ? WHERE id_bus = ?";
    private static final String DELETE_BUS = "DELETE FROM bus WHERE id_bus = ?;";

    @Override
    public List<Bus> getAll() {
        List<Bus> bus = new ArrayList<>();
        Connection con = connectionPool.get();
        try (PreparedStatement pst = con.prepareStatement(GET_ALL_BUSES)) {
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                bus.add(extractor.extractBus(rs));
            }
            return bus;
        } catch (SQLException ex) {
            throw new DBLayerException("Failed to get Bus" + bus, ex);
        }
    }

    @Override
    public Bus getBusById(int id) {
        Connection con = connectionPool.get();
        try (PreparedStatement pst = con.prepareStatement(GET_BUS_BY_ID)) {
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();
            rs.relative(1);
            return extractor.extractBus(rs);
        } catch (SQLException ex) {
            throw new DBLayerException("Failed to get bus by id=" + id, ex);
        }
    }

    @Override
    public void add(Bus bus) {
        Connection con = connectionPool.get();
        try (PreparedStatement pst = con.prepareStatement(ADD_BUS)) {
            int k = 1;
            pst.setInt(k++, bus.getRoute().getId());
            pst.setInt(k++, bus.getSeat());
            pst.executeUpdate();
        } catch (SQLException ex) {
            throw new DBLayerException("Failed to add bus" + bus, ex);
        }
    }

    @Override
    public void update(Bus bus) {
        Connection con = connectionPool.get();
        try (PreparedStatement pst = con.prepareStatement(UPDATE_BUS)) {
            int k = 1;
            pst.setInt(k++, bus.getRoute().getId());
            pst.setInt(k++, bus.getSeat());
            pst.setInt(k, bus.getId());
            pst.executeUpdate();
        } catch (SQLException ex) {
            throw new DBLayerException("Failed to update bus" + bus, ex);
        }
    }

    @Override
    public void delete(int id) {
        Connection con = connectionPool.get();
        try (PreparedStatement pst = con.prepareStatement(DELETE_BUS)) {
            pst.setInt(1, id);
            pst.executeUpdate();
        } catch (SQLException e) {
            throw new DBLayerException("Failed to delete bus", e);
        }

    }

}

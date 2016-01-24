package ua.petrov.transport.db.dao.bus;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ua.petrov.transport.core.entity.Bus;
import ua.petrov.transport.db.connection.MySQLConnection;
import ua.petrov.transport.db.dao.Dao;
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
public class BusDAO extends Dao implements IBusDAO {

    @Autowired
    private Extractor extractor;

    private static final String GET_ALL_BUSES = "SELECT * FROM bus;";
    private static final String GET_BUS_BY_ID = "SELECT * FROM bus WHERE id_bus = ?;";
    private static final String ADD_BUS = "INSERT INTO Bus (id_route, seat) VALUES (?,?);";
    private static final String UPDATE_BUS = "UPDATE bus set id_route = ?, seat = ? WHERE id_bus = ?";
    private static final String DELETE_BUS = "DELETE FROM bus WHERE id_bus = ?;";

    @Override
    public List<Bus> getAll() {
        List<Bus> bus = new ArrayList<>();
        Connection con = MySQLConnection.getWebInstance();
        try (PreparedStatement pstm = con.prepareStatement(GET_ALL_BUSES)) {
            ResultSet rs = pstm.executeQuery();
            while (rs.next()) {
                bus.add(extractor.extractBus(rs));
            }
            return bus;
        } catch (SQLException ex) {
            rollback(con);
            throw new DBLayerException("Failed to get Bus" + bus, ex);
        } finally {
            commit(con);
        }
    }

    @Override
    public Bus getBusById(int id) {
        Connection con = MySQLConnection.getWebInstance();
        try (PreparedStatement pstm = con.prepareStatement(GET_BUS_BY_ID)) {
            pstm.setInt(1, id);
            ResultSet rs = pstm.executeQuery();
            rs.relative(1);
            return extractor.extractBus(rs);
        } catch (SQLException ex) {
            rollback(con);
            throw new DBLayerException("Failed to get bus by id=" + id, ex);
        } finally {
            commit(con);
        }
    }

    @Override
    public void add(Bus bus) {
        Connection con = MySQLConnection.getWebInstance();
        try (PreparedStatement pstm = con.prepareStatement(ADD_BUS)) {
            int k = 1;
            pstm.setInt(k++, bus.getRoute().getId());
            pstm.setInt(k++, bus.getSeat());
            pstm.executeUpdate();
        } catch (SQLException ex) {
            rollback(con);
            throw new DBLayerException("Failed to add bus" + bus, ex);
        } finally {
            commit(con);
        }
    }

    @Override
    public void update(Bus bus) {
        Connection con = MySQLConnection.getWebInstance();
        try (PreparedStatement pstm = con.prepareStatement(UPDATE_BUS)) {
            int k = 1;
            pstm.setInt(k++, bus.getRoute().getId());
            pstm.setInt(k++, bus.getSeat());
            pstm.setInt(k, bus.getId());
            pstm.executeUpdate();
        } catch (SQLException ex) {
            rollback(con);
            throw new DBLayerException("Failed to update bus" + bus, ex);
        } finally {
            commit(con);
        }
    }

    @Override
    public void delete(int id) {
        Connection con = MySQLConnection.getWebInstance();
        try (PreparedStatement pstm = con.prepareStatement(DELETE_BUS)) {
            pstm.setInt(1, id);
            pstm.executeUpdate();
        } catch (SQLException e) {
            rollback(con);
            throw new DBLayerException("Failed to delete bus", e);
        } finally {
            commit(con);
        }

    }

}

package ua.petrov.transport.db.dao.station;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ua.petrov.transport.core.entity.Station;
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
public class StationDAO extends Dao implements IStationDAO {

    @Autowired
    private Extractor extractor;

    private static final String GET_STATION_BY_ID = "SELECT * FROM station where id_station = ?";
    private static final String GET_ALL_STATION = "SELECT * FROM station";
    private static final String ADD_STATION = "INSERT INTO station (name, stop_time) VALUES (?,?)";
    private static final String UPDATE_STATION = "UPDATE station set name = ?, stop_time = ? WHERE id_station = ?";
    private static final String DELETE_STATION = "DELETE FROM station WHERE id_station = ?;";

    @Override
    public List<Station> getAll() {
        List<Station> stations = new ArrayList<>();
        Connection con = MySQLConnection.getWebInstance();
        try (PreparedStatement pstm = con.prepareStatement(GET_ALL_STATION)) {
            ResultSet rs = pstm.executeQuery();
            while (rs.next()) {
                stations.add(extractor.extractStation(rs));
            }
            return stations;
        } catch (SQLException ex) {
            rollback(con);
            throw new DBLayerException("Failed to get stations" + stations, ex);
        } finally {
            commit(con);
        }
    }

    @Override
    public Station getStationById(int id) {
        Connection con = MySQLConnection.getWebInstance();
        try (PreparedStatement pstm = con.prepareStatement(GET_STATION_BY_ID)) {
            pstm.setInt(1, id);
            ResultSet rs = pstm.executeQuery();
            rs.relative(1);
            return extractor.extractStation(rs);
        } catch (SQLException ex) {
            rollback(con);
            throw new DBLayerException("Failed to get station by id=" + id, ex);
        } finally {
            commit(con);
        }
    }

    @Override
    public void add(Station station) {
        Connection con = MySQLConnection.getWebInstance();
        try (PreparedStatement pstm = con.prepareStatement(ADD_STATION)) {
            int k = 1;
            pstm.setString(k++, station.getName());
            pstm.setTime(k++, station.getStopTime());
            pstm.executeUpdate();
        } catch (SQLException e) {
            rollback(con);
            throw new DBLayerException("Failed to add station", e);
        } finally {
            commit(con);
        }
    }

    @Override
    public void update(Station station) {
        Connection con = MySQLConnection.getWebInstance();
        try (PreparedStatement pstm = con.prepareStatement(UPDATE_STATION)) {
            int k = 1;
            pstm.setString(k++, station.getName());
            pstm.setTime(k++, station.getStopTime());
            pstm.setInt(k, station.getId());
            pstm.executeUpdate();
        } catch (SQLException e) {
            rollback(con);
            throw new DBLayerException("Failed to update station", e);
        } finally {
            commit(con);
        }
    }

    @Override
    public void delete(int id) {
        Connection con = MySQLConnection.getWebInstance();
        try (PreparedStatement pstm = con.prepareStatement(DELETE_STATION)) {
            pstm.setInt(1, id);
            pstm.executeUpdate();
        } catch (SQLException e) {
            rollback(con);
            throw new DBLayerException("Failed to delete station", e);
        } finally {
            commit(con);
        }
    }
}

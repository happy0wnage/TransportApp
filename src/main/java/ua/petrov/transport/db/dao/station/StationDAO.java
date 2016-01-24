package ua.petrov.transport.db.dao.station;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import ua.petrov.transport.core.entity.Station;
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
public class StationDAO implements IStationDAO {

    @Autowired
    private Extractor extractor;

    @Qualifier("connectionPool")
    @Autowired
    private IConnectionPool<Connection> connectionPool;

    private static final String GET_STATION_BY_ID = "SELECT * FROM station where id_station = ?";
    private static final String GET_ALL_STATION = "SELECT * FROM station";
    private static final String ADD_STATION = "INSERT INTO station (name, stop_time) VALUES (?,?)";
    private static final String UPDATE_STATION = "UPDATE station set name = ?, stop_time = ? WHERE id_station = ?";
    private static final String DELETE_STATION = "DELETE FROM station WHERE id_station = ?;";

    @Override
    public List<Station> getAll() {
        List<Station> stations = new ArrayList<>();
        Connection con = connectionPool.get();
        try (PreparedStatement pst = con.prepareStatement(GET_ALL_STATION)) {
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                stations.add(extractor.extractStation(rs));
            }
            return stations;
        } catch (SQLException ex) {
            throw new DBLayerException("Failed to get stations" + stations, ex);
        }
    }

    @Override
    public Station getStationById(int id) {
        Connection con = connectionPool.get();
        try (PreparedStatement pst = con.prepareStatement(GET_STATION_BY_ID)) {
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();
            rs.relative(1);
            return extractor.extractStation(rs);
        } catch (SQLException ex) {
            throw new DBLayerException("Failed to get station by id=" + id, ex);
        }
    }

    @Override
    public void add(Station station) {
        Connection con = connectionPool.get();
        try (PreparedStatement pst = con.prepareStatement(ADD_STATION)) {
            int k = 1;
            pst.setString(k++, station.getName());
            pst.setTime(k++, station.getStopTime());
            pst.executeUpdate();
        } catch (SQLException e) {
            throw new DBLayerException("Failed to add station", e);
        }
    }

    @Override
    public void update(Station station) {
        Connection con = connectionPool.get();
        try (PreparedStatement pst = con.prepareStatement(UPDATE_STATION)) {
            int k = 1;
            pst.setString(k++, station.getName());
            pst.setTime(k++, station.getStopTime());
            pst.setInt(k, station.getId());
            pst.executeUpdate();
        } catch (SQLException e) {
            throw new DBLayerException("Failed to update station", e);
        }
    }

    @Override
    public void delete(int id) {
        Connection con = connectionPool.get();
        try (PreparedStatement pst = con.prepareStatement(DELETE_STATION)) {
            pst.setInt(1, id);
            pst.executeUpdate();
        } catch (SQLException e) {
            throw new DBLayerException("Failed to delete station", e);
        }
    }
}

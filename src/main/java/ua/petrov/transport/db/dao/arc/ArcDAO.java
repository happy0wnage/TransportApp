package ua.petrov.transport.db.dao.arc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import ua.petrov.transport.core.entity.Arc;
import ua.petrov.transport.db.connection.IConnectionPool;
import ua.petrov.transport.db.constants.DbTables.ArcFields;
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
public class ArcDAO implements IArcDAO {

    @Autowired
    private Extractor extractor;

    @Qualifier("connectionPool")
    @Autowired
    private IConnectionPool<Connection> connectionPool;

    private static final String GET_ALL_ARCS = "SELECT * FROM arc";
    private static final String GET_ARC_BY_ID = "SELECT * FROM arc WHERE id_arc = ?";
    private static final String GET_ARCS_BY_ID_STATION = "SELECT * FROM arc WHERE id_station_from = ? OR id_station_to = ?;";
    private static final String ADD_ARC = "INSERT INTO arc (id_station_from, id_station_to, travel_time) VALUES (?,?,?)";
    private static final String UPDATE_ARC = "UPDATE arc set id_station_from = ?, id_station_to = ?, travel_time = ? WHERE id_arc = ?";
    private static final String DELETE_ARC = "DELETE FROM arc WHERE id_arc = ?;";

    @Override
    public List<Arc> getAll() {
        List<Arc> arc = new ArrayList<>();
        Connection con = connectionPool.get();
        try (PreparedStatement pst = con.prepareStatement(GET_ALL_ARCS)) {
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                arc.add(extractor.extractArc(rs));
            }
            return arc;
        } catch (SQLException ex) {
            throw new DBLayerException("Failed to get Arcs" + arc, ex);
        }
    }

    @Override
    public List<Arc> getArcsByStationId(int id) {
        Connection con = connectionPool.get();
        try (PreparedStatement pstm = con.prepareStatement(GET_ARCS_BY_ID_STATION)) {
            List<Arc> arcs = new ArrayList<>();
            int k = 1;
            pstm.setInt(k++, id);
            pstm.setInt(k, id);
            ResultSet rs = pstm.executeQuery();
            while (rs.next()) {
                arcs.add(extractor.extractArc(rs));
            }
            return arcs;
        } catch (SQLException ex) {
            throw new DBLayerException("Failed to get arc by id_station=" + id, ex);
        }
    }

    @Override
    public Arc getArcById(int id) {
        Connection con = connectionPool.get();
        try (PreparedStatement pstm = con.prepareStatement(GET_ARC_BY_ID)) {
            pstm.setInt(1, id);
            ResultSet rs = pstm.executeQuery();
            rs.relative(1);
            return extractor.extractArc(rs);
        } catch (SQLException ex) {
            throw new DBLayerException("Failed to get arc by id=" + id, ex);
        }
    }

    @Override
    public int add(Arc arc) {
        Connection con = connectionPool.get();
        String generatedColumns[] = {ArcFields.ID_ARC};
        try (PreparedStatement pstm = con.prepareStatement(ADD_ARC, generatedColumns)) {
            int k = 1;
            pstm.setInt(k++, arc.getFromStation().getId());
            pstm.setInt(k++, arc.getToStation().getId());
            pstm.setTime(k, arc.getTravelTime());
            pstm.executeUpdate();

            ResultSet rs = pstm.getGeneratedKeys();
            rs.next();
            return rs.getInt(1);
        } catch (SQLException ex) {
            throw new DBLayerException("Failed to add arc" + arc, ex);
        }
    }

    @Override
    public void update(Arc arc) {
        Connection con = connectionPool.get();
        try (PreparedStatement pstm = con.prepareStatement(UPDATE_ARC)) {
            int k = 1;
            pstm.setInt(k++, arc.getFromStation().getId());
            pstm.setInt(k++, arc.getToStation().getId());
            pstm.setTime(k++, arc.getTravelTime());
            pstm.setInt(k, arc.getId());
            pstm.executeUpdate();
        } catch (SQLException ex) {
            throw new DBLayerException("Failed to update arc" + arc, ex);
        }
    }

    @Override
    public void delete(int id) {
        Connection con = connectionPool.get();
        try (PreparedStatement pstm = con.prepareStatement(DELETE_ARC)) {
            pstm.setInt(1, id);
            pstm.executeUpdate();
        } catch (SQLException e) {
            throw new DBLayerException("Failed to delete arc", e);
        }
    }

}

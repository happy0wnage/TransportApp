package ua.petrov.transport.db.dao.results;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ua.petrov.transport.core.JAXB.event.ResultEvent;
import ua.petrov.transport.db.connection.MySQLConnection;
import ua.petrov.transport.db.dao.Dao;
import ua.petrov.transport.db.dao.Extractor;
import ua.petrov.transport.exception.DBLayerException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Владислав on 10.01.2016.
 */
@Repository
public class ResultsDAO extends Dao implements IResultsDAO {

    private static final String GET_ALL_RESULTS = "SELECT * FROM results ORDER BY id_results DESC;";
    private static final String GET_RESULT_BY_ID = "SELECT * FROM results WHERE id_results = ?;";
    private static final String ADD_RESULTS = "INSERT INTO results (start_date, end_date, loading_percent, routes, satisfied_count, dissatisfied_count) VALUES (?,?,?,?,?,?);";

    @Autowired
    private Extractor extractor;

    @Override
    public List<ResultEvent> getAll() {
        List<ResultEvent> events = new ArrayList<>();
        Connection con = MySQLConnection.getWebInstance();
        try (PreparedStatement pstm = con.prepareStatement(GET_ALL_RESULTS)) {
            ResultSet rs = pstm.executeQuery();
            while (rs.next()) {
                events.add(extractor.extractEvent(rs));
            }
            return events;
        } catch (SQLException ex) {
            rollback(con);
            throw new DBLayerException("Failed to get Results" + events, ex);
        } finally {
            commit(con);
        }
    }

    @Override
    public ResultEvent getResultById(int id) {
        Connection con = MySQLConnection.getWebInstance();
        try (PreparedStatement pstm = con.prepareStatement(GET_RESULT_BY_ID)) {
            pstm.setInt(1, id);
            ResultSet rs = pstm.executeQuery();
            rs.relative(1);
            return extractor.extractEvent(rs);
        } catch (SQLException ex) {
            rollback(con);
            throw new DBLayerException("Failed to get result by id=" + id, ex);
        } finally {
            commit(con);
        }
    }

    @Override
    public void add(ResultEvent event) {
        Connection con = MySQLConnection.getWebInstance();
        try (PreparedStatement pstm = con.prepareStatement(ADD_RESULTS)) {
            int k = 1;
            pstm.setTimestamp(k++, event.getStartDate());
            pstm.setTimestamp(k++, event.getEndDate());
            pstm.setDouble(k++, event.getLoadingPercent());
            pstm.setString(k++, event.getRoutes());
            pstm.setInt(k++, event.getSatisfiedCount());
            pstm.setInt(k++, event.getDissatisfiedCount());
            pstm.executeUpdate();
        } catch (SQLException ex) {
            rollback(con);
            throw new DBLayerException("Failed to add result" + event, ex);
        } finally {
            commit(con);
        }
    }
}

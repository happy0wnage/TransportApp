package ua.petrov.transport.db.dao.results;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import ua.petrov.transport.core.JAXB.event.ResultEvent;
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
 * Created by Владислав on 10.01.2016.
 */
@Repository
public class ResultsDAO implements IResultsDAO {

    @Autowired
    private Extractor extractor;

    @Qualifier("connectionPool")
    @Autowired
    private IConnectionPool<Connection> connectionPool;

    private static final String GET_ALL_RESULTS = "SELECT * FROM results ORDER BY id_results DESC;";
    private static final String GET_RESULT_BY_ID = "SELECT * FROM results WHERE id_results = ?;";
    private static final String ADD_RESULTS = "INSERT INTO results (start_date, end_date, loading_percent, routes, satisfied_count, dissatisfied_count) VALUES (?,?,?,?,?,?);";

    @Override
    public List<ResultEvent> getAll() {
        List<ResultEvent> events = new ArrayList<>();
        Connection con = connectionPool.get();
        try (PreparedStatement pst = con.prepareStatement(GET_ALL_RESULTS)) {
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                events.add(extractor.extractEvent(rs));
            }
            return events;
        } catch (SQLException ex) {
            throw new DBLayerException("Failed to get Results" + events, ex);
        }
    }

    @Override
    public ResultEvent getResultById(int id) {
        Connection con = connectionPool.get();
        try (PreparedStatement pst = con.prepareStatement(GET_RESULT_BY_ID)) {
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();
            rs.relative(1);
            return extractor.extractEvent(rs);
        } catch (SQLException ex) {
            throw new DBLayerException("Failed to get result by id=" + id, ex);
        }
    }

    @Override
    public void add(ResultEvent event) {
        Connection con = connectionPool.get();
        try (PreparedStatement pst = con.prepareStatement(ADD_RESULTS)) {
            int k = 1;
            pst.setTimestamp(k++, event.getStartDate());
            pst.setTimestamp(k++, event.getEndDate());
            pst.setDouble(k++, event.getLoadingPercent());
            pst.setString(k++, event.getRoutes());
            pst.setInt(k++, event.getSatisfiedCount());
            pst.setInt(k++, event.getDissatisfiedCount());
            pst.executeUpdate();
        } catch (SQLException ex) {
            throw new DBLayerException("Failed to add result" + event, ex);
        }
    }
}

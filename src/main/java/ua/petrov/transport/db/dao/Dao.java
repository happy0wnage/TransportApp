package ua.petrov.transport.db.dao;

import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by Владислав on 25.11.2015.
 */
public class Dao {

    private static final Logger LOGGER = Logger.getLogger(Dao.class);

    public void rollback(Connection con) {
        if (con != null) {
            try {
                con.rollback();
            } catch (SQLException ex) {
                LOGGER.error("RollBack: " + ex.getMessage());
            }
        }
    }

    public void commit(Connection con) {
        if (con != null) {
            try {
                con.commit();
                con.close();
            } catch (SQLException ex) {
                LOGGER.error("Commit: " + ex.getMessage());
            }
        }
    }
}

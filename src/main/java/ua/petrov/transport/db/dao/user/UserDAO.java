
package ua.petrov.transport.db.dao.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import ua.petrov.transport.core.entity.User;
import ua.petrov.transport.db.connection.IConnectionPool;
import ua.petrov.transport.db.constants.DbTables.UserFields;
import ua.petrov.transport.db.dao.Extractor;
import ua.petrov.transport.exception.DBLayerException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Владислав on 12.01.2016.
 */
@Repository
public class UserDAO implements IUserDAO {

    @Autowired
    private Extractor extractor;

    @Qualifier("connectionPool")
    @Autowired
    private IConnectionPool<Connection> connectionPool;
    
    private static final String GET_USERS = "SELECT * FROM user;";
    private static final String GET_USER_BY_ID = "SELECT * FROM user WHERE id_user = ?;";
    private static final String GET_USER_BY_LOGIN = "SELECT * FROM user WHERE login = ?;";
    private static final String ADD_USER = "INSERT INTO user (login, password) VALUES (?,?);";
    private static final String DELETE_USER = "DELETE FROM user WHERE id_user = ?;";

    @Override
    public User getUserById(int id) {
        Connection con = connectionPool.get();
        try (PreparedStatement pst = con.prepareStatement(GET_USER_BY_ID)) {
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();
            rs.relative(1);
            return extractor.extractUser(rs);
        } catch (SQLException ex) {
            throw new DBLayerException("Failed to get user by id=" + id, ex);
        }
    }

    @Override
    public User getUserByLogin(String login) {
        Connection con = connectionPool.get();
        try (PreparedStatement pst = con.prepareStatement(GET_USER_BY_LOGIN)) {
            pst.setString(1, login);
            ResultSet rs = pst.executeQuery();
            rs.relative(1);
            return extractor.extractUser(rs);
        } catch (SQLException ex) {
            throw new DBLayerException("Failed to get user by login=" + login, ex);
        }
    }

    @Override
    public List<User> getAll() {
        List<User> users = new ArrayList<>();
        Connection con = connectionPool.get();
        try (PreparedStatement pst = con.prepareStatement(GET_USERS)) {
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                users.add(extractor.extractUser(rs));
            }
            return users;
        } catch (SQLException ex) {
            throw new DBLayerException("Failed to get user" + users, ex);
        }
    }

    @Override
    public int add(User user) {
        Connection con = connectionPool.get();
        String generatedColumns[] = {UserFields.ID_USER};
        try (PreparedStatement pst = con.prepareStatement(ADD_USER, generatedColumns)) {
            int k = 1;
            pst.setString(k++, user.getLogin());
            pst.setString(k, user.getPassword());
            pst.executeUpdate();

            ResultSet rs = pst.getGeneratedKeys();
            rs.next();
            return rs.getInt(1);
        } catch (SQLException e) {
            throw new DBLayerException("Failed to add user", e);
        }
    }

    @Override
    public void delete(int id) {
        Connection con = connectionPool.get();
        try (PreparedStatement pst = con.prepareStatement(DELETE_USER)) {
            pst.setInt(1, id);
            pst.executeUpdate();
        } catch (SQLException e) {
            throw new DBLayerException("Failed to delete user", e);
        }
    }
}

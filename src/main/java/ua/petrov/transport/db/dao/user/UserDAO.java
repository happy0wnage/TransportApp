
package ua.petrov.transport.db.dao.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ua.petrov.transport.core.entity.User;
import ua.petrov.transport.db.connection.MySQLConnection;
import ua.petrov.transport.db.constants.DbTables.UserFields;
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
 * Created by Владислав on 12.01.2016.
 */
@Repository
public class UserDAO extends Dao implements IUserDAO {

    private static final String GET_USERS = "SELECT * FROM user;";
    private static final String GET_USER_BY_ID = "SELECT * FROM user WHERE id_user = ?;";
    private static final String GET_USER_BY_LOGIN = "SELECT * FROM user WHERE login = ?;";
    private static final String ADD_USER = "INSERT INTO user (login, password) VALUES (?,?);";
    private static final String DELETE_USER = "DELETE FROM user WHERE id_user = ?;";

    @Autowired
    private Extractor extractor;

    @Override
    public User getUserById(int id) {
        Connection con = MySQLConnection.getWebInstance();
        try (PreparedStatement pstm = con.prepareStatement(GET_USER_BY_ID)) {
            pstm.setInt(1, id);
            ResultSet rs = pstm.executeQuery();
            rs.relative(1);
            return extractor.extractUser(rs);

        } catch (SQLException ex) {
            rollback(con);
            throw new DBLayerException("Failed to get user by id=" + id, ex);
        } finally {
            commit(con);
        }
    }

    @Override
    public User getUserByLogin(String login) {
        Connection con = MySQLConnection.getWebInstance();
        try (PreparedStatement pstm = con.prepareStatement(GET_USER_BY_LOGIN)) {
            pstm.setString(1, login);
            ResultSet rs = pstm.executeQuery();
            rs.relative(1);
            return extractor.extractUser(rs);

        } catch (SQLException ex) {
            rollback(con);
            throw new DBLayerException("Failed to get user by login=" + login, ex);
        } finally {
            commit(con);
        }
    }

    @Override
    public List<User> getAll() {
        List<User> users = new ArrayList<>();
        Connection con = MySQLConnection.getWebInstance();
        try (PreparedStatement pstm = con.prepareStatement(GET_USERS)) {
            ResultSet rs = pstm.executeQuery();
            while (rs.next()) {
                users.add(extractor.extractUser(rs));
            }
            return users;
        } catch (SQLException ex) {
            rollback(con);
            throw new DBLayerException("Failed to get user" + users, ex);
        } finally {
            commit(con);
        }
    }

    @Override
    public int add(User user) {
        Connection con = MySQLConnection.getWebInstance();
        String generatedColumns[] = {UserFields.ID_USER};
        try (PreparedStatement pstm = con.prepareStatement(ADD_USER, generatedColumns)) {
            int k = 1;
            pstm.setString(k++, user.getLogin());
            pstm.setString(k, user.getPassword());
            pstm.executeUpdate();

            ResultSet rs = pstm.getGeneratedKeys();
            rs.next();
            return rs.getInt(1);
        } catch (SQLException e) {
            rollback(con);
            throw new DBLayerException("Failed to add user", e);
        } finally {
            commit(con);
        }
    }

    @Override
    public void delete(int id) {
        Connection con = MySQLConnection.getWebInstance();
        try (PreparedStatement pstm = con.prepareStatement(DELETE_USER)) {
            pstm.setInt(1, id);
            pstm.executeUpdate();
        } catch (SQLException e) {
            rollback(con);
            throw new DBLayerException("Failed to delete user", e);
        } finally {
            commit(con);
        }
    }
}

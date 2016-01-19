

package ua.petrov.transport.db.dao.user;

import org.springframework.stereotype.Repository;
import ua.petrov.transport.core.entity.User;

import java.util.List;

/**
 * Created by Владислав on 12.01.2016.
 */
@Repository
public interface IUserDAO {

    User getUserById(int id);

    User getUserByLogin(String login);

    List<User> getAll();

    int add(User user);

    void delete(int id);
}

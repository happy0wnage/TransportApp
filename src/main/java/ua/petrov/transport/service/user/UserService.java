package ua.petrov.transport.service.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ua.petrov.transport.core.entity.User;
import ua.petrov.transport.db.dao.user.IUserDAO;
import ua.petrov.transport.db.transaction.TransactionManager;

import java.util.List;

@Service
public class UserService implements IUserService {

    @Autowired
    private IUserDAO userDAO;

    @Qualifier("mySqlTransactionManager")
    @Autowired
    private TransactionManager manager;

    @Override
    public User getUserById(int id) {
        return manager.doInTransaction(() -> userDAO.getUserById(id));
    }

    @Override
    public User getUserByLogin(String login) {
        return manager.doInTransaction(() -> userDAO.getUserByLogin(login));
    }

    @Override
    public List<User> getAll() {
        return manager.doInTransaction(userDAO::getAll);
    }

    @Override
    public int add(User user) {
        return manager.doInTransaction(() -> userDAO.add(user));
    }

    @Override
    public void delete(int id) {
        manager.doInTransaction(() -> {
            userDAO.delete(id);
            return null;
        });
    }
}

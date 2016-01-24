package ua.petrov.transport.service.user;

import org.springframework.stereotype.Service;
import ua.petrov.transport.core.entity.User;

import java.util.List;

@Service
public interface IUserService {

    User getUserById(int id);

    User getUserByLogin(String login);

    List<User> getAll();

    int add(User user);

    void delete(int id);
}

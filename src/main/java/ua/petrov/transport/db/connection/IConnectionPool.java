package ua.petrov.transport.db.connection;

import org.springframework.stereotype.Service;

@Service
public interface IConnectionPool<T> {

    void put(T connection);

    T get();

    void remove();
}

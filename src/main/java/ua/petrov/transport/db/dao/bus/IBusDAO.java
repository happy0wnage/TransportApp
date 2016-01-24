package ua.petrov.transport.db.dao.bus;

import org.springframework.stereotype.Repository;
import ua.petrov.transport.core.entity.Bus;

import java.util.List;

/**
 * Created by Владислав on 25.11.2015.
 */
@Repository
public interface IBusDAO {

    List<Bus> getAll();

    Bus getBusById(int id);

    void add(Bus bus);

    void update(Bus bus);

    void delete(int id);

}

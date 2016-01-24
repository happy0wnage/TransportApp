package ua.petrov.transport.service.bus;

import org.springframework.stereotype.Service;
import ua.petrov.transport.core.entity.Bus;

import java.util.List;

@Service
public interface IBusService {

    List<Bus> getAll();

    Bus getBusById(int id);

    void add(Bus bus);

    void update(Bus bus);

    void delete(int id);
}

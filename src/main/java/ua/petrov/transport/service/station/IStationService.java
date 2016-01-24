package ua.petrov.transport.service.station;

import org.springframework.stereotype.Service;
import ua.petrov.transport.core.entity.Station;

import java.util.List;

@Service
public interface IStationService {

    List<Station> getAll();

    Station getStationById(int id);

    void add(Station station);

    void update(Station station);

    void delete(int id);

}

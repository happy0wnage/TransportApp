package ua.petrov.transport.db.dao.station;

import org.springframework.stereotype.Repository;
import ua.petrov.transport.core.entity.Station;

import java.util.List;

/**
 * Created by Владислав on 25.11.2015.
 */
@Repository
public interface IStationDAO {

    List<Station> getAll();

    Station getStationById(int id);

    void add(Station station);

    void update(Station station);

    void delete(int id);

}
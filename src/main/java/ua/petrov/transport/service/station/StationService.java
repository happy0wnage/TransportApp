package ua.petrov.transport.service.station;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ua.petrov.transport.core.entity.Station;
import ua.petrov.transport.db.dao.station.IStationDAO;
import ua.petrov.transport.db.transaction.TransactionManager;

import java.util.List;

@Service
public class StationService implements IStationService {

    @Autowired
    private IStationDAO stationDAO;

    @Qualifier("mySqlTransactionManager")
    @Autowired
    private TransactionManager manager;

    @Override
    public List<Station> getAll() {
        return manager.doInTransaction(() -> stationDAO.getAll());
    }

    @Override
    public Station getStationById(int id) {
        return manager.doInTransaction(() -> stationDAO.getStationById(id));
    }

    @Override
    public void add(Station station) {
        manager.doInTransaction(() -> {
            stationDAO.add(station);
            return null;
        });
    }

    @Override
    public void update(Station station) {
        manager.doInTransaction(() -> {
            stationDAO.update(station);
            return null;
        });
    }

    @Override
    public void delete(int id) {
        manager.doInTransaction(() -> {
            stationDAO.delete(id);
            return null;
        });
    }
}

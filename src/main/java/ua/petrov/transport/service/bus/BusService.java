package ua.petrov.transport.service.bus;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ua.petrov.transport.core.entity.Bus;
import ua.petrov.transport.db.dao.bus.IBusDAO;
import ua.petrov.transport.db.transaction.TransactionManager;

import java.util.List;

@Service
public class BusService implements IBusService {

    @Autowired
    private IBusDAO busDAO;

    @Qualifier("mySqlTransactionManager")
    @Autowired
    private TransactionManager manager;

    @Override
    public List<Bus> getAll() {
        return manager.doInTransaction(() -> busDAO.getAll());
    }

    @Override
    public Bus getBusById(int id) {
        return manager.doInTransaction(() -> busDAO.getBusById(id));
    }

    @Override
    public void add(Bus bus) {
        manager.doInTransaction(() -> {
            busDAO.add(bus);
            return null;
        });
    }

    @Override
    public void update(Bus bus) {
        manager.doInTransaction(() -> {
            busDAO.update(bus);
            return null;
        });
    }

    @Override
    public void delete(int id) {
        manager.doInTransaction(() -> {
            busDAO.delete(id);
            return null;
        });
    }
}

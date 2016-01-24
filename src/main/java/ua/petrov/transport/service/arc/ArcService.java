package ua.petrov.transport.service.arc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ua.petrov.transport.core.entity.Arc;
import ua.petrov.transport.db.dao.arc.IArcDAO;
import ua.petrov.transport.db.transaction.TransactionManager;

import java.util.List;

@Service
public class ArcService implements IArcService {

    @Autowired
    private IArcDAO arcDAO;

    @Qualifier("mySqlTransactionManager")
    @Autowired
    private TransactionManager manager;

    @Override
    public List<Arc> getAll() {
        return manager.doInTransaction(arcDAO::getAll);
    }

    @Override
    public List<Arc> getArcsByStationId(int id) {
        return manager.doInTransaction(() -> arcDAO.getArcsByStationId(id));
    }

    @Override
    public Arc getArcById(int id) {
        return manager.doInTransaction(() -> arcDAO.getArcById(id));
    }

    @Override
    public int add(Arc arc) {
        return manager.doInTransaction(() -> arcDAO.add(arc));

    }

    @Override
    public void update(Arc arc) {
        manager.doInTransaction(() -> {
            arcDAO.update(arc);
            return null;
        });
    }

    @Override
    public void delete(int id) {
        manager.doInTransaction(() -> {
            arcDAO.delete(id);
            return null;
        });
    }
}

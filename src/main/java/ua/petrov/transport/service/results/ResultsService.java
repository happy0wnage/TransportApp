package ua.petrov.transport.service.results;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ua.petrov.transport.core.JAXB.event.ResultEvent;
import ua.petrov.transport.db.dao.results.IResultsDAO;
import ua.petrov.transport.db.transaction.TransactionManager;

import java.util.List;

@Service
public class ResultsService implements IResultsService{

    @Autowired
    private IResultsDAO resultsDAO;

    @Qualifier("mySqlTransactionManager")
    @Autowired
    private TransactionManager manager;

    @Override
    public List<ResultEvent> getAll() {
        return manager.doInTransaction(() -> resultsDAO.getAll());
    }

    @Override
    public ResultEvent getResultById(int id) {
        return manager.doInTransaction(() -> resultsDAO.getResultById(id));
    }

    @Override
    public void add(ResultEvent event) {
        manager.doInTransaction(() -> {
            resultsDAO.add(event);
            return null;
        });
    }
}

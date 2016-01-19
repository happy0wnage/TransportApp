package ua.petrov.transport.db.dao.results;

import org.springframework.stereotype.Repository;
import ua.petrov.transport.core.JAXB.event.ResultEvent;

import java.util.List;

/**
 * Created by Владислав on 10.01.2016.
 */
@Repository
public interface IResultsDAO {

    List<ResultEvent> getAll();

    ResultEvent getResultById(int id);

    void add(ResultEvent event);
}

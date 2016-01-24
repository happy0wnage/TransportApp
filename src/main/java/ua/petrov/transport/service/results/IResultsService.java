package ua.petrov.transport.service.results;

import org.springframework.stereotype.Service;
import ua.petrov.transport.core.JAXB.event.ResultEvent;

import java.util.List;

@Service
public interface IResultsService {

    List<ResultEvent> getAll();

    ResultEvent getResultById(int id);

    void add(ResultEvent event);
}

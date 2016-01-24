package ua.petrov.transport.service.arc;

import org.springframework.stereotype.Service;
import ua.petrov.transport.core.entity.Arc;

import java.util.List;

@Service
public interface IArcService {

    List<Arc> getAll();

    List<Arc> getArcsByStationId(int id);

    Arc getArcById(int id);

    int add(Arc arc);

    void update(Arc arc);

    void delete(int id);
}

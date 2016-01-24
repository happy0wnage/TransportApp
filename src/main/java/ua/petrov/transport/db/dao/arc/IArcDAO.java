package ua.petrov.transport.db.dao.arc;

import org.springframework.stereotype.Repository;
import ua.petrov.transport.core.entity.Arc;

import java.util.List;

/**
 * Created by Владислав on 25.11.2015.
 */
@Repository
public interface IArcDAO {

    List<Arc> getAll();

    List<Arc> getArcsByStationId(int id);

    Arc getArcById(int id);

    int add(Arc arc);

    void update(Arc arc);

    void delete(int id);

}

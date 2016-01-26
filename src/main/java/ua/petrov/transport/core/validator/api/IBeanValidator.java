package ua.petrov.transport.core.validator.api;

import org.springframework.stereotype.Component;
import ua.petrov.transport.core.entity.ViewBean;

import java.util.List;
import java.util.Map;

@Component
public interface IBeanValidator{
	Map<String, List<String>> validateBean(ViewBean bean);

}

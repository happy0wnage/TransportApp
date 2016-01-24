package ua.petrov.transport.core.validator.api;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public interface IBeanValidator<T> {
	Map<String, List<String>> validateBean(T bean);

}

package ua.petrov.transport.web.controller;

import org.springframework.web.servlet.ModelAndView;
import ua.petrov.transport.web.Constants;
import ua.petrov.transport.web.Constants.Message;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

public abstract class AbstractController {

    protected ModelAndView createMaV(String viewName) {
        return new ModelAndView(viewName);
    }

    protected ModelAndView createMaV() {
        return new ModelAndView();
    }

    protected ModelAndView getModelWithErrors(Map<String, List<String>> errors, ModelAndView modelAndView, HttpSession session, String url) {
        List<String> messages = new ArrayList<>();
        for (Entry<String, List<String>> error : errors.entrySet()) {
            messages.addAll(error.getValue().stream().collect(Collectors.toList()));
        }
        session.setAttribute(Message.VALIDATION_ERRORS, messages);
        modelAndView.setViewName(url);
        return modelAndView;
    }

    protected ModelAndView getModelToTheSamePage(ModelAndView modelAndView, HttpServletRequest request) {
        modelAndView.setViewName(Constants.REDIRECT + request.getHeader(Constants.REFERER));
        return modelAndView;
    }

    protected String getHeader(HttpServletRequest request) {
        return Constants.REDIRECT + request.getHeader(Constants.REFERER);
    }
}

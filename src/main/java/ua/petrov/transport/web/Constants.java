package ua.petrov.transport.web;

/**
 * Created by Владислав on 07.01.2016.
 */
public class Constants {

    public static final String REDIRECT = "redirect:";
    public static final String REFERER = "referer";
    public static final String ADD = "/add";
    public static final String DELETE = "/delete";
    public static final String UPDATE = "/update";

    public static final String ADD_ROUTE = "/addBus";
    public static final String INDEX = "/index.html";
    public static final String VIEW = "/view";
    public static final String PAGE = "/page";
    public static final String START = "/start";
    public static final String PAUSE = "/pause";
    public static final String ADD_BUS = "/addBus";

    public static String MESSAGE;
    public static String ERROR_MESSAGE = "errorMessage";

    public static class Mapping {
        public static final String ROUTE = "/route";
        public static final String SIMULATION = "/simulation";
        public static final String STATION = "/station";
        public static final String BUS = "/bus";
        public static final String ARC = "/arc";
        public static final String USER = "/user";
        public static final String REGISTER = "/register";
        public static final String LOGOUT = "/logout";
        public static final String LOGIN = "/login";
        public static final String DOWNLOAD = "/download";
        public static final String RESULTS = "/results";
    }

    public static class View {
        public static final String INDEX = "index";
        public static final String SIMULATION = "simulation";
        public static final String REGISTER = "register";
    }

    public static class Entities {
        public static final String ROUTE = "route";
        public static final String ARC = "arc";
        public static final String RESULTS = "results";
        public static final String STATION = "station";
        public static final String BUS = "bus";
        public static final String DAILY_FLOW = "daily_flow";
        public static final String LOGGED_USER = "logged_user";
        public static final String SIMULATION_PROCESS = "simulation_process";
    }

    public static class ParserPath {
        public static final String RESULTS_XML = "results/results.xml";
        public static final String INPUT_PASSENGERS = "input/inputPassengers.xml";
        public static final String SECURITY_XML = "security/rules.xml";
    }

}

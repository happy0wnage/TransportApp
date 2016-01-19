package ua.petrov.transport.core.JAXB.event;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Владислав on 07.01.2016.
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Results {

    @XmlElement(name = "event", required = true)
    private List<ResultEvent> eventList = new ArrayList<>();

    public Results() {}

    public Results(List<ResultEvent> eventList) {
        this.eventList = eventList;
    }

    public void addEvent(ResultEvent event) {
        eventList.add(event);
    }

    public List<ResultEvent> getEventList() {
        return eventList;
    }

    public void setEventList(List<ResultEvent> eventList) {
        this.eventList = eventList;
    }
}

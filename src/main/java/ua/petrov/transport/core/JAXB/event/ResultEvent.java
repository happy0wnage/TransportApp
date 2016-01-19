package ua.petrov.transport.core.JAXB.event;

import ua.petrov.transport.core.JAXB.adapter.DateTimeAdapter;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.sql.Timestamp;

/**
 * Created by Владислав on 07.01.2016.
 */

@XmlType(name = "event",
        propOrder = {
                "startDate",
                "endDate",
                "loadingPercent",
                "routes",
                "satisfiedCount",
                "dissatisfiedCount",
        })
@XmlAccessorType(XmlAccessType.FIELD)
public class ResultEvent {

    @XmlAttribute
    private int id;

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    @XmlElement(name = "start_date", required = true)
    private Timestamp startDate;

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    @XmlElement(name = "end_date", required = true)
    private Timestamp endDate;

    @XmlElement(name = "loading_percent", required = true)
    private double loadingPercent;

    @XmlElement(name = "routes", required = true)
    private String routes;

    @XmlElement(name = "satisfied_count", required = true)
    private int satisfiedCount;

    @XmlElement(name = "dissatisfied_count", required = true)
    private int dissatisfiedCount;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Timestamp getStartDate() {
        return startDate;
    }

    public void setStartDate(Timestamp startDate) {
        this.startDate = startDate;
    }

    public Timestamp getEndDate() {
        return endDate;
    }

    public void setEndDate(Timestamp endDate) {
        this.endDate = endDate;
    }

    public double getLoadingPercent() {
        return loadingPercent;
    }

    public void setLoadingPercent(double loadingPercent) {
        this.loadingPercent = loadingPercent;
    }

    public String getRoutes() {
        return routes;
    }

    public void setRoutes(String routes) {
        this.routes = routes;
    }

    public int getSatisfiedCount() {
        return satisfiedCount;
    }

    public void setSatisfiedCount(int satisfiedCount) {
        this.satisfiedCount = satisfiedCount;
    }

    public int getDissatisfiedCount() {
        return dissatisfiedCount;
    }

    public void setDissatisfiedCount(int dissatisfiedCount) {
        this.dissatisfiedCount = dissatisfiedCount;
    }

    @Override
    public String toString() {
        return "Event{" +
                ", startDate='" + startDate + '\'' +
                ", endDate='" + endDate + '\'' +
                ", loadingPercent=" + loadingPercent +
                ", routes=" + routes +
                ", satisfiedCount=" + satisfiedCount +
                ", dissatisfiedCount=" + dissatisfiedCount +
                '}';
    }
}

package ua.petrov.transport.core.entity;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Владислав on 11.01.2016.
 */
@XmlRootElement(name = "buses")
@XmlAccessorType(XmlAccessType.FIELD)
public class Buses {

    @XmlElement(name = "bus")
    private List<Bus> busList = new ArrayList<>();

    private void addToBusList(Bus bus) {
        busList.add(bus);
    }

    public List<Bus> getBusList() {
        return busList;
    }

    public void setBusList(List<Bus> busList) {
        this.busList = busList;
    }

    @Override
    public String toString() {
        return "Buses{" +
                "busList=" + busList +
                '}';
    }
}

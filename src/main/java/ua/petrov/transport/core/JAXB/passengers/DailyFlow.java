package ua.petrov.transport.core.JAXB.passengers;

import javax.xml.bind.annotation.*;
import java.util.List;

/**
 * Created by Владислав on 11.01.2016.
 */
@XmlRootElement(name = "daily_flow")
@XmlAccessorType(XmlAccessType.FIELD)
public class DailyFlow {

    @XmlElement(name = "period")
    private List<ArrivalPeriod> periods;

    public List<ArrivalPeriod> getPeriods() {
        return periods;
    }

    public void setPeriods(List<ArrivalPeriod> periods) {
        this.periods = periods;
    }

    @Override
    public String toString() {
        return "DailyFrow{" +
                "periods=" + periods +
                '}';
    }
}

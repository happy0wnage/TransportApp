package ua.petrov.transport.core.JAXB.adapter;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.sql.Time;
import java.time.LocalTime;

/**
 * Created by Владислав on 11.01.2016.
 */
public class TimeAdapter extends XmlAdapter<String, Time> {

    @Override
    public Time unmarshal(String v) throws Exception {
        return Time.valueOf(LocalTime.parse(v));
    }

    @Override
    public String marshal(Time v) throws Exception {
        return v.toLocalTime().toString();
    }
}

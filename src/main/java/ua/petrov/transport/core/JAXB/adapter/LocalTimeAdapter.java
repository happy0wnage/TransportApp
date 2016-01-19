package ua.petrov.transport.core.JAXB.adapter;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.time.LocalTime;

/**
 * Created by Владислав on 11.01.2016.
 */
public class LocalTimeAdapter extends XmlAdapter<String, LocalTime> {

    @Override
    public LocalTime unmarshal(String v) throws Exception {
        return LocalTime.parse(v);
    }

    @Override
    public String marshal(LocalTime v) throws Exception {
        return v.toString();
    }
}

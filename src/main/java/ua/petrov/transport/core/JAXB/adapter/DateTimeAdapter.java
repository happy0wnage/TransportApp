package ua.petrov.transport.core.JAXB.adapter;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Created by Владислав on 10.01.2016.
 */
public class DateTimeAdapter extends XmlAdapter<String, Timestamp> {

    DateTimeFormatter pattern = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public Timestamp unmarshal(String v) throws Exception {
        return Timestamp.valueOf(LocalDateTime.parse(v, pattern));
    }

    @Override
    public String marshal(Timestamp v) throws Exception {
        return v.toLocalDateTime().format(pattern);
    }
}

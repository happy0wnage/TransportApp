package ua.petrov.transport.core.JAXB;

import org.springframework.core.io.ClassPathResource;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Владислав on 18.01.2016.
 */
public class ParserUtil {

    public static <T> void marshal(T t, String path) {
        try {
            File file = new ClassPathResource(path).getFile();
            JAXBContext jaxbContext = JAXBContext.newInstance(t.getClass());
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            jaxbMarshaller.marshal(t, file);
        } catch (IOException | JAXBException e) {
            e.printStackTrace();
        }
    }

    public static <T> T unmarshal(Class<T> cl, String path) {
        try {
            InputStream is = new ClassPathResource(path).getInputStream();
            JAXBContext jc = JAXBContext.newInstance(cl);
            Unmarshaller u = jc.createUnmarshaller();
            return (T) u.unmarshal(is);
        } catch (JAXBException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}

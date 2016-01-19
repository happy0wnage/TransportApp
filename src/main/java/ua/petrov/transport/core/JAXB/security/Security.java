package ua.petrov.transport.core.JAXB.security;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * Created by Владислав on 15.01.2016.
 */
@XmlRootElement(name = "security")
@XmlAccessorType(XmlAccessType.FIELD)
public class Security {

    @XmlElement(name = "rule")
    private List<Rule> rules;

    public List<Rule> getRules() {
        return rules;
    }

    public void setRules(List<Rule> rules) {
        this.rules = rules;
    }

    @Override
    public String toString() {
        return "Security{" +
                "rules=" + rules +
                '}';
    }
}

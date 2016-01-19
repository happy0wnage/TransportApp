package ua.petrov.transport.core.entity;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;

/**
 * Created by ��������� on 23 ��� 2015 �..
 */
@XmlType(name = "type")
@XmlEnum
public enum Type {

    CIRCLE, DIRECT;

    public int getId() {
        return this.ordinal() + 1;
    }

    public static int getId(Type type) {
        return type.ordinal() + 1;
    }

    public static Type getTypeById(int id) {
        return values()[id - 1];
    }

    @Override
    public String toString() {
        return name().toLowerCase();
    }

    public static void main(String[] args) {
        System.out.println(Type.getTypeById(1));
    }
}



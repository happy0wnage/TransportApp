package ua.petrov.transport.core.entity;

/**
 * Created by ��������� on 25 ��� 2015 �..
 */
public class Entity {

    private int id = 0;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Entity() {}

    public Entity(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Entity{" +
                "id=" + id +
                '}';
    }
}

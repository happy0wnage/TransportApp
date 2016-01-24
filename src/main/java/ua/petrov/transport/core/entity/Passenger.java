package ua.petrov.transport.core.entity;

/**
 * Created by ��������� on 23 ��� 2015 �..
 */
public class Passenger implements Comparable<Passenger>{

    private long waitingTime;

    private Station currentStation;

    private Station destination;

    private long timeOfTheDay;

    public Passenger() {
    }

    public Station getCurrentStation() {
        return currentStation;
    }

    public void setCurrentStation(Station currentStation) {
        this.currentStation = currentStation;
    }

    public long getWaitingTime() {
        return waitingTime;
    }

    public void setWaitingTime(long waitingTime) {
        this.waitingTime = waitingTime;
    }

    public Station getDestination() {
        return destination;
    }

    public void setDestination(Station destination) {
        this.destination = destination;
    }

    public long getTimeOfTheDay() {
        return timeOfTheDay;
    }

    public void setTimeOfTheDay(long timeOfTheDay) {
        this.timeOfTheDay = timeOfTheDay;
    }


    @Override
    public String toString() {
        return "Passenger{" +
                "waitingTime=" + waitingTime +
                ", destination=" + destination +
                ", currentStation=" + currentStation +
                ", timeOfTheDay=" + timeOfTheDay +
                '}';
    }

    @Override
    public int compareTo(Passenger o) {
        return Long.compare(timeOfTheDay, o.getTimeOfTheDay());
    }
}

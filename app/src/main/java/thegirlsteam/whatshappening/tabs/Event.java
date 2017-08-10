package thegirlsteam.whatshappening.tabs;

/**
 * Created by Dona Maria on 12/1/2016.
 * Object to store information related to an event
 */

public class Event {
    private String eventName;
    private String eventVenue;
    private String eventTime;
    private String eventType;
    private String eventLat;
    private String eventLon;
    private double distance;
    private long timeToEvent;
    private String sensorLat;
    private String sensorLon;
    private String imageUrl;

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getEventVenue() {
        return eventVenue;
    }

    public void setEventVenue(String eventVenue) {
        this.eventVenue = eventVenue;
    }

    public String getEventTime() {
        return eventTime;
    }

    public void setEventTime(String eventTime) {
        this.eventTime = eventTime;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getEventLat() {
        return eventLat;
    }

    public void setEventLat(String eventLat) {
        this.eventLat = eventLat;
    }

    public String getEventLon() {
        return eventLon;
    }

    public void setEventLon(String eventLon) {
        this.eventLon = eventLon;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public long getTimeToEvent() {
        return timeToEvent;
    }

    public void setTimeToEvent(long timeToEvent) {
        this.timeToEvent = timeToEvent;
    }

    public String getSensorLat() {
        return sensorLat;
    }

    public void setSensorLat(String sensorLat) {
        this.sensorLat = sensorLat;
    }

    public String getSensorLon() {
        return sensorLon;
    }

    public void setSensorLon(String sensorLon) {
        this.sensorLon = sensorLon;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}

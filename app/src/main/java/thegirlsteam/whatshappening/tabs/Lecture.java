package thegirlsteam.whatshappening.tabs;

/**
 * Created by Dona Maria on 12/1/2016.
 * Stores informations related to lectures
 */
public class Lecture {
    private String course;
    private String professor;
    private String classroom;
    private String timing;
    private String startTime;
    private String section;
    private double distance;
    private long timeToEvent;
    private String sensorLat;
    private String sensorLon;
    private String lat;
    private String lon;
    private String imageUrl;

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getProfessor() {
        return professor;
    }

    public void setProfessor(String professor) {
        this.professor = professor;
    }

    public String getClassroom() {
        return classroom;
    }

    public void setClassroom(String classroom) {
        this.classroom = classroom;
    }

    public String getTiming() {
        return timing;
    }

    public void setTiming(String timing) {
        this.timing = timing;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
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

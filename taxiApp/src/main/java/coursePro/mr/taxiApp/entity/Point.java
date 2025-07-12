package coursePro.mr.taxiApp.entity;

import jakarta.persistence.Embeddable;

@Embeddable
public class Point {
    private Double latitude;  // Changé de double à Double
    private Double longitude; // Changé de double à Double
    private String address;
    private String place_name;

    public Point() {
    }

    public Point(Double latitude, Double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Point(Double latitude, Double longitude, String address, String place_name) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.address = address;
        this.place_name = place_name;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPlace_name() {
        return place_name;
    }

    public void setPlace_name(String place_name) {
        this.place_name = place_name;
    }
}
package beans;

public class Location {
	private int id;
    private double longitude;
    private double latitude;
    private String address;

    public Location() {
    }

    public Location(int id, double longitude, double latitude, String address) {
        this.id = id;
        this.longitude = longitude;
        this.latitude = latitude;
        this.address = address;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}

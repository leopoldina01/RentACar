package beans;

import java.util.ArrayList;


public class RentACarStore {
    private int id;
    private String name;
    private ArrayList<Integer> offeredVehicles;
    private String workingTime;
    private WorkingStatus workingStatus;
    private Location location;
    private int locationId;
    private String logoUrl;
    private double rating;
    
    public RentACarStore() {
    	offeredVehicles = new ArrayList<>();
    }

    public RentACarStore(int id, String name, ArrayList<Integer> offeredVehicles, WorkingStatus workingStatus, String workingTime, Location location, int locationId, String logoUrl, double rating) {
        this.id = id;
        this.name = name;
        this.offeredVehicles = offeredVehicles;
        this.workingTime = workingTime;
        this.workingStatus = workingStatus;
        this.location = location;
        this.locationId = locationId;
        this.logoUrl = logoUrl;
        this.rating = rating;
    }

    public String getWorkingTime() {
        return workingTime;
    }

    public void setWorkingTime(String workingTime) {
        this.workingTime = workingTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Integer> getOfferedVehicles() {
        return offeredVehicles;
    }

    public void setOfferedVehicles(ArrayList<Integer> offeredVehicles) {
        this.offeredVehicles = offeredVehicles;
    }

    public int getLocationId() {
        return locationId;
    }

    public void setLocationId(int locationId) {
        this.locationId = locationId;
    }

    public WorkingStatus getWorkingStatus() {
        return workingStatus;
    }

    public void setWorkingStatus(WorkingStatus workingStatus) {
        this.workingStatus = workingStatus;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "RentACarStore{" +
                "name='" + name + '\'' +
                ", workingStatus=" + workingStatus +
                ", location='" + location + '\'' +
                ", logoUrl='" + logoUrl + '\'' +
                ", rating=" + rating +
                '}';
    }
}

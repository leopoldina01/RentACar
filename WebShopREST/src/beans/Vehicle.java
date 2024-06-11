package beans;


public class Vehicle {
    private int id;
    private String brand;
    private String model;
    private double price;
    private VehicleType vehicleType;
    private RentACarStore store;
    private GearshiftType gearshiftType;
    private FuelType fuelType;
    private double consumption;
    private int doorsNumber;
    private int passengersNumber;
    private String description;
    private String imageUrl;
    private Status status;
    
    public Vehicle() {
    }
    
    public Vehicle(int id, String brand, String model, double price, VehicleType vehicleType, RentACarStore store, GearshiftType gearshiftType, FuelType fuelType, double consumption, int doorsNumber, int passengersNumber, String description, String imageUrl, Status status) {
        this.id = id;
        this.brand = brand;
        this.model = model;
        this.price = price;
        this.vehicleType = vehicleType;
        this.store = store;
        this.gearshiftType = gearshiftType;
        this.fuelType = fuelType;
        this.consumption = consumption;
        this.doorsNumber = doorsNumber;
        this.passengersNumber = passengersNumber;
        this.description = description;
        this.imageUrl = imageUrl;
        this.status = status;
    }

    public Status getStatus() {
		return status;
	}
	public void setStatus(Status status) {
		this.status = status;
	}
	public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public VehicleType getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(VehicleType vehicleType) {
        this.vehicleType = vehicleType;
    }

    public GearshiftType getGearshiftType() {
        return gearshiftType;
    }

    public void setGearshiftType(GearshiftType gearshiftType) {
        this.gearshiftType = gearshiftType;
    }

    public FuelType getFuelType() {
        return fuelType;
    }

    public void setFuelType(FuelType fuelType) {
        this.fuelType = fuelType;
    }

    public double getConsumption() {
        return consumption;
    }

    public void setConsumption(double consumption) {
        this.consumption = consumption;
    }

    public int getDoorsNumber() {
        return doorsNumber;
    }

    public void setDoorsNumber(int doorsNumber) {
        this.doorsNumber = doorsNumber;
    }

    public int getPassengersNumber() {
        return passengersNumber;
    }

    public void setPassengersNumber(int passengersNumber) {
        this.passengersNumber = passengersNumber;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public RentACarStore getStore() {
        return store;
    }

    public void setStore(RentACarStore store) {
        this.store = store;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Vehicle{" +
                "brand='" + brand + '\'' +
                ", model='" + model + '\'' +
                ", price=" + price +
                ", vehicleType=" + vehicleType +
                ", store=" + store +
                ", gearshiftType=" + gearshiftType +
                ", fuelType=" + fuelType +
                ", consumption=" + consumption +
                ", doorsNumber=" + doorsNumber +
                ", passengersNumber=" + passengersNumber +
                ", description='" + description + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                '}';
    }
}

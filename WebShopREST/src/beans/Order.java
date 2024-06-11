package beans;

import java.time.LocalDate;
import java.util.ArrayList;

public class Order {
	private int id;
	private String idString;
    private ArrayList<Integer> rentedVehicles;
    private RentACarStore rentACarStore;
    private String rentalDate;
    private String pickUpDate;
    private String returnDate;
    private int price;
    private String customerFirstAndLastName;
    private OrderStatus status;
    private int customerId;
    private String comment;
    private String cancelDate;

    public Order() {
    	rentedVehicles = new ArrayList<Integer>();
    }

    public Order(int id, String idString, ArrayList<Integer> rentedVehicles, RentACarStore rentACarStore, String rentalDate, String pickUpDate, String returnDate, int price, String customerFirstAndLastName, OrderStatus status, int customerId, String comment, String cancelDate) {
    	this.id = id;
        this.idString = idString;
        this.rentedVehicles = rentedVehicles;
        this.rentACarStore = rentACarStore;
        this.rentalDate = rentalDate;
        this.pickUpDate = pickUpDate;
        this.returnDate = returnDate;
        this.price = price;
        this.customerFirstAndLastName = customerFirstAndLastName;
        this.status = status;
        this.customerId = customerId;
        this.comment = comment;
        this.cancelDate = cancelDate;
    }
    
    public String getCancelDate() {
		return cancelDate;
	}

	public void setCancelDate(String cancelDate) {
		this.cancelDate = cancelDate;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getPickUpDate() {
		return pickUpDate;
	}

	public void setPickUpDate(String pickUpDate) {
		this.pickUpDate = pickUpDate;
	}

	public String getReturnDate() {
		return returnDate;
	}

	public void setReturnDate(String returnDate) {
		this.returnDate = returnDate;
	}

	public int getCustomerId() {
		return customerId;
	}

	public void setCustomerId(int customerId) {
		this.customerId = customerId;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public String getIdString() {
        return idString;
    }

    public void setIdString(String idString) {
        this.idString = idString;
    }

    public ArrayList<Integer> getRentedVehicles() {
        return rentedVehicles;
    }

    public void setRentedVehicles(ArrayList<Integer> rentedVehicles) {
        this.rentedVehicles = rentedVehicles;
    }

    public RentACarStore getRentACarStore() {
        return rentACarStore;
    }

    public void setRentACarStore(RentACarStore rentACarStore) {
        this.rentACarStore = rentACarStore;
    }

    public String getRentalDate() {
        return rentalDate;
    }

    public void setRentalDate(String rentalDate) {
        this.rentalDate = rentalDate;
    }
    
    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getCustomerFirstAndLastName() {
        return customerFirstAndLastName;
    }

    public void setCustomerFirstAndLastName(String customerFirstAndLastName) {
        this.customerFirstAndLastName = customerFirstAndLastName;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Order{" +
                "idString='" + idString + '\'' +
                ", rentedVehicles=" + rentedVehicles +
                ", rentACarStore=" + rentACarStore +
                ", rentalDate=" + rentalDate +
                ", price=" + price +
                ", customerFirstAndLastName='" + customerFirstAndLastName + '\'' +
                ", status=" + status +
                '}';
    }
}

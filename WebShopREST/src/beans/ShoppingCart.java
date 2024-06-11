package beans;

import java.util.ArrayList;

public class ShoppingCart {
    private int id;
    private ArrayList<Integer> vehicles;
    private int price;
    private String pickUpDate;
    private String returnDate;

    public ShoppingCart() {
    	vehicles = new ArrayList<Integer>();
    	price = 0;
    	pickUpDate = "0000-00-00";
    	returnDate = "0000-00-00";
    }

    public ShoppingCart(int id, ArrayList<Integer> vehicles, int price, String pickUpDate, String returnDate) {
        this.vehicles = vehicles;
        this.id = id;
        this.price = price;
        this.pickUpDate = pickUpDate;
        this.returnDate = returnDate;
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

	public ArrayList<Integer> getVehicles() {
        return vehicles;
    }

    public void setVehicles(ArrayList<Integer> vehicles) {
        this.vehicles = vehicles;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "ShoppingCart{" +
                "vehicles=" + vehicles +
                ", price=" + price +
                '}';
    }
}

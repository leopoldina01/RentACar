package beans;

import java.util.ArrayList;

public class Customer extends User {
	private ArrayList<Integer> allOrders;
    private ShoppingCart shoppingCart;
    private int score;
    private CustomerType customerType;
    private boolean suspicious;

    public Customer() {
    	allOrders = new ArrayList<Integer>();
    	shoppingCart = new ShoppingCart();
    }

    //change this constructor later
    public Customer(int id, String username, String password, String firstName, String lastName, String gender, String dateOfBirth, Role role, ArrayList<Integer> allOrders, ShoppingCart shoppingCart, int score, CustomerType customerType, boolean suspicious) {
    	super(id, username, password, firstName, lastName, gender, dateOfBirth);
        this.allOrders = allOrders;
        this.shoppingCart = shoppingCart;
        this.score = score;
        this.customerType = customerType;
        this.suspicious = suspicious;
    }
    
	public boolean getSuspicious() {
		return suspicious;
	}

	public void setSuspicious(boolean suspicious) {
		this.suspicious = suspicious;
	}

	public ArrayList<Integer> getAllOrders() {
        return allOrders;
    }

    public void setAllOrders(ArrayList<Integer> allOrders) {
        this.allOrders = allOrders;
    }

    public ShoppingCart getShoppingCart() {
        return shoppingCart;
    }

    public void setShoppingCart(ShoppingCart shoppingCart) {
        this.shoppingCart = shoppingCart;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public CustomerType getCustomerType() {
        return customerType;
    }

    public void setCustomerType(CustomerType customerType) {
        this.customerType = customerType;
    }
}

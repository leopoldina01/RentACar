package dao;

import beans.Customer;
import beans.Order;
import beans.User;
import beans.ShoppingCart;
import beans.CustomerType;
import beans.UserType;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.stream.Collectors;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class CustomerDAO {
	private HashMap<Integer, Customer> customers = new HashMap<Integer, Customer>();

    public CustomerDAO() {
    }

    public Collection<Customer> findAll() {
    	deserialize();
        return customers.values();
    }

    public Customer findCustomer(int id) {
    	deserialize();
        return customers.containsKey(id) ? customers.get(id) : null;
    }
    
    public Customer findCustomerByUsername(String username)
    {
    	deserialize();
    	
    	for (Customer c : customers.values())
    	{
    		if (c.getUsername().equals(username))
    		{
    			return c;
    		}
    	}
    	
    	return null;
    }
    
    public Collection<Customer> getSuspiciousCustomers()
    {
    	deserialize();
    	
    	OrderDAO orderDAO = new OrderDAO();
    	
    	for (Customer c : customers.values())
    	{
    		c.setSuspicious(orderDAO.isCustomerSuspicious(c.getAllOrders()));
    		update(c.getId(), c);
    	}
    	
    	Collection<Customer> suspiciousCustomers = findAll();
    	suspiciousCustomers = suspiciousCustomers.stream().filter(c -> c.getSuspicious() == true).collect(Collectors.toList());
    	
    	return suspiciousCustomers;
    }
    
    public void removePointsForCancelingOrder(Order order)
    {
    	Customer customer = findCustomer(order.getCustomerId());
    	
    	int customerCurrentScore = customer.getScore();
    	customerCurrentScore -= (order.getPrice()/1000*133*4);
    	
    	customer.setScore(customerCurrentScore);
    	
    	update(customer.getId(), customer);
    }
    
    public Customer removeShoppingCartAfterOrder(Customer customer)
    {
    	ArrayList<Integer> vehicles = customer.getShoppingCart().getVehicles();
    	vehicles.clear();
    	customer.getShoppingCart().setVehicles(vehicles);
    	customer.getShoppingCart().setPrice(0);
    	
    	customer = calculateCystomerType(customer);
    	
    	return update(customer.getId(), customer);
    }
    
    public Customer calculateCystomerType(Customer customer)
    {
    	if (customer.getScore() < 30000)
    	{
    		customer.getCustomerType().setType(UserType.BRONZE);
    		customer.getCustomerType().setDiscount(0);
    	}
    	else if (customer.getScore() < 40000)
    	{
    		customer.getCustomerType().setType(UserType.SILVER);
    		customer.getCustomerType().setDiscount(3);
    	}
    	else 
    	{
    		customer.getCustomerType().setType(UserType.GOLD);
    		customer.getCustomerType().setDiscount(5);
    	}
    	
    	return customer;
    }
    
    public Customer updateLoggedCustomer(String customerUsername)
    {
    	Customer customer = findCustomerByUsername(customerUsername);
    	ArrayList<Integer> vehicles = customer.getShoppingCart().getVehicles();
    	vehicles.clear();
    	customer.getShoppingCart().setVehicles(vehicles);
    	customer.getShoppingCart().setPrice(0);
    	
    	return update(customer.getId(), customer);
    }
    
    public Customer updateShoppingCartDates(String pickUpDate, String returnDate, int customerId)
    {
    	Customer customer = findCustomer(customerId);
    	customer.getShoppingCart().setPickUpDate(pickUpDate);
    	customer.getShoppingCart().setReturnDate(returnDate);
    	return update(customer.getId(), customer);
    }
    
	public int calculateDays(Customer customer)
	{
		LocalDate pickUpDate = LocalDate.parse(customer.getShoppingCart().getPickUpDate());
		LocalDate returnDate = LocalDate.parse(customer.getShoppingCart().getReturnDate());
		
		int numberOfDays = (int) ChronoUnit.DAYS.between(pickUpDate, returnDate);
		
		return numberOfDays;
	}
    
    public Customer AddCarToCart(int vehicleId, int customerId, int price)
    {
    	Customer customer = findCustomer(customerId);
    	ArrayList<Integer> vehicles = customer.getShoppingCart().getVehicles();
    	vehicles.add(vehicleId);
    	
    	Collections.sort(vehicles);
    	
    	int numberOfDays = calculateDays(customer);
    	
    	customer.getShoppingCart().setVehicles(vehicles);
    	int currentPrice = customer.getShoppingCart().getPrice();
    	currentPrice += (price*numberOfDays);
    	customer.getShoppingCart().setPrice(currentPrice);
    	
    	return update(customerId, customer);
    }
    
    public Customer RemoveFromCart(int vehicleId, int customerId, int price)
    {
    	Customer customer = findCustomer(customerId);
    	ArrayList<Integer> vehicles = customer.getShoppingCart().getVehicles();
    	vehicles.remove(vehicles.indexOf(vehicleId));
    	customer.getShoppingCart().setVehicles(vehicles);
    	
    	int numberOfDays = calculateDays(customer);
    	
    	int currentPrice = customer.getShoppingCart().getPrice();
    	currentPrice -= (price*numberOfDays);
    	customer.getShoppingCart().setPrice(currentPrice);
    	
    	return update(customerId, customer);
    }
    
    public void registrate(User user)
    {
    	Customer customer = new Customer();
    	customer.setId(user.getId());
    	customer.setUsername(user.getUsername());
    	customer.setPassword(user.getPassword());
    	customer.setFirstName(user.getFirstName());
    	customer.setLastName(user.getLastName());
    	customer.setGender(user.getGender());
    	customer.setDateOfBirth(user.getDateOfBirth());
    	customer.setRole(user.getRole());
    	customer.setIsBlocked(user.getIsBlocked());
    	
    	customer.setAllOrders(new ArrayList<Integer>());
    	ShoppingCart cart = new ShoppingCart();
    	cart.setId(0);
    	cart.setVehicles(new ArrayList<Integer>());
    	cart.setPrice(0);
    	cart.setPickUpDate("0000-00-00");
    	cart.setReturnDate("0000-00-00");
    	customer.setShoppingCart(cart);
    	customer.setScore(0);
    	CustomerType type = new CustomerType();
    	customer.setCustomerType(type);
    	customer.getCustomerType().setId(1);
    	customer.getCustomerType().setType(UserType.BRONZE);
    	customer.getCustomerType().setDiscount(0);
    	customer.getCustomerType().setRequiredScore(0);
    	customer.setSuspicious(false);
    	
    	save(customer);
    }
    
    public void blockCustomer(int customerId)
    {
    	Customer customer = findCustomer(customerId);
    	customer.setIsBlocked(true);
    	update(customer.getId(), customer);
    }

    public Customer save(Customer customer) {
    	deserialize();
    	
        customers.put(customer.getId(), customer);
        
        serialize();
        
        return customer;
    }

    public Customer update(int id, Customer customer)
    {
        Customer customerToUpdate = findCustomer(id);

        if (customerToUpdate == null)
        {
            return save(customer);
        }

        customerToUpdate.setUsername(customer.getUsername());
        customerToUpdate.setPassword(customer.getPassword());
        customerToUpdate.setFirstName(customer.getFirstName());
        customerToUpdate.setLastName(customer.getLastName());
        customerToUpdate.setGender(customer.getGender());
        customerToUpdate.setDateOfBirth(customer.getDateOfBirth());
        customerToUpdate.setRole(customer.getRole());
        customerToUpdate.setIsBlocked(customer.getIsBlocked());
        customerToUpdate.setAllOrders(customer.getAllOrders());
        customerToUpdate.setShoppingCart(customer.getShoppingCart());
        customerToUpdate.setScore(customer.getScore());
        customerToUpdate.setCustomerType(customer.getCustomerType());
        customerToUpdate.setSuspicious(customer.getSuspicious());

        serialize();

        return customerToUpdate;
    }

    public void delete (int id)
    {
        customers.remove(id);
        serialize();
    }
    
    public void serialize()
    {
    	try {
    		File file = new File(System.getProperty("user.dir") + "/WebContent/resources/Customers.json");
    		FileWriter fileWriter = new FileWriter(file);
    		Gson gson = new GsonBuilder().setPrettyPrinting().create();
    		JsonArray jsonArray = new JsonArray();
    		
    		for (Customer customer : customers.values())
    		{
    			JsonObject jsonObject = new JsonObject();
    			jsonObject.addProperty("id", customer.getId());
    			jsonObject.addProperty("username", customer.getUsername());
    			jsonObject.addProperty("password", customer.getPassword());
    			jsonObject.addProperty("firstName", customer.getFirstName());
    			jsonObject.addProperty("lastName", customer.getLastName());
    			jsonObject.addProperty("gender", customer.getGender());
    			jsonObject.addProperty("dateOfBirth", customer.getDateOfBirth());
    			jsonObject.addProperty("role", customer.getRole().toString());
    			jsonObject.addProperty("isBlocked", customer.getIsBlocked());
    			jsonObject.addProperty("suspicious", customer.getSuspicious());
    			
    			JsonArray allOrdersArray = new JsonArray();
    			for (int el : customer.getAllOrders())
    			{
    				allOrdersArray.add(el);
    			}
    			
    			jsonObject.add("allOrders", allOrdersArray);
    			
    				JsonObject shoppingCartObject = new JsonObject();
    				shoppingCartObject.addProperty("id", customer.getShoppingCart().getId());
    				
    				JsonArray vehiclesArray = new JsonArray();
    				for (int el : customer.getShoppingCart().getVehicles())
    				{
    					vehiclesArray.add(el);
    				}
    				
    				shoppingCartObject.add("vehicles", vehiclesArray);
    				shoppingCartObject.addProperty("price", customer.getShoppingCart().getPrice());
    				shoppingCartObject.addProperty("pickUpDate", customer.getShoppingCart().getPickUpDate());
    				shoppingCartObject.addProperty("returnDate", customer.getShoppingCart().getReturnDate());
    				
    				jsonObject.add("shoppingCart", shoppingCartObject);
    			
    			jsonObject.addProperty("score", customer.getScore());
    			
    				JsonObject customerTypeObject = new JsonObject();
    				customerTypeObject.addProperty("id", customer.getCustomerType().getId());
    				customerTypeObject.addProperty("type", customer.getCustomerType().getType().toString());
    				customerTypeObject.addProperty("discount", customer.getCustomerType().getDiscount());
    				customerTypeObject.addProperty("requiredScore", customer.getCustomerType().getRequiredScore());
    				
    				jsonObject.add("customerType", customerTypeObject);
    			
    			jsonArray.add(jsonObject);
    		}
    		
    		String jsonData = gson.toJson(jsonArray);
    		fileWriter.write(jsonData);
    		fileWriter.close();
    		
    	} catch (Exception e)
    	{
    		System.out.println("ne mogu da serijalizujem customer");
    	}
    }
    
    public void deserialize()
    {
    	try {
    		File file = new File(System.getProperty("user.dir") + "/WebContent/resources/Customers.json");
    		FileReader fileReader = new FileReader(file);
    		Gson gson = new Gson();
    		Customer[] customersArray = gson.fromJson(fileReader, Customer[].class);
    		
    		if (customersArray == null)
    		{
    			return;
    		}
    		
    		for (Customer customer : customersArray)
    		{
    			customers.put(customer.getId(), customer);
    		}
    		
    		fileReader.close();
    		
    	} catch (Exception e)
    	{
    		System.out.println("ne mogu da deserijalizujem customer");
    	}
    }
}

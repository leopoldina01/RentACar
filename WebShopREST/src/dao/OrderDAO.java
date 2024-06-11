package dao;

import beans.Customer;

import beans.Order;
import beans.OrderStatus;
import beans.RentACarStore;
import beans.Status;
import beans.User;
import beans.Vehicle;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.HashSet;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.Random;

import java.time.temporal.ChronoUnit;

public class OrderDAO {
	private HashMap<Integer, Order> orders = new HashMap<Integer, Order>();
	
	public OrderDAO()
	{
		
	}
	
	public Collection<Order> findAll()
	{
		deserialize();
		return orders.values();
	}
	
	public Collection<Order> cancelOrder(Order order)
	{
		deserialize();
		Order updatedOrder = findOrder(order.getId());
		
		updatedOrder.setStatus(OrderStatus.CANCELED);
		
		CustomerDAO dao = new CustomerDAO();
		dao.removePointsForCancelingOrder(order);
		
		updatedOrder.setCancelDate(LocalDate.now().toString());
		
		update(updatedOrder.getId(), updatedOrder);
		
		Customer customer = dao.findCustomer(order.getCustomerId());
		
		deserialize();
		
		customer.setSuspicious(isCustomerSuspicious(customer.getAllOrders()));
		customer = dao.calculateCystomerType(customer);
		
		dao.update(customer.getId(), customer);
		
		return findAllOrders(customer.getAllOrders());
	}
	
	public boolean isCustomerSuspicious(ArrayList<Integer> allOrders)
	{
		ArrayList<Order> customerOrders = findAllOrders(allOrders);
		
		LocalDate currentDate = LocalDate.now();
		
		int numberOfCancelations = 0;
		
		for(Order o : customerOrders)
		{
			if (o.getStatus() == OrderStatus.CANCELED)
			{
				LocalDate oneMonthAgo = currentDate.minus(1, ChronoUnit.MONTHS);
				
				if (LocalDate.parse(o.getCancelDate()).isAfter(oneMonthAgo))
				{
					numberOfCancelations++;
				}
			}
			
		}
		
		return numberOfCancelations > 5;
	}
	
	public Collection<Order> approveOrder(Order order)
	{
		deserialize();
		order.setStatus(OrderStatus.APPROVED);
		
		update(order.getId(), order);
		
		deserialize();
		return findOrdersByStore(order.getRentACarStore().getId());
	}
	
	public Collection<Order> rejectOrder(String comment, int orderId)
	{
		Order order = findOrder(orderId);
		order.setStatus(OrderStatus.REJECTED);
		order.setComment(comment);
		
		update(order.getId(), order);
		
		deserialize();
		return findOrdersByStore(order.getRentACarStore().getId());
	}
	
	public Collection<Order> makeOrderTaken(int orderId)
	{
		Order order = findOrder(orderId);
		order.setStatus(OrderStatus.TAKEN);
		
		update(order.getId(), order);
		
		VehicleDAO vehicleDAO = new VehicleDAO();
		vehicleDAO.makeRented(order.getRentedVehicles());
		
		deserialize();
		return findOrdersByStore(order.getRentACarStore().getId());
	}
	
	public Collection<Order> returnOrder(int orderId)
	{
		Order order = findOrder(orderId);
		order.setStatus(OrderStatus.RETURNED);
		
		update(order.getId(), order);
		
		VehicleDAO vehicleDAO = new VehicleDAO();
		vehicleDAO.returnVehicles(order.getRentedVehicles());
		
		deserialize();
		return findOrdersByStore(order.getRentACarStore().getId());
	}
	
	public Boolean findIfAvailable(Vehicle vehicle, String pickUpDate, String returnDate)
	{
		deserialize();
		
		for (Order o : orders.values())
		{
			if (o.getRentedVehicles().contains(vehicle.getId()))
			{
				LocalDate pickUpDateLocal = LocalDate.parse(pickUpDate);
				LocalDate returnDateLocal = LocalDate.parse(returnDate);
				
				if ( ( ( pickUpDateLocal.isAfter(LocalDate.parse(o.getPickUpDate())) || pickUpDateLocal.isEqual(LocalDate.parse(o.getPickUpDate())) )
					&& (pickUpDateLocal.isBefore(LocalDate.parse(o.getReturnDate())) || pickUpDateLocal.isEqual(LocalDate.parse(o.getReturnDate())) ) )
					||
					(( returnDateLocal.isAfter(LocalDate.parse(o.getPickUpDate())) || returnDateLocal.isEqual(LocalDate.parse(o.getPickUpDate())) )
					&& ( returnDateLocal.isBefore(LocalDate.parse(o.getReturnDate())) || returnDateLocal.isEqual(LocalDate.parse(o.getReturnDate())) ))
					)
					{
						return false;
					}
			}
		}
		
		return true;
	}
	
	public Order findOrder(int id)
	{
		deserialize();
		return orders.containsKey(id) ? orders.get(id) : null;
	}
	
	public int calculateDays(Customer customer)
	{
		LocalDate pickUpDate = LocalDate.parse(customer.getShoppingCart().getPickUpDate());
		LocalDate returnDate = LocalDate.parse(customer.getShoppingCart().getReturnDate());
		
		int numberOfDays = (int) ChronoUnit.DAYS.between(pickUpDate, returnDate);
		
		return numberOfDays;
	}
	
	public String generateUniqueString()
	{
		int length = 10;
		StringBuilder sb = new StringBuilder();
		
		Random random = new Random();
		
		for (int i = 0; i < length; i++)
		{
			char randomChar = (char) (random.nextInt(26) + 'a');
			sb.append(randomChar);
		}
		
		String randomString = sb.toString();
		
		return randomString;
	}
	
	public Customer createFinalOrder(Customer customer)
	{
		ArrayList<Integer> stores = new ArrayList<Integer>();
		ArrayList<Vehicle> vehicles = new ArrayList<Vehicle>();
		ArrayList<Integer> allOrders = new ArrayList<Integer>();
		
		int totalOrderPrice = customer.getShoppingCart().getPrice();
		int additionalScore = totalOrderPrice/1000 * 133;
		int customerScore = customer.getScore();
		
		customer.setScore(customerScore + additionalScore);
		
		
		
		//treba jos dodati da se promeni potrebni broj bodova, tip korisnika i ostalo
		
		int numberOfDays = calculateDays(customer);
		
		VehicleDAO vehicleDAO = new VehicleDAO();
		
		allOrders = customer.getAllOrders();
		
		for (int v : customer.getShoppingCart().getVehicles())
		{
			Vehicle vehicle = vehicleDAO.findVehicle(v);
			vehicle.setStatus(Status.RENTED);
			vehicleDAO.update(vehicle.getId(), vehicle);
			vehicles.add(vehicle);
		}
		
		
		for (Vehicle v : vehicles)
		{
			if(!stores.contains(v.getStore().getId()))
			{
				stores.add(v.getStore().getId());
			}
		}
		
		for (Integer s : stores)
		{
			Order newOrder = new Order();
			ArrayList<Integer> rentedVehicles = new ArrayList<Integer>();
			int orderPrice = 0;
			
			for (Vehicle v : vehicles)
			{
				if (v.getStore().getId() == s)
				{
					rentedVehicles.add(v.getId());
					orderPrice += v.getPrice();
				}
			}
			
			orderPrice = orderPrice*numberOfDays;
			int discountedPrice = orderPrice;
			
			if (customer.getCustomerType().getDiscount() != 0)
			{
				discountedPrice = orderPrice - (customer.getCustomerType().getDiscount()/100*orderPrice);
			}
			
			newOrder.setIdString(generateUniqueString());
			newOrder.setRentedVehicles(rentedVehicles);
			newOrder.setRentACarStore(getRentACarStore(s));
			newOrder.setRentalDate(LocalDate.now().toString());
			newOrder.setPickUpDate(customer.getShoppingCart().getPickUpDate());
			newOrder.setReturnDate(customer.getShoppingCart().getReturnDate());
			newOrder.setPrice(discountedPrice);
			newOrder.setCustomerFirstAndLastName(customer.getFirstName() + " " + customer.getLastName());
			newOrder.setStatus(OrderStatus.PROCESSING);
			newOrder.setCustomerId(customer.getId());
			newOrder.setComment(new String());
			newOrder.setCancelDate(new String());
			save(newOrder);
			
			allOrders.add(newOrder.getId());
		}
		
		customer.setAllOrders(allOrders);
		CustomerDAO customerDAO = new CustomerDAO();
		customer = customerDAO.removeShoppingCartAfterOrder(customer);
		
		return customer;
	}
	
	public Customer createFinalOrder(Customer customer, ArrayList<Vehicle> vehicles)
	{
		ArrayList<Integer> stores = new ArrayList<Integer>();
		
		for (Vehicle v : vehicles)
		{
			if(!stores.contains(v.getStore().getId()))
			{
				stores.add(v.getStore().getId());
			}
		}
		
		for (Integer s : stores)
		{
			Order newOrder = new Order();
			ArrayList<Integer> rentedVehicles = new ArrayList<Integer>();
			int orderPrice = 0;
			
			for (Vehicle v : vehicles)
			{
				if (v.getStore().getId() == s)
				{
					rentedVehicles.add(v.getId());
					orderPrice += v.getPrice();
				}
			}
			
			newOrder.setIdString("0000000000");
			newOrder.setRentedVehicles(rentedVehicles);
			newOrder.setRentACarStore(getRentACarStore(s));
			newOrder.setRentalDate(LocalDate.now().toString());
			newOrder.setPickUpDate(customer.getShoppingCart().getPickUpDate());
			newOrder.setReturnDate(customer.getShoppingCart().getReturnDate());
			newOrder.setPrice(orderPrice);
			newOrder.setCustomerFirstAndLastName(customer.getFirstName() + " " + customer.getLastName());
			newOrder.setStatus(OrderStatus.PROCESSING);
			newOrder.setCustomerId(customer.getId());
			newOrder.setCancelDate(new String());
			
			save(newOrder);
		}
		
		CustomerDAO customerDAO = new CustomerDAO();
		customer = customerDAO.removeShoppingCartAfterOrder(customer);
		
		return customer;
	}
	
	
	
	public RentACarStore getRentACarStore(int storeId)
	{
		RentACarStoreDAO dao = new RentACarStoreDAO();
		return dao.findRentACarStore(storeId);
	}
	
	public ArrayList<Order> findAllOrders(ArrayList<Integer> allOrders)
	{
		deserialize();
		ArrayList<Order> customerOrders = new ArrayList<Order>();
		
		for (Order o : orders.values())
		{
			for (Integer i : allOrders)
			{
				if (i == o.getId())
				{
					customerOrders.add(o);
				}
			}
		}
		
		return customerOrders;
	}
	
	public ArrayList<Order> getSearchedOrdersForCustomer(int priceFrom, int priceTo, String dateFrom, String dateTo, int customerId, String sort, String storeName) 
	{
		ArrayList<Order> ordersByCustomer = findOrdersByCustomer(customerId);
		ArrayList<Order> searchOrdersList = findOrdersByCustomer(customerId);
		
		if (priceFrom != -1)
		{
			ordersByCustomer = (ArrayList<Order>) ordersByCustomer.stream().filter(o -> o.getPrice() > priceFrom).collect(Collectors.toList());
			searchOrdersList = (ArrayList<Order>) searchOrdersList.stream().filter(o -> o.getPrice() > priceFrom).collect(Collectors.toList());
		}
		
		if (priceTo != -1)
		{
			ordersByCustomer = (ArrayList<Order>) ordersByCustomer.stream().filter(o -> o.getPrice() < priceTo).collect(Collectors.toList());
			searchOrdersList = (ArrayList<Order>) searchOrdersList.stream().filter(o -> o.getPrice() < priceTo).collect(Collectors.toList());
		}
		
		if (!storeName.equals("null"))
		{
			searchOrdersList = (ArrayList<Order>) searchOrdersList.stream().filter(o -> o.getRentACarStore().getName().toLowerCase().contains(storeName.toLowerCase())).collect(Collectors.toList());
			ordersByCustomer = (ArrayList<Order>) ordersByCustomer.stream().filter(o -> o.getRentACarStore().getName().toLowerCase().contains(storeName.toLowerCase())).collect(Collectors.toList());
		}
		
		if (!dateFrom.equals("0000-00-00"))
		{
			LocalDate localDateFrom = LocalDate.parse(dateFrom);
			int i = 0;
			for (Order o : ordersByCustomer)
			{
				
				LocalDate rentalDate = LocalDate.parse(o.getRentalDate());
				if (rentalDate.isBefore(localDateFrom))
				{
					searchOrdersList.remove(i);
				}
				
				i++;
			}
			
			//ordersByStore = (ArrayList<Order>) ordersByStore.stream().filter(o -> LocalDate.parse(o.getRentalDate()).isAfter(localDateFrom)).collect(Collectors.toList());
		}
		
		if (!dateTo.equals("0000-00-00"))
		{
			LocalDate localDateTo = LocalDate.parse(dateTo);
			int i = 0;
			for (Order o : ordersByCustomer)
			{
				LocalDate rentalDate = LocalDate.parse(o.getRentalDate());
				if (rentalDate.isAfter(localDateTo))
				{
					searchOrdersList.remove(i);
				}
				
				i++;
			}
		}
		
		if (sort.equals("priceAscending"))
		{
			sortByPriceAscending(searchOrdersList);
		} else if (sort.equals("priceDescending"))
		{
			sortByPriceDescending(searchOrdersList);
		} else if (sort.equals("dateAscending"))
		{
			sortByDateAscending(searchOrdersList);
		} else if (sort.equals("dateDescending"))
		{
			sortByDateDescending(searchOrdersList);
		} else if (sort.equals("storeNameAscending"))
		{
			sortByStoreAscending(searchOrdersList);
		} else if (sort.equals("storeNameDescending"))
		{
			sortByStoreDescending(searchOrdersList);
		}
		
		return searchOrdersList;
	}
	
	public ArrayList<Order> getSearchedOrders(int priceFrom, int priceTo, String dateFrom, String dateTo, int storeId, String sort)
	{
		ArrayList<Order> ordersByStore = findOrdersByStore(storeId);
		ArrayList<Order> searchOrdersList = findOrdersByStore(storeId);
		
		if (priceFrom != -1)
		{
			ordersByStore = (ArrayList<Order>) ordersByStore.stream().filter(o -> o.getPrice() > priceFrom).collect(Collectors.toList());
			searchOrdersList = (ArrayList<Order>) searchOrdersList.stream().filter(o -> o.getPrice() > priceFrom).collect(Collectors.toList());
		}
		
		if (priceTo != -1)
		{
			ordersByStore = (ArrayList<Order>) ordersByStore.stream().filter(o -> o.getPrice() < priceTo).collect(Collectors.toList());
			searchOrdersList = (ArrayList<Order>) searchOrdersList.stream().filter(o -> o.getPrice() < priceTo).collect(Collectors.toList());
		}
		
		if (!dateFrom.equals("0000-00-00"))
		{
			LocalDate localDateFrom = LocalDate.parse(dateFrom);
			int i = 0;
			for (Order o : ordersByStore)
			{
				
				LocalDate rentalDate = LocalDate.parse(o.getRentalDate());
				if (rentalDate.isBefore(localDateFrom))
				{
					searchOrdersList.remove(i);
				}
				
				i++;
			}
			
			//ordersByStore = (ArrayList<Order>) ordersByStore.stream().filter(o -> LocalDate.parse(o.getRentalDate()).isAfter(localDateFrom)).collect(Collectors.toList());
		}
		
		if (!dateTo.equals("0000-00-00"))
		{
			LocalDate localDateTo = LocalDate.parse(dateTo);
			int i = 0;
			for (Order o : ordersByStore)
			{
				LocalDate rentalDate = LocalDate.parse(o.getRentalDate());
				if (rentalDate.isAfter(localDateTo))
				{
					searchOrdersList.remove(i);
				}
				
				i++;
			}
		}
		
		if (sort.equals("priceAscending"))
		{
			sortByPriceAscending(searchOrdersList);
		} else if (sort.equals("priceDescending"))
		{
			sortByPriceDescending(searchOrdersList);
		} else if (sort.equals("dateAscending"))
		{
			sortByDateAscending(searchOrdersList);
		} else if (sort.equals("dateDescending"))
		{
			sortByDateDescending(searchOrdersList);
		}
		
		return searchOrdersList;
	}
	
	public static void sortByPriceAscending(ArrayList<Order> ordersToSort)
	{
		Collections.sort(ordersToSort, Comparator.comparing(Order::getPrice));
	}
	
	public static void sortByPriceDescending(ArrayList<Order> ordersToSort)
	{
		Collections.sort(ordersToSort, Comparator.comparing(Order::getPrice).reversed());
	}
	
	public static void sortByDateAscending(ArrayList<Order> ordersToSort)
	{
		Collections.sort(ordersToSort, Comparator.comparing(Order::getRentalDate));
	}
	
	public static void sortByDateDescending(ArrayList<Order> ordersToSort)
	{
		Collections.sort(ordersToSort, Comparator.comparing(Order::getRentalDate).reversed());
	}
	
	public static void sortByStoreAscending(ArrayList<Order> ordersToSort)
	{
		Collections.sort(ordersToSort, Comparator.comparing(order -> order.getRentACarStore().getName()));
	}
	
	public static void sortByStoreDescending(ArrayList<Order> ordersToSort)
	{
		Collections.sort(ordersToSort, Comparator.comparing(Order::getRentACarStore, Comparator.comparing(RentACarStore::getName).reversed()));
	}
	
	public Order save(Order order)
	{
		deserialize();
		Integer maxId = -1;
		
		for (int id : orders.keySet())
		{
			int idNum = id;
			if (idNum > maxId)
			{
				maxId = idNum;
			}
		}
		
		maxId++;
		order.setId(maxId);
		
		//here goes unique string of 10 characters
		
		orders.put(order.getId(), order);
		serialize();
		return order;
	}
	
	public Order update(int id, Order order)
	{
		Order orderToUpdate = findOrder(id);
		
		if (orderToUpdate == null)
		{
			return save(order);
		}
		
		orderToUpdate.setCustomerFirstAndLastName(order.getCustomerFirstAndLastName());
		orderToUpdate.setIdString(order.getIdString());
		orderToUpdate.setPickUpDate(order.getPickUpDate());
		orderToUpdate.setReturnDate(order.getReturnDate());
		orderToUpdate.setPrice(order.getPrice());
		orderToUpdate.setRentACarStore(order.getRentACarStore());
		orderToUpdate.setRentalDate(order.getRentalDate());
		orderToUpdate.setRentedVehicles(order.getRentedVehicles());
		orderToUpdate.setStatus(order.getStatus());
		orderToUpdate.setComment(order.getComment());
		orderToUpdate.setCancelDate(order.getCancelDate());
		
		serialize();
		
		return orderToUpdate;
	}
	
	public void delete (int id)
	{
		orders.remove(id);
	}
	
	public ArrayList<Order> findOrdersByCustomer(int id)
	{
		deserialize();
		ArrayList<Order> ordersByCustomer = new ArrayList<Order>();
		
		for (Order o : orders.values())
		{
			if (o.getCustomerId() == id)
			{
				ordersByCustomer.add(o);
			}
		}
		
		return ordersByCustomer;
	}
	
	public ArrayList<Order> findOrdersByStore(int id)
	{
		deserialize();
		ArrayList<Order> ordersByStore = new ArrayList<Order>();
		
		for (Order o : orders.values())
		{
			if (o.getRentACarStore().getId() == id)
			{
				ordersByStore.add(o);
			}
		}
		
		return ordersByStore;
	}
	
	public ArrayList<Customer> findCustomersByStore(int id)
	{
		deserialize();
		CustomerDAO customerDAO = new CustomerDAO();
		
		Set<Integer> uniqueCustomersId = new HashSet<>();
		for (Order o : orders.values()) {
		    if (o.getRentACarStore().getId() == id) {
		        uniqueCustomersId.add(o.getCustomerId());
		    }
		}

		List<Integer> customersByStoreId = new ArrayList<>(uniqueCustomersId);
		
		ArrayList<Customer> customersByStore = new ArrayList<>();
		
		for (Integer i : customersByStoreId)
		{
			Customer customer = customerDAO.findCustomer(i);
			customersByStore.add(customer);
		}
		
		return customersByStore;
	}
	
	public void serialize()
	{
		try {
			File file = new File(System.getProperty("user.dir") + "/WebContent/resources/Orders.json");
			FileWriter fileWriter = new FileWriter(file);
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			JsonArray jsonArray = new JsonArray();
			
			for (Order order : orders.values())
			{
				JsonObject jsonObject = new JsonObject();
				jsonObject.addProperty("id", order.getId());
				jsonObject.addProperty("idString", order.getIdString());
				
				JsonArray rentedVehiclesArray = new JsonArray();
				for (int el : order.getRentedVehicles())
				{
					rentedVehiclesArray.add(el);
				}
				
				jsonObject.add("rentedVehicles", rentedVehiclesArray);
				
					JsonObject rentACarStoreObject = new JsonObject();
					rentACarStoreObject.addProperty("id", order.getRentACarStore().getId());
					rentACarStoreObject.addProperty("name", order.getRentACarStore().getName());
	    			//jsonObject.addProperty("offeredVehicles", store.getOfferedVehicles().toArray());
	    			
	    			JsonArray vehiclesArray = new JsonArray();
	    			for(int el : order.getRentACarStore().getOfferedVehicles()) {
	    				vehiclesArray.add(el);
	    			}
	    			
	    			rentACarStoreObject.add("offeredVehicles", vehiclesArray);
	    			
	    			rentACarStoreObject.addProperty("workingTime", order.getRentACarStore().getWorkingTime());
	    			rentACarStoreObject.addProperty("workingStatus", order.getRentACarStore().getWorkingStatus().toString());
	    			
		    			JsonObject locationObject = new JsonObject();
		    			locationObject.addProperty("id", order.getRentACarStore().getLocation().getId());
		    			locationObject.addProperty("longitude", order.getRentACarStore().getLocation().getLongitude());
		    			locationObject.addProperty("latitude", order.getRentACarStore().getLocation().getLatitude());
		    			locationObject.addProperty("address", order.getRentACarStore().getLocation().getAddress());
	    			
	    			rentACarStoreObject.add("location", locationObject);
    			
	    			rentACarStoreObject.addProperty("locationId", order.getRentACarStore().getLocationId());
	    			rentACarStoreObject.addProperty("logoUrl", order.getRentACarStore().getLogoUrl());
	    			rentACarStoreObject.addProperty("rating", order.getRentACarStore().getRating());
	    		
	    		jsonObject.add("rentACarStore", rentACarStoreObject);
	    		jsonObject.addProperty("rentalDate", order.getRentalDate().toString());
	    		jsonObject.addProperty("pickUpDate", order.getPickUpDate());
	    		jsonObject.addProperty("returnDate", order.getReturnDate());
	    		jsonObject.addProperty("price", order.getPrice());
	    		jsonObject.addProperty("customerFirstAndLastName", order.getCustomerFirstAndLastName());
	    		jsonObject.addProperty("status", order.getStatus().toString());
	    		jsonObject.addProperty("customerId", order.getCustomerId());
	    		jsonObject.addProperty("comment", order.getComment());
	    		jsonObject.addProperty("cancelDate", order.getCancelDate());
	    		
	    		jsonArray.add(jsonObject);
			}
			
			String jsonData = gson.toJson(jsonArray);
    		fileWriter.write(jsonData);
    		fileWriter.close();
			
		} catch (Exception e)
		{
			System.out.println("ne moze da se serijalizuje order");
		}
	}
	
	public void deserialize() 
	{
		try {
			File file = new File(System.getProperty("user.dir") + "/WebContent/resources/Orders.json");
			FileReader fileReader = new FileReader(file);
			Gson gson = new Gson();
			Order[] ordersArray = gson.fromJson(fileReader, Order[].class);
			
			if (ordersArray == null)
			{
				return;
			}
			
			for (Order order : ordersArray)
			{
				orders.put(order.getId(), order);
			}
			
			fileReader.close();
			
		} catch (Exception e)
		{
			System.out.println("ne mogu da deserijalizujem order");
		}
	}
}

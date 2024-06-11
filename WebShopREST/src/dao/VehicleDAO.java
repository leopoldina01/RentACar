package dao;


import beans.Customer;
import beans.RentACarStore;
import beans.Status;
import beans.Vehicle;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class VehicleDAO {
    private HashMap<Integer, Vehicle> vehicles = new HashMap<Integer, Vehicle>();
    
    public VehicleDAO() {}
    
    public void serialize() {
    	try {
    		File file = new File(System.getProperty("user.dir") + "/WebContent/resources/Vehicles.json");
    		FileWriter fileWriter = new FileWriter(file);
    		Gson gson = new GsonBuilder().setPrettyPrinting().create();
    		JsonArray jsonArray = new JsonArray();
    		
    		for(Vehicle vehicle : vehicles.values()) {
    			JsonObject jsonObject = new JsonObject();
    			jsonObject.addProperty("id", vehicle.getId());
    			jsonObject.addProperty("brand", vehicle.getBrand());
    			jsonObject.addProperty("model", vehicle.getModel());
    			jsonObject.addProperty("price", vehicle.getPrice());
    			jsonObject.addProperty("vehicleType", vehicle.getVehicleType().toString());

    				JsonObject storeObject = new JsonObject();
    				storeObject.addProperty("id", vehicle.getStore().getId());
    				storeObject.addProperty("name", vehicle.getStore().getName());
        			
        			JsonArray vehiclesArray = new JsonArray();
        			for(int el : vehicle.getStore().getOfferedVehicles()) {
        				vehiclesArray.add(el);
        			}
        			
        			storeObject.add("offeredVehicles", vehiclesArray);
        			
        			storeObject.addProperty("workingTime", vehicle.getStore().getWorkingTime());
        			storeObject.addProperty("workingStatus", vehicle.getStore().getWorkingStatus().toString());
        			
        				JsonObject locationObject = new JsonObject();
        				locationObject.addProperty("id", vehicle.getStore().getLocation().getId());
        				locationObject.addProperty("longitude", vehicle.getStore().getLocation().getLongitude());
        				locationObject.addProperty("latitude", vehicle.getStore().getLocation().getLatitude());
        				locationObject.addProperty("address", vehicle.getStore().getLocation().getAddress());
        			
        				storeObject.add("location", locationObject);
        			
        			storeObject.addProperty("locationId", vehicle.getStore().getLocationId());
        			storeObject.addProperty("logoUrl", vehicle.getStore().getLogoUrl());
        			storeObject.addProperty("rating", vehicle.getStore().getRating());
    			
        		jsonObject.add("store", storeObject);
        			
    			jsonObject.addProperty("gearshiftType", vehicle.getGearshiftType().toString());
    			jsonObject.addProperty("fuelType", vehicle.getFuelType().toString());
    			jsonObject.addProperty("consumption", vehicle.getConsumption());
    			jsonObject.addProperty("doorsNumber", vehicle.getDoorsNumber());
    			jsonObject.addProperty("passengersNumber", vehicle.getPassengersNumber());
    			jsonObject.addProperty("description", vehicle.getDescription());
    			jsonObject.addProperty("imageUrl", vehicle.getImageUrl());
    			jsonObject.addProperty("status", vehicle.getStatus().toString());
    			   			
    			jsonArray.add(jsonObject);
    		}
    		String jsonData = gson.toJson(jsonArray);
    		fileWriter.write(jsonData);
    		fileWriter.close();
    		
    	}catch(Exception e) {
    		System.out.println("Ne mogu da serijalizujem vehicle");
    	}
    }
	
	public void deserialize() {
		try {
			File file = new File(System.getProperty("user.dir") + "/WebContent/resources/Vehicles.json");
			FileReader fileReader = new FileReader(file);
			Gson gson = new Gson();
			Vehicle[] vehiclesArray = gson.fromJson(fileReader, Vehicle[].class);
			
			if(vehiclesArray ==  null) {
				return;
			}
			
			for(Vehicle vehicle : vehiclesArray) {
				vehicles.put(vehicle.getId(), vehicle);
			}
			
			fileReader.close();
			
		}catch(Exception e) {
			System.out.println("Ne mogu da deserijalizujem vehicle");
		}
	}
	
	public void makeRented(ArrayList<Integer> rentedVehicles)
	{
		for (int v : rentedVehicles)
		{
			Vehicle vehicle = findVehicle(v);
			vehicle.setStatus(Status.RENTED);
			update(vehicle.getId(), vehicle);
		}
	}
	
	public void returnVehicles(ArrayList<Integer> rentedVehicles)
	{
		for (int v : rentedVehicles)
		{
			Vehicle vehicle = findVehicle(v);
			vehicle.setStatus(Status.AVAILABLE);
			update(vehicle.getId(), vehicle);
		}
	}
	
	public ArrayList<Vehicle> findOrderedVehicles(ArrayList<Integer> orderedVehicles)
	{
		deserialize();
		ArrayList<Vehicle> customerVehicles = new ArrayList<Vehicle>();
		
		for (Vehicle o : vehicles.values())
		{
			for (Integer i : orderedVehicles)
			{
				if (i == o.getId())
				{
					customerVehicles.add(o);
				}
			}
		}
		
		return customerVehicles;
	}
    
    public Collection<Vehicle> findAll(){
    	deserialize();
        return vehicles.values();
    }
    
    public Vehicle findVehicle(int  id){
    	deserialize();
        return vehicles.containsKey(id) ? vehicles.get(id) : null;
    }
    
    public ArrayList<Vehicle> getVehiclesAfterAddingToCart(String pickUpDate, String returnDate, int customerId)
    {
    	ArrayList<Vehicle> availableVehicles = getSearchedAvailableVehicles(pickUpDate, returnDate);
    	
    	CustomerDAO customerDAO = new CustomerDAO();
    	Customer customer = customerDAO.findCustomer(customerId);
    	
    	ArrayList<Vehicle> vehiclesAfterAddingToCart = new ArrayList<Vehicle>();
    	
    	for (Vehicle v : availableVehicles)
    	{
    		if (!customer.getShoppingCart().getVehicles().contains(v.getId()))
    		{
    			vehiclesAfterAddingToCart.add(v);
    		}
    	}
    	
    	return vehiclesAfterAddingToCart;
    }
    
    public ArrayList<Vehicle> getSearchedAvailableVehicles(String pickUpDate, String returnDate)
    {
    	deserialize();
    	
    	ArrayList<Vehicle> searchedAvailableVehicles = new ArrayList<Vehicle>();
    	ArrayList<Vehicle> searchRentedVehicles = new ArrayList<Vehicle>();
    	
    	for (Vehicle v : vehicles.values())
    	{
    		if (v.getStatus().toString().equals("AVAILABLE"))
    		{
    			searchedAvailableVehicles.add(v);
    		}
    		else 
    		{
    			searchRentedVehicles.add(v);
    		}
    	}
    	
    	searchRentedVehicles = checkRentedVehicles(searchRentedVehicles, pickUpDate, returnDate);
    	
    	for (Vehicle v : searchRentedVehicles)
    	{
    		searchedAvailableVehicles.add(v);
    	}
    	
    	return searchedAvailableVehicles;
    }
    
    public ArrayList<Vehicle> checkRentedVehicles(ArrayList<Vehicle> searchRentedVehicles, String pickUpDate, String returnDate)
    {
    	ArrayList<Vehicle> changedList = new ArrayList<Vehicle>();
    	
    	OrderDAO dao = new OrderDAO();
    	
    	for (Vehicle v : searchRentedVehicles)
    	{
    		if (dao.findIfAvailable(v, pickUpDate, returnDate))
    		{
    			changedList.add(v);
    		}
    	}
    	
    	return changedList;
    }
    
    public Vehicle save(Vehicle vehicle){
    	deserialize();
        Integer maxId = -1;
        for(int id : vehicles.keySet()){
            int idNum = id;
            if(idNum > maxId){
                maxId = idNum;
            }
        }
        maxId++;
        vehicle.setId(maxId);
        
        ArrayList<Integer> offeredVehiclesIds = new ArrayList<Integer>();
        
        for(int vehicleId : vehicle.getStore().getOfferedVehicles()) {
        	offeredVehiclesIds.add(vehicleId);
        }
        offeredVehiclesIds.add(vehicle.getId());
        vehicle.getStore().setOfferedVehicles(offeredVehiclesIds);
        
        RentACarStoreDAO rentACarStoreDAO = new RentACarStoreDAO();
        rentACarStoreDAO.update(vehicle.getStore().getId(), vehicle.getStore());
        
        ManagerDAO managerDAO = new ManagerDAO();
        managerDAO.updateStore(vehicle.getStore());
        
        //updateEveryVehicleInThisStore(vehicle.getStore());
        
        vehicles.put(vehicle.getId(), vehicle);
        serialize();
        return vehicle;
    }
    
    public RentACarStore updateEveryVehicleInThisStore(int storeId) {
    	RentACarStoreDAO rentACarStoreDAO = new RentACarStoreDAO();
    	RentACarStore store = new RentACarStore();
    	store = rentACarStoreDAO.findRentACarStore(storeId);
    	Vehicle vehicle = new Vehicle();
    	for(int vehicleId : store.getOfferedVehicles()) {
    		vehicle = findVehicle(vehicleId);
    		vehicle.setStore(store);
    		update(vehicleId, vehicle);
    	}
    	return store;
    }
    
    public Vehicle update(int id, Vehicle vehicle){
    	deserialize();
        Vehicle vehicleToUpdate = findVehicle(id);
        if(vehicleToUpdate == null){
            return save(vehicle);
        }
        vehicleToUpdate.setBrand(vehicle.getBrand());
        vehicleToUpdate.setModel(vehicle.getModel());
        vehicleToUpdate.setPrice(vehicle.getPrice());
        vehicleToUpdate.setVehicleType(vehicle.getVehicleType());
        vehicleToUpdate.setStore(vehicle.getStore());
        vehicleToUpdate.setGearshiftType(vehicle.getGearshiftType());
        vehicleToUpdate.setFuelType(vehicle.getFuelType());
        vehicleToUpdate.setConsumption(vehicle.getConsumption());
        vehicleToUpdate.setDoorsNumber(vehicle.getDoorsNumber());
        vehicleToUpdate.setPassengersNumber(vehicle.getPassengersNumber());
        vehicleToUpdate.setDescription(vehicle.getDescription());
        vehicleToUpdate.setImageUrl(vehicle.getImageUrl());
        vehicleToUpdate.setStatus(vehicle.getStatus());
        //delete(id);
        //save(vehicleToUpdate);
        serialize();
        return vehicleToUpdate;
    }
    
    public void delete(int id){
    	deserialize();
    	Vehicle vehicle = new Vehicle();
    	vehicle = findVehicle(id);
    	
    	ArrayList<Integer> offeredVehiclesIds = new ArrayList<Integer>();
    	ArrayList<Integer> offeredVehiclesForSaving = new ArrayList<Integer>();
        
        for(int vehicleId : vehicle.getStore().getOfferedVehicles()) {
        	offeredVehiclesIds.add(vehicleId);
        }
        
        for(int vehicleId : offeredVehiclesIds) {
        	if(vehicleId != id) {
        		offeredVehiclesForSaving.add(vehicleId);
        	}
        }
        
        //offeredVehiclesIds.remove(id);
        vehicle.getStore().setOfferedVehicles(offeredVehiclesForSaving);
        
        RentACarStoreDAO rentACarStoreDAO = new RentACarStoreDAO();
        rentACarStoreDAO.update(vehicle.getStore().getId(), vehicle.getStore());
        
        ManagerDAO managerDAO = new ManagerDAO();
        managerDAO.updateStore(vehicle.getStore());
        
        updateEveryVehicleInThisStore(vehicle.getStore().getId());
    	
        vehicles.remove(id);
        serialize();
    }
}

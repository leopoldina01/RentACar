package dao;

import beans.Location;
import beans.Manager;
import beans.RentACarStore;
import beans.User;
import beans.WorkingStatus;

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

public class ManagerDAO {
	private HashMap<Integer, Manager> managers = new HashMap<Integer, Manager>();

    public ManagerDAO() {}

    public Collection<Manager> findAll()
    {
        return managers.values();
    }
    
    public Manager updateStore(RentACarStore store) {
    	deserialize();
    	for(Manager m : managers.values()) {
    		if(m.getRentACarStore().getId() == store.getId()) {
    			m.setRentACarStore(store);
    			return update(m.getId(), m);
    		}
    	}
    	return null;
    }
    
    public Collection<Manager> findAllAvailable()
    {
    	deserialize();
    	ArrayList<Manager> availableManagers = new ArrayList<Manager>();
        
    	for(Manager m : managers.values()) {
    		//System.out.println(m.getRentACarStore().getId());
    		if(m.getRentACarStore().getId() == 0) {
    			availableManagers.add(m);
    		}
    	}
    	
    	return availableManagers;
    }
    
    public Manager findManagerByUsername(String username)
    {
    	deserialize();
    	for (Manager m : managers.values())
    	{
    		if (m.getUsername().equals(username))
    		{
    			return m;
    		}
    	}
    	return null;
    }

    public Manager findManager(int id)
    {
    	deserialize();
    	
    	for (Manager m : managers.values())
    	{
    		if (m.getId() == id)
    		{
    			return m;
    		}
    	}
    	
    	return null;
    }
    
    public void blockManager(int managerId)
    {
    	Manager manager = findManager(managerId);
    	manager.setIsBlocked(true);
    	update(manager.getId(), manager);
    }
    
    public Manager save(User user)
    {
		deserialize();
		
		Manager manager = new Manager();
		manager.setDateOfBirth(user.getDateOfBirth());
		manager.setFirstName(user.getFirstName());
		manager.setGender(user.getGender());
		manager.setId(user.getId());
		manager.setLastName(user.getLastName());
		manager.setPassword(user.getPassword());
		manager.setRole(user.getRole());
		manager.setIsBlocked(user.getIsBlocked());
		manager.setUsername(user.getUsername());
		
		manager.setRentACarStore(new RentACarStore());
		manager.getRentACarStore().setLocation(new Location());
		manager.getRentACarStore().setLogoUrl("");
		manager.getRentACarStore().setName("");
		manager.getRentACarStore().setWorkingStatus(WorkingStatus.OPENED);
		manager.getRentACarStore().setWorkingTime("");
		manager.getRentACarStore().getLocation().setAddress("");
		
		/*
		 * Integer maxId = -1;
		 * 
		 * for(int id : managers.keySet()){ int idNum = id; if(idNum > maxId){ maxId =
		 * idNum; } }
		 * 
		 * maxId++; manager.setId(maxId);
		 */
		
        managers.values().add(manager);
        
        serialize();
        
        return manager;
    }
    
    public Manager createManager(User user)
    {
    	deserialize();
		
		Manager manager = new Manager();
		manager.setDateOfBirth(user.getDateOfBirth());
		manager.setFirstName(user.getFirstName());
		manager.setGender(user.getGender());
		manager.setId(user.getId());
		manager.setLastName(user.getLastName());
		manager.setPassword(user.getPassword());
		manager.setRole(user.getRole());
		manager.setIsBlocked(user.getIsBlocked());
		manager.setUsername(user.getUsername());
		
		manager.setRentACarStore(new RentACarStore());
		manager.getRentACarStore().setLocation(new Location());
		manager.getRentACarStore().setLogoUrl("");
		manager.getRentACarStore().setName("");
		manager.getRentACarStore().setWorkingStatus(WorkingStatus.OPENED);
		manager.getRentACarStore().setWorkingTime("");
		manager.getRentACarStore().getLocation().setAddress("");
		
		//save(manager);
		
		managers.put(manager.getId(), manager);
        
        serialize();
		
		return manager;
    }

    public Manager save(Manager manager){
    	deserialize();
    	
		
		  Integer maxId = -1;
		  
		  for(int id : managers.keySet()){ int idNum = id; if(idNum > maxId){ maxId =
		  idNum; } }
		  
		  maxId++; manager.setId(maxId);
		 
        managers.put(manager.getId(), manager);
        
        serialize();
        
        return manager;
    }

    public Manager update(int id, Manager manager)
    {
    	deserialize();
        Manager managerToUpdate = findManager(id);

        if (managerToUpdate == null)
        {
            return save(manager);
        }

        managerToUpdate.setUsername(manager.getUsername());
        managerToUpdate.setPassword(manager.getPassword());
        managerToUpdate.setFirstName(manager.getFirstName());
        managerToUpdate.setLastName(manager.getLastName());
        managerToUpdate.setGender(manager.getGender());
        managerToUpdate.setDateOfBirth(manager.getDateOfBirth());
        managerToUpdate.setRole(manager.getRole());
        managerToUpdate.setIsBlocked(manager.getIsBlocked());
        managerToUpdate.setRentACarStore(manager.getRentACarStore());

        //delete(id);

        //save(managerToUpdate);

        serialize();
        
        return managerToUpdate;
    }

    public void delete (int id)
    {
        managers.remove(id);
    }
    
    public void serialize()
    {
    	try 
    	{
    		File file = new File(System.getProperty("user.dir") + "/WebContent/resources/Managers.json");
    		FileWriter fileWriter = new FileWriter(file);
    		Gson gson = new GsonBuilder().setPrettyPrinting().create();
    		JsonArray jsonArray = new JsonArray();
    		
    		for (Manager manager : managers.values())
    		{
    			JsonObject jsonObject = new JsonObject();
    			jsonObject.addProperty("id", manager.getId());
    			jsonObject.addProperty("username", manager.getUsername());
    			jsonObject.addProperty("password", manager.getPassword());
    			jsonObject.addProperty("firstName", manager.getFirstName());
    			jsonObject.addProperty("lastName", manager.getLastName());
    			jsonObject.addProperty("gender", manager.getGender());
    			jsonObject.addProperty("dateOfBirth", manager.getDateOfBirth());
    			jsonObject.addProperty("role", "MANAGER");
    			jsonObject.addProperty("isBlocked", manager.getIsBlocked());
    			
	    			JsonObject RentACarStoreObject = new JsonObject();
	    			RentACarStoreObject.addProperty("id", manager.getRentACarStore().getId());
	    			RentACarStoreObject.addProperty("name", manager.getRentACarStore().getName());
	    			//jsonObject.addProperty("offeredVehicles", store.getOfferedVehicles());
	    			
	    			JsonArray vehiclesArray = new JsonArray();
	    			for(int el : manager.getRentACarStore().getOfferedVehicles()) {
	    				vehiclesArray.add(el);
	    			}
	    			
	    			RentACarStoreObject.add("offeredVehicles", vehiclesArray);
	    			
	    			RentACarStoreObject.addProperty("workingTime", manager.getRentACarStore().getWorkingTime());
	    			RentACarStoreObject.addProperty("workingStatus", manager.getRentACarStore().getWorkingStatus().toString());
	    			
						JsonObject locationObject = new JsonObject();
						locationObject.addProperty("id", manager.getRentACarStore().getLocation().getId());
						locationObject.addProperty("longitude", manager.getRentACarStore().getLocation().getLongitude());
						locationObject.addProperty("latitude", manager.getRentACarStore().getLocation().getLatitude());
						locationObject.addProperty("address", manager.getRentACarStore().getLocation().getAddress());
						
					RentACarStoreObject.add("location", locationObject);
	    			
					RentACarStoreObject.addProperty("locationId", manager.getRentACarStore().getLocationId());
					RentACarStoreObject.addProperty("logoUrl", manager.getRentACarStore().getLogoUrl());
					RentACarStoreObject.addProperty("rating", manager.getRentACarStore().getRating());
	    			
	    		jsonObject.add("rentACarStore", RentACarStoreObject);
    			
    			jsonArray.add(jsonObject);
    		}
    		
    		String jsonData = gson.toJson(jsonArray);
    		fileWriter.write(jsonData);
    		fileWriter.close();
    	} 
    	catch(Exception e) 
    	{
    		System.out.println("Ok");
    	}
    }
    
    public void deserialize()
    {
    	try
    	{
    		File file = new File(System.getProperty("user.dir") + "/WebContent/resources/Managers.json");
			FileReader fileReader = new FileReader(file);
			Gson gson = new Gson();
			Manager[] managersArray = gson.fromJson(fileReader, Manager[].class);
			
			if (managersArray == null)
			{
				return;
			}
			
			for (Manager manager : managersArray)
			{
				managers.put(manager.getId(), manager);
			}
			
			fileReader.close();
    	}
    	catch(Exception e) {
			System.out.println("Ok");
		}
    }
}

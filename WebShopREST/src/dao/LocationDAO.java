package dao;

import beans.Location;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Collection;
import java.util.HashMap;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class LocationDAO {
	private HashMap<Integer, Location> locations = new HashMap<Integer, Location>();

    public LocationDAO() {
    }

    public void serialize() {
    	try
    	{
    		File file = new File(System.getProperty("user.dir") + "/WebContent/resources/Locations.json");
    		FileWriter fileWriter = new FileWriter(file);
    		Gson gson = new GsonBuilder().setPrettyPrinting().create();
    		JsonArray jsonArray = new JsonArray();
    		
    		for(Location loc : locations.values()) {
    			JsonObject jsonObject = new JsonObject();
    			jsonObject.addProperty("id", loc.getId());
    			jsonObject.addProperty("latitude", loc.getLatitude());
    			jsonObject.addProperty("longitude", loc.getLongitude());
    			jsonObject.addProperty("address", loc.getAddress());
    			
    			jsonArray.add(jsonObject);
    		}
    		String jsonData = gson.toJson(jsonArray);
    		fileWriter.write(jsonData);
    		fileWriter.close();
    	}
    	catch (Exception e)
    	{
    		System.out.println("Ok");
    	}
    }
    
    public void deserialize() {
    	try {
			File file = new File(System.getProperty("user.dir") + "/WebContent/resources/Locations.json");
			FileReader fileReader = new FileReader(file);
			Gson gson = new Gson();
			Location[] locationsArray = gson.fromJson(fileReader, Location[].class);
			
			if(locationsArray ==  null) {
				return;
			}
			
			for(Location loc : locationsArray) {
				locations.put(loc.getId(), loc);
			}
			
			fileReader.close();
			
		}catch(Exception e) {
			System.out.println("Ok");
		}
    }
    
    
    public Collection<Location> findAll()
    {
    	deserialize();
        return locations.values();
    }

    public Location findLocation(int id)
    {
    	deserialize();
        return locations.containsKey(id) ? locations.get(id) : null;
    }
    
    public Location getLocationOrCreateNew(double longitude, double latitude, String address) {
    	deserialize();
    	for(Location loc : findAll()) {
    		if(loc.getAddress().equals(address) && loc.getLatitude() == latitude && loc.getLongitude() == longitude) {
    			return loc;
    		}
    	}
    	Location location = new Location();
    	location.setAddress(address);
    	location.setLatitude(latitude);
    	location.setLongitude(longitude);
    	return save(location);
    }
    
    public Location save(Location location){
    	deserialize();
        Integer maxId = -1;
        for(int id : locations.keySet()){
            int idNum = id;
            if(idNum > maxId){
                maxId = idNum;
            }
        }
        maxId++;
        location.setId(maxId);
        locations.put(location.getId(), location);
        serialize();
        return location;
    }

    public Location update(int id, Location location)
    {
    	deserialize();
        Location locationToUpdate = findLocation(id);

        if (locationToUpdate == null)
        {
            return save(location);
        }

        locationToUpdate.setLongitude(location.getLongitude());
        locationToUpdate.setLatitude(location.getLatitude());
        locationToUpdate.setAddress(location.getAddress());

        delete(id);

        save(locationToUpdate);
        serialize();
        return locationToUpdate;
    }

    public void delete (int id)
    {
    	deserialize();
        locations.remove(id);
        serialize();
    }
}

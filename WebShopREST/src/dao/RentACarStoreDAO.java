package dao;

import beans.Comment;
import beans.CommentStatus;
import beans.RentACarStore;
import beans.Vehicle;
import beans.WorkingStatus;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class RentACarStoreDAO {
	private HashMap<Integer, RentACarStore> stores = new HashMap<Integer, RentACarStore>();

	public RentACarStoreDAO() {
	}

	public void serialize() {
    	try {
    		File file = new File(System.getProperty("user.dir") + "/WebContent/resources/RentACarStore.json");
    		FileWriter fileWriter = new FileWriter(file);
    		Gson gson = new GsonBuilder().setPrettyPrinting().create();
    		JsonArray jsonArray = new JsonArray();
    		
    		for(RentACarStore store : stores.values()) {
    			JsonObject jsonObject = new JsonObject();
    			jsonObject.addProperty("id", store.getId());
    			jsonObject.addProperty("name", store.getName());
    			//jsonObject.addProperty("offeredVehicles", store.getOfferedVehicles().toArray());
    			
    			JsonArray vehiclesArray = new JsonArray();
    			for(int el : store.getOfferedVehicles()) {
    				vehiclesArray.add(el);
    			}
    			
    			jsonObject.add("offeredVehicles", vehiclesArray);
    			
    			jsonObject.addProperty("workingTime", store.getWorkingTime());
    			jsonObject.addProperty("workingStatus", store.getWorkingStatus().toString());
    			
    			JsonObject locationObject = new JsonObject();
    			locationObject.addProperty("id", store.getLocation().getId());
    			locationObject.addProperty("longitude", store.getLocation().getLongitude());
    			locationObject.addProperty("latitude", store.getLocation().getLatitude());
    			locationObject.addProperty("address", store.getLocation().getAddress());
    			
    			jsonObject.add("location", locationObject);
    			
    			jsonObject.addProperty("locationId", store.getLocationId());
    			jsonObject.addProperty("logoUrl", store.getLogoUrl());
    			jsonObject.addProperty("rating", store.getRating());
    			
    			jsonArray.add(jsonObject);
    		}
    		String jsonData = gson.toJson(jsonArray);
    		fileWriter.write(jsonData);
    		fileWriter.close();
    		
    	}catch(Exception e) {
    		System.out.println("Ne mogu da serijalizujem store");
    	}
    }
	
	public void deserialize() {
		try {
			File file = new File(System.getProperty("user.dir") + "/WebContent/resources/RentACarStore.json");
			FileReader fileReader = new FileReader(file);
			Gson gson = new Gson();
			RentACarStore[] storesArray = gson.fromJson(fileReader, RentACarStore[].class);
			
			if(storesArray ==  null) {
				return;
			}
			
			for(RentACarStore store : storesArray) {
				stores.put(store.getId(), store);
			}
			
			fileReader.close();
			
		}catch(Exception e) {
			System.out.println("Ne mogu da deserijalizujem store");
		}
	}
	
	public ArrayList<Vehicle> getStoreVehicles(int storeId){
		deserialize();
		VehicleDAO vehicleDAO = new VehicleDAO();

		ArrayList<Vehicle> vehicles = new ArrayList<Vehicle>();
		RentACarStore store = new RentACarStore();
		store = findRentACarStore(storeId);
		
		for(int id : store.getOfferedVehicles()) {
			vehicles.add(vehicleDAO.findVehicle(id));
		}
		
		return vehicles;
	}
	
	public RentACarStore updateStoreRating(int storeId, int newRating) {
		deserialize();
		RentACarStore store = new RentACarStore();
		store = findRentACarStore(storeId);
		CommentDAO commentDAO = new CommentDAO();
		double counter = 0;
		double sum = 0;
		
		for(Comment com : commentDAO.findAll()) {
			if(com.getStatus() == CommentStatus.APPROVED) {				
				counter++;
				sum = sum + com.getRating();
			}
		}
		
		store.setRating(sum/counter);
		serialize();
		return store;
	}
	
	public ArrayList<RentACarStore> getFilteredStores(String sort, String gearshift, String fuel, String opened){
		deserialize();
		ArrayList<RentACarStore> filteredStores = new ArrayList<RentACarStore>();
		ArrayList<RentACarStore> filteredStoresForRemoval = new ArrayList<RentACarStore>();
		
		for(RentACarStore store : stores.values()) {
			filteredStores.add(store);
			filteredStoresForRemoval.add(store);
		}
		
		for(RentACarStore store : filteredStores) {

			/*FILTEROVANJE PO MENJACU*/
			VehicleDAO vehicleDAO = new VehicleDAO();
			ArrayList<Vehicle> vehicles = new ArrayList<Vehicle>();
			int counter1 = 0;
			for(int id : store.getOfferedVehicles()) {
				vehicles.add(vehicleDAO.findVehicle(id));
			}
			
			for(Vehicle v : vehicles) {
				if(v.getGearshiftType().toString().equals(gearshift) && !gearshift.equals("SELECTEDGEARSHIFT")) {
					counter1++;
				}
			}
			
			if(counter1 == 0 && !gearshift.equals("SELECTEDGEARSHIFT")) {
				filteredStoresForRemoval.remove(store);
			}
			/*FILTEROVANJE PO GORIVU*/
			vehicles = new ArrayList<Vehicle>();
			int counter2 = 0;
			for(int id : store.getOfferedVehicles()) {
				vehicles.add(vehicleDAO.findVehicle(id));
			}
			
			for(Vehicle v : vehicles) {
				if(v.getFuelType().toString().equals(fuel) && !fuel.equals("SELECTEDFUEL")) {
					counter2++;
				}
			}
			
			if(counter2 == 0 && !fuel.equals("SELECTEDFUEL")) {
				filteredStoresForRemoval.remove(store);
			}
			
			/*FILTEROVANJE PO STATUSU OTVORENOSTI*/
			
			/*
			 * if (store.getWorkingStatus() == WorkingStatus.CLOSED &&
			 * opened.equals("true")) { filteredStoresForRemoval.remove(store); }
			 */
			
			//boolean nalaziSe = getCurrentlyOpenedStores().contains(store);
			
			 boolean nalaziSe = daLiSeNalazi(getCurrentlyOpenedStores(), store.getId());
			
			 if(!nalaziSe && opened.equals("true")) 
			 {
				 filteredStoresForRemoval.remove(store); 
			 }
			 
			
		}
		/*SORTIRANJE*/
		if(sort.equals("nameAsc")) {
			sortByNameAscending(filteredStoresForRemoval);
		}else if(sort.equals("nameDesc")) {
			sortByNameDescending(filteredStoresForRemoval);
		}else if(sort.equals("locationAsc")) {
			sortByLocationAscending(filteredStoresForRemoval);
		}else if(sort.equals("locationDesc")) {
			sortByLocationDescending(filteredStoresForRemoval);
		}else if(sort.equals("ratingAsc")) {
			sortByRatingAscending(filteredStoresForRemoval);
		}else if(sort.equals("ratingDesc")) {
			sortByRatingDescending(filteredStoresForRemoval);
		}

		return filteredStoresForRemoval;
	}
	
	public boolean daLiSeNalazi(ArrayList<RentACarStore> storovi, int storeId)
	{
		for (RentACarStore s : storovi)
		{
			if (s.getId() == storeId)
			{
				return true;
			}
		}
		
		return false;
	}
	
	public static void sortByRatingAscending(ArrayList<RentACarStore> stores) {
		Collections.sort(stores, Comparator.comparing(RentACarStore::getRating));
	}
	
	public static void sortByRatingDescending(ArrayList<RentACarStore> stores) {
        Collections.sort(stores, Comparator.comparing(RentACarStore::getRating).reversed());
    }
	
	public static void sortByLocationAscending(ArrayList<RentACarStore> stores) {
		Collections.sort(stores, Comparator.comparing(store -> store.getLocation().getAddress()));
	}
	
	public static void sortByLocationDescending(ArrayList<RentACarStore> stores) {
		Collections.sort(stores, Comparator.comparing(store -> store.getLocation().getAddress(), Comparator.reverseOrder()));
	}	
	
	public static void sortByNameAscending(ArrayList<RentACarStore> stores) {
        Collections.sort(stores, Comparator.comparing(RentACarStore::getName));
    }
	
	public static void sortByNameDescending(ArrayList<RentACarStore> stores) {
        Collections.sort(stores, Comparator.comparing(RentACarStore::getName).reversed());
    }
	
	public ArrayList<RentACarStore> izbaciStore(ArrayList<RentACarStore> storovi, int storeId)
	{
		ArrayList<RentACarStore> noviStorovi = new ArrayList<RentACarStore>();
		
		for (RentACarStore s : storovi)
		{
			if (s.getId() != storeId)
			{
				noviStorovi.add(s);
			}
		}
		
		return noviStorovi;
	}
	
	public ArrayList<RentACarStore> getCurrentlyOpenedStores(){
		ArrayList<RentACarStore> openedOnlyStores = new ArrayList<RentACarStore>();
		for(RentACarStore s : stores.values()) {
			openedOnlyStores.add(s);
		}
		
		for(RentACarStore store : stores.values()) {
			String[] times = store.getWorkingTime().split(" - ");
			
			LocalTime startTime = LocalTime.parse(times[0]);
			LocalTime endTime = LocalTime.parse(times[1]);
			LocalTime currentTime = LocalTime.now();
			LocalTime midnight = LocalTime.MIDNIGHT;
			
			if(!endTime.isBefore(startTime)) {
				if (currentTime.isAfter(startTime) && currentTime.isBefore(endTime)) {
		            store.setWorkingStatus(WorkingStatus.OPENED);
					update(store.getId(), store);
		        } else {
		        	store.setWorkingStatus(WorkingStatus.CLOSED);
					update(store.getId(), store);
					openedOnlyStores = izbaciStore(openedOnlyStores, store.getId());
		            //openedOnlyStores.remove(store);
		        }
			}else {
				if (currentTime.isAfter(startTime)  || currentTime.isBefore(endTime)) 
				{
					store.setWorkingStatus(WorkingStatus.OPENED);
					update(store.getId(), store);
				} else {
					store.setWorkingStatus(WorkingStatus.CLOSED);
					update(store.getId(), store);
					openedOnlyStores = izbaciStore(openedOnlyStores, store.getId());
		            //openedOnlyStores.remove(store);
				}
			}
		}
		
		return openedOnlyStores;
	}
	
	public ArrayList<RentACarStore> getSearchedStores(String address, double rating, String name, String vehicleType){
		deserialize();
		ArrayList<RentACarStore> searchedStores = new ArrayList<RentACarStore>();
		ArrayList<RentACarStore> searchedStoresForRemoval = new ArrayList<RentACarStore>();
		
		//punimo listu 
		for(RentACarStore store : stores.values()) {
			searchedStores.add(store);
			searchedStoresForRemoval.add(store);
		}
		
		for(RentACarStore store : searchedStores) {
			if(rating > store.getRating() && rating != 0) {
				searchedStoresForRemoval.remove(store);
			}
			
			if(store.getName().toLowerCase().contains(name.toLowerCase()) == false && !name.equals("null")) {
				searchedStoresForRemoval.remove(store);
			}
			
			String[] parts = store.getLocation().getAddress().split(",");
			
			if(parts[1].toLowerCase().contains(address.toLowerCase()) == false && !address.equals("null")) {
				searchedStoresForRemoval.remove(store);
			}
			
			VehicleDAO vehicleDAO = new VehicleDAO();
			ArrayList<Vehicle> vehicles = new ArrayList<Vehicle>();
			int counter = 0;
			for(int id : store.getOfferedVehicles()) {
				vehicles.add(vehicleDAO.findVehicle(id));
			}
			
			for(Vehicle v : vehicles) {
				if(v.getVehicleType().toString().equals(vehicleType) && !vehicleType.equals("SELECTCAR")) {
					counter++;
				}
			}
			
			if(counter == 0 && !vehicleType.equals("SELECTCAR")) {
				searchedStoresForRemoval.remove(store);
			}
		}
		
		return searchedStoresForRemoval;
	}
	
	public Collection<RentACarStore> findAll() {
		// return stores.values();
		deserialize();
		return sortByWorkingStatus(stores);
	}

	public ArrayList<RentACarStore> sortByWorkingStatus(HashMap<Integer, RentACarStore> stores) {
		ArrayList<RentACarStore> rentACarStores = new ArrayList<RentACarStore>();
		deserialize();
		for (RentACarStore store : stores.values()) {
			if (store.getWorkingStatus() == WorkingStatus.OPENED) {
				rentACarStores.add(store);
			}
		}

		for (RentACarStore store : stores.values()) {
			if (store.getWorkingStatus() == WorkingStatus.CLOSED) {
				rentACarStores.add(store);
			}
		}

		return rentACarStores;
	}

	public RentACarStore findRentACarStore(int id) {
		deserialize();
		return stores.containsKey(id) ? stores.get(id) : null;
	}

	public RentACarStore save(RentACarStore rentACarStore) {
		deserialize();
		Integer maxId = -1;
		for (int id : stores.keySet()) {
			int idNum = id;
			if (idNum > maxId) {
				maxId = idNum;
			}
		}
		maxId++;
		rentACarStore.setId(maxId);
		stores.put(rentACarStore.getId(), rentACarStore);
		serialize();
		return rentACarStore;
	}

	public RentACarStore update(int id, RentACarStore rentACarStore) {
		RentACarStore rentACarStoreToUpdate = new RentACarStore();
		rentACarStoreToUpdate = findRentACarStore(id);
		if (rentACarStoreToUpdate == null)
			return save(rentACarStore);
		rentACarStoreToUpdate.setName(rentACarStore.getName());
		rentACarStoreToUpdate.setOfferedVehicles(rentACarStore.getOfferedVehicles());
		rentACarStoreToUpdate.setWorkingTime(rentACarStore.getWorkingTime());
		rentACarStoreToUpdate.setWorkingStatus(rentACarStore.getWorkingStatus());
		rentACarStoreToUpdate.setLocation(rentACarStore.getLocation());
		rentACarStoreToUpdate.setLocationId(rentACarStore.getLocationId());
		rentACarStoreToUpdate.setLogoUrl(rentACarStore.getLogoUrl());
		rentACarStoreToUpdate.setRating(rentACarStore.getRating());
		//delete(id);
		//save(rentACarStoreToUpdate);
		serialize();
		return rentACarStoreToUpdate;
	}

	public void delete(int id) {
		stores.remove(id);
	}
}

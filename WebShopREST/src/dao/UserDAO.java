package dao;

import beans.Customer;
import beans.RentACarStore;
import beans.Role;
import beans.User;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class UserDAO {
	HashMap<Integer, User> users = new HashMap<Integer, User>();
	private String contextPath;
	
    public UserDAO(String contextPath) {
    	deserialize(contextPath);
    	this.contextPath = contextPath;
    }

    public Collection<User> findAll()
    {	
    	deserialize(contextPath);
        return users.values();
    }

    public User findUser(int id)
    {
    	deserialize(contextPath);
        User user =  users.containsKey(id) ? users.get(id) : null;
        return user;
    }
    
    public Collection<User> searchUsers(String username, String firstName, String lastName)
    {
    	deserialize(contextPath);
    	Collection<User> searchedUsers = findAll();
    	
    	if (username != null)
    	{
    		searchedUsers = searchedUsers.stream().filter(u -> u.getUsername().toLowerCase().contains(username.toLowerCase())).collect(Collectors.toList());
    	}
    	
    	if (firstName != null)
    	{
    		searchedUsers = searchedUsers.stream().filter(u -> u.getFirstName().toLowerCase().contains(firstName.toLowerCase())).collect(Collectors.toList());
    	}
    	
    	if (lastName != null)
    	{
    		searchedUsers = searchedUsers.stream().filter(u -> u.getLastName().toLowerCase().contains(lastName.toLowerCase())).collect(Collectors.toList());
    	}
    	
    	return searchedUsers;
    }
    
    public User findUserByPasswordAndUsername(String username, String password) {
    	deserialize(contextPath);
    	for(User u : users.values()) {
    		if(u.getPassword().equals(password) && u.getUsername().equals(username)) {
    			return u;
    		}
    	}
    	return null;
    }
    
    public User findUserByUsername(String username) {
    	deserialize(contextPath);
    	for(User u : users.values()) {
    		if(u.getUsername().equals(username)) {
    			return u;
    		}
    	}
    	return null;
    }
    
    public Collection<User> blockThisUser(User user)
    {
    	user.setIsBlocked(true);
    	
    	update(user.getId(), user);
    	
    	if (user.getRole() == Role.CUSTOMER)
    	{
    		CustomerDAO customerDAO = new CustomerDAO();
    		customerDAO.blockCustomer(user.getId());
    	}
    	
    	if (user.getRole() == Role.MANAGER)
    	{
    		ManagerDAO managerDAO = new ManagerDAO();
    		managerDAO.blockManager(user.getId());
    	}
    	
    	deserialize(contextPath);
    	return users.values();
    }
    
    public Collection<Customer> blockThisUser(Customer customer)
    {
    	User user = findUser(customer.getId());
    	
    	user.setIsBlocked(true);
    	
    	update(user.getId(), user);
    	
    	CustomerDAO customerDAO = new CustomerDAO();
    	
    	if (user.getRole() == Role.CUSTOMER)
    	{
    		customerDAO.blockCustomer(user.getId());
    	}
    	
    	if (user.getRole() == Role.MANAGER)
    	{
    		ManagerDAO managerDAO = new ManagerDAO();
    		managerDAO.blockManager(user.getId());
    	}
    	
    	deserialize(contextPath);
    	return customerDAO.getSuspiciousCustomers();
    }
    
    public User save(User user){
    	deserialize(contextPath);
    	
    	for (Entry<Integer, User> u: users.entrySet())
    	{
    		if (u.getValue().getUsername().equals(user.getUsername()))
    		{
    			return null;
    		}
    	}
    	
    	if (!validation(user))
    	{
    		return null;
    	}
    	
        Integer maxId = -1;
        for(int id : users.keySet()){
            int idNum = id;
            if(idNum > maxId){
                maxId = idNum;
            }
        }
        maxId++;
        user.setId(maxId);
        users.put(user.getId(), user);
        
        
        serialize(contextPath);
        
        return user;
    }

    public User update(int id, User user)
    {
    	deserialize(contextPath);
        User userToUpdate = findUser(id);
        if (userToUpdate == null)
        {
            return save(user);
        }

        for (Entry<Integer, User> u: users.entrySet())
    	{
        	
    		if (u.getValue().getUsername().equals(user.getUsername()) && u.getValue().getId() != user.getId())
    		{
    			return null;
    		}
    	}
        
        
        userToUpdate.setUsername(user.getUsername());
        userToUpdate.setPassword(user.getPassword());
        userToUpdate.setFirstName(user.getFirstName());
        userToUpdate.setLastName(user.getLastName());
        userToUpdate.setGender(user.getGender());
        userToUpdate.setDateOfBirth(user.getDateOfBirth());
        userToUpdate.setRole(user.getRole());
        userToUpdate.setIsBlocked(user.getIsBlocked());

        //users.put(id, userToUpdate); //put metoda overriduje vrednost koja se nalazila na tom kljucu!

        if(!validateDate(userToUpdate.getDateOfBirth())) {
        	return null;
        }
    	serialize(contextPath);
    	
    	return userToUpdate;
    }

    public void delete (int id)
    {
        users.remove(id);
        serialize(contextPath);
    }
    
    public void serialize(String contextPath)
    {
    	try 
    	{
    		File file = new File(System.getProperty("user.dir") + "/WebContent/resources/Users.json");
    		FileWriter fileWriter = new FileWriter(file);
    		Gson gson = new GsonBuilder().setPrettyPrinting().create();
    		JsonArray jsonArray = new JsonArray();
    		
    		for (User user : users.values())
    		{
    			JsonObject jsonObject = new JsonObject();
    			jsonObject.addProperty("id", user.getId());
    			jsonObject.addProperty("username", user.getUsername());
    			jsonObject.addProperty("password", user.getPassword());
    			jsonObject.addProperty("firstName", user.getFirstName());
    			jsonObject.addProperty("lastName", user.getLastName());
    			jsonObject.addProperty("gender", user.getGender());
    			jsonObject.addProperty("dateOfBirth", user.getDateOfBirth());
    			jsonObject.addProperty("role", user.getRole().toString());
    			jsonObject.addProperty("isBlocked", user.getIsBlocked());
    			
    			jsonArray.add(jsonObject);
    		}
    		
    		//gson.toJson(jsonArray);
    		
    		String jsonString = gson.toJson(jsonArray);
    		fileWriter.write(jsonString);
    		
    		fileWriter.close();
    	}
    	catch (Exception e)
    	{
    		System.out.println("Ok");
    	}
    }
    
    public ArrayList<User> getFilteredUsers(String sortVal, String typeVal, String roleVal){
    	deserialize(contextPath);
		ArrayList<User> filteredUsers = new ArrayList<User>();
		ArrayList<User> filteredUsersForRemoval = new ArrayList<User>();
		
		for(User u : users.values()) {
			filteredUsers.add(u);
			filteredUsersForRemoval.add(u);
		}
		
		for(User user : filteredUsers) {
			/*FILTEROVANJE PO ROLE-u*/
			if(!user.getRole().toString().equals(roleVal) && !roleVal.equals("SELECTEDROLE")) {
				filteredUsersForRemoval.remove(user);
			}
			
			/*FILTEROVANJE PO CUSTOMERTYPE*/
			CustomerDAO customerDAO = new CustomerDAO();
			Customer customer = new Customer();
			if(!typeVal.equals("SELECTEDTYPE")) {
				if(user.getRole().toString().equals("MANAGER") || user.getRole().toString().equals("ADMINISTRATOR")) {										
					filteredUsersForRemoval.remove(user);
				}else {
					customer = customerDAO.findCustomer(user.getId());
					if(!customer.getCustomerType().getType().toString().equals(typeVal)) {
						filteredUsersForRemoval.remove(user);
					}
				}
			}
		}
		
		/*SORTIRANJE*/
		
		if(sortVal.equals("firstnameAsc")) {
			sortByFirstNameAscending(filteredUsersForRemoval);
		}else if(sortVal.equals("firstnameDesc")) {
			sortByFirstNameDescending(filteredUsersForRemoval);
		}else if(sortVal.equals("lastnameAsc")) {
			sortByLastNameAscending(filteredUsersForRemoval);
		}else if(sortVal.equals("lastnameDesc")) {
			sortByLastNameDescending(filteredUsersForRemoval);
		}else if(sortVal.equals("usernameAsc")) {
			sortByUsernameAscending(filteredUsersForRemoval);
		}else if(sortVal.equals("usernameDesc")) {
			sortByUsernameDescending(filteredUsersForRemoval);
		}else if(sortVal.equals("scoreAsc")) {
			filteredUsersForRemoval = sortByScoreAscending(filteredUsersForRemoval);
		}else if(sortVal.equals("scoreDesc")) {
			filteredUsersForRemoval = sortByScoreDescending(filteredUsersForRemoval);
		}
		
		return filteredUsersForRemoval;
    }
        
    public static void sortByFirstNameAscending(ArrayList<User> users) {
        Collections.sort(users, Comparator.comparing(User::getFirstName));
    }
    
    public static void sortByFirstNameDescending(ArrayList<User> users) {
        Collections.sort(users, Comparator.comparing(User::getFirstName).reversed());
    }
    
    public static void sortByLastNameAscending(ArrayList<User> users) {
        Collections.sort(users, Comparator.comparing(User::getLastName));
    }
    
    public static void sortByLastNameDescending(ArrayList<User> users) {
        Collections.sort(users, Comparator.comparing(User::getLastName).reversed());
    }
    
    public static void sortByUsernameAscending(ArrayList<User> users) {
        Collections.sort(users, Comparator.comparing(User::getUsername));
    }
    
    public static void sortByUsernameDescending(ArrayList<User> users) {
        Collections.sort(users, Comparator.comparing(User::getUsername).reversed());
    }
    
    public ArrayList<User> sortByScoreAscending(ArrayList<User> users) {
    	ArrayList<Customer> customerList = new ArrayList<>();
    	ArrayList<User> usersList = new ArrayList<>();
    	CustomerDAO customerDAO = new CustomerDAO();
    	
    	for(User user : users) {
    		if(user.getRole().toString().equals("CUSTOMER")) {
    			customerList.add(customerDAO.findCustomer(user.getId()));
    		}
    	}
    	sortCustomersByScoreAscending(customerList);
    	
    	for(Customer c : customerList) {
    		User user = (User) c;
    		usersList.add(user);
    	}
    	return usersList;
    }
    
    public ArrayList<User> sortByScoreDescending(ArrayList<User> users) {
    	ArrayList<Customer> customerList = new ArrayList<>();
    	ArrayList<User> usersList = new ArrayList<>();
    	CustomerDAO customerDAO = new CustomerDAO();
    	
    	for(User user : users) {
    		if(user.getRole().toString().equals("CUSTOMER")) {
    			customerList.add(customerDAO.findCustomer(user.getId()));
    		}
    	}
    	sortCustomersByScoreDescending(customerList);
    	
    	for(Customer c : customerList) {
    		User user = (User) c;
    		usersList.add(user);
    	}
    	return usersList;
    }
    
    public static void sortCustomersByScoreAscending(ArrayList<Customer> customers) {
        Collections.sort(customers, Comparator.comparing(Customer::getScore));
    }
    
    public static void sortCustomersByScoreDescending(ArrayList<Customer> customers) {
        Collections.sort(customers, Comparator.comparing(Customer::getScore).reversed());
    }
    
	
    public void deserialize(String contextPath)
    {
    	try
    	{
    		File file = new File(System.getProperty("user.dir") + "/WebContent/resources/Users.json");
			FileReader fileReader = new FileReader(file);
			Gson gson = new Gson();
			User[] usersArray = gson.fromJson(fileReader, User[].class);
			
			if(usersArray ==  null) {
				return;
			}
			
			for(User user : usersArray) {
				users.put(user.getId(), user);
			}
			
			fileReader.close();
    	} catch (Exception e)
    	{
    		System.out.println("Ok");
    	}
    }
    
    
    private boolean validation(User user)
	{
		if (!validateIfNull(user))
		{
			return false;
		}
		
		if (!validateDate(user.getDateOfBirth()))
		{
			return false;
		}
		
		return true;
	}
	
	private boolean validateIfNull(User user)
	{
		if (user.getUsername().isBlank())
		{
			return false;
		} 
		else if (user.getPassword().isBlank())
		{
			return false;
		}
		else if (user.getFirstName().isBlank())
		{
			return false;
		}
		else if (user.getLastName().isBlank())
		{
			return false;
		}
		else if (user.getGender().isBlank())
		{
			return false;
		}
		else if (user.getDateOfBirth().isBlank())
		{
			return false;
		}
		
		return true;
	}
	
	private boolean validateDate(String date)
	{
		String dateRegex = "\\d{4}-\\d{2}-\\d{2}";
		
		if (!date.matches(dateRegex))
		{
			return false;
		}
		
		try {
			LocalDate currentDate = LocalDate.now();
			LocalDate dateOfBirth = LocalDate.parse(date);
			int age = currentDate.getYear() - dateOfBirth.getYear();

	        if (age < 18) {
	            return false;
	        }

	        if (age >= 100) {
	            return false;
	        }
		}
		catch (Exception e)
		{
			System.out.println("Date is invalid");
			return false;
		}
		
		return true;
	}
}

package dao;

import beans.Comment;
import beans.CommentStatus;
import beans.Role;

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

public class CommentDAO {
    private HashMap<Integer, Comment> comments = new HashMap<Integer, Comment>();

    public CommentDAO(){}
    
    
    public void serialize() {
    	try
    	{
    		File file = new File(System.getProperty("user.dir") + "/WebContent/resources/Comments.json");
    		FileWriter fileWriter = new FileWriter(file);
    		Gson gson = new GsonBuilder().setPrettyPrinting().create();
    		JsonArray jsonArray = new JsonArray();
    		
    		for(Comment com : comments.values()) {
    			JsonObject jsonObject = new JsonObject();
    			jsonObject.addProperty("id", com.getId());
    			jsonObject.addProperty("userId", com.getUserId());
    			jsonObject.addProperty("storeId", com.getStoreId());
    			jsonObject.addProperty("content", com.getContent());
    			jsonObject.addProperty("rating", com.getRating());
    			jsonObject.addProperty("status", com.getStatus().toString());
    			
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
			File file = new File(System.getProperty("user.dir") + "/WebContent/resources/Comments.json");
			FileReader fileReader = new FileReader(file);
			Gson gson = new Gson();
			Comment[] commentsArray = gson.fromJson(fileReader, Comment[].class);
			
			if(commentsArray == null) {
				return;
			}
			
			for(Comment com : commentsArray) {
				comments.put(com.getId(), com);
			}
			
			fileReader.close();
			
		}catch(Exception e) {
			System.out.println("Ne mogu da serijalizujem comment");
		}
    }
    
    public Collection<Comment> findAll(){
    	deserialize();
        return comments.values();
    }
    
    public ArrayList<Comment> getCommentsByRole(String role, int storeId){
    	deserialize();
    	ArrayList<Comment> returnCommentList = new ArrayList<>();
    	
    	if(role.equals(Role.ADMINISTRATOR.toString()) || role.equals(Role.MANAGER.toString())) {
    		for(Comment com : comments.values()) {
    			if(storeId == com.getStoreId()) {    				
    				returnCommentList.add(com);
    			}
    		}
    	}
    	
    	if(role.equals(Role.CUSTOMER.toString())) {
    		for(Comment com : comments.values()) {
    			if(com.getStatus().toString().equals("APPROVED") && storeId == com.getStoreId()) {
    				returnCommentList.add(com);
    			}
    		}
    	}
    	
    	return returnCommentList;
    }
    
    public Comment findComment(int id){
    	deserialize();
        return comments.containsKey(id) ? comments.get(id) : null;
    }

    public Comment save(Comment comment){
    	deserialize();
        Integer maxId = -1;
        for (int id : comments.keySet()) {
            int idNum = id;
            if (idNum > maxId) {
                maxId = idNum;
            }
        }
        maxId++;
        comment.setId(maxId);
        comments.put(comment.getId(), comment);
        serialize();
        return comment;
    }

    public Comment update(int id, Comment comment) {
    	deserialize();
    	Comment commentToUpdate = findComment(id);
        if (commentToUpdate == null) {        	
        	return save(comment);
        }

        commentToUpdate.setUserId(comment.getUserId());
        commentToUpdate.setStoreId(comment.getStoreId());
        commentToUpdate.setContent(comment.getContent());
        commentToUpdate.setRating(comment.getRating());
        commentToUpdate.setStatus(comment.getStatus());
        //delete(id);
        //save(commentToUpdate);
        serialize();
        return commentToUpdate;
    }

    public void delete(int id) {
    	deserialize();
        comments.remove(id);
        serialize();
    }
}

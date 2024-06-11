package services;

import beans.RentACarStore;
import beans.Vehicle;
import dao.RentACarStoreDAO;

import java.util.ArrayList;
import java.util.Collection;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;


@Path("/stores")
public class RentACarStoreService {

    @Context
    ServletContext ctx;


    public RentACarStoreService() {

    }

    @PostConstruct
    public void init() {
        if (ctx.getAttribute("rentACarStoreDAO") == null) {
            ctx.setAttribute("rentACarStoreDAO", new RentACarStoreDAO());
        }
    }

    @GET
    @Path("/dobaviSveStorove")
    @Produces(MediaType.APPLICATION_JSON)
    public Collection<RentACarStore> getStores() {
        RentACarStoreDAO dao = (RentACarStoreDAO) ctx.getAttribute("rentACarStoreDAO");
        
        return dao.findAll();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public RentACarStore getRentACarStore(@PathParam("id") int id) {
        RentACarStoreDAO dao = (RentACarStoreDAO) ctx.getAttribute("rentACarStoreDAO");
        return dao.findRentACarStore(id);
    }
    
    @GET
    @Path("/search/{address}/{rating}/{storeName}/{vehicleType}")
    @Produces(MediaType.APPLICATION_JSON)
    public ArrayList<RentACarStore> getSearchedStores(@PathParam("address") String address, @PathParam("rating") double rating, @PathParam("storeName") String name, @PathParam("vehicleType") String vehicleType){
    	RentACarStoreDAO dao = (RentACarStoreDAO) ctx.getAttribute("rentACarStoreDAO");
    	return dao.getSearchedStores(address, rating, name, vehicleType);
    }
    
    @GET
    @Path("/filter/{sort}/{gearshift}/{fuel}/{opened}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ArrayList<RentACarStore> getFilteredStores(@PathParam("sort") String sort, @PathParam("gearshift") String gearshift, @PathParam("fuel") String fuel, @PathParam("opened") String opened){
    	RentACarStoreDAO dao = (RentACarStoreDAO) ctx.getAttribute("rentACarStoreDAO");
    	return dao.getFilteredStores(sort, gearshift, fuel, opened);
    }
    
    @GET
    @Path("/vehicles/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ArrayList<Vehicle> getStoreVehicles(@PathParam("id") int id){
    	RentACarStoreDAO dao = (RentACarStoreDAO) ctx.getAttribute("rentACarStoreDAO");
    	return dao.getStoreVehicles(id);
    }
    
    @PUT
    @Path("/setWorkingStatus")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ArrayList<RentACarStore> setWorkingStatus(){
    	RentACarStoreDAO dao = (RentACarStoreDAO) ctx.getAttribute("rentACarStoreDAO");
    	return dao.getCurrentlyOpenedStores();
    }
    
    @POST
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    public RentACarStore addRentACarStore(RentACarStore store) {
        RentACarStoreDAO dao = (RentACarStoreDAO) ctx.getAttribute("rentACarStoreDAO");
        return dao.save(store);
    }

    @PUT
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public RentACarStore updateRentACarStore(@PathParam("id") int id, RentACarStore store) {
        RentACarStoreDAO dao = (RentACarStoreDAO) ctx.getAttribute("rentACarStoreDAO");
        return dao.update(id, store);
    }
    
    @PUT
    @Path("/updateRating/{storeId}/{newRating}")
    @Produces(MediaType.APPLICATION_JSON)
    public RentACarStore updateStoreRating(@PathParam("storeId") int storeId, @PathParam("newRating") int newRating) {
    	 RentACarStoreDAO dao = (RentACarStoreDAO) ctx.getAttribute("rentACarStoreDAO");
    	 return dao.updateStoreRating(storeId, newRating);
    }
    
    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public void removeRentACarStore(@PathParam("id") int id) {
        RentACarStoreDAO dao = (RentACarStoreDAO) ctx.getAttribute("rentACarStoreDAO");
        dao.delete(id);
    }
}
package services;

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

import beans.RentACarStore;
import beans.Vehicle;
import dao.VehicleDAO;

@Path("/vehicles")
public class VehicleService {
	
	@Context
	ServletContext ctx;
	
	public VehicleService() {
		
	}
	
	@PostConstruct
	public void init()
	{
		if (ctx.getAttribute("vehicleDAO") == null)
		{
			ctx.setAttribute("vehicleDAO", new VehicleDAO());
		}
	}
	
	@POST
	@Path("/")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public ArrayList<Vehicle> getOrderedVehicles(ArrayList<Integer> orderedVehicles)
	{
		VehicleDAO dao = (VehicleDAO) ctx.getAttribute("vehicleDAO");
		return dao.findOrderedVehicles(orderedVehicles);
	}
	
	@POST
	@Path("/save")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Vehicle saveVehicle(Vehicle vehicle)
	{
		VehicleDAO dao = (VehicleDAO) ctx.getAttribute("vehicleDAO");
		return dao.save(vehicle);
	}
	
	@PUT
	@Path("/{storeId}")
	@Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
	public RentACarStore updateEveryVehicleInThisStore(@PathParam("storeId") int storeId)
	{
		VehicleDAO dao = (VehicleDAO) ctx.getAttribute("vehicleDAO");
		return dao.updateEveryVehicleInThisStore(storeId);
	}
	
	@DELETE
	@Path("/{vehicleId}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public void deleteVehicle(@PathParam("vehicleId") int vehicleId) {
		VehicleDAO dao = (VehicleDAO) ctx.getAttribute("vehicleDAO");
		dao.delete(vehicleId);
	}
	
	@PUT
	@Path("/izmeni")
	@Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
	public Vehicle updateVehicle(Vehicle vehicle)
	{
		VehicleDAO dao = (VehicleDAO) ctx.getAttribute("vehicleDAO");
		return dao.update(vehicle.getId(), vehicle);
	}

	@GET
	@Path("/allVehicles")
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<Vehicle> getAllVehicles()
	{
		VehicleDAO dao = (VehicleDAO) ctx.getAttribute("vehicleDAO");
		return dao.findAll();
	}
	
	@GET
	@Path("/searchAvailableVehicles/{pickUpDate}/{returnDate}")
	@Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
	public ArrayList<Vehicle> getSearchedVehicles(@PathParam("pickUpDate") String pickUpDate, @PathParam("returnDate") String returnDate)
	{
		VehicleDAO dao = (VehicleDAO) ctx.getAttribute("vehicleDAO");
		return dao.getSearchedAvailableVehicles(pickUpDate, returnDate);
	}
	
	@GET
	@Path("/addVehicleToCart/{pickUpDate}/{dateOfReturn}/{customerId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<Vehicle> getVehiclesAfterAddingToCart(@PathParam("pickUpDate") String pickUpDate, @PathParam("dateOfReturn") String returnDate, @PathParam("customerId") int customerId)
	{
		VehicleDAO dao = (VehicleDAO) ctx.getAttribute("vehicleDAO");
		return dao.getVehiclesAfterAddingToCart(pickUpDate, returnDate, customerId);
	}
}

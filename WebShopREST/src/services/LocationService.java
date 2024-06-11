package services;

import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import beans.Location;
import dao.LocationDAO;

@Path("/locations")
public class LocationService {
	
	@Context
	ServletContext ctx;
	
	public LocationService() {
		
	}
	
	@PostConstruct
	public void init() {
		if (ctx.getAttribute("locationDAO") == null) {
			ctx.setAttribute("locationDAO", new LocationDAO());
		}
	}
	
	@GET
	@Path("/{longitude}/{latitude}/{address}")
	@Produces(MediaType.APPLICATION_JSON)
	public Location getLocationOrCreateNew(@PathParam("longitude") double longitude, @PathParam("latitude") double latitude, @PathParam("address") String address) {
		LocationDAO dao = (LocationDAO) ctx.getAttribute("locationDAO");
		return dao.getLocationOrCreateNew(longitude, latitude, address);
	}
}
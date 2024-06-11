package services;

import java.util.Collection;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import beans.Manager;
import beans.User;
import dao.ManagerDAO;

@Path("/user/managers")
public class ManagerService {
	@Context
	ServletContext ctx;

	public ManagerService() {
	}

	@PostConstruct
	public void init() {
		if (ctx.getAttribute("managerDAO") == null) {
			ctx.setAttribute("managerDAO", new ManagerDAO());
		}
	}

	@GET
	@Path("/{username}")
	@Produces(MediaType.APPLICATION_JSON)
	public Manager getManagerByUsername(@PathParam("username") String username) {
		ManagerDAO dao = (ManagerDAO) ctx.getAttribute("managerDAO");
		return dao.findManagerByUsername(username);
	}

	@GET
	@Path("/")
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<Manager> findAllAvailable() {
		ManagerDAO dao = (ManagerDAO) ctx.getAttribute("managerDAO");
		return dao.findAllAvailable();
	}

	
	@GET  
	@Path("/banana/{id}")  
	@Produces(MediaType.APPLICATION_JSON) 
	public Manager getManager(@PathParam("id") int id) 
	{ 
		ManagerDAO dao = (ManagerDAO) ctx.getAttribute("managerDAO"); 
		return dao.findManager(id); 
	}
	 

	@POST
	@Path("/")
	@Produces(MediaType.APPLICATION_JSON)
	public Manager getManagerPost(User user) {
		ManagerDAO dao = (ManagerDAO) ctx.getAttribute("managerDAO");
		return dao.createManager(user);
	}

	@PUT
	@Path("/")
	@Produces(MediaType.APPLICATION_JSON)
	public Manager updateManager(Manager manager) {
		ManagerDAO dao = (ManagerDAO) ctx.getAttribute("managerDAO");
		return dao.update(manager.getId(), manager);
	}

}

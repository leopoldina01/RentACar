package services;

import java.util.ArrayList;
import java.util.Collection;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import beans.Customer;
import beans.User;
import dao.UserDAO;

@Path("/user")
public class UserService {
	@Context
    ServletContext ctx;

	public UserService() {
	}

	@PostConstruct
	public void init()
	{
		if (ctx.getAttribute("userDAO") == null)
		{
			String contextPath = ctx.getRealPath("");
			ctx.setAttribute("userDAO", new UserDAO(contextPath));
		}
	}
	
	@POST
	@Path("/login")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response login(User user, @Context HttpServletRequest request){
		UserDAO dao = (UserDAO) ctx.getAttribute("userDAO");
		User loggedUser = dao.findUserByPasswordAndUsername(user.getUsername(), user.getPassword());
		if(loggedUser == null) {
			return Response.status(400).entity("Username/Password are incorrect.").build();
		}
		request.getSession().setAttribute("user", loggedUser);
		return Response.status(200).build();
	}
	
	@POST
	@Path("/logout")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public void logout(@Context HttpServletRequest request) {
		request.getSession().invalidate();
	}
	
	@GET
	@Path("/currentLoggedUser")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public User getCurrentLoggedUser(@Context HttpServletRequest request) {
		User user = (User) request.getSession().getAttribute("user");
		return user;
	}
	
	@POST
	@Path("/")
	@Produces(MediaType.APPLICATION_JSON)
	public User getUserPost(User user) 
	{
		UserDAO dao = (UserDAO) ctx.getAttribute("userDAO");
		return dao.save(user);
	}
	
	@GET
	@Path("/{username}/{password}")
	@Produces(MediaType.APPLICATION_JSON)
	public User getUserByPasswordAndUsername(@PathParam("username") String username, @PathParam("password") String password) 
	{
		UserDAO dao = (UserDAO) ctx.getAttribute("userDAO");
		return dao.findUserByPasswordAndUsername(username, password);
	}
	
	@GET
	@Path("/findby/{username}")
	@Produces(MediaType.APPLICATION_JSON)
	public User getuserByUsername(@PathParam("username") String username) {
		UserDAO dao = (UserDAO) ctx.getAttribute("userDAO");
		return dao.findUserByUsername(username);
	}
	
	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public User getUser(@PathParam("id") int id)
	{
		UserDAO dao = (UserDAO) ctx.getAttribute("userDAO");
		return dao.findUser(id);
	}
	
	@GET
	@Path("/showAllUsers")
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<User> getAllUsers()
	{
		UserDAO dao = (UserDAO) ctx.getAttribute("userDAO");
		return dao.findAll();
	}
	
	@GET
	@Path("/filter/{sortVal}/{typeVal}/{roleVal}")
	@Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
	public ArrayList<User> getFilteredUsers(@PathParam("sortVal") String sortVal, @PathParam("typeVal") String typeVal, @PathParam("roleVal") String roleVal){
		UserDAO dao = (UserDAO) ctx.getAttribute("userDAO");
		return dao.getFilteredUsers(sortVal, typeVal, roleVal);
	}
	
	
	@POST
	@Path("/searchUsers")
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<User> searchUsers(User searchUser)
	{
		UserDAO dao = (UserDAO) ctx.getAttribute("userDAO");
		return dao.searchUsers(searchUser.getUsername(), searchUser.getFirstName(), searchUser.getLastName());
	}
	
	@POST
	@Path("/blockUser")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Collection<User> blockThisUser(User user)
	{
		UserDAO dao = (UserDAO) ctx.getAttribute("userDAO");
		return dao.blockThisUser(user);
	}
	
	@POST
	@Path("/blockCustomer")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Collection<Customer> blockThisCustomer(Customer customer)
	{
		UserDAO dao = (UserDAO) ctx.getAttribute("userDAO");
		return dao.blockThisUser(customer);
	}
	
	@PUT
    @Path("/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public User updateUser(@PathParam("id") int id, User user, @Context HttpServletRequest request) {
        UserDAO dao = (UserDAO) ctx.getAttribute("userDAO");
        User updatedUser = dao.update(id,user);
        request.getSession().setAttribute("user", updatedUser);
        return updatedUser;
    }
}
package services;

import java.util.Collection;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import beans.Customer;
import beans.User;
import dao.CustomerDAO;

@Path("/users/customers")
public class CustomerService {
	
	@Context
	ServletContext ctx;
	
	public CustomerService()
	{
		
	}
	
	@PostConstruct
	public void init()
	{
		if (ctx.getAttribute("customerDAO") == null)
		{
			ctx.setAttribute("customerDAO", new CustomerDAO());
		}
	}
	
	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Customer getCustomer(@PathParam("id") int id)
	{
		CustomerDAO dao = (CustomerDAO) ctx.getAttribute("customerDAO");
		return dao.findCustomer(id);
	}
	
	@GET
	@Path("/addCarToCart/{vehicleId}/{customerId}/{price}")
	@Produces(MediaType.APPLICATION_JSON)
	public Customer updateCustomer(@PathParam("vehicleId") int vehicleId, @PathParam("customerId") int customerId, @PathParam("price") int price)
	{
		CustomerDAO dao = (CustomerDAO) ctx.getAttribute("customerDAO");
		return dao.AddCarToCart(vehicleId, customerId, price);
	}
	
	@GET
	@Path("/loggedIn/{customerUsername}")
	@Produces(MediaType.APPLICATION_JSON)
	public Customer updateLoggedCustomer(@PathParam("customerUsername") String customerUsername)
	{
		CustomerDAO dao = (CustomerDAO) ctx.getAttribute("customerDAO");
		return dao.updateLoggedCustomer(customerUsername);
	}
	
	@GET
	@Path("/removeFromCart/{vehicleId}/{customerId}/{price}")
	@Produces(MediaType.APPLICATION_JSON)
	public Customer updateCustomerRemoveFromCart(@PathParam("vehicleId") int vehicleId, @PathParam("customerId") int customerId, @PathParam("price") int price)
	{
		CustomerDAO dao = (CustomerDAO) ctx.getAttribute("customerDAO");
		return dao.RemoveFromCart(vehicleId, customerId, price);
	}
	
	@GET
	@Path("/updateDates/{pickUpDate}/{returnDate}/{customerId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Customer updateShoppingCartDates(@PathParam("pickUpDate") String pickUpDate, @PathParam("returnDate") String returnDate, @PathParam("customerId") int customerId)
	{
		CustomerDAO dao = (CustomerDAO) ctx.getAttribute("customerDAO");
		return dao.updateShoppingCartDates(pickUpDate, returnDate, customerId);
	}
	
	@GET
	@Path("/findSuspiciousCustomers")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Collection<Customer> getSuspiciousCustomers()
	{
		CustomerDAO dao = (CustomerDAO) ctx.getAttribute("customerDAO");
		return dao.getSuspiciousCustomers();
	}
	
	@POST
	@Path("/registrate")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public void registrateCustomer(User user)
	{
		CustomerDAO dao = (CustomerDAO) ctx.getAttribute("customerDAO");
		dao.registrate(user);
	}
}

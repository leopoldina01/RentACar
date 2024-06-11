package services;

import java.util.ArrayList;
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
import beans.Order;
import beans.Vehicle;
import dao.OrderDAO;

@Path("/orders")
public class OrderService {
	
	@Context
	ServletContext ctx;
	
	public OrderService() {
		
	}
	
	@PostConstruct
	public void init()
	{
		if (ctx.getAttribute("orderDAO") == null)
		{
			ctx.setAttribute("orderDAO", new OrderDAO());
		}
	}
	
	@POST
	@Path("/")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public ArrayList<Order> getCustomerOrders(ArrayList<Integer> allOrders)
	{
		OrderDAO dao = (OrderDAO) ctx.getAttribute("orderDAO");
		return dao.findAllOrders(allOrders);
	}
	
//	@POST
//	@Path("/createFinalOrder")
//	@Produces(MediaType.APPLICATION_JSON)
//	@Consumes(MediaType.APPLICATION_JSON)
//	public Customer createFinalOrder(Customer customer, ArrayList<Vehicle> vehicles)
//	{
//		OrderDAO dao = (OrderDAO) ctx.getAttribute("orderDAO");
//		return dao.createFinalOrder(customer, vehicles);
//	}
//	
	
	@POST
	@Path("/createFinalOrder")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Customer createFinalOrder(Customer customer)
	{
		OrderDAO dao = (OrderDAO) ctx.getAttribute("orderDAO");
		return dao.createFinalOrder(customer);
	}
	
	@POST
	@Path("/cancelOrder")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Collection<Order> cancelOrder(Order order)
	{
		OrderDAO dao = (OrderDAO) ctx.getAttribute("orderDAO");
		return dao.cancelOrder(order);
	}
	
	@POST
	@Path("/approveOrder")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Collection<Order> approveOrder(Order order)
	{
		OrderDAO dao = (OrderDAO) ctx.getAttribute("orderDAO");
		return dao.approveOrder(order);
	}
	
	@GET
	@Path("/rejectOrder/{comment}/{orderId}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Collection<Order> rejectOrder(@PathParam("comment") String comment, @PathParam("orderId") int orderId)
	{
		OrderDAO dao = (OrderDAO) ctx.getAttribute("orderDAO");
		return dao.rejectOrder(comment, orderId);
	}
	
	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<Order> getOrdersByStore(@PathParam("id") int id)
	{
		OrderDAO dao = (OrderDAO) ctx.getAttribute("orderDAO");
		return dao.findOrdersByStore(id);
	}
	
	@GET
	@Path("/customers/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<Customer> getCustomersByStore(@PathParam("id") int id)
	{
		OrderDAO dao = (OrderDAO) ctx.getAttribute("orderDAO");
		return dao.findCustomersByStore(id);
	}
	
	@GET
	@Path("/search/{priceFrom}/{priceTo}/{dateFrom}/{dateTo}/{storeId}/{sort}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public ArrayList<Order> getSearchedOrders(@PathParam("priceFrom") int priceFrom, @PathParam("priceTo") int priceTo, @PathParam("dateFrom") String dateFrom, @PathParam("dateTo") String dateTo, @PathParam("storeId") int storeId, @PathParam("sort") String sort)
	{
		OrderDAO dao = (OrderDAO) ctx.getAttribute("orderDAO");
		return dao.getSearchedOrders(priceFrom, priceTo, dateFrom, dateTo, storeId, sort);
	}
	
	@GET
	@Path("/searchCustomerOrders/{priceFrom}/{priceTo}/{dateFrom}/{dateTo}/{customerId}/{searchStoreName}/{sort}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public ArrayList<Order> getSearchedOrdersForCustomer(@PathParam("priceFrom") int priceFrom, @PathParam("priceTo") int priceTo, @PathParam("dateFrom") String dateFrom, @PathParam("dateTo") String dateTo, @PathParam("customerId") int storeId, @PathParam("sort") String sort, @PathParam("searchStoreName") String searchStoreName)
	{
		OrderDAO dao = (OrderDAO) ctx.getAttribute("orderDAO");
		return dao.getSearchedOrdersForCustomer(priceFrom, priceTo, dateFrom, dateTo, storeId, sort, searchStoreName);
	}
	
	@GET
	@Path("/orderIsTaken/{orderId}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Collection<Order> makeOrderTaken(@PathParam("orderId") int orderId)
	{
		OrderDAO dao = (OrderDAO) ctx.getAttribute("orderDAO");
		return dao.makeOrderTaken(orderId);
	}
	
	@GET
	@Path("/returnOrder/{orderId}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Collection<Order> returnOrder(@PathParam("orderId") int orderId)
	{
		OrderDAO dao = (OrderDAO) ctx.getAttribute("orderDAO");
		return dao.returnOrder(orderId);
	}
}

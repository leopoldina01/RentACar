package services;

import java.util.ArrayList;

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

import beans.Comment;
import dao.CommentDAO;

@Path("/comments")
public class CommentService {
		
	
	@Context
	ServletContext ctx;
	
	@PostConstruct
	public void init() {
		if(ctx.getAttribute("commentDAO") == null) {
			ctx.setAttribute("commentDAO", new CommentDAO());
		}
	}
	
	@POST
	@Path("/save")
	@Produces(MediaType.APPLICATION_JSON)
	public Comment saveComment(Comment comment) 
	{
		CommentDAO dao = (CommentDAO) ctx.getAttribute("commentDAO");
		return dao.save(comment);
	}
	
	@GET
	@Path("/{role}/{storeId}")
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<Comment> getCommentsByRole(@PathParam("role") String role, @PathParam("storeId") int storeId){
		CommentDAO dao = (CommentDAO) ctx.getAttribute("commentDAO");
		return dao.getCommentsByRole(role, storeId);
	}
	
	@PUT
	@Path("/")
	@Produces(MediaType.APPLICATION_JSON)
	public Comment updateComment(Comment comment) {
		CommentDAO dao = (CommentDAO) ctx.getAttribute("commentDAO");
		return dao.update(comment.getId(), comment);
	}
	
}

package it.drwolf.resources;

import io.quarkus.security.Authenticated;
import io.vertx.core.eventbus.EventBus;
import it.drwolf.model.dtos.BookDTO;
import it.drwolf.model.entities.Book;
import it.drwolf.repositories.BookRepository;
import it.drwolf.services.LongRunning;
import it.drwolf.services.TaskScheduler;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.eclipse.microprofile.openapi.annotations.enums.SecuritySchemeType;
import org.eclipse.microprofile.openapi.annotations.security.SecurityScheme;
import org.jboss.resteasy.annotations.jaxrs.PathParam;

import java.util.List;

@Path("/book")
public class BookResource {

	@Inject
	BookRepository bookRepository;

	@Inject
	TaskScheduler taskScheduler;

	@Inject
	JsonWebToken jwt;


	@Inject
	EventBus bus;

	@GET
	@RolesAllowed({ "corso","admin" })
	public List<Book> all(){
		return bookRepository.listAll();
	}

	@POST
	@Transactional
	public Book add(Book book){
		bookRepository.persist(book);
		taskScheduler.scheduleTask();
		return book;
	}

	@PUT
	@Transactional
	@Path("{id}")
	public Book update(@PathParam Long id, Book book){
		bookRepository.persist(book);
		taskScheduler.scheduleTask();

		return book;
	}


	@GET
	@Path("long-running")
	public void longRunningTask(){
		bus.send(LongRunning.LONG,"Hello!");
	}


}

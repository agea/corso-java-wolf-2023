package it.drwolf.resources;

import io.quarkus.security.Authenticated;
import it.drwolf.model.dtos.BookDTO;
import it.drwolf.model.entities.Book;
import it.drwolf.repositories.BookRepository;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.eclipse.microprofile.openapi.annotations.enums.SecuritySchemeType;
import org.eclipse.microprofile.openapi.annotations.security.SecurityScheme;

import java.util.List;

@Path("/book")
@Authenticated
public class BookResource {

	@Inject
	BookRepository bookRepository;

	@Inject
	JsonWebToken jwt;


	@GET
	@RolesAllowed({ "corso","admin" })
	public List<Book> all(){
		return bookRepository.listAll();
	}

	@POST
	@Transactional
	public Book add(Book book){
		bookRepository.persist(book);
		return book;
	}

}

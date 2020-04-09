package user.login;

import models.User;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Optional;

@Path(LoginResource.RESOURCE_PATH)
@Produces(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class LoginResource{

    public static final String RESOURCE_PATH = "/user/login";

    private static final long serialVersionUID = 5070928362318899898L;

    @Inject
    private UserDAO userDAO;

    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response login(@FormParam("email") String email,
                          @FormParam("password") String password){

        Optional<User> userOptional = userDAO.login(email, password);
        userOptional.ifPresent(System.out::println);

        return userOptional.map(u ->
                Response.status(Response.Status.NO_CONTENT).build()).
                orElse(Response.status(Response.Status.UNAUTHORIZED).build());
    }
}

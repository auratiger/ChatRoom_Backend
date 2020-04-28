package Verification;

import org.apache.log4j.Logger;
import user.login.UserDAO;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path(EmailResource.RESOURCE_PATH)
@Produces(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class EmailResource {
    public static final String RESOURCE_PATH = "/email_verification/verify/";

    private static final Logger logger = Logger.getLogger(EmailResource.class);

    @Inject
    private UserDAO userDAO;

    @POST
    @Path("{email}/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response verify(@PathParam("email") String email, @PathParam("id") Long id){
        logger.debug("received email verification from " + email + " id " + id);

//        userDAO.verifyEmail(id);

        return Response.ok().build();
    }
}

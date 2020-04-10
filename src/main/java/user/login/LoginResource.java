package user.login;

import admin.Application;
import admin.EmailClient;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import models.Member;
import models.User;
import org.apache.log4j.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.security.Key;
import java.util.Date;
import java.util.Optional;

@Path(LoginResource.RESOURCE_PATH)
@Produces(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class LoginResource{

    public static final String RESOURCE_PATH = "/user";

    private static final Logger logger = Logger.getLogger(LoginResource.class);

    private static final long serialVersionUID = 5070928362318899898L;

    @Inject
    private EmailClient emailClient;

    @Inject
    private UserDAO userDAO;

    @Inject
    private MemberDAO memberDAO;

    @POST
    @Path("login")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response login(@FormParam("email") String email,
                          @FormParam("password") String password){

        Optional<User> userOptional = userDAO.login(email, password);
        String token = issueToken("one", "Smith", "John");
        emailClient.sendMessage("auratiger00@gmail.com");

        return userOptional.map(u ->
                Response.ok().entity(token).build()).
                orElse(Response.status(Response.Status.UNAUTHORIZED).build());
    }

    @POST
    @Path("signup")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createUser(String newUserJson){

        try {
            Member persisted = memberDAO.addMember(Member.fromJson(newUserJson));
            URI location = URI.create(Application.APPLICATION_PATH + RESOURCE_PATH + "/" + persisted.getId());
            String token = issueToken(persisted.getId().toString(), location.toString(), "empty jwt");
            emailClient.sendMessage(persisted.getEmail());
            return Response.created(location).entity(token).build();
        }catch (Exception ex){
            logger.error("User already exists");
            return Response.status(Response.Status.CONFLICT).entity("User already exists").build();
        }
    }

    private String issueToken(String id, String issuer, String subject){

        Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

        return Jwts.builder().
                setId(id).
                setIssuer(issuer).
                setSubject(subject).
                setIssuedAt(new Date()).
                signWith(key).
                compact();
    }
}
package user.login;

import Filters.JwtTokenNeeded;
import admin.Application;
import admin.EmailClient;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import models.Member;
import models.User;
import org.apache.log4j.Logger;
import user.member.MemberDAO;

import javax.crypto.SecretKey;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.util.Optional;
import java.util.Properties;

@Path(LoginResource.RESOURCE_PATH)
@Produces(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class LoginResource{

    public static final String RESOURCE_PATH = "/user";

    private static final Logger logger = Logger.getLogger(LoginResource.class);

    private static final long serialVersionUID = 5070928362318899898L;

    private final String privateKey;

    {
        Properties props = new Properties();
        try {
            props.load(new FileInputStream(
                    "C:\\Users\\jboxers\\IdeaProjects\\ChatRoom_Backend\\src\\main\\resources\\application.properties"
            ));
        }catch (IOException e){
            e.printStackTrace();
        }

        privateKey = props.getProperty("jwt.key");
    }

    @Inject
    private EmailClient emailClient;

    @Inject
    private UserDAO userDAO;

    @Inject
    private MemberDAO memberDAO;

    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(@FormParam("email") String email,
                          @FormParam("password") String password){

        Optional<User> userOptional = userDAO.login(email, password);

        return userOptional.map(u ->
                Response.ok()
                        .entity(issueToken(u.getId().toString(), u.toJson().toString()))
                        .build()).
                orElse(Response.status(Response.Status.UNAUTHORIZED).build());
    }

    @POST
    @Path("/signup")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createUser(String newUserJson){
        logger.info(newUserJson);
        try {
            Member persisted = memberDAO.addMember(Member.fromJson(newUserJson));
            logger.info("created new User: " + persisted);
            URI location = URI.create(Application.APPLICATION_PATH + RESOURCE_PATH + "/" + persisted.getId());
            String token = issueToken(persisted.getId().toString(), persisted.toJson().toString());
            emailClient.sendMessage(persisted.getEmail(), persisted.getId());
            return Response.created(location).entity(token).build();
        }catch (Exception ex){
            logger.error("User already exists " + ex.toString());
            return Response.status(Response.Status.CONFLICT).entity("User already exists").build();
        }
    }

    private String issueToken(String id, String payload){

        SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(privateKey));

        return Jwts.builder().
                setId(id).
                setSubject(payload).
                signWith(key).
                compact();
    }
}
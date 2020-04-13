package Filters;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.apache.log4j.Logger;

import javax.annotation.Priority;
import javax.crypto.SecretKey;
import javax.servlet.annotation.WebFilter;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

@Provider
@JwtTokenNeeded
//@WebFilter("/chat")
@Priority(Priorities.AUTHENTICATION)
public class JwtTokenNeededFilter implements ContainerRequestFilter {

    private static final Logger logger = Logger.getLogger(JwtTokenNeededFilter.class);

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {

        Properties props = new Properties();
        try {
            props.load(new FileInputStream(
                    "C:\\Users\\jboxers\\IdeaProjects\\ChatRoom_Backend\\src\\main\\resources\\application.properties"
            ));
        }catch (IOException e){
            e.printStackTrace();
        }

        String privateKey = props.getProperty("jwt.key");

        String authorizationHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);
        String token = authorizationHeader.substring("Bearer".length()).trim();

        SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(privateKey));

        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            logger.debug("valid jwt token: " + token);
        }catch (Exception e){
            logger.debug("invalid jwt token");
            requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
        }
    }
}

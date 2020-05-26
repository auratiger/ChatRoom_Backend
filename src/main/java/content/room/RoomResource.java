package content.room;

import content.message.MessageResource;
import models.Room;
import models.User;
import user.login.UserDAO;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Path(RoomResource.RESOURCE_PATH)
@Produces(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class RoomResource {
    public static final String RESOURCE_PATH = "chat/resource";

    @Inject
    RoomDAO roomDAO;

    @Inject
    UserDAO userDAO;

    @GET
    @Path("/rooms/{userid}")
    public Response getRooms(@PathParam("userid") long userId){

        List<Room> rooms = roomDAO.findRoomByUser(userId);

        JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();

        for (Room room : rooms) {
            arrayBuilder.add(room.toJson());
            System.out.println(room);
        }
        objectBuilder.add("groups", arrayBuilder);

        return Response.ok().entity(objectBuilder.build().toString()).build();
    }

    @POST
    @Path("/rooms/{userid}/{groupName}")
    public Response createRoom(@PathParam("userid") long userId, @PathParam("groupName") String name){

        Room newRoom = roomDAO.addRoom(new Room(name, LocalDateTime.now()));
        Optional<User> user = userDAO.findUserById(userId);
        user.ifPresent(newRoom::addUser);

        return Response.ok().build();
    }
}

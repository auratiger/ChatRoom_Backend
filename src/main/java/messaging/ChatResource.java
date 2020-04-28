package messaging;

import content.message.MessageDAO;
import models.Message;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path(ChatResource.RESOURCE_PATH)
@Produces(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class ChatResource {
    public static final String RESOURCE_PATH = "chat/resource";

    @Inject
    private MessageDAO messageDAO;

    @POST
    @Path("/messages/{userid}/{roomid}")
    public Response getMessages(@PathParam("userid") long userid, @PathParam("roomid") long roomid){

        List<Message> messages = messageDAO.findMessagesByRoom(roomid);
        for(Message message: messages){
            System.out.println(message);
        }

        JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
        JsonArrayBuilder builder = Json.createArrayBuilder();
        for(int i = 0; i < messages.size(); i++){
            builder.add(i, messages.get(i).toJson());
        }
        objectBuilder.add("messages", builder);

        return Response.ok().entity(objectBuilder.build().toString()).build();
    }
}

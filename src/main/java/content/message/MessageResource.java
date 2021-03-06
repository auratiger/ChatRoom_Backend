package content.message;

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

@Path(MessageResource.RESOURCE_PATH)
@Produces(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class MessageResource {
    public static final String RESOURCE_PATH = "chat/resource";

    @Inject
    private MessageDAO messageDAO;

    @GET
    @Path("/messages/{userid}/{roomid}")
    public Response getMessages(@PathParam("userid") long userid, @PathParam("roomid") long roomid){

        // TODO: need to tweak around with the limit of messages
        List<Message> messages = messageDAO.findMessagesByRoom(roomid);

        JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
        JsonArrayBuilder builder = Json.createArrayBuilder();
        for(int i = 0; i < messages.size(); i++){
            builder.add(i, messages.get(i).toJson());
        }
        objectBuilder.add("messages", builder);

        return Response.ok().entity(objectBuilder.build().toString()).build();
    }

    @DELETE
    @Path("/messages/{message_id}")
    public Response deleteMessage(@PathParam("message_id") long message_id){
        int deleted = messageDAO.deleteMessageById(message_id);
        return deleted == 1 ? Response.ok().entity(deleted).build() :
                Response.status(400).entity(deleted).build();
    }

    @PUT
    @Path("messages/{message_id}/{newContent}")
    public Response updateMessage(@PathParam("message_id") long message_id,
                                  @PathParam("newContent") String newContent){

        if(newContent == null){
            newContent = "";
        }

        Message message = messageDAO.findMessageById(message_id);
        message.setContent(newContent);

        return Response.ok().build();
    }
}

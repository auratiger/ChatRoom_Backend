package models;

import org.hibernate.annotations.CreationTimestamp;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonReader;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.StringReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

@Entity
@NamedQueries({
    @NamedQuery(name = "findMessageByUserAndRoom",
        query = "select m from Message m where m.user.id = :userid and m.room.id = :roomid"),
    @NamedQuery(name = "findMessagesByUser",
        query = "select m from Message m where m.user.id = :userid"),
    @NamedQuery(name="findMessagesByRoom",
        query = "select m from Message m where m.room.id = :roomid"),
    @NamedQuery(name="findLatestMessagesByRoom",
        query = "select m from Message m where m.room.id = :roomid order by m.timestamp desc"),
    @NamedQuery(name = "deleteMessageById",
        query = "delete from Message m where m.id = :message_id")
})
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Version
    private int version;

    @Size(min = 1)
    private String content;

    @ManyToOne
    @NotNull
    private User user;

    @ManyToOne
    @NotNull
    private Room room;

    @CreationTimestamp
    private LocalDateTime timestamp;

    public Message(){

    }

    public Message(String content){

    }

    public Message(@Size(min = 1) String content, @NotNull User user, @NotNull Room room) {
        this.content = content;
        this.user = user;
        this.room = room;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public JsonObject toJson(){
        JsonObjectBuilder builder = Json.createObjectBuilder();

        if(id != null){
            builder.add("id", id);
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        builder.add("text", content);
        builder.add("author", user.getUsername());
        builder.add("timestamp", formatter.format(timestamp));
        builder.add("roomid", room.getId());

        return builder.build();
    }

    public static Message fromJson(String jsonMessage){

        return null;
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj) return true;
        if(obj == null || getClass() != obj.getClass()) return false;

        Message message = (Message) obj;

        return Objects.equals(content, message.content) &&
                Objects.equals(user, message.user) &&
                Objects.equals(room, message.room) &&
                Objects.equals(timestamp, message.timestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(content, user, room, timestamp);
    }

    @Override
    public String toString() {
        String displayContent = content;
        if (content.length() > 30) {
            displayContent = content.substring(0, 29);
        }

        String author;
        if(user == null){
            author = "Unknown";
        }else{
            author = user.getUsername();
        }

        String roomName;
        if(room == null){
            roomName = "Unknown";
        }else {
            roomName = room.getName();
        }

        return "Message{" +
                "content='" + displayContent + '\'' +
                ", author=" + author +
                ", room=" + roomName +
                ", timestamp=" + timestamp +
                '}';
    }
}

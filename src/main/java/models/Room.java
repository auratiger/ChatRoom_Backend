package models;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.HashSet;
import java.util.Set;

@Entity
@NamedQueries({
        @NamedQuery(name="findRoomsByUserId",
            query = "select r from Room r join r.users u where u.id = :user_id"),
        @NamedQuery(name="findAllRooms",
            query = "select r from Room r")
})
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Size(min = 1, max = 25)
    private String name;
    
    private String image = "C:\\Users\\jboxers\\IdeaProjects\\ChatRoom_Backend\\src\\main\\resources\\userImages\\user_image.jpg";

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "USER_ROOM",
                joinColumns = {
                    @JoinColumn(name = "ROOM_ID")
                },
                inverseJoinColumns = {
                    @JoinColumn(name = "USER_ID")
                })
    private Set<User> users = new HashSet<>();

    @NotNull
    private LocalDateTime timestamp;

    public Room(){

    }

    public Room(String name, LocalDateTime timestamp) {
        this.name = name;
        this.timestamp = timestamp;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    public void addUser(User user){
        this.users.add(user);
    }

    public JsonObject toJson(){
        JsonObjectBuilder builder = Json.createObjectBuilder();
//        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();

        if(id != null){
            builder.add("id", id);
        }

        builder.add("name", name);
        builder.add("image", encodeImage(image));

//        for(User user : users){
//            arrayBuilder.add(user.toJson());
//        }
//        builder.add("users", arrayBuilder);

        return builder.build();
    }

    private String encodeImage(String imagePath) {
        String base64Image = "";
        File file = new File(imagePath);
        try (FileInputStream imageInFile = new FileInputStream(file)) {
            // Reading a Image file from file system
            byte imageData[] = new byte[(int) file.length()];
            imageInFile.read(imageData);
            base64Image = Base64.getEncoder().encodeToString(imageData);
        } catch (FileNotFoundException e) {
            System.out.println("Image not found" + e);
        } catch (IOException ioe) {
            System.out.println("Exception while reading the Image " + ioe);
        }
        return base64Image;
    }

    @Override
    public String toString() {
        return "Room{" +
                "id='" + id + '\'' +
                "name='" + name + '\'' +
                '}';
    }
}

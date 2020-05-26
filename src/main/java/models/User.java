package models;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(name = "Users")
@NamedQueries({
        @NamedQuery(name = "findUserByEmailAndPassword",
                query = "select u from User u where u.email = :email and u.password = :password"),
        @NamedQuery(name = "findUserByUsername",
                query = "select u from User u where u.username = :username"),
        @NamedQuery(name = "findUserByEmail",
                query = "select u from User u where u.email = :email")
})
@XmlRootElement
public abstract class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Version
    private int version;

    @Size(min = 1, max = 15)
    private String username;
    @Size(min = 1, max = 30)
    private String password;

//    @Size(min = 1, max = 30)
//    private String nickname;

    @Size(min = 1, max = 15)
    private String firstName;

    @Size(min = 1, max = 15)
    private String lastName;

    @Size(min = 1)
    private String email;

    private boolean emailVerified = false;

    @NotNull
    private LocalDateTime timestamp;

    @NotNull
    @OneToMany
    private List<User> friends = new ArrayList<>();

    @ManyToMany(mappedBy = "users")
    private Set<Room> rooms = new HashSet<>();

    private String userIcon = "C:\\Users\\jboxers\\IdeaProjects\\ChatRoom_Backend\\src\\main\\resources\\userImages\\user_image.jpg"; // saves the file path to the icon of the user

    public User() {
    }

    public User(String username, String password, String firstName, String lastName, String email, LocalDateTime timestamp){
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.timestamp = timestamp;
    }

    public User(String username, String password, String firstName, String lastName, String email, LocalDateTime timestamp, String userIcon){
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.timestamp = timestamp;
        this.userIcon = userIcon;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getUserIcon() {
        return userIcon;
    }

    public void setUserIcon(String userIcon) {
        this.userIcon = userIcon;
    }

    public Set<Room> getRooms() {
        return rooms;
    }

    public void setRooms(Set<Room> rooms) {
        this.rooms = rooms;
    }

    public boolean isEmailVerified() {
        return emailVerified;
    }

    public void setEmailVerified(boolean emailVerified) {
        this.emailVerified = emailVerified;
    }

    public List<User> getFriends() {
        return friends;
    }

    public void setFriends(List<User> friends) {
        this.friends = friends;
    }

    public void addFriend(User user){
        this.friends.add(user);
    }

    public JsonObject toJson(){
        JsonObjectBuilder builder = Json.createObjectBuilder();
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();

        if(id != null){
            builder.add("id", id);
        }

        builder.add("username", username).
                add("userIcon", encodeImage(userIcon)).
                add("email", email);

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

    public static <T extends User> T fromJson(JsonObject newUser, T instance){
        instance.setUsername(newUser.getString("username"));
        instance.setPassword(newUser.getString("password"));
        instance.setFirstName(newUser.getString("firstName"));
        instance.setLastName(newUser.getString("lastName"));
        instance.setEmail(newUser.getString("email"));
        instance.setTimestamp(LocalDateTime.now());
        // this should be changed to add a blob of the picture which is stored
        // on the server the the specified path {userIcon}
//        instance.setUserIcon(newUser.getString("userIcon"));
        return instance;
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, password, firstName, lastName, email);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if(obj == null || getClass() != obj.getClass()) return false;

        User user = (User)obj;

        return Objects.equals(username, user.username) &&
                Objects.equals(password, user.password) &&
                Objects.equals(firstName, user.firstName) &&
                Objects.equals(lastName, user.lastName) &&
                Objects.equals(email, user.email);
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                "userName='" + username + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}

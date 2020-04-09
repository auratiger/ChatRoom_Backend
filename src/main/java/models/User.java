package models;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import java.time.LocalDateTime;
import java.util.Objects;

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

    private String username;
    private String password;

    private String firstName;
    private String lastName;

    private String email;

    @NotNull
    private LocalDateTime created;

    private String userIcon; // saves the file path to the icon of the user

    public User(){

    }

    public User(String username, String password, String firstName, String lastName, String email, LocalDateTime created, String userIcon) {
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.created = created;
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

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public String getUserIcon() {
        return userIcon;
    }

    public void setUserIcon(String userIcon) {
        this.userIcon = userIcon;
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
                "userName='" + username + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}

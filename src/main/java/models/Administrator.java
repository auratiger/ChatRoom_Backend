package models;

import javax.persistence.Entity;
import java.time.LocalDateTime;

@Entity
public class Administrator extends User {

    public Administrator(){

    }

    public Administrator(String username, String password, String firstName, String lastName, String email, LocalDateTime created, String userIcon) {
        super(username, password, firstName, lastName, email, created, userIcon);
    }

    @Override
    public String toString() {
        return "Administrator{" +
                "userName='" + getUsername() + '\'' +
                ", firstName='" + getFirstName() + '\'' +
                ", lastName='" + getLastName() + '\'' +
                ", email='" + getEmail() + '\'' +
                '}';
    }
}

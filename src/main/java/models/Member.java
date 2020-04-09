package models;

import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import java.time.LocalDateTime;

@Entity
public class Member extends User {

    public Member(){

    }

    public Member(String username, String password, String firstName, String lastName, String email, LocalDateTime created, String userIcon) {
        super(username, password, firstName, lastName, email, created, userIcon);
    }

    @Override
    public String toString() {
        return "Member{" +
                "userName='" + getUsername() + '\'' +
                ", firstName='" + getFirstName() + '\'' +
                ", lastName='" + getLastName() + '\'' +
                ", email='" + getEmail() + '\'' +
                '}';
    }
}

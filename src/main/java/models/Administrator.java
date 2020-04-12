package models;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.persistence.Entity;
import java.io.StringReader;
import java.time.LocalDateTime;

@Entity
public class Administrator extends User {

    public Administrator(){

    }

    public Administrator(String username, String password, String firstName, String lastName, String email, LocalDateTime created, String userIcon) {
        super(username, password, firstName, lastName, email, created, userIcon);
    }

    public static Administrator fromJson(String administratorJson){
        JsonReader reader = Json.createReader(new StringReader(administratorJson));
        JsonObject object = reader.readObject();
        Administrator administrator = User.fromJson(object, new Administrator());

        return administrator;
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

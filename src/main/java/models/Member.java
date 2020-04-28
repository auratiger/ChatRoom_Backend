package models;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.persistence.Entity;
import java.io.StringReader;
import java.time.LocalDateTime;

@Entity
public class Member extends User {

    public Member(){

    }

    public Member(String username, String password, String firstName, String lastName, String email, LocalDateTime created, String userIcon) {
        super(username, password, firstName, lastName, email, created, userIcon);
    }

    public static Member fromJson(String memberJson){
        JsonReader reader = Json.createReader(new StringReader(memberJson));
        JsonObject object = reader.readObject();
        Member member = User.fromJson(object, new Member());

        return member;
    }

    @Override
    public String toString() {
        return "Member{" +
                "id='" + getId() + '\'' +
                "userName='" + getUsername() + '\'' +
                ", firstName='" + getFirstName() + '\'' +
                ", lastName='" + getLastName() + '\'' +
                ", email='" + getEmail() + '\'' +
                '}';
    }
}

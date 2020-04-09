package models;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
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

    @NotNull
    private LocalDateTime created;

    public Message(){

    }

    public Message(@Size(min = 1) String content, @NotNull User user, @NotNull Room room, @NotNull LocalDateTime created) {
        this.content = content;
        this.user = user;
        this.room = room;
        this.created = created;
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

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj) return true;
        if(obj == null || getClass() != obj.getClass()) return false;

        Message message = (Message) obj;

        return Objects.equals(content, message.content) &&
                Objects.equals(user, message.user) &&
                Objects.equals(room, message.room) &&
                Objects.equals(created, message.created);
    }

    @Override
    public int hashCode() {
        return Objects.hash(content, user, room, created);
    }

    @Override
    public String toString() {
        String displayContent = content;
        if (content.length() > 30) {
            displayContent = content.substring(0, 29);
        }
        return "Message{" +
                "content='" + displayContent + '\'' +
                ", author=" + user.getUsername() +
                ", room=" + room.getName() +
                ", created=" + created +
                '}';
    }
}

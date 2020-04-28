package admin;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

@Singleton
@Startup
public class EmailClient {

    private Properties cred;
    private Properties props;
    private Session session;

    private String from ;
    private String password;

    private final String host = "smtp.gmail.com";

    @PostConstruct
    private void construct(){
        props = new Properties();

        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.ssl.trust", "smtp.gmail.com");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", "587");

        cred = new Properties();
        try {
            cred.load(new FileInputStream(
                    "C:\\Users\\jboxers\\IdeaProjects\\ChatRoom_Backend\\src\\main\\resources\\application.properties"
            ));
        }catch (IOException e){
            e.printStackTrace();
        }

        from = cred.getProperty("email.address");
        password = cred.getProperty("email.password");
    }

    private Message createMessage(String to, Long id){
        session = Session.getInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(from, password);
            }
        });

        Message message = new MimeMessage(session);

        try {
            message.setFrom(new InternetAddress(from));

            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));

            message.setSubject("Testing Subject");

            message.setContent(
                    "<div style='height:100%; border:1px solid black; text-align:center;'>" +
                            "<h1>This is actual message embedded in HTML tags</h1>" +
                            "<a href='http://localhost:3000/email_verification/verify/" + to + id + " target='_blank'>" +
                            "<h2>click here to verify your email</h2>" +
                            "</a>" +
                            "</div>",
                    "text/html");

        } catch (Exception e) {
            e.printStackTrace();
        }

        return message;
    }

    public void sendMessage(String to, Long id){
        try {
            Message message = createMessage(to, id);
            Transport.send(message);
            System.out.println("sent");
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}












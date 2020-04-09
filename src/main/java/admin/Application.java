package admin;

import javax.ws.rs.ApplicationPath;

@ApplicationPath(Application.APPLICATION_PATH)
public class Application extends javax.ws.rs.core.Application {
    public static final String APPLICATION_PATH = "/rest";
}

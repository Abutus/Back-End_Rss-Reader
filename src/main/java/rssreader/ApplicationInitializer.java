package rssreader;

import org.glassfish.jersey.server.ResourceConfig;
import javax.ws.rs.ApplicationPath;

@ApplicationPath("/")
public class ApplicationInitializer extends ResourceConfig{

    public ApplicationInitializer(){
        packages("rssreader");
        register(AuthenticationFilter.class);
    }
}

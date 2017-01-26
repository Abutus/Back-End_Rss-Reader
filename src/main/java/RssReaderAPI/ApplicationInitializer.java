package RssReaderAPI;

import org.glassfish.jersey.server.ResourceConfig;
import javax.ws.rs.ApplicationPath;

@ApplicationPath("/")
public class ApplicationInitializer extends ResourceConfig{

    public ApplicationInitializer(){
        packages("RssReaderAPI");
    }
}

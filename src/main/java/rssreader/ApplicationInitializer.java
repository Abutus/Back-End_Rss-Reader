package rssreader;

import org.glassfish.jersey.server.ResourceConfig;
import rssreader.entity.RssFeedEntity;
import rssreader.factory.DaoFactory;

import javax.ws.rs.ApplicationPath;
import java.time.Clock;
import java.time.Instant;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@ApplicationPath("/")
public class ApplicationInitializer extends ResourceConfig{

    public ApplicationInitializer(){
        packages("rssreader");
        register(AuthenticationFilter.class);

        ScheduledExecutorService scheduledService = Executors.newSingleThreadScheduledExecutor();
        scheduledService.scheduleWithFixedDelay(() -> {
            ExecutorService executorService = Executors.newCachedThreadPool();
            List<RssFeedEntity> rssFeedEntities = DaoFactory.getRssFeedDAO().getAllFeeds();
            Instant now = Clock.systemDefaultZone().instant();
            rssFeedEntities.forEach(rssFeed ->
                {if(rssFeed.getLastUpdate() == null || (now.getEpochSecond() - rssFeed.getLastUpdate().getEpochSecond() > 600))
                        executorService.submit(new RssNewsUpdater(rssFeed));
                });
            try {
                executorService.awaitTermination(5, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, 0, 5, TimeUnit.SECONDS);
    }
}

package rssreader.operations;

import org.hibernate.Session;
import rssreader.entity.RssFeedEntity;

import java.util.List;

public class AddListOperation extends DbOperation{
    private static final int SESSION_BUFFER_SIZE  = 200;
    private List<RssFeedEntity> rssFeeds;

    public AddListOperation(List<RssFeedEntity> rssFeeds){
        this.rssFeeds = rssFeeds;
    }

    @Override
    void performInTransaction(Session session) {
        int i = 0;
        for (RssFeedEntity rssFeed : rssFeeds){
            session.save(rssFeed);
            ++i;
            if(i % SESSION_BUFFER_SIZE == 0){
                session.flush();
                session.clear();
            }
        }
    }
}

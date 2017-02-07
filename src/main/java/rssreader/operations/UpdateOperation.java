package rssreader.operations;

import org.hibernate.Session;
import rssreader.entity.RssFeedEntity;

public class UpdateOperation extends DbOperation{
    private RssFeedEntity rssFeed;

    public UpdateOperation(RssFeedEntity rssFeed)
    {
        this.rssFeed = rssFeed;
    }

    @Override
    void performInTransaction(Session session)
    {
        session.update(rssFeed);
    }
}

package rssreader.operations;

import org.hibernate.Session;
import rssreader.entity.RssFeedEntity;

public class AddOperation extends DbOperation{
    private RssFeedEntity rssFeed;

    public AddOperation(RssFeedEntity rssFeed)
    {
        this.rssFeed = rssFeed;
    }

    @Override
    void performInTransaction(Session session)
    {
        session.save(rssFeed);
    }
}

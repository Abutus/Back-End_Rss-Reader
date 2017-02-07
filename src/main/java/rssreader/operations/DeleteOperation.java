package rssreader.operations;

import org.hibernate.Session;

public class DeleteOperation extends DbOperation{
    private long id;

    public DeleteOperation(long id)
    {
        this.id = id;
    }

    @Override
    void performInTransaction(Session session)
    {
        session.createQuery("DELETE FROM RssFeedEntity " +
                "WHERE feed_id = :feedId")
                .setParameter("feedId", id).executeUpdate();
    }
}

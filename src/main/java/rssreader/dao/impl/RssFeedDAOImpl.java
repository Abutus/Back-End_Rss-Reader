package rssreader.dao.impl;

import rssreader.dao.RssFeedDAO;
import rssreader.entity.RssFeedEntity;
import rssreader.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.query.Query;
import java.util.List;
import java.util.Optional;

public class RssFeedDAOImpl implements RssFeedDAO {

    @Override
    public List<RssFeedEntity> getAllFeeds() {
        List<RssFeedEntity> rssFeeds;
        try(Session session = HibernateUtil.getSessionFactory().openSession()){
            Query query = session.createQuery("FROM RssFeedEntity");
            rssFeeds = query.list();
        }
        return rssFeeds;
    }

    @Override
    public List<RssFeedEntity> getFeedsPage(int start, int end) {
        List<RssFeedEntity> rssFeeds;
        try(Session session = HibernateUtil.getSessionFactory().openSession()){
            Query query = session.createQuery("FROM RssFeedEntity WHERE rownum <= :endRow")
                    .setParameter("endRow", end)
                    .setFirstResult(start);
            rssFeeds = query.list();
        }
        return rssFeeds;
    }

    @Override
    public List<RssFeedEntity> getFeedsByTitle(String title){
        List<RssFeedEntity> rssFeeds;
        try(Session session = HibernateUtil.getSessionFactory().openSession()){
            Query query = session.createQuery("FROM RssFeedEntity " +
                    "WHERE lower(feed_title) LIKE lower(:searchTitle) " +
                    "ORDER BY feed_title ASC")
                    .setParameter("searchTitle", '%' + title + '%');
            rssFeeds = query.list();
        }
        return rssFeeds;
    }

    @Override
    public List<RssFeedEntity> getFeedsPageByTitle(String title, int start, int end) {
        List<RssFeedEntity> rssFeeds;
        try(Session session = HibernateUtil.getSessionFactory().openSession()){
            Query query = session.createQuery("FROM RssFeedEntity " +
                    "WHERE lower(feed_title) LIKE lower( :searchTitle) AND rownum <= :endRow " +
                    "ORDER BY feed_title ASC")
                    .setParameter("searchTitle", '%' + title + '%')
                    .setInteger("endRow", end)
                    .setFirstResult(start);
            rssFeeds = query.list();
        }
        return rssFeeds;
    }

    @Override
    public Optional<RssFeedEntity> getFeedById(long id) {
        try(Session session = HibernateUtil.getSessionFactory().openSession()){
            return Optional.ofNullable(session.get(RssFeedEntity.class, id));
        }
    }

    @Override
    public void addFeed(RssFeedEntity rssFeed) {
        DbOperation addOperation = new AddOperation(rssFeed);
        addOperation.exec();
    }

    @Override
    public void addFeeds(List<RssFeedEntity> rssFeeds){
        DbOperation addListOperation = new AddListOperation(rssFeeds);
        addListOperation.exec();
    }

    @Override
    public void updateFeed(RssFeedEntity rssFeed){
        DbOperation updateOperation = new UpdateOperation(rssFeed);
        updateOperation.exec();
    }

    @Override
    public void deleteFeedById(long id){
        DbOperation deleteOperation = new DeleteOperation(id);
        deleteOperation.exec();
    }

    abstract class DbOperation{
        void exec() {
            try(Session session = HibernateUtil.getSessionFactory().openSession())
            {
                session.beginTransaction();
                performInTransaction(session);
                session.getTransaction().commit();
            }
        }

        abstract void performInTransaction(Session session);
    }

    class AddOperation extends DbOperation{
        private RssFeedEntity rssFeed;

        AddOperation(RssFeedEntity rssFeed)
        {
            this.rssFeed = rssFeed;
        }

        @Override
        void performInTransaction(Session session)
        {
            session.save(rssFeed);
        }
    }

    class AddListOperation extends DbOperation{
        private List<RssFeedEntity> rssFeeds;

        AddListOperation(List<RssFeedEntity> rssFeeds){
            this.rssFeeds = rssFeeds;
        }

        @Override
        void performInTransaction(Session session) {
            int i = 0;
            for (RssFeedEntity rssFeed : rssFeeds){
                session.save(rssFeed);
                ++i;
                if(i % 20 == 0){
                    session.flush();
                    session.clear();
                }
            }
        }
    }

    class UpdateOperation extends DbOperation {
        private RssFeedEntity rssFeed;

        UpdateOperation(RssFeedEntity rssFeed)
        {
            this.rssFeed = rssFeed;
        }

        @Override
        void performInTransaction(Session session)
        {
            session.update(rssFeed);
        }
    }

    class DeleteOperation extends DbOperation {
        private long id;

        DeleteOperation(long id)
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
}

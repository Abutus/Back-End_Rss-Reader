package rssreader.dao.impl;

import rssreader.dao.RssFeedDAO;
import rssreader.entity.RssFeedEntity;
import rssreader.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.math.BigDecimal;
import java.util.ArrayList;
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
            Query query = session.createNativeQuery("SELECT feedId, feedTitle, feedLink " +
                    "FROM (SELECT ROWNUM rnum, feeds.FEED_ID as feedId, feeds.FEED_TITLE as feedTitle, feeds.FEED_LINK as feedLink " +
                    "FROM RSSREADER.RSSFEEDS feeds " +
                    "WHERE ROWNUM <= " + end + ") " +
                    "WHERE rnum >= " + start);
            rssFeeds = ConvertObjectList(query.list());
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
            Query query = session.createNativeQuery("SELECT feedId, feedTitle, feedLink " +
                    "FROM (SELECT ROWNUM rnum, feeds.FEED_ID as feedId, feeds.FEED_TITLE as feedTitle, feeds.FEED_LINK as feedLink " +
                        "FROM RSSREADER.RSSFEEDS feeds " +
                        "WHERE ROWNUM <= " + end + ") " +
                    "WHERE rnum >= " + start + " AND lower(feedTitle) LIKE lower('%" + title + "%') " +
                    "ORDER BY feedTitle ASC");
            rssFeeds = ConvertObjectList(query.list());
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

    private List<RssFeedEntity> ConvertObjectList(List objectList){
        List<RssFeedEntity> rssFeeds = new ArrayList<>();
        for (Object obj : objectList){
            Object[] entity = (Object[]) obj;
            RssFeedEntity rssFeed = new RssFeedEntity();
            BigDecimal feedId = (BigDecimal) entity[0];
            rssFeed.setId(feedId.longValue());
            rssFeed.setTitle((String)entity[1]);
            rssFeed.setLink((String)entity[2]);
            rssFeeds.add(rssFeed);
        }
        return rssFeeds;
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

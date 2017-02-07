package rssreader.dao.impl;

import rssreader.dao.RssFeedDAO;
import rssreader.entity.RssFeedEntity;
import rssreader.operations.*;
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
                        "WHERE ROWNUM <= :end) " +
                    "WHERE rnum >= :start")
                    .setParameter("end", end)
                    .setParameter("start", start);
            rssFeeds = createRssFeedsFromSqlQueryResult(query.list());
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
                        "WHERE ROWNUM <= :end) " +
                    "WHERE rnum >= :start AND lower(feedTitle) LIKE lower( :title) " +
                    "ORDER BY feedTitle ASC")
                    .setParameter("end", end)
                    .setParameter("start", start)
                    .setParameter("title", '%' + title + '%');
            rssFeeds = createRssFeedsFromSqlQueryResult(query.list());
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
        addOperation.execute();
    }

    @Override
    public void addFeeds(List<RssFeedEntity> rssFeeds){
        DbOperation addListOperation = new AddListOperation(rssFeeds);
        addListOperation.execute();
    }

    @Override
    public void updateFeed(RssFeedEntity rssFeed){
        DbOperation updateOperation = new UpdateOperation(rssFeed);
        updateOperation.execute();
    }

    @Override
    public void deleteFeedById(long id){
        DbOperation deleteOperation = new DeleteOperation(id);
        deleteOperation.execute();
    }

    private List<RssFeedEntity> createRssFeedsFromSqlQueryResult(List objectList){
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
}

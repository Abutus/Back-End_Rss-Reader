package RssReaderAPI.DAO.Impl;

import RssReaderAPI.DAO.RssFeedDAO;
import RssReaderAPI.DTO.RssFeedDto;
import RssReaderAPI.Util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.query.Query;
import java.sql.SQLException;
import java.util.List;

public class RssFeedDAOImpl implements RssFeedDAO{
    @Override
    public void addFeed(RssFeedDto rssFeedDto)  throws SQLException{
        Session session = null;
        try{
            session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            session.save(rssFeedDto);
            session.getTransaction().commit();
        } finally {
            if(session != null && session.isOpen())
            {
                session.close();
            }
        }
    }

    @Override
    public RssFeedDto getFeedById(long id) throws SQLException {
        Session session = null;
        RssFeedDto feed;
        try{
            session = HibernateUtil.getSessionFactory().openSession();
            feed = session.get(RssFeedDto.class, id);
        } finally {
            if(session != null && session.isOpen())
            {
                session.close();
            }
        }
        return feed;
    }

    @Override
    public List<RssFeedDto> getFeedsByTitle(String title, long start, long end) throws SQLException {
        Session session = null;
        List<RssFeedDto> rssFeeds;
        try{
            session = HibernateUtil.getSessionFactory().openSession();
            Query query = session.createQuery("FROM RssFeedDto " +
                    "WHERE feed_id > :minId AND feed_id < :maxId AND lower(feed_title) LIKE :searchTitle " +
                    "ORDER BY feed_title ASC")
                    .setParameter("minId", start - 1)
                    .setParameter("maxId", end + 1)
                    .setParameter("searchTitle", '%' + title.toLowerCase() + '%');
            rssFeeds = query.list();
        } finally {
            if(session != null && session.isOpen())
            {
                session.close();
            }
        }
        return rssFeeds;
    }

    @Override
    public void updateFeedById(long id, RssFeedDto newRssFeed)  throws SQLException{
        Session session = null;
        try{
            session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            session.createQuery("UPDATE RssFeedDto " +
                    "SET feed_title = :newTitle, feed_link = :newLink " +
                    "WHERE feed_id = :id")
                    .setParameter("newTitle", newRssFeed.getTitle())
                    .setParameter("newLink", newRssFeed.getLink())
                    .setParameter("id", id).executeUpdate();
            session.getTransaction().commit();
        } catch (Exception ex) {
            throw new SQLException(ex.getMessage());
        } finally {
            if(session != null && session.isOpen())
            {
                session.close();
            }
        }
    }

    @Override
    public void deleteFeedById(long id)  throws SQLException{
        Session session = null;
        try{
            session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            session.createQuery("DELETE FROM RssFeedDto " +
                    "WHERE feed_id = :feedId")
                    .setParameter("feedId", id).executeUpdate();
            session.getTransaction().commit();
        } finally {
            if(session != null && session.isOpen())
            {
                session.close();
            }
        }
    }
}

package RssReaderAPI.DAO.Impl;

import RssReaderAPI.DAO.RssFeedDAO;
import RssReaderAPI.DTO.RssFeedDto;
import RssReaderAPI.Util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.query.Query;
import java.util.List;

public class RssFeedDAOImpl implements RssFeedDAO {

    @Override
    public List<RssFeedDto> getAllFeeds() {
        Session session = null;
        List<RssFeedDto> rssFeeds;
        try{
            session = HibernateUtil.getSessionFactory().openSession();
            rssFeeds = session.createQuery("FROM RssFeedDto").list();
        } finally {
            if(session != null && session.isOpen())
            {
                session.close();//посмотреть интерфейс closeble
            }
        }
        return rssFeeds;
    }

    @Override
    public List<RssFeedDto> getFeedsByTitle(String title){//exception
        Session session = null;
        List<RssFeedDto> rssFeeds;
        try{
            session = HibernateUtil.getSessionFactory().openSession();
            Query query = session.createQuery("FROM RssFeedDto " +
                    "WHERE lower(feed_title) LIKE :searchTitle " +
                    "ORDER BY feed_title ASC")
                    .setParameter("searchTitle", '%' + title.toLowerCase() + '%');
            rssFeeds = query.list();
        } finally {
            if(session != null && session.isOpen())
            {
                session.close();//посмотреть интерфейс closeble
            }
        }
        return rssFeeds;
    }

    @Override
    public RssFeedDto getFeedById(long id) {
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
    public void addFeed(RssFeedDto rssFeedDto) {
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
    public void updateFeedById(long id, RssFeedDto newRssFeed){
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
        } finally {
            if(session != null && session.isOpen())
            {
                session.close();
            }
        }
    }

    @Override
    public void deleteFeedById(long id){
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

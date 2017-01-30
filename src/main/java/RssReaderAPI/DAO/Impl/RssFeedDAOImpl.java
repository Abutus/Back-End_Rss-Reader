package RssReaderAPI.DAO.Impl;

import RssReaderAPI.DAO.RssFeedDAO;
import RssReaderAPI.DTO.RssFeedDto;
import RssReaderAPI.Util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;
import java.util.Optional;

public class RssFeedDAOImpl implements RssFeedDAO {

    @Override
    public Optional<List<RssFeedDto>> getAllFeeds() {
        Optional<List<RssFeedDto>> rssFeeds;
        try(Session session = HibernateUtil.getSessionFactory().openSession()){
            rssFeeds = Optional.ofNullable(session.createQuery("FROM RssFeedDto").list());
        }
        return rssFeeds;
    }

    @Override
    public Optional<List<RssFeedDto>> getFeedsByTitle(String title){
        Optional<List<RssFeedDto>> rssFeeds;
        try(Session session = HibernateUtil.getSessionFactory().openSession()){
            Query query = session.createQuery("FROM RssFeedDto " +
                    "WHERE lower(feed_title) LIKE :searchTitle " +
                    "ORDER BY feed_title ASC")
                    .setParameter("searchTitle", '%' + title.toLowerCase() + '%');
            rssFeeds = Optional.ofNullable(query.list());
        }
        return rssFeeds;
    }

    @Override
    public Optional<RssFeedDto> getFeedById(long id) {
        Optional<RssFeedDto> feed;
        try(Session session = HibernateUtil.getSessionFactory().openSession()){
            feed = Optional.ofNullable(session.get(RssFeedDto.class, id));
        }
        return feed;
    }

    @Override
    public void addFeed(RssFeedDto rssFeedDto) {
        try(Session session = HibernateUtil.getSessionFactory().openSession()){
            session.beginTransaction();
            session.save(rssFeedDto);
            session.getTransaction().commit();
        }
    }

    @Override
    public void updateFeedById(long id, RssFeedDto newRssFeed){
        try(Session session = HibernateUtil.getSessionFactory().openSession()){
            session.beginTransaction();
            session.createQuery("UPDATE RssFeedDto " +
                    "SET feed_title = :newTitle, feed_link = :newLink " +
                    "WHERE feed_id = :id")
                    .setParameter("newTitle", newRssFeed.getTitle())
                    .setParameter("newLink", newRssFeed.getLink())
                    .setParameter("id", id).executeUpdate();
            session.getTransaction().commit();
        }
    }

    @Override
    public void deleteFeedById(long id){
        try(Session session = HibernateUtil.getSessionFactory().openSession()){
            session.beginTransaction();
            session.createQuery("DELETE FROM RssFeedDto " +
                    "WHERE feed_id = :feedId")
                    .setParameter("feedId", id).executeUpdate();
            session.getTransaction().commit();
        }
    }
}

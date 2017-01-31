package rssreader.dao.impl;

import rssreader.dao.RssFeedDAO;
import rssreader.dto.RssFeedDto;
import rssreader.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.query.Query;
import java.util.List;
import java.util.Optional;

public class RssFeedDAOImpl implements RssFeedDAO {

    @Override
    public List<RssFeedDto> getAllFeeds() {
        List<RssFeedDto> rssFeeds;
        try(Session session = HibernateUtil.getSessionFactory().openSession()){
            rssFeeds = session.createQuery("FROM RssFeedDto").list();
        }
        return rssFeeds;
    }

    @Override
    public List<RssFeedDto> getFeedsPage(int start, int end) {
        List<RssFeedDto> rssFeeds;
        try(Session session = HibernateUtil.getSessionFactory().openSession()){
            Query query = session.createQuery("FROM RssFeedDto WHERE rownum <= :endRow")
                    .setInteger("endRow", end)
                    .setFirstResult(start);
            rssFeeds = query.list();
        }
        return rssFeeds;
    }

    @Override
    public List<RssFeedDto> getFeedsByTitle(String title){
        List<RssFeedDto> rssFeeds;
        try(Session session = HibernateUtil.getSessionFactory().openSession()){
            Query query = session.createQuery("FROM RssFeedDto " +
                    "WHERE lower(feed_title) LIKE lower(:searchTitle) " +
                    "ORDER BY feed_title ASC")
                    .setParameter("searchTitle", '%' + title + '%');
            rssFeeds = query.list();
        }
        return rssFeeds;
    }

    @Override
    public List<RssFeedDto> getFeedsPageByTitle(String title, int start, int end) {
        List<RssFeedDto> rssFeeds;
        try(Session session = HibernateUtil.getSessionFactory().openSession()){
            Query query = session.createQuery("FROM RssFeedDto " +
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
    public Optional<RssFeedDto> getFeedById(long id) {
        try(Session session = HibernateUtil.getSessionFactory().openSession()){
            return Optional.ofNullable(session.get(RssFeedDto.class, id));
        }
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
    public void updateFeed(RssFeedDto rssFeed){
        try(Session session = HibernateUtil.getSessionFactory().openSession()){
            session.beginTransaction();
            session.update(rssFeed);
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

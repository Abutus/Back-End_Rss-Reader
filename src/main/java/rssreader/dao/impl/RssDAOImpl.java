package rssreader.dao.impl;

import org.hibernate.Session;
import org.hibernate.query.Query;
import rssreader.dao.RssDAO;
import rssreader.entity.RssEntity;
import rssreader.util.HibernateUtil;

import java.util.List;
import java.util.Optional;

public class RssDAOImpl implements RssDAO{

    @Override
    public List<RssEntity> getAllNews(long feedId) {
        List<RssEntity> news;
        try(Session session = HibernateUtil.getSessionFactory().openSession()){
            Query query = session.createQuery("FROM RssEntity WHERE feed_id = :feedId")
                    .setParameter("feedId", feedId);
            news = query.list();
        }
        return news;
    }

    @Override
    public List<RssEntity> getNewsPage(long feedId, int start, int end) {
        return null;
    }

    @Override
    public Optional<RssEntity> getNewsById(long id) {
        try(Session session = HibernateUtil.getSessionFactory().openSession()){
            return Optional.ofNullable(session.get(RssEntity.class, id));
        }
    }
}

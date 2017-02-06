package rssreader.dao.impl;

import org.hibernate.Session;
import org.hibernate.query.Query;
import rssreader.dao.RssDAO;
import rssreader.entity.RssEntity;
import rssreader.util.HibernateUtil;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
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
        List<RssEntity> news;
        try(Session session = HibernateUtil.getSessionFactory().openSession()){
            Query query = session.createNativeQuery("SELECT newsId, newsTitle, newsLink, newsDescription, newsGuid, newsPubDate " +
                    "FROM (SELECT ROWNUM rnum, news.NEWS_ID as newsId, news.NEWS_TITLE as newsTitle, news.NEWS_LINK as newsLink, news.NEWS_DESCRIPTION as newsDescription, news.NEWS_GUID as newsGuid, news.NEWS_PUBDATE as newsPubDate " +
                    "FROM (SELECT * FROM RSSREADER.NEWS WHERE FEED_ID = " + feedId + ") news " +
                    "WHERE ROWNUM <= " + end + ") " +
                    "WHERE rnum >= " + start);
            news = ConvertObjectList(query.list());
        }
        return news;
    }

    @Override
    public Optional<RssEntity> getNewsById(long id) {
        try(Session session = HibernateUtil.getSessionFactory().openSession()){
            return Optional.ofNullable(session.get(RssEntity.class, id));
        }
    }

    private List<RssEntity> ConvertObjectList(List objectList){
        List<RssEntity> rssFeeds = new ArrayList<>();
        for (Object obj : objectList){
            Object[] entity = (Object[]) obj;
            RssEntity news = new RssEntity();
            BigDecimal newsId = (BigDecimal) entity[0];
            news.setId(newsId.longValue());
            news.setTitle((String)entity[1]);
            news.setLink((String)entity[2]);
            news.setDescription((String)entity[3]);
            news.setGuid((String)entity[4]);
            Timestamp pubDate = (Timestamp) entity[5];
            news.setPubDate(pubDate.toInstant());
            rssFeeds.add(news);
        }
        return rssFeeds;
    }
}

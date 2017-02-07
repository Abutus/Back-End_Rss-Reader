package rssreader.dao.impl;

import org.hibernate.Session;
import org.hibernate.query.Query;
import rssreader.dao.RssNewsDAO;
import rssreader.entity.RssNewsItemEntity;
import rssreader.util.HibernateUtil;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RssNewsDAOImpl implements RssNewsDAO {

    @Override
    public List<RssNewsItemEntity> getAllNews(long feedId) {
        List<RssNewsItemEntity> news;
        try(Session session = HibernateUtil.getSessionFactory().openSession()){
            Query query = session.createQuery("FROM RssNewsItemEntity WHERE feed_id = :feedId")
                    .setParameter("feedId", feedId);
            news = query.list();
        }
        return news;
    }

    @Override
    public List<RssNewsItemEntity> getNewsPage(long feedId, int start, int end) {
        List<RssNewsItemEntity> news;
        try(Session session = HibernateUtil.getSessionFactory().openSession()){
            Query query = session.createNativeQuery("SELECT newsId, newsTitle, newsLink, newsDescription, newsGuid, newsPubDate " +
                    "FROM (SELECT ROWNUM rnum, news.NEWS_ID as newsId, news.NEWS_TITLE as newsTitle, news.NEWS_LINK as newsLink, news.NEWS_DESCRIPTION as newsDescription, news.NEWS_GUID as newsGuid, news.NEWS_PUBDATE as newsPubDate " +
                        "FROM (SELECT * FROM RSSREADER.NEWS WHERE FEED_ID = :feedId) news " +
                        "WHERE ROWNUM <= :end) " +
                    "WHERE rnum >= :start")
                    .setParameter("feedId", feedId)
                    .setParameter("end", end)
                    .setParameter("start", start);
            news = createRssNewsFromSqlQueryResult(query.list());
        }
        return news;
    }

    @Override
    public Optional<RssNewsItemEntity> getNewsById(long id) {
        try(Session session = HibernateUtil.getSessionFactory().openSession()){
            return Optional.ofNullable(session.get(RssNewsItemEntity.class, id));
        }
    }

    private List<RssNewsItemEntity> createRssNewsFromSqlQueryResult(List objectList){
        List<RssNewsItemEntity> rssFeeds = new ArrayList<>();
        for (Object obj : objectList){
            Object[] entity = (Object[]) obj;
            RssNewsItemEntity news = new RssNewsItemEntity();
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

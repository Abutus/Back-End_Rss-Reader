import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import rssreader.dao.RssNewsDAO;
import rssreader.dao.impl.RssFeedDAOImpl;
import rssreader.dao.impl.RssNewsDAOImpl;
import rssreader.entity.RssFeedEntity;
import rssreader.entity.RssNewsItemEntity;

import java.util.List;
import java.util.Optional;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;

public class RssNewsDAOImplTests {
    private RssNewsDAO dao;
    private RssFeedEntity feed;

    @Before
    public void createDAO(){
        this.dao = new RssNewsDAOImpl();
        feed = new RssFeedEntity();
        feed.setId(27);
        feed.setTitle("NewTestFeed");
        feed.setLink("https://www.gazeta.ru/export/rss/lastnews.xml");
        new RssFeedDAOImpl().addFeed(feed);
    }

    @Test
    public void testGetAllFeeds(){
        List<RssNewsItemEntity> entityList = dao.getAllNews(feed.getId());
        assertNotNull("Should return list", entityList);
        entityList.forEach(entity -> assertEquals("Should be equals", entity.getRssFeed(), feed));
    }

    @Test
    public void testGetNewsPage(){
        int start = 2;
        int end = 5;
        List<RssNewsItemEntity> entityList = dao.getNewsPage(feed.getId(), start, end);
        assertNotNull("Should return list", entityList);
        assertEquals("Count of news should be correct", true, entityList.size() <= (end - start + 1));
        entityList.forEach(entity -> assertEquals("Should be equals", entity.getRssFeed(), feed));
    }

    @Test
    public void testGetNewsById(){
        int id = 27;
        RssNewsItemEntity newsItemEntity = new RssNewsItemEntity();
        newsItemEntity.setId(id);
        newsItemEntity.setTitle("new news item");
        feed.addNews(newsItemEntity);
        new RssFeedDAOImpl().updateFeed(feed);

        Optional<RssNewsItemEntity> entityFromDB = dao.getNewsById(id);
        Assert.assertEquals("Should return entity", true, entityFromDB.isPresent());
        Assert.assertEquals("Id should be equals", newsItemEntity.getId(), entityFromDB.get().getId());
        Assert.assertEquals("Title should be equals", newsItemEntity.getTitle(), entityFromDB.get().getTitle());
    }

    @Test
    public void testDeleteNewsByFeedId(){
        dao.deleteNewsByFeedId(feed.getId());
        List<RssNewsItemEntity> entityList = dao.getAllNews(feed.getId());
        assertNotNull("Should return list", entityList);
        assertEquals("Count of news should be 0", entityList.size(), 0);
    }
}

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import rssreader.dao.RssFeedDAO;
import rssreader.dao.impl.RssFeedDAOImpl;
import rssreader.entity.RssFeedEntity;

import java.util.List;
import java.util.Optional;

import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertEquals;

public class RssFeedDAOImplTests {
    private RssFeedDAO dao;

    @Before
    public void createDAO(){
        this.dao = new RssFeedDAOImpl();
    }

    @Test
    public void testGetAllFeeds(){
        List<RssFeedEntity> entityList = dao.getAllFeeds();
        assertNotNull("Should return list", entityList);
    }

    @Test
    public void testGetFeedsPage(){
        List<RssFeedEntity> entityList = dao.getFeedsPage(2, 5);
        assertNotNull("Should return list", entityList);
    }

    @Test
    public void testGetFeedsByTitle(){
        List<RssFeedEntity> entityList = dao.getFeedsByTitle("zet3");
        assertNotNull("Should return list", entityList);
    }

    @Test
    public void testGetFeedsPageBy(){
        List<RssFeedEntity> entityList = dao.getFeedsPageByTitle("zet3",2, 5);
        assertNotNull("Should return list", entityList);
    }

    @Test
    public void testGetFeedById() {
        readFeed(addNewFeed());
    }

    @Test
    public void testAddFeed() {
        readFeed(addNewFeed());
    }

    @Test
    public void testUpdateFeed() {
        RssFeedEntity feed = addNewFeed();
        readFeed(feed);
        feed.setTitle("UpdatedTestFeed");
        dao.updateFeed(feed);
        readFeed(feed);
    }

    @Test
    public void testDeleteFeed() {
        RssFeedEntity feed = addNewFeed();
        readFeed(feed);
        dao.deleteFeedById(feed.getId());
        Optional<RssFeedEntity> entityFromDB = dao.getFeedById(feed.getId());
        assertEquals("Entity should be deleted", false, entityFromDB.isPresent());
    }

    private RssFeedEntity addNewFeed(){
        int id = 27;
        RssFeedEntity newFeed = new RssFeedEntity();
        newFeed.setId(id);
        newFeed.setTitle("NewTestFeed");
        newFeed.setLink("https://www.gazeta.ru/export/rss/lastnews.xml");
        dao.addFeed(newFeed);
        return newFeed;
    }

    private void readFeed(RssFeedEntity newFeed){
        Optional<RssFeedEntity> entityFromDB = dao.getFeedById(newFeed.getId());
        assertEquals("Should return entity", true, entityFromDB.isPresent());
        assertEquals("Should equals", newFeed.getId(), entityFromDB.get().getId());
        assertEquals("Should equals", newFeed.getTitle(), entityFromDB.get().getTitle());
        assertEquals("Should equals", newFeed.getLink(), entityFromDB.get().getLink());
    }
}

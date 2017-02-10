import org.junit.BeforeClass;
import org.junit.Test;
import rssreader.dao.RssFeedDAO;
import rssreader.dao.impl.RssFeedDAOImpl;
import rssreader.entity.RssFeedEntity;

import java.util.List;
import java.util.Optional;

import static junit.framework.TestCase.assertNotNull;

public class RssFeedDAOImplTests {
    private RssFeedDAO dao;

    @BeforeClass
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
}

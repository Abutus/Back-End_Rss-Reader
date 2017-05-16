import org.junit.Test;
import rssreader.dao.RssFeedDAO;
import rssreader.dao.RssNewsDAO;
import rssreader.dao.impl.RssFeedDAOImpl;
import rssreader.dao.impl.RssNewsDAOImpl;
import rssreader.factory.DaoFactory;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;

public class DaoFactoryTests {

    @Test
    public void testGetRssFeedDAO(){
        RssFeedDAO rssFeedDAO = DaoFactory.getRssFeedDAO();
        assertEquals("Should return correct class", RssFeedDAOImpl.class, rssFeedDAO.getClass());
        assertNotNull("Should return session factory", rssFeedDAO);
    }

    @Test
    public void testGetRssNewsDAO(){
        RssNewsDAO rssDAO = DaoFactory.getRssNewsDAO();
        assertEquals("Should return correct class", RssNewsDAOImpl.class, rssDAO.getClass());
        assertNotNull("Should return session factory", rssDAO);
    }
}

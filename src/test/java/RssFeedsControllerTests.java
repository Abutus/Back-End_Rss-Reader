import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Test;
import rssreader.dto.RssFeedDto;
import rssreader.resourcecontrollers.RssFeedsController;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertNull;
import static org.junit.Assert.assertEquals;


public class RssFeedsControllerTests extends JerseyTest {

    @Override
    public Application configure(){
        return new ResourceConfig(RssFeedsController.class);
    }

    @Test
    public void testGetAllFeeds() {
        Response output = target("/RssFeeds/feeds/").request().get();
        assertEquals("Should return status 200", 200, output.getStatus());
        assertEquals("Should return JSON", MediaType.APPLICATION_JSON_TYPE, output.getMediaType());
        assertNotNull("Should return list", output.getEntity());
    }

    @Test
    public void testGetFeedsPage() {
        Response output = target("/RssFeeds/feeds/").queryParam("start", 2).queryParam("end", 5).request().get();
        assertEquals("Should return status 200", 200, output.getStatus());
        assertEquals("Should return JSON", MediaType.APPLICATION_JSON_TYPE, output.getMediaType());
        assertNotNull("Should return list", output.getEntity());
    }

    @Test()
    public void testGetFeedsPageWithException() {
        Response output = target("/RssFeeds/feeds/").queryParam("start", -1).queryParam("end", 5).request().get();
        assertEquals("Should return status 400", 400, output.getStatus());
        assertNull("Should not return list", output.getEntity());
    }

    @Test
    public void testGetAllFeedsByTitle() {
        Response output = target("/RssFeeds/feeds/").queryParam("title", "zEt2").request().get();
        assertEquals("Should return status 200", 200, output.getStatus());
        assertEquals("Should return JSON", MediaType.APPLICATION_JSON_TYPE, output.getMediaType());
        assertNotNull("Should return list", output.getEntity());
    }

    @Test
    public void testGetFeedsPageByTitle() {
        Response output = target("/RssFeeds/feeds/").queryParam("title", "zEt2").queryParam("start", 2).queryParam("end", 5).request().get();
        assertEquals("Should return status 200", 200, output.getStatus());
        assertEquals("Should return JSON", MediaType.APPLICATION_JSON_TYPE, output.getMediaType());
        assertNotNull("Should return list", output.getEntity());
    }



    @Test
    public void testGetFeedsPageByTitleWithException() {
        Response output = target("/RssFeeds/feeds/").queryParam("title", "zEt2").queryParam("start", 2).queryParam("end", 1).request().get();
        assertEquals("Should return status 400", 400, output.getStatus());
        assertNull("Should not return list", output.getEntity());
    }

    @Test
    public void testGetAllNews() {
        Response output = target("/RssFeeds/feeds/1/news/").request().get();
        assertEquals("Should return status 200", 200, output.getStatus());
        assertEquals("Should return JSON", MediaType.APPLICATION_JSON_TYPE, output.getMediaType());
        assertNotNull("Should return list", output.getEntity());
    }

    @Test
    public void testGetNewsPage() {
        Response output = target("/RssFeeds/feeds/1/news/").queryParam("start", 2).queryParam("end", 5).request().get();
        assertEquals("Should return status 200", 200, output.getStatus());
        assertEquals("Should return JSON", MediaType.APPLICATION_JSON_TYPE, output.getMediaType());
        assertNotNull("Should return list", output.getEntity());
    }

    @Test
    public void testGetNewsPageWithException() {
        Response output = target("/RssFeeds/feeds/1/news/").queryParam("start", 2).queryParam("end", 1).request().get();
        assertEquals("Should return status 400", 400, output.getStatus());
        assertNull("Should not return list", output.getEntity());
    }

    @Test
    public void testGetSingleNews() {
        Response output = target("/RssFeeds/feeds/1/news/27/").request().get();
        assertEquals("Should return status 200", 200, output.getStatus());
        assertEquals("Should return JSON", MediaType.APPLICATION_JSON_TYPE, output.getMediaType());
        assertNotNull("Should return item", output.getEntity());
    }


    @Test
    public void testGetSingleNewsWithException() {
        Response output = target("/RssFeeds/feeds/1/news/0/").request().get();
        assertEquals("Should return status 404", 404, output.getStatus());
        assertNull("Should not return item", output.getEntity());
    }

    @Test
    public void testAddFeed() {
        RssFeedDto newFeed = new RssFeedDto();
        newFeed.setTitle("NewTestFeed");
        newFeed.setLink("https://www.gazeta.ru/export/rss/lastnews.xml");
        Response output = target("/RssFeeds/feeds/").request().post(Entity.entity(newFeed, MediaType.APPLICATION_JSON));
        assertEquals("Should return status 201", 201, output.getStatus());
        assertEquals("Should return JSON", MediaType.APPLICATION_JSON_TYPE, output.getMediaType());
        assertNotNull("Should return list", output.getEntity());
    }

    @Test
    public void testUpdateFeed() {
        RssFeedDto newFeed = new RssFeedDto();
        newFeed.setId(1);
        newFeed.setTitle("UpdatedFeed");
        newFeed.setLink("https://www.gazeta.ru/export/rss/lastnews.xml");
        Response output = target("/RssFeeds/feeds/").request().put(Entity.entity(newFeed, MediaType.APPLICATION_JSON));
        assertEquals("Should return status 200", 200, output.getStatus());
    }

    @Test
    public void testDeleteFeed() {
        RssFeedDto newFeed = new RssFeedDto();
        newFeed.setId(27);
        newFeed.setTitle("FeedForDelete");
        newFeed.setLink("https://www.gazeta.ru/export/rss/lastnews.xml");
        Response output = target("/RssFeeds/feeds/").request().post(Entity.entity(newFeed, MediaType.APPLICATION_JSON));
        assertEquals("Should return status 200", 200, output.getStatus());
        output = target("/RssFeeds/feeds/").queryParam("id", 27).request().delete();
        assertEquals("Should return status 204", 204, output.getStatus());
    }
}

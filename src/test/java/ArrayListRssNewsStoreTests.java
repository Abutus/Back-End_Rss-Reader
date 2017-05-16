import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import rssreader.entity.RssNewsItemEntity;
import rssreader.rssstores.RssNewsStore;
import rssreader.rssstores.impl.ArrayListRssNewsStore;

import static org.junit.Assert.assertEquals;

public class ArrayListRssNewsStoreTests {
    private RssNewsStore store;

    @Before
    public void createStore(){
        store = new ArrayListRssNewsStore();
    }

    @Test
    public void testClearStore(){
        store.add(createNewsEntty());
        assertEquals("Store should have item", true, store.getRssList().size() > 0);
        store.clear();
        assertEquals("Store should not have item", false, store.getRssList().size() > 0);
    }

    @Test
    public void testAddItemAndGetStore(){
        RssNewsItemEntity newsItemDto = createNewsEntty();
        store.add(newsItemDto);
        int storeSize = store.getRssList().size();
        assertEquals("Store should have item", true, storeSize > 0);
        assertEquals("Store should have new item", newsItemDto, store.getRssList().get(storeSize - 1));
    }

    private RssNewsItemEntity createNewsEntty(){
        RssNewsItemEntity feed = Mockito.mock(RssNewsItemEntity.class);
        feed.setId(1);
        feed.setTitle("new feed dto number" + 1);
        feed.setLink("http://new_link/");
        return feed;
    }
}

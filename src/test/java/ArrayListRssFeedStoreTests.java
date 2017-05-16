import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import rssreader.dto.RssFeedDto;
import rssreader.rssstores.RssFeedStore;
import rssreader.rssstores.impl.ArrayListRssFeedStore;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class ArrayListRssFeedStoreTests {
    private RssFeedStore store;

    @Before
    public void createStore(){
        store = new ArrayListRssFeedStore();
    }

    @Test
    public void testClearStore(){
        store.add(createFeedDto());
        assertEquals("Store should have item", true, store.getRssFeedList().size() > 0);
        store.clear();
        assertEquals("Store should not have item", false, store.getRssFeedList().size() > 0);
    }

    @Test
    public void testAddItemAndGetStore(){
        RssFeedDto feedDto = createFeedDto();
        store.add(feedDto);
        int storeSize = store.getRssFeedList().size();
        assertEquals("Store should have item", true, storeSize > 0);
        assertEquals("Store should have new item", feedDto, store.getRssFeedList().get(storeSize - 1));
    }

    private RssFeedDto createFeedDto(){
        RssFeedDto feed = Mockito.mock(RssFeedDto.class);
        feed.setId(1);
        feed.setTitle("new feed dto number" + 1);
        feed.setLink("http://new_link/");
        return feed;
    }
}

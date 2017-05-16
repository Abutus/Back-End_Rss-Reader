import org.junit.Test;
import org.mockito.Mockito;
import rssreader.dto.RssNewsItemDto;
import rssreader.entity.RssNewsItemEntity;
import rssreader.factory.RssNewsItemDtoFactory;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertEquals;

public class RssNewsItemDtoFactoryTests {

    @Test
    public void testGetInstance(){
        RssNewsItemDtoFactory newsItemDtoFactory = RssNewsItemDtoFactory.getInstance();
        assertNotNull("Should return session factory", newsItemDtoFactory);
    }

    @Test
    public void testCreateRssFeedDto(){
        int id = 5;
        RssNewsItemEntity newsItemEntity = createMockRssNewsItemEntity(id);
        RssNewsItemDto newsItemDto = RssNewsItemDtoFactory.getInstance().createRssNewsItemDto(newsItemEntity);

        validationOfFieldsClasses(newsItemEntity, newsItemDto);
    }

    @Test
    public void testCreateRssFeedDtoList(){
        List<RssNewsItemEntity> newsItemEntity = new ArrayList<>();
        for (int i = 0; i < 10; ++i){
            newsItemEntity.add(createMockRssNewsItemEntity(i));
        }

        List<RssNewsItemDto> newsItemDtos = RssNewsItemDtoFactory.getInstance().createRssNewsItemDtoList(newsItemEntity);
        for (int i = 0; i < 10; ++i){
            validationOfFieldsClasses(newsItemEntity.get(i), newsItemDtos.get(i));
        }
    }

    private RssNewsItemEntity createMockRssNewsItemEntity(int id){
        RssNewsItemEntity newsItemEntity = Mockito.mock(RssNewsItemEntity.class);
        newsItemEntity.setId(id);
        newsItemEntity.setTitle("new entity number" + id);
        newsItemEntity.setLink("http://new_link/");
        return newsItemEntity;
    }

    private void  validationOfFieldsClasses(RssNewsItemEntity newsItemEntity, RssNewsItemDto newsItemDto){
        assertEquals("Id should be equals", newsItemEntity.getId(), newsItemDto.getId());
        assertEquals("Title should be equals", newsItemEntity.getTitle(), newsItemDto.getTitle());
        assertEquals("Link should be equals", newsItemEntity.getLink(), newsItemDto.getLink());
    }
}

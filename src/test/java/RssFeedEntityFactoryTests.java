import org.junit.Test;
import org.mockito.Mockito;
import rssreader.dto.RssFeedDto;
import rssreader.entity.RssFeedEntity;
import rssreader.factory.RssFeedEntityFactory;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertEquals;

public class RssFeedEntityFactoryTests {

    @Test
    public void testGetInstance(){
        RssFeedEntityFactory feedEntityFactory = RssFeedEntityFactory.getInstance();
        assertNotNull("Should return session factory", feedEntityFactory);
    }

    @Test
    public void testCreateRssFeedDto(){
        int id = 5;
        RssFeedDto feedDto = createMockRssFeedDto(id);
        RssFeedEntity feedEntity = RssFeedEntityFactory.getInstance().createRssFeedEntity(feedDto);

        validationOfFieldsClasses(feedEntity, feedDto);
    }

    @Test
    public void testCreateRssFeedDtoList(){
        List<RssFeedDto> feedDtos = new ArrayList<>();
        for (int i = 0; i < 10; ++i){
            feedDtos.add(createMockRssFeedDto(i));
        }

        List<RssFeedEntity> feedEntities = RssFeedEntityFactory.getInstance().createRssFeedEntityList(feedDtos);
        for (int i = 0; i < 10; ++i){
            validationOfFieldsClasses(feedEntities.get(i), feedDtos.get(i));
        }
    }

    private RssFeedDto createMockRssFeedDto(int id){
        RssFeedDto feedDto = Mockito.mock(RssFeedDto.class);
        feedDto.setId(id);
        feedDto.setTitle("new entity number" + id);
        feedDto.setLink("http://new_link/");
        return feedDto;
    }

    private void  validationOfFieldsClasses(RssFeedEntity feedEntity, RssFeedDto feedDto){
        assertEquals("Id should be equals", feedEntity.getId(), feedDto.getId());
        assertEquals("Title should be equals", feedEntity.getTitle(), feedDto.getTitle());
        assertEquals("Link should be equals", feedEntity.getLink(), feedDto.getLink());
    }
}

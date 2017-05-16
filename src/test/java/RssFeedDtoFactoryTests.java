import org.junit.Test;
import org.mockito.Mockito;
import rssreader.dto.RssFeedDto;
import rssreader.entity.RssFeedEntity;
import rssreader.factory.RssFeedDtoFactory;

import java.time.Clock;
import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertEquals;

public class RssFeedDtoFactoryTests {

    @Test
    public void testGetInstance(){
        RssFeedDtoFactory feedDtoFactory = RssFeedDtoFactory.getInstance();
        assertNotNull("Should return session factory", feedDtoFactory);
    }

    @Test
    public void testCreateRssFeedDto(){
        int id = 5;
        RssFeedEntity feedEntity = createMockRssFeedEntity(id);
        RssFeedDto feedDto = RssFeedDtoFactory.getInstance().createRssFeedDto(feedEntity);

        validationOfFieldsClasses(feedEntity, feedDto);
    }

    @Test
    public void testCreateRssFeedDtoList(){
        List<RssFeedEntity> feedEntities = new ArrayList<>();
        for (int i = 0; i < 10; ++i){
            feedEntities.add(createMockRssFeedEntity(i));
        }

        List<RssFeedDto> feedDtos = RssFeedDtoFactory.getInstance().createRssFeedDtoList(feedEntities);
        for (int i = 0; i < 10; ++i){
            validationOfFieldsClasses(feedEntities.get(i), feedDtos.get(i));
        }
    }

    private RssFeedEntity createMockRssFeedEntity(int id){
        RssFeedEntity feedEntity = Mockito.mock(RssFeedEntity.class);
        feedEntity.setId(id);
        feedEntity.setTitle("new entity number" + id);
        feedEntity.setLink("http://new_link/");
        feedEntity.setLastUpdate(Clock.systemDefaultZone().instant());
        return feedEntity;
    }

    private void  validationOfFieldsClasses(RssFeedEntity feedEntity, RssFeedDto feedDto){
        assertEquals("Id should be equals", feedEntity.getId(), feedDto.getId());
        assertEquals("Title should be equals", feedEntity.getTitle(), feedDto.getTitle());
        assertEquals("Link should be equals", feedEntity.getLink(), feedDto.getLink());
    }
}

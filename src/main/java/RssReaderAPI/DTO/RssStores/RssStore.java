package RssReaderAPI.DTO.RssStores;

import RssReaderAPI.DTO.RssDto;
import java.util.List;

public interface RssStore {
    void clear();
    void add(RssDto currentRss);
    List<RssDto> getRssList();
}

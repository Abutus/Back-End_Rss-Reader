package rssreader.dto.rssstores;

import rssreader.dto.RssDto;
import java.util.List;

public interface RssStore {
    void clear();
    void add(RssDto currentRss);
    List<RssDto> getRssList();
}

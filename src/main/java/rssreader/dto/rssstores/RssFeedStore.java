package rssreader.dto.rssstores;

import rssreader.dto.RssFeedDto;
import java.util.List;

public interface RssFeedStore {
    void clear();
    void add(RssFeedDto currentRssFeed);
    List<RssFeedDto> getRssFeedList();
}

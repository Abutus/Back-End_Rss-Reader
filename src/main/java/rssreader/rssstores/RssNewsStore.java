package rssreader.rssstores;

import rssreader.entity.RssNewsItemEntity;
import java.util.List;

public interface RssNewsStore {
    void clear();
    void add(RssNewsItemEntity currentRss);
    List<RssNewsItemEntity> getRssList();
}

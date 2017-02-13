package rssreader.dao;

import rssreader.entity.RssNewsItemEntity;

import java.util.List;
import java.util.Optional;

public interface RssNewsDAO {
    List<RssNewsItemEntity> getAllNews(long feedId);
    List<RssNewsItemEntity> getNewsPage(long feedId, int start, int end);
    Optional<RssNewsItemEntity> getNewsById(long id);
    void deleteNewsByFeedId(long feedId);
}

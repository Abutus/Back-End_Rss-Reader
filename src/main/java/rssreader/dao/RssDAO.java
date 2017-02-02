package rssreader.dao;

import rssreader.entity.RssEntity;

import java.util.List;
import java.util.Optional;

public interface RssDAO {
    List<RssEntity> getAllNews(long feedId);
    List<RssEntity> getNewsPage(long feedId, int start, int end);
    Optional<RssEntity> getNewsById(long id);
}

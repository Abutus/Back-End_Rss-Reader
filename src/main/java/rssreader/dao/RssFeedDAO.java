package rssreader.dao;

import rssreader.dto.RssFeedDto;

import java.util.List;
import java.util.Optional;

public interface RssFeedDAO {
    List<RssFeedDto> getAllFeeds();
    List<RssFeedDto> getFeedsPage(int start, int end);
    List<RssFeedDto> getFeedsByTitle(String title);
    List<RssFeedDto> getFeedsPageByTitle(String title, int start, int end);
    Optional<RssFeedDto> getFeedById(long id);
    void addFeed(RssFeedDto rssFeedDto);
    void addFeeds(List<RssFeedDto> rssFeedDtos);
    void updateFeed(RssFeedDto rssFeedDto);
    void deleteFeedById(long id);
}

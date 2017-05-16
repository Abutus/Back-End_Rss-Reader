package rssreader.dao;

import rssreader.entity.RssFeedEntity;

import java.util.List;
import java.util.Optional;

public interface RssFeedDAO {
    List<RssFeedEntity> getAllFeeds();
    List<RssFeedEntity> getFeedsPage(int start, int end);
    List<RssFeedEntity> getFeedsByTitle(String title);
    List<RssFeedEntity> getFeedsPageByTitle(String title, int start, int end);
    Optional<RssFeedEntity> getFeedById(long id);
    void addFeed(RssFeedEntity rssFeed);
    void addFeeds(List<RssFeedEntity> rssFeeds);
    void updateFeed(RssFeedEntity rssFeed);
    void deleteFeedById(long id);
}

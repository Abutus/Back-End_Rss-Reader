package RssReaderAPI.DAO;

import RssReaderAPI.DTO.RssFeedDto;

import java.util.List;
import java.util.Optional;

public interface RssFeedDAO {
    Optional<List<RssFeedDto>> getAllFeeds();
    Optional<List<RssFeedDto>> getFeedsByTitle(String title);
    Optional<RssFeedDto> getFeedById(long id);
    void addFeed(RssFeedDto rssFeedDto);
    void updateFeedById(long id, RssFeedDto rssFeedDto);
    void deleteFeedById(long id);
}

package RssReaderAPI.DAO;

import RssReaderAPI.DTO.RssFeedDto;
import java.util.List;

public interface RssFeedDAO {
    List<RssFeedDto> getAllFeeds();
    List<RssFeedDto> getFeedsByTitle(String title);
    RssFeedDto getFeedById(long id);
    void addFeed(RssFeedDto rssFeedDto);
    void updateFeedById(long id, RssFeedDto rssFeedDto);
    void deleteFeedById(long id);
}

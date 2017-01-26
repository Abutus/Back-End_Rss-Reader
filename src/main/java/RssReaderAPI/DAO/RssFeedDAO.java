package RssReaderAPI.DAO;

import RssReaderAPI.DTO.RssFeedDto;

import java.sql.SQLException;
import java.util.List;

public interface RssFeedDAO {
    void addFeed(RssFeedDto rssFeedDto) throws SQLException;
    RssFeedDto getFeedById(long id) throws SQLException;
    List<RssFeedDto> getFeedsByTitle(String title, long start, long end) throws SQLException;
    void updateFeedById(long id, RssFeedDto rssFeedDto) throws SQLException;
    void deleteFeedById(long id) throws SQLException;
}

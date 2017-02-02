package rssreader.services;

import rssreader.RssFeedSaxHandler;
import rssreader.dto.RssDto;
import rssreader.dto.RssFeedDto;
import rssreader.entity.RssEntity;
import rssreader.entity.RssFeedEntity;
import rssreader.exceptions.BadRequestException;
import rssreader.exceptions.InternalServerError;
import rssreader.exceptions.ResourceNotFoundException;
import rssreader.factory.DaoFactory;
import rssreader.RssSaxHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import rssreader.factory.DtoFactory;
import rssreader.factory.EntityFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.time.Clock;
import java.time.Instant;
import java.util.*;

public class RssFeedsService {
    private static final RssFeedsService INSTANCE = new RssFeedsService();

    public static RssFeedsService getInstance(){
        return INSTANCE;
    }

    public List<RssFeedDto> getAllFeeds() throws ResourceNotFoundException {
        List<RssFeedEntity> rssFeeds = DaoFactory.getRssFeedDAO().getAllFeeds();
        return DtoFactory.getInstance().getRssFeedDtoList(rssFeeds);
    }

    public List<RssFeedDto> getFeedsByTitle(String title) throws ResourceNotFoundException {
        List<RssFeedEntity> rssFeeds = DaoFactory.getRssFeedDAO().getFeedsByTitle(title);
        return DtoFactory.getInstance().getRssFeedDtoList(rssFeeds);
    }

    public List<RssFeedDto> getFeedsPage(long start, long end) throws ResourceNotFoundException, BadRequestException {
        if(start < 0) {
            throw new BadRequestException("Index of the first element is less than acceptable");
        } else if (end < start) {
            throw new BadRequestException("Index of the last element is less than index of the first element");
        }
        List<RssFeedEntity> rssFeeds = DaoFactory.getRssFeedDAO().getFeedsPage((int)start, (int)end);
        return DtoFactory.getInstance().getRssFeedDtoList(rssFeeds);
    }

    public List<RssFeedDto> getFeedsPageByTitle(String title, long start, long end) throws ResourceNotFoundException, BadRequestException {
        if(start < 0) {
            throw new BadRequestException("Index of the first element is less than acceptable");
        } else if (end < start) {
            throw new BadRequestException("Index of the last element is less than index of the first element");
        }
        List<RssFeedEntity> rssFeeds = DaoFactory.getRssFeedDAO().getFeedsPageByTitle(title, (int)start, (int)end);
        return DtoFactory.getInstance().getRssFeedDtoList(rssFeeds);
    }

    public List<RssDto> getAllNews(long feedId) throws ResourceNotFoundException, InternalServerError {
        Optional<RssFeedEntity> rssFeed = DaoFactory.getRssFeedDAO().getFeedById(feedId);
        if (!rssFeed.isPresent()) {
            throw new ResourceNotFoundException("Feeds with id : " + feedId + " not found. Can't get it!");
        }
        Instant now = Clock.systemDefaultZone().instant();
        if(rssFeed.get().getLastUpdate() == null || (now.getEpochSecond() - rssFeed.get().getLastUpdate().getEpochSecond() > 600)){
            updateNews(rssFeed.get());
        }
        List<RssEntity> news = DaoFactory.getRssDAO().getAllNews(rssFeed.get().getId());
        return DtoFactory.getInstance().getRssDtoList(news);
    }

    public List<RssDto> getNewsPage(long feedId, long start, long end) throws BadRequestException, ResourceNotFoundException, InternalServerError {
        if(start < 0) {
            throw new BadRequestException("Index of the first element is less than acceptable");
        } else if (end < start) {
            throw new BadRequestException("Index of the last element is less than index of the first element");
        }
        Optional<RssFeedEntity> rssFeed = DaoFactory.getRssFeedDAO().getFeedById(feedId);
        if (!rssFeed.isPresent()) {
            throw new ResourceNotFoundException("Feeds with id : " + feedId + " not found. Can't get it!");
        }
        Instant now = Clock.systemDefaultZone().instant();
        if(rssFeed.get().getLastUpdate() == null || (now.getEpochSecond() - rssFeed.get().getLastUpdate().getEpochSecond() > 6000)){
            updateNews(rssFeed.get());
        }
        List<RssEntity> newsList = DaoFactory.getRssDAO().getNewsPage(feedId, (int)start, (int)end);
        return DtoFactory.getInstance().getRssDtoList(newsList);
    }

    public RssDto getSingleNews(long feedId, long newsId) throws ResourceNotFoundException, InternalServerError {
        Optional<RssEntity> news = DaoFactory.getRssDAO().getNewsById(newsId);
        if(!news.isPresent()) {
            throw new ResourceNotFoundException("News with id : " + newsId + " not found in feed with id: " + feedId + ". Can't get it!");
        }
        return DtoFactory.getInstance().getRssDto(news.get());
    }

    public List<String> getMostUsedWords(long feedId, long rssId, int count) throws ResourceNotFoundException, InternalServerError {
        RssDto rssDto = INSTANCE.getSingleNews(feedId, rssId);

        Map<String, Integer> numbersUsesOfWords = new HashMap<>();
        StringTokenizer tokenizer = new StringTokenizer(rssDto.getDescription(), " ,.:;!?\"\t\n");
        while (tokenizer.hasMoreTokens()){
            String token = tokenizer.nextToken();
            Integer value = numbersUsesOfWords.get(token);
            if(value != null){
                numbersUsesOfWords.put(token, value + 1);
            } else {
                numbersUsesOfWords.put(token, 1);
            }
        }
        List<Map.Entry<String, Integer>> listMapEntry = new ArrayList(numbersUsesOfWords.entrySet());
        listMapEntry.sort((o1, o2) -> o2.getValue() - o1.getValue());

        List<String> mostUsedWords = new ArrayList<>();
        int wordsCount = 0;
        Iterator<Map.Entry<String, Integer>> entryIterator = listMapEntry.iterator();
        while (entryIterator.hasNext() && wordsCount < count){
            mostUsedWords.add(entryIterator.next().getKey());
            ++wordsCount;
        }
        return mostUsedWords;
    }

    public void addFeed(RssFeedDto rssFeed){
        DaoFactory.getRssFeedDAO().addFeed(EntityFactory.getInstance().getRssFeedEntity(rssFeed));
    }

    public List<RssFeedDto> addFeeds(InputStream inputStream) throws InternalServerError {
        List<RssFeedDto> rssFeeds;
        try {
            SAXParser saxParser = SAXParserFactory.newInstance().newSAXParser();
            RssFeedSaxHandler handler = new RssFeedSaxHandler();

            saxParser.parse(new InputSource(inputStream), handler);
            rssFeeds = handler.getStore().getRssFeedList();
        } catch (ParserConfigurationException | SAXException | IOException e) {
            throw  new InternalServerError(e.getMessage());
        }
        DaoFactory.getRssFeedDAO().addFeeds(EntityFactory.getInstance().getRssFeedEntityList(rssFeeds));
        return rssFeeds;
    }

    public void updateFeed(RssFeedDto updatedRssFeed) throws ResourceNotFoundException {
        Optional<RssFeedEntity> rssFeedDto = DaoFactory.getRssFeedDAO().getFeedById(updatedRssFeed.getId());
        if (!rssFeedDto.isPresent()){
            throw new ResourceNotFoundException("Feed with id: " + updatedRssFeed.getId() + " not found. Can't retrieve it!");
        } else {
            DaoFactory.getRssFeedDAO().updateFeed(EntityFactory.getInstance().getRssFeedEntity(updatedRssFeed));
        }
    }

    public void deleteFeed(long feedId) throws ResourceNotFoundException {
        Optional<RssFeedEntity> rssFeedDto = DaoFactory.getRssFeedDAO().getFeedById(feedId);
        if (!rssFeedDto.isPresent()){
            throw new ResourceNotFoundException("Feed with id: " + feedId + " not found. Can't delete it!");
        } else {
            DaoFactory.getRssFeedDAO().deleteFeedById(feedId);
        }
    }

    public void updateNews(RssFeedEntity feedEntity) throws InternalServerError {
        List<RssDto> news;
        try {
            SAXParser saxParser = SAXParserFactory.newInstance().newSAXParser();
            RssSaxHandler handler = new RssSaxHandler();
            URL url = new URL(feedEntity.getLink());
            Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("proxy01.merann.ru", 8080));
            URLConnection urlConnection = url.openConnection(proxy);
            urlConnection.connect();

            saxParser.parse(new InputSource(urlConnection.getInputStream()), handler);
            news = handler.getStore().getRssList();
        } catch (ParserConfigurationException | SAXException | IOException e) {
            throw  new InternalServerError(e.getMessage());
        }
        List<RssEntity> newsEntities = EntityFactory.getInstance().getRssEntityList(news);
        for(RssEntity newsEntity : newsEntities){
            feedEntity.addNews(newsEntity);
        }
        Instant now = Clock.systemDefaultZone().instant();
        feedEntity.setLastUpdate(now);
        DaoFactory.getRssFeedDAO().updateFeed(feedEntity);
    }
}

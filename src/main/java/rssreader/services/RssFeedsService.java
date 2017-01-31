package rssreader.services;

import rssreader.dto.RssDto;
import rssreader.dto.RssFeedDto;
import rssreader.exceptions.BadRequestException;
import rssreader.exceptions.InternalServerError;
import rssreader.exceptions.ResourceNotFoundException;
import rssreader.DaoFactory;
import rssreader.RssFeedSaxHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;

public class RssFeedsService {
    private static final RssFeedsService INSTANCE = new RssFeedsService();

    public static RssFeedsService getInstance(){
        return INSTANCE;
    }

    public List<RssFeedDto> getAllFeeds() throws ResourceNotFoundException {
        List<RssFeedDto> rssFeeds;
        rssFeeds = DaoFactory.getInstance().getRssFeedDAO().getAllFeeds();
        return rssFeeds;
    }

    public List<RssFeedDto> getFeedsByTitle(String title) throws ResourceNotFoundException {
        List<RssFeedDto> rssFeeds;
        rssFeeds = DaoFactory.getInstance().getRssFeedDAO().getFeedsByTitle(title);
        return rssFeeds;
    }

    public List<RssFeedDto> getFeedsPage(long start, long end) throws ResourceNotFoundException, BadRequestException {
        if(start < 0) {
            throw new BadRequestException("Index of the first element is less than acceptable");
        } else if (end < start) {
            throw new BadRequestException("Index of the last element is less than index of the first element");
        }
        return DaoFactory.getInstance().getRssFeedDAO().getFeedsPage((int)start, (int)end);
    }

    public List<RssFeedDto> getFeedsPageByTitle(String title, long start, long end) throws ResourceNotFoundException, BadRequestException {
        if(start < 0) {
            throw new BadRequestException("Index of the first element is less than acceptable");
        } else if (end < start) {
            throw new BadRequestException("Index of the last element is less than index of the first element");
        }
        return DaoFactory.getInstance().getRssFeedDAO().getFeedsPageByTitle(title, (int)start, (int)end);
    }

    public List<RssDto> getAllNews(long feedId) throws ResourceNotFoundException, InternalServerError {
        Optional<RssFeedDto> rssFeedDto = DaoFactory.getInstance().getRssFeedDAO().getFeedById(feedId);
        if (!rssFeedDto.isPresent()) {
            throw new ResourceNotFoundException("Feeds with id : " + feedId + " not found. Can't get it!");
        }
        List<RssDto> rssDtos;
        try {
            SAXParser saxParser = SAXParserFactory.newInstance().newSAXParser();
            RssFeedSaxHandler handler = new RssFeedSaxHandler();
            URL url = new URL(rssFeedDto.get().getLink());
            Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("proxy01.merann.ru", 8080));
            URLConnection urlConnection = url.openConnection(proxy);
            urlConnection.connect();

            saxParser.parse(new InputSource(urlConnection.getInputStream()), handler);
            rssDtos = handler.getStore().getRssList();
        } catch (ParserConfigurationException | SAXException | IOException e) {
            throw  new InternalServerError(e.getMessage());
        }
        return rssDtos;
    }

    public List<RssDto> getNewsPage(long feedId, long start, long end) throws ResourceNotFoundException, BadRequestException, InternalServerError {
        if(start < 0) {
            throw new BadRequestException("Index of the first element is less than acceptable");
        } else if (end < start) {
            throw new BadRequestException("Index of the last element is less than index of the first element");
        }
        List<RssDto> newsList = INSTANCE.getAllNews(feedId);
        if(start > newsList.size()) {
            throw new BadRequestException("Index of the first element is more than acceptable");
        } else if(end > newsList.size()) {
            throw new BadRequestException("Index of the last element is more than acceptable");
        }
        return newsList.subList((int)start, (int)end);
    }

    public RssDto getSingleNews(long feedId, long newsId) throws ResourceNotFoundException, InternalServerError {
        List<RssDto> newsList= INSTANCE.getAllNews(feedId);
        if(newsId > newsList.size() - 1) {
            throw new ResourceNotFoundException("News with id : " + newsId + " not found in feed with id: " + feedId + ". Can't get it!");
        } else if(newsId < 0) {
            throw new ResourceNotFoundException("News with id : " + newsId + " not found in feed with id: " + feedId + ". Can't get it!");
        }
        return newsList.get((int)newsId);
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
        DaoFactory.getInstance().getRssFeedDAO().addFeed(rssFeed);
    }

    public void updateFeed(RssFeedDto updatedRssFeed) throws ResourceNotFoundException {
        Optional<RssFeedDto> rssFeedDto = DaoFactory.getInstance().getRssFeedDAO().getFeedById(updatedRssFeed.getId());
        if (!rssFeedDto.isPresent()){
            throw new ResourceNotFoundException("Feed with id: " + updatedRssFeed.getId() + " not found. Can't retrieve it!");
        } else {
            DaoFactory.getInstance().getRssFeedDAO().updateFeed(updatedRssFeed);
        }
    }

    public void deleteFeed(long feedId) throws ResourceNotFoundException {
        Optional<RssFeedDto> rssFeedDto = DaoFactory.getInstance().getRssFeedDAO().getFeedById(feedId);
        if (!rssFeedDto.isPresent()){
            throw new ResourceNotFoundException("Feed with id: " + feedId + " not found. Can't delete it!");
        } else {
            DaoFactory.getInstance().getRssFeedDAO().deleteFeedById(feedId);
        }
    }
}

package RssReaderAPI.Services;

import RssReaderAPI.DTO.RssDto;
import RssReaderAPI.DTO.RssFeedDto;
import RssReaderAPI.Exceptions.BadRequestException;
import RssReaderAPI.Exceptions.ResourceNotFoundException;
import RssReaderAPI.Factory;
import RssReaderAPI.RssFeedSaxHandler;
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
        rssFeeds = Factory.getInstance().getRssFeedDAO().getAllFeeds();
        if (rssFeeds.size() == 0) {
            throw new ResourceNotFoundException("Feeds are not found. Can't get it!");
        }
        return rssFeeds;
    }

    public List<RssFeedDto> getFeedsByTitle(String title) throws ResourceNotFoundException {
        List<RssFeedDto> rssFeeds;
        rssFeeds = Factory.getInstance().getRssFeedDAO().getFeedsByTitle(title);
        if (rssFeeds.size() == 0){
            throw new ResourceNotFoundException("Feeds with substring :\"" + title + "\" in title not found. Can't get it!");
        }
        return rssFeeds;
    }

    public List<RssFeedDto> getFeedsPage(long start, long end) throws ResourceNotFoundException, BadRequestException {
        ++end;
        if(start < 0) {
            throw new BadRequestException("Index of the first element is less than acceptable");
        } else if (end < start) {
            throw new BadRequestException("Index of the last element is less than index of the first element");
        }
        List<RssFeedDto> rssFeeds = INSTANCE.getAllFeeds();
        if(start > rssFeeds.size()) {
            throw new BadRequestException("Index of the first element is more than acceptable");
        } else if(end > rssFeeds.size()) {
            throw new BadRequestException("Index of the last element is more than acceptable");
        }
        return rssFeeds.subList((int)start, (int)end);
    }

    public List<RssFeedDto> getFeedsPageByTitle(String title, long start, long end) throws ResourceNotFoundException, BadRequestException {
        ++end;
        if(start < 0) {
            throw new BadRequestException("Index of the first element is less than acceptable");
        } else if (end < start) {
            throw new BadRequestException("Index of the last element is less than index of the first element");
        }
        List<RssFeedDto> rssFeeds = INSTANCE.getFeedsByTitle(title);
        if(start > rssFeeds.size()) {
            throw new BadRequestException("Index of the first element is more than acceptable");
        } else if(end > rssFeeds.size()) {
            throw new BadRequestException("Index of the last element is more than acceptable");
        }
        return rssFeeds.subList((int)start, (int)end);
    }

    public List<RssDto> getAllNews(long feedId) throws ResourceNotFoundException {
        RssFeedDto rssFeedDto = Factory.getInstance().getRssFeedDAO().getFeedById(feedId); //optional
        if (rssFeedDto == null) {
            throw new ResourceNotFoundException("Feeds with id : " + feedId + " not found. Can't get it!");
        }
        List<RssDto> rssDtos = null;
        try {
            SAXParser saxParser = SAXParserFactory.newInstance().newSAXParser();
            RssFeedSaxHandler handler = new RssFeedSaxHandler();
            URL url = new URL(rssFeedDto.getLink());
            Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("proxy01.merann.ru", 8080));
            URLConnection urlConnection = url.openConnection(proxy);
            urlConnection.connect();

            saxParser.parse(new InputSource(urlConnection.getInputStream()), handler);
            rssDtos = handler.getStore().getRssList();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return rssDtos;
    }

    public List<RssDto> getNewsPage(long feedId, long start, long end) throws ResourceNotFoundException, BadRequestException {
        ++end;
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

    public RssDto getSingleNews(long feedId, long newsId) throws ResourceNotFoundException {
        List<RssDto> newsList= INSTANCE.getAllNews(feedId);
        if(newsId > newsList.size() - 1) {
            throw new ResourceNotFoundException("News with id : " + newsId + " not found in feed with id: " + feedId + ". Can't get it!");
        } else if(newsId < 0) {
            throw new ResourceNotFoundException("News with id : " + newsId + " not found in feed with id: " + feedId + ". Can't get it!");
        }
        return newsList.get((int)newsId);
    }

    public String[] getMostUsedWords(long feedId, long rssId, int count) throws ResourceNotFoundException {
        String[] mostUsedWords = new String[count];
        RssDto rssDto = INSTANCE.getSingleNews(feedId, rssId);
        try {
            Map<String, Integer> hashMap = new HashMap<>();
            StringTokenizer tokenizer = new StringTokenizer(rssDto.getDescription(), " ,.:;!?\"\t\n");
            while (tokenizer.hasMoreTokens()){
                final String token = tokenizer.nextToken();
                final Integer value = hashMap.get(token);//final?

                if(value != null){
                    hashMap.put(token, value + 1);
                } else {
                    hashMap.put(token, 1);
                }
            }
            List<Map.Entry<String, Integer>> list = new ArrayList(hashMap.entrySet());
            Collections.sort(list, (o1, o2) -> o2.getValue() - o1.getValue());
            for(int i = 0; i < count; ++i){
                mostUsedWords[i] = list.get(i).getKey();
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return mostUsedWords;
    }

    public void addFeed(RssFeedDto rssFeed){
        Factory.getInstance().getRssFeedDAO().addFeed(rssFeed);
    }

    public void updateFeed(long feedId, RssFeedDto updatedRssFeed) throws ResourceNotFoundException {
        RssFeedDto rssFeedDto = Factory.getInstance().getRssFeedDAO().getFeedById(feedId);
        if (rssFeedDto == null){
            throw new ResourceNotFoundException("Feed with id: " + feedId + " not found. Can't retrieve it!");
        } else {
            Factory.getInstance().getRssFeedDAO().updateFeedById(feedId, updatedRssFeed);
        }
    }

    public void deleteFeed(long feedId) throws ResourceNotFoundException {
        RssFeedDto rssFeedDto = Factory.getInstance().getRssFeedDAO().getFeedById(feedId);
        if (rssFeedDto == null){
            throw new ResourceNotFoundException("Feed with id: " + feedId + " not found. Can't delete it!");
        } else {
            Factory.getInstance().getRssFeedDAO().deleteFeedById(feedId);
        }
    }
}

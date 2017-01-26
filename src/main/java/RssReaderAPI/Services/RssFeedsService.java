package RssReaderAPI.Services;

import RssReaderAPI.DTO.RssDto;
import RssReaderAPI.DTO.RssFeedDto;
import RssReaderAPI.Exceptions.FeedNotFoundException;
import RssReaderAPI.Factory;
import RssReaderAPI.RssFeedSaxHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.sql.SQLException;
import java.util.*;

public class RssFeedsService {
    private static final RssFeedsService INSTANCE = new RssFeedsService();

    public static RssFeedsService getInstance(){
        return INSTANCE;
    }

    public void addFeed(RssFeedDto rssFeed){
        try {
            Factory.getInstance().getRssFeedDAO().addFeed(rssFeed);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<RssFeedDto> getFeedsByTitle(String title, long start, long end) throws FeedNotFoundException {
        List<RssFeedDto> rssFeeds = null;
        try {
            rssFeeds = Factory.getInstance().getRssFeedDAO().getFeedsByTitle(title, start, end);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (rssFeeds.size() == 0){
            throw new FeedNotFoundException("Feeds with substring :\"" + title + "\" in title not found. Can't get it!");
        }
        return rssFeeds;
    }

    public List<RssDto> getRssByFeed(long feedId, long start, long end) throws FeedNotFoundException {
        RssFeedDto rssFeedDto = null;
        List<RssDto> rssDtos = null;
        try{
            rssFeedDto = Factory.getInstance().getRssFeedDAO().getFeedById(feedId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (rssFeedDto == null){
            throw new FeedNotFoundException("Feeds with id : " + feedId + " not found. Can't get it!");
        } else {
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
        }
        if(start > 0){
            if(end <= rssDtos.size()){
                return rssDtos.subList((int)start - 1, (int)end);
            } else {
                return rssDtos.subList((int)start - 1, rssDtos.size());
            }
        } else {
            if(end <= rssDtos.size()){
                return rssDtos.subList(0, (int)end - 1);
            } else {
                return rssDtos;
            }
        }
    }

    public RssDto getRssFromFeedById(long feedId, long rssId) throws FeedNotFoundException {
        List<RssDto> rssList= INSTANCE.getRssByFeed(feedId, 0, Integer.MAX_VALUE);
        RssDto rssDto;
        try{
            rssDto = rssList.get((int)rssId - 1);
        } catch (Exception e) {
            throw new FeedNotFoundException("Rss with id : " + rssId + " not found in feed with id : " + feedId + ". Can't get it!");
        }
        return rssDto;
    }

    public String[] getMostUsedWords(long feedId, long rssId) throws FeedNotFoundException {
        String[] mostUsedWords = new String[5];
        RssDto rssDto = INSTANCE.getRssFromFeedById(feedId, rssId);
        try {
            HashMap<String, Integer> hashMap = new HashMap<>();
            StringTokenizer tokenizer = new StringTokenizer(rssDto.getDescription(), " ,.:;!?\"\t\n");
            while (tokenizer.hasMoreTokens()){
                final String token = tokenizer.nextToken();
                final Integer value = hashMap.get(token);

                if(value != null){
                    hashMap.put(token, value + 1);
                } else {
                    hashMap.put(token, 1);
                }
            }
            List<Map.Entry<String, Integer>> list = new ArrayList(hashMap.entrySet());
            Collections.sort(list, new Comparator<Map.Entry<String, Integer>>(){
                @Override
                public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                    return o2.getValue() - o1.getValue();
                }
            });
            for(int i = 0; i < 5; ++i){
                mostUsedWords[i] = "Word: " + list.get(i).getKey() + " was used " + list.get(i).getValue().toString() + " times";
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return mostUsedWords;
    }

    public void updateFeed(long feedId, RssFeedDto updatedRssFeed) throws FeedNotFoundException {
        RssFeedDto rssFeedDto = null;
        try {
            rssFeedDto = Factory.getInstance().getRssFeedDAO().getFeedById(feedId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (rssFeedDto == null){
            throw new FeedNotFoundException("Feed with id: " + feedId + " not found. Can't retrieve it!");
        } else {
            try {
                Factory.getInstance().getRssFeedDAO().updateFeedById(feedId, updatedRssFeed);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void deleteFeed(long feedId) throws FeedNotFoundException {
        RssFeedDto rssFeedDto = null;
        try {
            rssFeedDto = Factory.getInstance().getRssFeedDAO().getFeedById(feedId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (rssFeedDto == null){
            throw new FeedNotFoundException("Feed with id: " + feedId + " not found. Can't delete it!");
        } else {
            try {
                Factory.getInstance().getRssFeedDAO().deleteFeedById(feedId);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}

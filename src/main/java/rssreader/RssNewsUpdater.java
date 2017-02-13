package rssreader;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import rssreader.entity.RssFeedEntity;
import rssreader.entity.RssNewsItemEntity;
import rssreader.exceptions.InternalServerError;
import rssreader.factory.DaoFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.time.Clock;
import java.time.Instant;
import java.util.List;
import java.util.concurrent.Callable;

public class RssNewsUpdater implements Callable<Boolean>{
    private RssFeedEntity feedEntity;

    public RssNewsUpdater(RssFeedEntity feedEntity) {
        this.feedEntity = feedEntity;
    }

    @Override
    public Boolean call() throws Exception {
        List<RssNewsItemEntity> news;
        try {
            SAXParser saxParser = SAXParserFactory.newInstance().newSAXParser();
            RssNewsSaxHandler handler = new RssNewsSaxHandler();
            URL url = new URL(feedEntity.getLink());
            Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("proxy01.merann.ru", 8080));
            URLConnection urlConnection = url.openConnection(proxy);
            urlConnection.connect();

            saxParser.parse(new InputSource(urlConnection.getInputStream()), handler);
            news = handler.getStore().getRssList();
        } catch (ParserConfigurationException | SAXException | IOException e) {
            throw new InternalServerError(e.getMessage());
        }

        DaoFactory.getRssNewsDAO().deleteNewsByFeedId(feedEntity.getId());
        feedEntity.getNews().clear();

        for(RssNewsItemEntity newsEntity : news){
            feedEntity.addNews(newsEntity);
        }
        Instant now = Clock.systemDefaultZone().instant();
        feedEntity.setLastUpdate(now);
        DaoFactory.getRssFeedDAO().updateFeed(feedEntity);
        return true;
    }
}

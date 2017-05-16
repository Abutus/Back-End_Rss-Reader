package rssreader.factory;

import rssreader.dao.RssNewsDAO;
import rssreader.dao.RssFeedDAO;
import rssreader.dao.impl.RssNewsDAOImpl;
import rssreader.dao.impl.RssFeedDAOImpl;

public class DaoFactory {
    private static RssNewsDAO rssDAO = null;
    private static RssFeedDAO rssFeedDAO = null;

    public static synchronized RssFeedDAO getRssFeedDAO(){
        if(rssFeedDAO == null){
            rssFeedDAO = new RssFeedDAOImpl();
        }
        return rssFeedDAO;
    }

    public static synchronized RssNewsDAO getRssNewsDAO(){
        if(rssDAO == null){
            rssDAO = new RssNewsDAOImpl();
        }
        return rssDAO;
    }
}

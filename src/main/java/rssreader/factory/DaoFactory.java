package rssreader.factory;

import rssreader.dao.RssDAO;
import rssreader.dao.RssFeedDAO;
import rssreader.dao.impl.RssDAOImpl;
import rssreader.dao.impl.RssFeedDAOImpl;

public class DaoFactory {
    private static RssDAO rssDAO = null;
    private static RssFeedDAO rssFeedDAO = null;

    public static synchronized RssFeedDAO getRssFeedDAO(){
        if(rssFeedDAO == null){
            rssFeedDAO = new RssFeedDAOImpl();
        }
        return rssFeedDAO;
    }

    public static synchronized RssDAO getRssDAO(){
        if(rssDAO == null){
            rssDAO = new RssDAOImpl();
        }
        return rssDAO;
    }
}

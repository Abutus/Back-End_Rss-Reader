package rssreader;

import rssreader.dao.RssFeedDAO;
import rssreader.dao.impl.RssFeedDAOImpl;

public class DaoFactory {
    private static RssFeedDAO rssFeedDAO = null;
    private static DaoFactory instance = null;

    public static synchronized DaoFactory getInstance(){
        if(instance == null){
            instance = new DaoFactory();
        }
        return instance;
    }

    public static synchronized RssFeedDAO getRssFeedDAO(){
        if(rssFeedDAO == null){
            rssFeedDAO = new RssFeedDAOImpl();
        }
        return rssFeedDAO;
    }
}

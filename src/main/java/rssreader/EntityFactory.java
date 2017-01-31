package rssreader;

import rssreader.dao.RssFeedDAO;
import rssreader.dao.impl.RssFeedDAOImpl;

public class EntityFactory {
    private static RssFeedDAO rssFeedDAO = null;
    private static EntityFactory instance = null;

    public static synchronized EntityFactory getInstance(){
        if(instance == null){
            instance = new EntityFactory();
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

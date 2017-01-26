package RssReaderAPI;

import RssReaderAPI.DAO.Impl.RssFeedDAOImpl;
import RssReaderAPI.DAO.RssFeedDAO;

public class Factory {
    private static RssFeedDAO rssFeedDAO = null;
    private static Factory instance = null;

    public static synchronized Factory getInstance(){
        if(instance == null){
            instance = new Factory();
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

package rssreader.operations;

import org.hibernate.Session;
import rssreader.util.HibernateUtil;

public abstract class DbOperation {
    public void execute() {
        try(Session session = HibernateUtil.getSessionFactory().openSession()){
            session.beginTransaction();
            performInTransaction(session);
            session.getTransaction().commit();
        }
    }

    abstract void performInTransaction(Session session);
}

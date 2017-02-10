import org.hibernate.SessionFactory;
import org.junit.BeforeClass;
import org.junit.Test;
import rssreader.util.HibernateUtil;

import static junit.framework.TestCase.assertNotNull;

public class HibernateUtilTests {
    SessionFactory sessionFactory;
    HibernateUtil hibernateUtil;

    @BeforeClass
    public void createDAO(){
        hibernateUtil = new HibernateUtil();
    }

    @Test
    public void testGetSessionFactory(){
        sessionFactory = hibernateUtil.getSessionFactory();
        assertNotNull("Should return session factory", sessionFactory);
    }
}

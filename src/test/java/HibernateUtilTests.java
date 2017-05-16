import org.hibernate.SessionFactory;
import org.junit.Test;
import rssreader.util.HibernateUtil;

import static junit.framework.TestCase.assertNotNull;

public class HibernateUtilTests {

    @Test
    public void testGetSessionFactory(){
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        assertNotNull("Should return session factory", sessionFactory);
    }
}

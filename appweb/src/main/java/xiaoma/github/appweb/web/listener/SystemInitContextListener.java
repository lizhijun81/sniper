package xiaoma.github.appweb.web.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * Created by Dell on 2015/5/5.
 */
@WebListener(value = "systemInitContextListener")
public class SystemInitContextListener implements ServletContextListener {
    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        logger.info("ServletContextEvent init");

    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        logger.info("ServletContextEvent Destroy");

    }
}

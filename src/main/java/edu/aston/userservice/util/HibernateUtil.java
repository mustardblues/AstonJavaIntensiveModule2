package edu.aston.userservice.util;

import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HibernateUtil {
    private static SessionFactory sessionFactory;

    private static final Logger logger = LoggerFactory.getLogger(HibernateUtil.class);

    public static SessionFactory getSessionFactory() {
        if(sessionFactory == null || sessionFactory.isClosed()) {
            buildSessionFactory();
        }

        return sessionFactory;
    }

    public static void shutdown() {
        if(sessionFactory != null && sessionFactory.isOpen()) {
            sessionFactory.close();

            logger.info("Session was closed.");
        }
    }

    private static void buildSessionFactory() {
        try {
            StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                    .configure("hibernate.cfg.xml")
                    .build();

            sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();

            logger.info("Connection to the database was completed successfully.");
        }
        catch(Exception exception) {
            shutdown();

            logger.error("Failed to connect to the database: {}.", exception.getMessage());
            throw new HibernateException("Failed to connect to the database");
        }
    }
}

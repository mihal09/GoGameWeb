package com.hellokoding.account;

import java.util.Properties;

import com.hellokoding.account.model.GamesEntity;
import com.hellokoding.account.model.GamesstatesEntity;
import com.hellokoding.account.model.UsersEntity;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.service.ServiceRegistry;

public class HibernateUtil {
    private static StandardServiceRegistry registry;
    private static SessionFactory sessionFactory;

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            try {
                StandardServiceRegistryBuilder registryBuilder =
                        new StandardServiceRegistryBuilder();


                Configuration configuration = new Configuration();
                Properties properties = new Properties();

                properties.put("hibernate.connection.driver_class", "com.mysql.cj.jdbc.Driver");
                properties.put("hibernate.connection.url", "jdbc:mysql://localhost:3306/accounts?serverTimezone=UTC&useSSL=false");
                properties.put("hibernate.connection.username", "root");
                properties.put("hibernate.connection.password", "haslo1");
                properties.put(Environment.DIALECT, "org.hibernate.dialect.MySQLDialect");
                properties.put(Environment.SHOW_SQL, "true");
                properties.put(Environment.HBM2DDL_AUTO, "update");
                configuration.setProperties(properties);

                configuration.addAnnotatedClass(GamesstatesEntity.class);
                configuration.addAnnotatedClass(GamesEntity.class);
                configuration.addAnnotatedClass(UsersEntity.class);

              //  configuration.configure("hibernate.cfg.xml");

                registry = registryBuilder.build();

                ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                        .applySettings(configuration.getProperties())
                        .build();
                sessionFactory = configuration.buildSessionFactory(serviceRegistry);

            } catch (Exception e) {
                System.out.println(e);
                System.out.println("SessionFactory creation failed");
                if (registry != null) {
                    StandardServiceRegistryBuilder.destroy(registry);
                }
            }
        }
        return sessionFactory;
    }

    public static void shutdown() {
        if (registry != null) {
            StandardServiceRegistryBuilder.destroy(registry);
        }
    }
}




package com.hellokoding.account.managers;

import com.hellokoding.account.HibernateUtil;
import com.hellokoding.account.model.GamesEntity;
import com.hellokoding.account.model.GamesstatesEntity;
import com.hellokoding.account.model.UsersEntity;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class PlayerManager {
    public UsersEntity getPlayer(String name){
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<UsersEntity> users = null;

        try{
            SQLQuery sqlQuery = session.createSQLQuery("SELECT * FROM user WHERE username = :param");
            sqlQuery.setParameter("param",name);
            users = (List<UsersEntity>) sqlQuery.addEntity(UsersEntity.class).list();
        }catch (HibernateException e){
            e.printStackTrace();
        } finally {
            session.close();
        }
        HibernateUtil.shutdown();
        return users.get(users.size() - 1);
    }


}

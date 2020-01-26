package com.hellokoding.account.managers;

import com.hellokoding.account.HibernateUtil;
import com.hellokoding.account.model.GamesEntity;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class GameManager {

    public void addGame(int boardSize,int idPlayerOne, int idPlayerTwo){
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;

        try {
            tx = session.beginTransaction();
            GamesEntity game = new GamesEntity();
            game.setBoardSize(boardSize);
            game.setIdPlayerOne(idPlayerOne);
            game.setIdPlayerTwo(idPlayerTwo);
            session.save(game);
            tx.commit();
        } catch (HibernateException e) {
            if (tx!=null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }

        HibernateUtil.shutdown();
    }

    public GamesEntity getGame(int id) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        GamesEntity game = null;
        try {
            game = session.get(GamesEntity.class, id);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
        return game;
    }


}

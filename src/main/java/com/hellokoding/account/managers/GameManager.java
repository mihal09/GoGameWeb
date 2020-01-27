package com.hellokoding.account.managers;

import com.hellokoding.account.HibernateUtil;
import com.hellokoding.account.model.GamesEntity;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class GameManager {

    public int addGame(int boardSize,int idPlayerOne, int idPlayerTwo){
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        GamesEntity game = null;
        int gameId = -1;

        try {
            tx = session.beginTransaction();
            game = new GamesEntity();
            game.setBoardSize(boardSize);
            game.setIdPlayerOne(idPlayerOne);
            game.setIdPlayerTwo(idPlayerTwo);
            session.save(game);
            session.flush();
            session.refresh(game);
            tx.commit();
        } catch (HibernateException e) {
            if (tx!=null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }

        HibernateUtil.shutdown();
        gameId = game.getId();
        return gameId;
    }

    public GamesEntity getGame(int id) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        GamesEntity game = null;
        try {
            game = (GamesEntity) session.get(GamesEntity.class, id);
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

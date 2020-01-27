package com.hellokoding.account.managers;

import com.hellokoding.account.HibernateUtil;
import com.hellokoding.account.model.GamesEntity;
import com.hellokoding.account.model.GamesstatesEntity;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

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

    synchronized public GamesEntity getLastGame(){
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<GamesEntity> games = null;

        try{
            SQLQuery sqlQuery = session.createSQLQuery("SELECT * FROM games");
            games = (List<GamesEntity>) sqlQuery.addEntity(GamesEntity.class).list();
        }catch (HibernateException e){
            e.printStackTrace();
        } finally {
            session.close();
        }

        HibernateUtil.shutdown();
        try {
            return games.get(games.size() - 1);
        }
        catch (Exception e){
            return null;
        }
    }


}

package com.hellokoding.account.managers;

import com.hellokoding.account.HibernateUtil;
import com.hellokoding.account.model.GamesstatesEntity;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.Iterator;
import java.util.List;

public class GameStateManager {

    /* Method to CREATE an employee in the database */
    public void addState(int gameID,String grid, String lastMove, String penultimateMove, String nextMove){
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;

        try {
            tx = session.beginTransaction();
            GamesstatesEntity state = new GamesstatesEntity();
            state.setGameId(gameID);
            state.setGrid(grid);
            state.setLastMove(lastMove);
            state.setPenultimateMove(penultimateMove);
            state.setNextMove(nextMove);
            session.save(state);
            tx.commit();
        } catch (HibernateException e) {
            if (tx!=null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }

        HibernateUtil.shutdown();
    }

    /* Method to DELETE an employee from the records */
    public void deleteEmployee(Integer GameID){
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;

        try {
            tx = session.beginTransaction();
            List<GamesstatesEntity> states = (List<GamesstatesEntity>) session.createCriteria(GamesstatesEntity.class).list();
            for (Iterator iterator = states.iterator(); iterator.hasNext();){
                GamesstatesEntity state = (GamesstatesEntity) iterator.next();
                if(state.getGameId() == GameID) session.delete(state);
            }
            tx.commit();
        } catch (HibernateException e) {
            if (tx!=null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }


    synchronized public GamesstatesEntity getLastGameState(int id){
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<GamesstatesEntity> states = null;

        try{
            SQLQuery sqlQuery = session.createSQLQuery("SELECT * FROM gamesstates WHERE gameID = :param");
            sqlQuery.setParameter("param",id);
            states = (List<GamesstatesEntity>) sqlQuery.addEntity(GamesstatesEntity.class).list();
        }catch (HibernateException e){
            e.printStackTrace();
        } finally {
            session.close();
        }

        HibernateUtil.shutdown();

        return states.get(states.size() - 1);
    }
}

package com.hellokoding.account.managers;

import com.hellokoding.account.HibernateUtil;
import com.hellokoding.account.model.GamesstatesEntity;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.Iterator;
import java.util.List;

public class GameStateManager {


    public static void main(String[] args) {


        GameStateManager ME = new GameStateManager();

        /* Add few employee records in database */
        ME.addState(1, "aaa", "aabba","bbb");
         ME.addState(1, "ccc", "aaa","bbb");
        ME.addState(2, "Pasd", "kkkk","asaff");

        /* List down all the employees */
        // ME.listEmployees();

        /* Update employee's records */
        //  ME.updateEmployee(empID1, 5000);

        /* Delete an employee from the database */
        // ME.deleteEmployee(empID2);

        /* List down new list of the employees */
        // ME.listEmployees();
    }

    /* Method to CREATE an employee in the database */
    public void addState(int gameID,String grid, String lastMove, String penultimateMove){
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;

        try {
            tx = session.beginTransaction();
            GamesstatesEntity state = new GamesstatesEntity();
            state.setGameId(gameID);
            state.setGrid(grid);
            state.setLastMove(lastMove);
            state.setPenultimateMove(penultimateMove);
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

    /* Method to  READ all the employees
    public void listEmployees( ){
        Session session = factory.openSession();
        Transaction tx = null;

        try {
            tx = session.beginTransaction();
            List employees = session.createQuery("FROM Employee").list();
            for (Iterator iterator = employees.iterator(); iterator.hasNext();){
                Grid employee = (Grid) iterator.next();
                System.out.print("First Name: " + employee.getFirstName());
                System.out.print("  Last Name: " + employee.getLastName());
                System.out.println("  Salary: " + employee.getSalary());
            }
            tx.commit();
        } catch (HibernateException e) {
            if (tx!=null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }*/

    /* Method to UPDATE salary for an employee
    public void updateEmployee(Integer EmployeeID, int salary ){
        Session session = factory.openSession();
        Transaction tx = null;

        try {
            tx = session.beginTransaction();
            Employee employee = (Employee)session.get(Employee.class, EmployeeID);
            employee.setSalary( salary );
            session.update(employee);
            tx.commit();
        } catch (HibernateException e) {
            if (tx!=null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }*/

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

    public GamesstatesEntity getLastGameState(int id){
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<GamesstatesEntity> states = null;

        try{
            states = (List<GamesstatesEntity>) session.createCriteria(GamesstatesEntity.class).list();
        }catch (HibernateException e){
            e.printStackTrace();
        } finally {
            session.close();
        }

        HibernateUtil.shutdown();

        return states.get(-1);
    }
}

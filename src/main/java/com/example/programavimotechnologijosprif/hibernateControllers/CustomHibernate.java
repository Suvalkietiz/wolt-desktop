package com.example.programavimotechnologijosprif.hibernateControllers;

import com.example.programavimotechnologijosprif.model.Food;
import com.example.programavimotechnologijosprif.model.FoodOrder;
import com.example.programavimotechnologijosprif.model.Restaurant;
import com.example.programavimotechnologijosprif.model.User;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Query;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Order;
import jakarta.persistence.criteria.Root;

import java.util.ArrayList;
import java.util.List;

public class CustomHibernate extends GenericHibernate {
    public CustomHibernate(EntityManagerFactory entityManagerFactory) {
        super(entityManagerFactory);
    }

    public User getUserByCrediantials(String login, String password) {
        User user = null;
        try{
            entityManager = entityManagerFactory.createEntityManager();
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<User> cq = cb.createQuery(User.class);
            //CriteriaQuery<User> cq = entityManager.getCriteriaBuilder().createQuery(User.class);
            Root<User> root = cq.from(User.class); // cia yra baze nuo kurios lipdau uzkluasa

            cq.select(root).where(cb.and(
                    cb.equal(root.get("login"), login),
                    cb.equal(root.get("password"), password)
            ));
            Query q = entityManager.createQuery(cq);
            user = (User)q.getSingleResult();
        } catch (Exception e){
            // pagalvopsim ka daryt
        } finally {
            if(entityManager != null){
                entityManager.close();
            }
        }
        return user;
    }



    public List<FoodOrder> getRestaurantOrders(Restaurant restaurant) {
        List<FoodOrder> foodOrders = new ArrayList<>();
        try{
            entityManager = entityManagerFactory.createEntityManager();
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<FoodOrder> cq = cb.createQuery(FoodOrder.class);
            Root<FoodOrder> root = cq.from(FoodOrder.class); // cia yra baze nuo kurios lipdau uzkluasa

            cq.select(root).where(
                cb.equal(root.get("restaurant"), restaurant)
            );
            Query q = entityManager.createQuery(cq);
            foodOrders = q.getResultList();
        } catch (Exception e){
            // pagalvopsim ka daryt
        } finally {
            if(entityManager != null){
                entityManager.close();
            }
        }
        return foodOrders;
    }


    public List<Food> getRestaurantFood(Restaurant restaurant) {
        List<Food> food = new ArrayList<>();
        try{
            entityManager = entityManagerFactory.createEntityManager();
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<Food> cq = cb.createQuery(Food.class);
            Root<Food> root = cq.from(Food.class); // cia yra baze nuo kurios lipdau uzkluasa

            cq.select(root).where(
                    cb.equal(root.get("restaurant"), restaurant)
            );
            Query q = entityManager.createQuery(cq);
            food = q.getResultList();
        } catch (Exception e){
            // pagalvopsim ka daryt
        } finally {
            if(entityManager != null){
                entityManager.close();
            }
        }
        return food;
    }






}

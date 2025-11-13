package com.example.programavimotechnologijosprif.hibernateControllers;

import com.example.programavimotechnologijosprif.Utils.FxUtils;
import com.example.programavimotechnologijosprif.model.*;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Query;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Order;
import jakarta.persistence.criteria.Root;
import javafx.scene.control.Alert;

import java.time.LocalDate;
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



    private List<FoodOrder> getUserFoodOrders(String attributeNameInFoodOrders, Object userEntity) {
        List<FoodOrder> foodOrders = new ArrayList<>();
        try{
            entityManager = entityManagerFactory.createEntityManager();
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<FoodOrder> cq = cb.createQuery(FoodOrder.class);
            Root<FoodOrder> root = cq.from(FoodOrder.class); // cia yra baze nuo kurios lipdau uzkluasa

            cq.select(root).where(
                    cb.equal(root.get(attributeNameInFoodOrders), userEntity)
            );
            foodOrders = entityManager.createQuery(cq).getResultList();

            //chatgpt for looP:
            // ✅ FORCE LOAD lazy relationships BEFORE closing EM
            for (FoodOrder order : foodOrders) {

                // These are safe and trigger the lazy loads:
                if (order.getAppUser() != null)
                    order.getAppUser().getId();

                if (order.getDriver() != null)
                    order.getDriver().getId();

                if (order.getRestaurant() != null)
                    order.getRestaurant().getId();

                // Trigger load of the items collection
                order.getItems().size();
            }

        } catch (Exception e){
            e.printStackTrace();
        } finally {
            if(entityManager != null){
                entityManager.close();
            }
        }
        return foodOrders;
    }

    public List<FoodOrder> getAppUserOrders(AppUser user) {
        return getUserFoodOrders("appUser", user);
    }

    public List<FoodOrder> getRestaurantOrders(Restaurant restaurant) {

        return getUserFoodOrders("restaurant", restaurant);
    }

    public List<FoodOrder> getDriverOrders(Driver driver) {
        return getUserFoodOrders("driver", driver);
    }


    public List<Message> getChatMessages(Chat chat) {
        List<Message> messages = new ArrayList<>();
        try{
            entityManager = entityManagerFactory.createEntityManager();
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<Message> cq = cb.createQuery(Message.class);
            Root<Message> root = cq.from(Message.class);
            System.out.println("Chat id: " + chat.getId());
            cq.select(root)
                    .where(cb.equal(root.get("chat").get("id"), chat.getId()))
                    .orderBy(cb.asc(root.get("timestamp")));
            Query q = entityManager.createQuery(cq);
            messages = q.getResultList();
        }  catch (Exception e){
            FxUtils.generateAlert(Alert.AlertType.ERROR, "Db error", "CustomHibernated failed to read Chat MEssages");
            e.printStackTrace();
        } finally {
            if(entityManager != null){
                entityManager.close();
            }
        }
        return messages;
    }





}

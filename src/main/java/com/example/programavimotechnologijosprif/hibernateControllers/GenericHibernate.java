/**
 * Sioje klaseje bus aprasyta bendra logika kaip updating, issaugot, trint ir perziuret DB duomenis
 */

package com.example.programavimotechnologijosprif.hibernateControllers;

import com.example.programavimotechnologijosprif.Utils.FxUtils;
import com.example.programavimotechnologijosprif.model.Driver;
import com.example.programavimotechnologijosprif.model.FoodOrder;
import com.example.programavimotechnologijosprif.model.Restaurant;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Query;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import javafx.scene.control.Alert;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class GenericHibernate {
    protected EntityManagerFactory entityManagerFactory;
    protected EntityManager entityManager;

    public GenericHibernate(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    public <T> void createEntity(T entity, String header, String alertMessage) {
        try{
            entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();
            entityManager.persist(entity); // persist = insert
            entityManager.getTransaction().commit();
        } catch (Exception ex){
            // noresiu ismest alert zmogui kad zinotu db operacijos metu buvo klaida
            //FxUtils.generateAlert(Alert.AlertType.INFORMATION, "Oh no", "DB error", "Something went wrong with createEntity");
            FxUtils.generateHeaderAlert(Alert.AlertType.ERROR, "DataBase Error", header, alertMessage);
        } finally {
            if(entityManager != null){
                entityManager.close();
            }
        }
    }

    public <T> void updateEntity(T entity) {
        try{
            entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();
            entityManager.merge(entity); // merge = update
            entityManager.getTransaction().commit();
        } catch (Exception ex){
            // noresiu ismest alert zmogui kad zinotu db operacijos metu buvo klaida
            FxUtils.generateExcetionAlert(ex);
        } finally {
            if(entityManager != null){
                entityManager.close();
            }
        }
    }

    public <T> void deleteEntity(T entity) {
        try{
            entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();
            entityManager.remove(entity);
            entityManager.getTransaction().commit();
        } catch (Exception ex){
            // noresiu ismest alert zmogui kad zinotu db operacijos metu buvo klaida

        } finally {
            if(entityManager != null){
                entityManager.close();
            }
        }
    }

    public <T> void deleteEntityById(Class<T> entityClass, int id) {
        try{
            entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();
            T entity = entityManager.find(entityClass, id);
            entityManager.remove(entity);
            entityManager.getTransaction().commit();
        } catch (Exception ex){
            // noresiu ismest alert zmogui kad zinotu db operacijos metu buvo klaida

        } finally {
            if(entityManager != null){
                entityManager.close();
            }
        }
    }

    public <T> List<T> getAllRecords(Class<T> entityClass) {
        List<T> list = new ArrayList<>();
        try{
            entityManager = entityManagerFactory.createEntityManager();
            CriteriaQuery cq = entityManager.getCriteriaBuilder().createQuery();
            cq.select(cq.from(entityClass));
            Query q = entityManager.createQuery(cq);
            list = q.getResultList();
        } catch (Exception ex){
            ex.printStackTrace(); // sugalvosiu kaip cia geriau
        } finally {
            if(entityManager != null){
                entityManager.close();
            }
        }
        return list;
    }


    public <T> T getEntityById(Class<T> entityClass, int id){
        T entity = null;
        try{
            entityManager = entityManagerFactory.createEntityManager();
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<T> cq = cb.createQuery(entityClass);
            Root<T> root = cq.from(entityClass);
            cq.select(root).where(cb.equal(root.get("id"), id));
            entity = entityManager.createQuery(cq).getSingleResult();
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            if(entityManager != null){
                entityManager.close();
            }
        }
        return entity;
    }




    protected List<FoodOrder> getUserFoodOrders(String attributeNameInFoodOrders, Object userEntity) {
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

}

/**
 * Sioje klaseje bus aprasyta bendra logika kaip updating, issaugot, trint ir perziuret DB duomenis
 */

package com.example.programavimotechnologijosprif.hibernateControllers;

import com.example.programavimotechnologijosprif.Utils.FxUtils;
import com.example.programavimotechnologijosprif.model.Admin;
import com.example.programavimotechnologijosprif.model.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Query;
import jakarta.persistence.criteria.CriteriaQuery;
import javafx.scene.control.Alert;

import java.util.ArrayList;
import java.util.List;

public class GenericHibernate {
    protected EntityManagerFactory entityManagerFactory;
    protected EntityManager entityManager;

    public GenericHibernate(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    public <T> void createEntity(T entity) {
        try{
            entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();
            entityManager.persist(entity); // persist = insert
            entityManager.getTransaction().commit();
        } catch (Exception ex){
            // noresiu ismest alert zmogui kad zinotu db operacijos metu buvo klaida
            FxUtils.generateAlert(Alert.AlertType.INFORMATION, "Oh no", "DB error", "Something went wrong with createEntity");
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
}

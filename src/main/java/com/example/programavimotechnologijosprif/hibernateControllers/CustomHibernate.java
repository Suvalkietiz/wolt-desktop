package com.example.programavimotechnologijosprif.hibernateControllers;

import com.example.programavimotechnologijosprif.model.User;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Query;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

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
        }
        return user;
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.kopylov.dao;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import ru.kopylov.persist.PostBox;

/**
 *
 * @author Сергей
 */
@Stateless
public class PostBoxFacade extends AbstractFacade<PostBox> {

    @PersistenceContext(unitName = "EmailServerPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public PostBoxFacade() {
        super(PostBox.class);
    }

    public PostBox findByLogin(String login) {
        TypedQuery<PostBox> query = em.createQuery("SELECT p FROM PostBox p WHERE p.login=:login", PostBox.class);
        return query.setParameter("login", login).getSingleResult();
    }

    public boolean isLoginExists(String login) {
        TypedQuery<PostBox> query = em.createQuery("SELECT p FROM PostBox p WHERE p.login=:login", PostBox.class);
        List<PostBox> pbList = query.setParameter("login", login).getResultList();

        if (pbList.isEmpty()) {
            return false;
        } else {
            return true;
        }
    }

}

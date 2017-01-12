/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.kopylov.dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import ru.kopylov.persist.PostBox;

/**
 *
 * @author Сергей
 */
public class LogineFacade extends AbstractFacade<PostBox>{
    
    @PersistenceContext(unitName = "EmailServerPU")
    private EntityManager em;

    public LogineFacade(Class<PostBox> entityClass) {
          super(PostBox.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
}

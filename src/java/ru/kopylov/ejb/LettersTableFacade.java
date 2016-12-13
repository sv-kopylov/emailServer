/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.kopylov.ejb;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import ru.kopylov.persist.LettersTable;

/**
 *
 * @author Сергей
 */
@Stateless
public class LettersTableFacade extends AbstractFacade<LettersTable> {

    @PersistenceContext(unitName = "EmailServerPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public LettersTableFacade() {
        super(LettersTable.class);
    }
    
}

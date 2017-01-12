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
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import ru.kopylov.persist.LettersTable;
import ru.kopylov.persist.LettersTable_;
import ru.kopylov.persist.PostBox;

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
    
     public List<LettersTable> findReceivedRange (int[] range, PostBox postBox) {
         
         System.out.println("findReceivedRange invoked");
                
        javax.persistence.criteria.CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
        javax.persistence.criteria.CriteriaQuery cq = builder.createQuery(LettersTable.class);
        Root root = cq.from(LettersTable.class);
        Predicate eq = builder.equal(root.get(LettersTable_.relation), LettersTable.Relation.RECEIVED);
        Predicate eqPostBox = builder.equal(root.get(LettersTable_.postBox), postBox);
        Predicate and = builder.and(eq, eqPostBox);
        cq.where(and);
        cq.select(root);
        javax.persistence.Query q = getEntityManager().createQuery(cq);
        q.setMaxResults(range[1] - range[0] + 1);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }
     
        public List<LettersTable> findSentRange (int[] range, PostBox postBox) {
                
        javax.persistence.criteria.CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
        javax.persistence.criteria.CriteriaQuery cq = builder.createQuery(LettersTable.class);
        Root root = cq.from(LettersTable.class);
        Predicate eq = builder.equal(root.get(LettersTable_.relation), LettersTable.Relation.SENT);
        Predicate eqPostBox = builder.equal(root.get(LettersTable_.postBox), postBox);
        Predicate and = builder.and(eq, eqPostBox);
        cq.where(and);
        cq.select(root);
        javax.persistence.Query q = getEntityManager().createQuery(cq);
        q.setMaxResults(range[1] - range[0] + 1);
        q.setFirstResult(range[0]);
            
        return q.getResultList();
    }
   
    public int countReceived(PostBox postBox) {
        javax.persistence.criteria.CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
        javax.persistence.criteria.CriteriaQuery cq = builder.createQuery();
        javax.persistence.criteria.Root<LettersTable> root = cq.from(LettersTable.class);
        Predicate equalRelation = builder.equal(root.get(LettersTable_.relation), LettersTable.Relation.RECEIVED);
        Predicate eqPostBox = builder.equal(root.get(LettersTable_.postBox), postBox);
        Predicate and = builder.and(equalRelation, eqPostBox);
        cq.where(and);
        cq.select(builder.count(root));
        javax.persistence.Query q = getEntityManager().createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }
    
    public int countSent(PostBox postBox) {
                javax.persistence.criteria.CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
        javax.persistence.criteria.CriteriaQuery cq = builder.createQuery();
        javax.persistence.criteria.Root<LettersTable> root = cq.from(LettersTable.class);
        Predicate equalRelation = builder.equal(root.get(LettersTable_.relation), LettersTable.Relation.SENT);
        Predicate eqPostBox = builder.equal(root.get(LettersTable_.postBox), postBox);
        Predicate and = builder.and(equalRelation, eqPostBox);
        cq.where(and);
        cq.select(builder.count(root));
        javax.persistence.Query q = getEntityManager().createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }
}

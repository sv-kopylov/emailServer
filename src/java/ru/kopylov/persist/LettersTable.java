/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.kopylov.persist;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Сергей
 */
@Entity
public class LettersTable implements Serializable {
    
    
    @ManyToOne
    private PostBox postBox;
    
    @ManyToOne
    private Letter letter;
    
    @Enumerated (EnumType.STRING)
    private Relation relation;
    
    @Temporal (value = TemporalType.TIMESTAMP)
    private Date timestamp;
    

  

   

    public Letter getLetter() {
        System.out.println("getLetter() invoked");
        return letter;
    }

    public void setLetter(Letter letter) {
        this.letter = letter;
    }

    public Relation getRelation() {
        System.out.println("getRelation() invoked");
        return relation;
    }

    public void setRelation(Relation relation) {
        this.relation = relation;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public PostBox getPostBox() {
        return postBox;
    }

    public void setPostBox(PostBox postBox) {
        this.postBox = postBox;
    }
    
    public enum Relation {SENT, RECEIVED}
    
    
    
    

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof LettersTable)) {
            return false;
        }
        LettersTable other = (LettersTable) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ru.kopylov.persist.LettersTable[ id=" + id + " ]";
    }
    
}

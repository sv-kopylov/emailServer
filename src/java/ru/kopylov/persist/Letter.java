/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.kopylov.persist;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

/**
 *
 * @author Сергей
 */
@Entity
public class Letter implements Serializable {
    
    String subject;
    String letter;
    
    @OneToMany (mappedBy = "letter")
    private List<LettersTable> thisLetterBoxes;

    public String getSubject() {
//        System.out.println("It is getSubject meth invoked");
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getLetter() {
        return letter;
    }

    public void setLetter(String letter) {
        this.letter = letter;
    }
    
    

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
        if (!(object instanceof Letter)) {
            return false;
        }
        Letter other = (Letter) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ru.kopylov.persist.Letter[ id=" + id + " ]";
    }

    public List<LettersTable> getThisLetterBoxes() {
        return thisLetterBoxes;
    }

    public void setThisLetterBoxes(List<LettersTable> thisLetterBoxes) {
        this.thisLetterBoxes = thisLetterBoxes;
    }
    
}

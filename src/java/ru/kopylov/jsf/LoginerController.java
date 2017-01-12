/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.kopylov.jsf;

import java.io.Serializable;
import java.util.ResourceBundle;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import ru.kopylov.dao.PostBoxFacade;
import ru.kopylov.jsf.util.JsfUtil;
import ru.kopylov.persist.PostBox;

/**
 *
 * @author Сергей
 */
@Named(value = "loginerController")
@SessionScoped
public class LoginerController implements Serializable {

    private PostBox current;

    @EJB
    private ru.kopylov.dao.PostBoxFacade ejbFacade;
    
    public String prepareCreate() {
        current = new PostBox();
        return "Create";
    }
    
     private PostBoxFacade getFacade() {
        return ejbFacade;
    }

    public String create() {
        try {
            getFacade().create(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("PostBoxCreated"));
            return prepareCreate();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }
}

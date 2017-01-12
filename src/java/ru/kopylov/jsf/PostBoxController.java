package ru.kopylov.jsf;

import ru.kopylov.persist.PostBox;
import ru.kopylov.jsf.util.JsfUtil;
import ru.kopylov.jsf.util.PaginationHelper;
import ru.kopylov.dao.PostBoxFacade;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;
import ru.kopylov.dao.LetterFacade;
import ru.kopylov.dao.LettersTableFacade;
import ru.kopylov.persist.Letter;
import ru.kopylov.persist.LettersTable;

@Named("postBoxController")
@SessionScoped
public class PostBoxController implements Serializable {

    private PostBox current;
    private Letter currentLetter;
    
    private DataModel items = null;
    private DataModel reseivedLetItems = null;
    private DataModel sentLetItems = null;
    
    private boolean receivedVisible = true;

    public boolean isReceivedVisible() {
        return receivedVisible;
    }

    public void setReceivedVisible(boolean receivedVisible) {
        this.receivedVisible = receivedVisible;
    }
    
    @EJB
    private ru.kopylov.dao.PostBoxFacade ejbFacade;
    @EJB
    private ru.kopylov.dao.LettersTableFacade ejbLetTableFacade;
    @EJB
    private ru.kopylov.dao.LetterFacade ejbLetterFacade;
    
    private PaginationHelper pagination;
    private PaginationHelper receivedLetterspagination;
    private PaginationHelper sentLetterspagination;
    private int selectedItemIndex;
    
    

    public PostBoxController() {
    }

    public PostBox getSelected() {
        if (current == null) {
            current = new PostBox();
            selectedItemIndex = -1;
        }
        return current;
    }
     public Letter getCurrentLetter() {
        if (currentLetter == null) {
            currentLetter = new Letter();
                    }
        return currentLetter;
    }
     public String prepareCreateLetter(){
         currentLetter = new Letter();
         return "CreateLetter";
     }

    private PostBoxFacade getFacade() {
        return ejbFacade;
    }
    private LettersTableFacade getEjbLetTableFacade() {
        return ejbLetTableFacade;
    }
    private LetterFacade getEjbLetterFacade() {
        return ejbLetterFacade;
    }
    public PaginationHelper getPagination() {
        if (pagination == null) {
            pagination = new PaginationHelper(10) {

                @Override
                public int getItemsCount() {
                    return getFacade().count();
                }

                @Override
                public DataModel createPageDataModel() {
                    return new ListDataModel (getFacade().findRange(new int[]{getPageFirstItem(), getPageFirstItem() + getPageSize()}));
                }
            };
        }
        return pagination;
    }
    public PaginationHelper getReceivedLetterPagination() {
        if (receivedLetterspagination == null) {
            receivedLetterspagination = new PaginationHelper(10) {

                @Override
//                изменить на countReceived
                public int getItemsCount() {
                    
                    return getEjbLetTableFacade().countReceived(current);
                }

                @Override
                public DataModel createPageDataModel() {
                    return new ListDataModel<LettersTable>(getEjbLetTableFacade().findReceivedRange(new int[]{getPageFirstItem(), getPageFirstItem() + getPageSize()}, current));
//                    return new ListDataModel(current.getThisBoxLetters());
                }
            };
            
        }
        
        return receivedLetterspagination;
     }
    public PaginationHelper getSentLetterPagination() {
        if (sentLetterspagination == null) {
            sentLetterspagination = new PaginationHelper(10) {

                @Override
                public int getItemsCount() {
                    
                    return getEjbLetTableFacade().countSent(current);
                }

                @Override
                public DataModel createPageDataModel() {
                    return new ListDataModel(getEjbLetTableFacade().findSentRange(new int[]{getPageFirstItem(), getPageFirstItem() + getPageSize()}, current));
                }
            };
        }
        return sentLetterspagination;
     }

    public String prepareList() {
        recreateModel();
        return "List";
    }

    public String prepareView() {
        current = (PostBox) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "View";
    }

    public String prepareCreate() {
        current = new PostBox();
        selectedItemIndex = -1;
        return "Create";
    }

    public String create() {
        try {
            if ( !getFacade().isLoginExists(current.getLogin())){
            getFacade().create(current);
            JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Bundle").getString("PostBoxCreated"));
            return "Received";
            } else {
                JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Bundle").getString("PostBoxExists"));
            }
            return null;
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }
    
       public String createLetter() {
        try {
            getEjbLetterFacade().create(currentLetter);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("LetterCreated"));
            return prepareCreate();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }
    
    public String checkLogin() {
        try {
            PostBox findBox = getFacade().findByLogin(current.getLogin());
            if (findBox!=null&&findBox.getPassword().equals(current.getPassword())){
                current = findBox;
                return "Received";
            } else if (findBox==null){
                JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Bundle").getString("AccountNotFound"));
                return null;
            } else if (!findBox.getPassword().equals(current.getPassword())){
                JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Bundle").getString("WrongPassword"));
                current = null;
                return null;
            }
                        
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            
        }
        return null;
    }

    public String prepareEdit() {
        current = (PostBox) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "Edit";
    }

    public String update() {
        try {
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("PostBoxUpdated"));
            return "View";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String destroy() {
        current = (PostBox) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        performDestroy();
        recreatePagination();
        recreateModel();
        return "List";
    }

    public String destroyAndView() {
        performDestroy();
        recreateModel();
        updateCurrentItem();
        if (selectedItemIndex >= 0) {
            return "View";
        } else {
            // all items were removed - go back to list
            recreateModel();
            return "List";
        }
    }

    private void performDestroy() {
        try {
            getFacade().remove(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("PostBoxDeleted"));
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
        }
    }

    private void updateCurrentItem() {
        int count = getFacade().count();
        if (selectedItemIndex >= count) {
            // selected index cannot be bigger than number of items:
            selectedItemIndex = count - 1;
            // go to previous page if last page disappeared:
            if (pagination.getPageFirstItem() >= count) {
                pagination.previousPage();
            }
        }
        if (selectedItemIndex >= 0) {
            current = getFacade().findRange(new int[]{selectedItemIndex, selectedItemIndex + 1}).get(0);
        }
    }

    public DataModel getItems() {
        if (items == null) {
            items = getPagination().createPageDataModel();
        }
        return items;
    }
    
    public DataModel getReseivedLetItems() {
        if (reseivedLetItems == null) {
            reseivedLetItems = getReceivedLetterPagination().createPageDataModel();
            
            Iterator a = reseivedLetItems.iterator();
            LettersTable lt;
            while (a.hasNext()){
                lt = (LettersTable)a.next();
                System.out.println(lt.getId());
                System.out.println(lt.getLetter().getSubject());
//                System.out.println(lt.getLetter().);
               
                System.out.println(lt.getPostBox().getLogin());
               
            }
            
        }
        return reseivedLetItems;
    }
     
    public DataModel getSentLetItems() {
        if (sentLetItems == null) {
            sentLetItems = getSentLetterPagination().createPageDataModel();
        }
        return sentLetItems;
    }

    private void recreateModel() {
        items = null;
    }

    private void recreatePagination() {
        pagination = null;
    }
    
    public String next() {
        getPagination().nextPage();
        recreateModel();
        return "List";
    }
    public String nextReceivedLet() {
        getReceivedLetterPagination().nextPage();
        recreateModel();
        return "Received";
    }

    public String previous() {
        getPagination().previousPage();
        recreateModel();
        return "List";
    }
    
    public String previousReceivedLet() {
        getReceivedLetterPagination().previousPage();
        recreateModel();
        return "Received";
    }

    public SelectItem[] getItemsAvailableSelectMany() {
        return JsfUtil.getSelectItems(ejbFacade.findAll(), false);
    }

    public SelectItem[] getItemsAvailableSelectOne() {
        return JsfUtil.getSelectItems(ejbFacade.findAll(), true);
    }

    public PostBox getPostBox(java.lang.Long id) {
        return ejbFacade.find(id);
    }

     public String setReceived() {
        receivedVisible = true;
        return null;
    }
     
      public String setSent() {
        receivedVisible = false;
        return null;
    }

    public String logOut (){
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        return "login";
    }
    
    public String prepareLogin (){
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        return "login";
    }
    

    @FacesConverter(forClass = PostBox.class)
    public static class PostBoxControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            PostBoxController controller = (PostBoxController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "postBoxController");
         
            return controller.getPostBox(getKey(value));
        }

        java.lang.Long getKey(String value) {
            java.lang.Long key;
            key = Long.valueOf(value);
            return key;
        }

        String getStringKey(java.lang.Long value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value);
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof PostBox) {
                PostBox o = (PostBox) object;
                return getStringKey(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + PostBox.class.getName());
            }
        }
        
      

    }
    
  

}

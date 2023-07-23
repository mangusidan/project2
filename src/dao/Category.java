/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Admin
 */
public class Category {

    private ObjectProperty<Integer> categoryId;
    private StringProperty categoryName;
    private ObjectProperty<Integer> parentId;
    private StringProperty parent;

    public Category() {
        this.categoryId = new SimpleObjectProperty<>();
        this.categoryName = new SimpleStringProperty();
        this.parentId = new SimpleObjectProperty<>();
        this.parent = new SimpleStringProperty();

    }

    public Integer getCategoryId() {
        return categoryId.get();
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId.set(categoryId);
    }

    public String getCategoryName() {
        return categoryName.get();
    }

    public void setCategoryName(String categoryName) {
        this.categoryName.set(categoryName);
    }

    public Integer getParentId() {
        return parentId.get();
    }

    public void setParentId(Integer parentId) {
        this.parentId.set(parentId);
    }
    
    public String getParent() {
        return parent.get();
    }

    public void setParent(String parent) {
        this.parent.set(parent);
    }
    
    public ObjectProperty<Integer> getCategoryIdProperty() {
        return categoryId;
    }

    public StringProperty getCategoryNameProperty() {
        return categoryName;
    }

    public ObjectProperty<Integer> getParentIdProperty() {
        return parentId;
    }
    
    public StringProperty getParentProperty() {
        return parent;
    }
    
    @Override
    public String toString(){
        return categoryName.get();
    }
    
}

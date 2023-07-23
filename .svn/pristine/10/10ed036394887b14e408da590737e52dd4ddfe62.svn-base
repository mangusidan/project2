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
public class Status {
    
    private ObjectProperty<Integer> statusCode;
    private StringProperty statusName;

    public Status() {
        this.statusCode = new SimpleObjectProperty<>();
        this.statusName = new SimpleStringProperty();

    }

    public Integer getStatusCode() {
        return statusCode.get();
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode.set(statusCode);
    }

    public String getStatusName() {
        return statusName.get();
    }

    public void setStatusName(String statusName) {
        this.statusName.set(statusName);
    }

    public ObjectProperty<Integer> getStatusCodeProperty() {
        return statusCode;
    }

    public StringProperty getStatusNameProperty() {
        return statusName;
    }

    @Override
    public String toString(){
        return statusName.get();
    }
}

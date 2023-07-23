/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.util.Date;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Admin
 */
public class Order {

    private ObjectProperty<Integer> orderId;
    private ObjectProperty<Integer> customerId;
    private StringProperty customerName;
    private ObjectProperty<Double> total;
    private ObjectProperty<Date> date;
    private ObjectProperty<Integer> statusCode;
    private StringProperty status;

    public Order() {
        this.orderId = new SimpleObjectProperty<>();
        this.customerId = new SimpleObjectProperty<>();
        this.customerName = new SimpleStringProperty();
        this.total = new SimpleObjectProperty<>();
        this.date = new SimpleObjectProperty<>();
        this.statusCode = new SimpleObjectProperty<>();
        this.status = new SimpleStringProperty();
    }

    public Integer getOrderId() {
        return orderId.get();
    }

    public Integer getCustomerId() {
        return customerId.get();
    }

    public String getCustomerName() {
        return customerName.get();
    }

    public Date getDate() {
        return date.get();
    }

    public Integer getStatusCode() {
        return statusCode.get();
    }

    public String getStatus() {
        return status.get();
    }
    
    public Double getTotal() {
        return total.get();
    }
    
    public void setOrderId(Integer orderId) {
        this.orderId.set(orderId);
    }

    public void setCustomerId(Integer customerId) {
        this.customerId.set(customerId);
    }

    public void setCustomerName(String customerName) {
        this.customerName.set(customerName);
    }

    public void setTotal(Double total) {
        this.total.set(total);
    }

    public void setDate(Date date) {
        this.date.set(date);
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode.set(statusCode);
    }

    public void setStatus(String status) {
        this.status.set(status);
    }

    public ObjectProperty<Integer> getOrderIdProperty() {
        return orderId;
    }

    public ObjectProperty<Integer> getCustomerIdProperty() {
        return customerId;
    }

    public StringProperty getCustomerNameProperty() {
        return customerName;
    }

    public ObjectProperty<Double> getTotalProperty() {
        return total;
    }

    public ObjectProperty<Date> getDateProperty() {
        return date;
    }

    public StringProperty getStatusProperty() {
        return status;
    }
}

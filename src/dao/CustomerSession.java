/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author Admin
 */
public class CustomerSession {
    private static CustomerSession instance;
    
    private String email;
    
    private ObservableList<Product> allProducts;
    private ObservableList<OrderDetail> cart = FXCollections.observableArrayList();
    
    private CustomerSession(){
    }
    
    public static CustomerSession getInstance(){
        if(instance==null){
            instance = new CustomerSession();
        } 
        return instance;
    }

    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public ObservableList<Product> getProducts() {
        return this.allProducts;
    }
    
    public void setProducts() {
        ProductDAO productDao = new ProductDAO();
        this.allProducts = productDao.selectAllActive();
    }
    
    public ObservableList<OrderDetail> getCart() {
        return this.cart;
    }
    
    
    public void clearCart() {
        this.cart.clear();
    }
    
    public void clearCustomerSession() {
        instance = null;
    }
}

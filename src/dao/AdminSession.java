/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

/**
 *
 * @author Admin
 */
public final class AdminSession {
    private static AdminSession instance;
    
    private String email;
    
    private AdminDAO adminDao = new AdminDAO();
    
    private AdminSession(){
    }
    
    public static AdminSession getInstance(){
        if(instance==null){
            instance = new AdminSession();
        } 
        return instance;
    }

    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public void clearAdminSession() {
        instance = null;
    }
}

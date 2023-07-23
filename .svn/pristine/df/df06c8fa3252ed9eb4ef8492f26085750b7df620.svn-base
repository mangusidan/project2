/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import dbconnection.DbFactory;
import static dbconnection.DbType.MYSQL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author Admin
 */
public class AdminDAO implements DAO<Admin> {

    @Override
    public ObservableList<Admin> selectAll() {
        ObservableList<Admin> admins = FXCollections.observableArrayList();

        try (
                Connection conn = DbFactory.getConnection(MYSQL);
                Statement stm = conn.createStatement();
                ResultSet rs = stm.executeQuery("SELECT * FROM Admin");) {
            while (rs.next()) {
                Admin a = new Admin();
                a.setAdminId(rs.getInt("adminID"));
                a.setAdminName(rs.getString("adminName"));
                a.setEmail(rs.getString("email"));
                a.setPassword(rs.getString("password_hash"));

                admins.add(a);
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return admins;
    }

    @Override
    public Admin insert(Admin newAdmin) {
        String sql = "INSERT into admin (adminName,email,password_hash) "
                + "VALUES (?, ?, ?)";
        ResultSet key = null;
        try (
                Connection conn = DbFactory.getConnection(MYSQL);
                PreparedStatement stmt
                = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);) {

            stmt.setString(1, newAdmin.getAdminName());
            stmt.setString(2, newAdmin.getEmail());
            stmt.setString(3, newAdmin.getPassword());

            int rowInserted = stmt.executeUpdate();

            if (rowInserted == 1) {
                key = stmt.getGeneratedKeys();
                key.next();
                int newKey = key.getInt(1);
                newAdmin.setAdminId(newKey);
                return newAdmin;
            } else {
                System.err.println("No admin inserted");
                return null;
            }

        } catch (Exception e) {
            System.err.println(e);
            return null;
        } finally {
            try {
                if (key != null) {
                    key.close();
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }

        }
    }

    @Override
    public boolean update(Admin admin) {
        String sql = "UPDATE admin SET `adminName` = ?, email = ?, password_hash = ? WHERE adminID = ?";
        try (
                Connection conn = DbFactory.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);) {
            stmt.setString(1, admin.getAdminName());
            stmt.setString(2, admin.getEmail());
            stmt.setString(3, admin.getPassword());
            stmt.setInt(4, admin.getAdminId());

            int updated_num_rows = stmt.executeUpdate();
            if (updated_num_rows == 1) {
                return true;
            } else {
//                logger.error(Translator.getResource().getString("update.error.message"));
                return false;
            }
        } catch (Exception e) {
//            logger.error(e.getMessage());
            return false;
        }
    }

    @Override
    public boolean delete(Admin admin) {
        String sql = "DELETE FROM admin WHERE adminID = ?;";
        try (
                Connection conn = DbFactory.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);) {
            stmt.setInt(1, admin.getAdminId());
            int del_num_rows = stmt.executeUpdate();
            if (del_num_rows == 1) {
                return true;
            } else {
//                logger.error(Translator.getResource().getString("delete.error.message"));
                return false;
            }
        } catch (Exception e) {
//            logger.error(e.getMessage());
            return false;
        }
    }

    @Override
    public Admin select(Integer i) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public Admin select(String email) {
        String sql = "SELECT * FROM admin WHERE email = ?";
        ResultSet rs = null;
        try (
                Connection conn = DbFactory.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);) {
            stmt.setString(1, email);
            rs = stmt.executeQuery();
            if (rs.next()) {
                Admin a = new Admin();
                a.setAdminId(rs.getInt("adminID"));
                a.setAdminName(rs.getString("adminName"));
                a.setEmail(rs.getString("email"));
                a.setPassword(rs.getString("password_hash"));
                return a;
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return null;
        }
        return null;
        
    }

    public boolean authenticateAdmin(String email, String password) {
        String sql = "SELECT * FROM Admin WHERE email = ? AND password_hash = ?";
        try (
                Connection conn = DbFactory.getConnection(MYSQL);
                PreparedStatement stmt = conn.prepareStatement(sql);) {
            stmt.setString(1, email);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return true;
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return false;
    }
    
    public boolean isUnique(String email){
        String sql = "SELECT * FROM admin WHERE email = ?";
        try (
                Connection conn = DbFactory.getConnection(MYSQL);
                PreparedStatement stmt = conn.prepareStatement(sql);) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return false;
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return true;
    }

}

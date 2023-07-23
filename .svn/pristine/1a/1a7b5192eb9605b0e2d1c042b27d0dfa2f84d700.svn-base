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
public class StatusDAO implements DAO<Status> {

    @Override
    public ObservableList<Status> selectAll() {
        ObservableList<Status> status = FXCollections.observableArrayList();

        try (
                Connection conn = DbFactory.getConnection();
                Statement stm = conn.createStatement();
                ResultSet rs = stm.executeQuery("SELECT * FROM `status`");) {
            while (rs.next()) {
                Status s = new Status();
                s.setStatusCode(rs.getInt(1));
                s.setStatusName(rs.getString(2));
                status.add(s);
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return status;
    }

    @Override
    public Status insert(Status newStatus) {
        String sql = "INSERT into `status` (statusName) VALUES (?)";
        ResultSet key = null;
        try (
                Connection conn = DbFactory.getConnection(MYSQL);
                PreparedStatement stmt
                = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);) {

            stmt.setString(1, newStatus.getStatusName());
            int rowInserted = stmt.executeUpdate();

            if (rowInserted == 1) {
                key = stmt.getGeneratedKeys();
                key.next();
                int newKey = key.getInt(1);
                newStatus.setStatusCode(newKey);
                return newStatus;
            } else {
                System.err.println("No status inserted");
                return null;
            }

        } catch (Exception e) {
            System.err.println("Inserting error: " + e);
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
    public boolean update(Status status) {
        String sql = "UPDATE `status` SET statusName = ? WHERE code = ?";
        try (
                Connection conn = DbFactory.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);) {
            stmt.setString(1, status.getStatusName());
            stmt.setInt(2, status.getStatusCode());

            int updated_num_rows = stmt.executeUpdate();
            if (updated_num_rows == 1) {
                return true;
            } else {
//                logger.error(Translator.getResource().getString("update.error.message"));
                return false;
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return false;
        }
    }

    @Override
    public boolean delete(Status status) {
        String sql = "DELETE FROM `status` WHERE code = ?";
        try (
                Connection conn = DbFactory.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);) {
            stmt.setInt(1, status.getStatusCode());
            int del_num_rows = stmt.executeUpdate();
            if (del_num_rows == 1) {
                return true;
            } else {
//                logger.error(Translator.getResource().getString("delete.error.message"));
                return false;
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return false;
        }
    }

    @Override
    public Status select(Integer i) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public boolean isUnique(String statusName){
        String sql = "SELECT * FROM `status` WHERE `statusName` = ?";
        try (
                Connection conn = DbFactory.getConnection(MYSQL);
                PreparedStatement stmt = conn.prepareStatement(sql);) {
            stmt.setString(1, statusName);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return false;
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return true;
    }
    
    public boolean isReferenced (Integer statusCode){
        String sql = "SELECT * FROM `order` WHERE status = ? LIMIT 1";
        try (
                Connection conn = DbFactory.getConnection(MYSQL);
                PreparedStatement stmt = conn.prepareStatement(sql);) {
            stmt.setInt(1, statusCode);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return true;
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return false;
    }
}

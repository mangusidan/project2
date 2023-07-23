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
public class CategoryDAO implements DAO<Category> {

    @Override
    public ObservableList<Category> selectAll() {
        ObservableList<Category> categories = FXCollections.observableArrayList();

        try (
                Connection conn = DbFactory.getConnection();
                Statement stm = conn.createStatement();
                ResultSet rs = stm.executeQuery("SELECT c.*, p.categoryName FROM `category` AS c LEFT JOIN `category` AS p ON p.categoryID = c.parent;");) {
            while (rs.next()) {
                Category c = new Category();
                c.setCategoryId(rs.getInt(1));
                c.setCategoryName(rs.getString(2));
                c.setParentId(rs.getInt(3));
                c.setParent(rs.getString(4));

                categories.add(c);
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return categories;
    }

    @Override
    public Category insert(Category newCategory) {
        String sql = "INSERT into category (categoryName,parent) VALUES (?, ?)";
        ResultSet key = null;
        try (
                Connection conn = DbFactory.getConnection(MYSQL);
                PreparedStatement stmt
                = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);) {

            stmt.setString(1, newCategory.getCategoryName());
            if (newCategory.getParentId() != null) {
                stmt.setInt(2, newCategory.getParentId());
            } else {
                stmt.setNull(2, java.sql.Types.NULL);
            }

            int rowInserted = stmt.executeUpdate();

            if (rowInserted == 1) {
                key = stmt.getGeneratedKeys();
                key.next();
                int newKey = key.getInt(1);
                newCategory.setCategoryId(newKey);
                return newCategory;
            } else {
                System.err.println("No category inserted");
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
    public boolean update(Category category) {
        String sql = "UPDATE category SET `categoryName` = ?, parent = ? WHERE categoryID = ?";
        try (
                Connection conn = DbFactory.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);) {
            stmt.setString(1, category.getCategoryName());
            if (category.getParentId() != null) {
                stmt.setInt(2, category.getParentId());
            } else {
                stmt.setNull(2, java.sql.Types.NULL);
            }
            stmt.setInt(3, category.getCategoryId());

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
    public boolean delete(Category category) {
        String sql = "DELETE FROM category WHERE categoryID = ?";
        try (
                Connection conn = DbFactory.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);) {
            stmt.setInt(1, category.getCategoryId());
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

    public Category select(Integer i) {
        Category c = new Category();
        String sql = "SELECT * FROM `category` WHERE categoryID = ?";
        try (
                Connection conn = DbFactory.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);) {
            stmt.setInt(1, i);
            ResultSet rs = stmt.executeQuery();
            rs.next();
            c.setCategoryId(rs.getInt(1));
            c.setCategoryName(rs.getString(2));
            c.setParentId(rs.getInt(3));
//            c.setParent(rs.getString(4));
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return c;
    }

    public ObservableList<Category> selectMain() {
        ObservableList<Category> categories = FXCollections.observableArrayList();

        try (
                Connection conn = DbFactory.getConnection();
                Statement stm = conn.createStatement();
                ResultSet rs = stm.executeQuery("SELECT * FROM `category` WHERE parent IS NULL");) {
            while (rs.next()) {
                Category c = new Category();
                c.setCategoryId(rs.getInt(1));
                c.setCategoryName(rs.getString(2));
                c.setParentId(rs.getInt(3));

                categories.add(c);
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return categories;
    }

    public ObservableList<Category> selectSub(Integer parentId) throws SQLException {
        ObservableList<Category> categories = FXCollections.observableArrayList();
        String sql = "SELECT * FROM `category` WHERE parent = ?";

        try (
                Connection conn = DbFactory.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);) {
            stmt.setInt(1, parentId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Category c = new Category();
                c.setCategoryId(rs.getInt(1));
                c.setCategoryName(rs.getString(2));
                c.setParentId(rs.getInt(3));

                categories.add(c);
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return categories;
    }

    public Category selectParent(Integer id) {
        Category c = new Category();
        String sql = "SELECT c.* FROM `category` AS c JOIN `category` AS p ON p.parent = c.categoryID WHERE p.categoryID = ?";

        try (
                Connection conn = DbFactory.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                c.setCategoryId(rs.getInt(1));
                c.setCategoryName(rs.getString(2));
                c.setParentId(rs.getInt(3));
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return c;
    }
    
    public boolean isUnique(String categoryName){
        String sql = "SELECT * FROM category WHERE categoryName = ?";
        try (
                Connection conn = DbFactory.getConnection(MYSQL);
                PreparedStatement stmt = conn.prepareStatement(sql);) {
            stmt.setString(1, categoryName);
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

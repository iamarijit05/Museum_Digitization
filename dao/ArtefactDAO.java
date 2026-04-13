package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

import model.Artefact;
import db.DBConnection;

public class ArtefactDAO {

    // INSERT
    //axvg
    public void addArtefact(Artefact artefact) {
        try {
            Connection con = DBConnection.getConnection();

            String sql = "INSERT INTO artefact(name, material, description, discovered_year, image_path, category_id, period_id, region_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, artefact.getName());
            ps.setString(2, artefact.getMaterial());
            ps.setString(3, artefact.getDescription());
            ps.setInt(4, artefact.getDiscovered_year());
            ps.setString(5, artefact.getImagePath());
            ps.setInt(6, artefact.getCategory_id());
            ps.setInt(7, artefact.getPeriod_id());
            ps.setInt(8, artefact.getRegion_id());

            ps.executeUpdate();
            System.out.println("Data Inserted Successfully");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //  FETCH ALL
    public List<Artefact> getAllArtefacts() {
        List<Artefact> list = new ArrayList<>();

        try {
            Connection con = DBConnection.getConnection();
            String sql = "SELECT * FROM artefact";

            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Artefact a = new Artefact(
                        rs.getInt("artefact_id"),
                        rs.getString("name"),
                        rs.getString("material"),
                        rs.getString("description"),
                        rs.getString("image_path"),
                        rs.getInt("category_id"),
                        rs.getInt("discovered_year"),
                        rs.getInt("period_id"),
                        rs.getInt("region_id")
                );
                list.add(a);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    //  DELETE
    public void deleteArtefact(int id) {
        try {
            Connection con = DBConnection.getConnection();

            String sql = "DELETE FROM artefact WHERE artefact_id = ?";

            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, id);

            int rows = ps.executeUpdate();
            if(rows > 0)
                System.out.println("Artefact deleted");
            if(rows == 0)
                throw new RuntimeException("Artefact Not Found");

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    //  UPDATE
    public void updateArtefact(Artefact artefact) {
        try {
            Connection con = DBConnection.getConnection();

            String sql = "UPDATE artefact SET name=?, material=?, description=?, discovered_year=?, image_path=?, category_id=?, period_id=?, region_id=? WHERE artefact_id=?";

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, artefact.getName());
            ps.setString(2, artefact.getMaterial());
            ps.setString(3, artefact.getDescription());
            ps.setInt(4, artefact.getDiscovered_year());
            ps.setString(5, artefact.getImagePath());
            ps.setInt(6, artefact.getCategory_id());
            ps.setInt(7, artefact.getPeriod_id());
            ps.setInt(8, artefact.getRegion_id());
            ps.setInt(9, artefact.getArtefactId());

            int rows = ps.executeUpdate();

            if (rows > 0)
                System.out.println("Artefact updated");
            else
                System.out.println("Artefact Not Found");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // GET CATEGORIES (for dropdown)
    public Map<String, Integer> getCategories() {
        Map<String, Integer> map = new HashMap<>();
        String sql = "SELECT category_id, category_name FROM category";

        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                map.put(rs.getString("category_name"), rs.getInt("category_id"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return map;
    }

    //  GET PERIODS (for dropdown)
    public Map<String, Integer> getPeriods() {
        Map<String, Integer> map = new HashMap<>();
        String sql = "SELECT period_id, period_name FROM period";

        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                map.put(rs.getString("period_name"), rs.getInt("period_id"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return map;
    }

    //  GET REGIONS (for dropdown)
    public Map<String, Integer> getRegions() {
        Map<String, Integer> map = new HashMap<>();
        String sql = "SELECT region_id, region_name FROM region";

        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                map.put(rs.getString("region_name"), rs.getInt("region_id"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return map;
    }

    public Artefact getArtefactById(int id) {
        Artefact a = null;
    
        try {
            Connection con = DBConnection.getConnection();
            String sql = "SELECT a.*, " +
            "c.category_name, " +
            "p.period_name, " +
            "r.region_name " +
            "FROM artefact a " +
            "JOIN category c ON a.category_id = c.category_id " +
            "JOIN period p ON a.period_id = p.period_id " +
            "JOIN region r ON a.region_id = r.region_id " +
            "WHERE a.artefact_id = ?";            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, id);
    
            ResultSet rs = ps.executeQuery();
    
            if (rs.next()) {
                a = new Artefact(
                    rs.getInt("artefact_id"),
                    rs.getString("name"),
                    rs.getString("material"),
                    rs.getString("description"),
                    rs.getString("image_path"),
                    rs.getInt("category_id"),
                    rs.getInt("discovered_year"),
                    rs.getInt("period_id"),
                    rs.getInt("region_id")
                );
            
                // NEW
                a.setCategoryName(rs.getString("category_name"));
                a.setPeriodName(rs.getString("period_name"));
                a.setRegionName(rs.getString("region_name"));
            }
    
        } catch (Exception e) {
            e.printStackTrace();
        }
    
        return a;
    }

    //method for grids in public page
    public List<Artefact> getArtefactsByCategory(int categoryId) {

        List<Artefact> list = new ArrayList<>();
    
        try {
            Connection con = DBConnection.getConnection();
    
            String sql = "SELECT * FROM artefact WHERE category_id = ?";
    
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, categoryId);
    
            ResultSet rs = ps.executeQuery();
    
            while (rs.next()) {
                Artefact a = new Artefact(
                        rs.getInt("artefact_id"),
                        rs.getString("name"),
                        rs.getString("material"),
                        rs.getString("description"),
                        rs.getString("image_path"),
                        rs.getInt("category_id"),
                        rs.getInt("discovered_year"),
                        rs.getInt("period_id"),
                        rs.getInt("region_id")
                );
    
                list.add(a);
            }
    
        } catch (Exception e) {
            e.printStackTrace();
        }
    
        return list;
    }
}
package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import model.Artefact;
import db.DBConnection;

public class ArtefactDAO {

    // used for data insertion
    public void addArtefact(Artefact artefact) {
        try {
            Connection con = DBConnection.getConnection();

            String sql = "INSERT INTO artefact(name, material, description, discovered_year, image_path, category_id, period_id, region_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

            PreparedStatement ps = con.prepareStatement(sql);
            //System.out.println("DAO METHOD CALLED");
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

    // fetch all data from db.
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
                        rs.getInt("region_id"));
                list.add(a);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // delete artefacts
    public void deleteArtefact(int id) {
        try {

            Connection con = DBConnection.getConnection();

            String sql = "DELETE FROM artefact WHERE artefact_id = ?";

            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            ps.executeUpdate();
            System.out.println("Artefact deleted");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // update artefacts
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
            if(rows > 0) 
                System.out.println("Artefact updated");
            else 
                System.out.println("Artefact Not Found");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

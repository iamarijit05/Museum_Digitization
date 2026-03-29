package service;

import dao.ArtefactDAO;
import model.Artefact;
import util.Session;
import model.UserRole;

import java.util.List;

public class ArtefactService {

    private ArtefactDAO dao = new ArtefactDAO();

    public void addArtefact(Artefact a) {
        if (Session.getRole() != UserRole.ADMIN) {
            throw new RuntimeException("Only admin can add artefacts");
        }
        if (a.getName() == null || a.getName().trim().isEmpty()) {
            throw new RuntimeException("Artefact name cannot be empty");
        }
        dao.addArtefact(a);
    }

    public List<Artefact> getAllArtefacts() {
        return dao.getAllArtefacts();
    }

    public void deleteArtefact(int id) {
        if (Session.getRole() != UserRole.ADMIN) {
            throw new RuntimeException("Only admin can delete artefacts");
        }
        dao.deleteArtefact(id);
    }
}
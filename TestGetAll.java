import dao.ArtefactDAO;
import model.Artefact;
import java.util.List;

public class TestGetAll {

    public static void main(String[] args) {

        ArtefactDAO dao = new ArtefactDAO();
        /*
         * List<Artefact> artefacts = dao.getAllArtefacts();
         * 
         * for (Artefact a : artefacts) {
         * System.out.println(a.getName());
         * }
         * dao.deleteArtefact(3);
         * artefacts = dao.getAllArtefacts();
         * 
         * for (Artefact a : artefacts) {
         * System.out.println(a.getName());
         * }
         */
        Artefact a = new Artefact(
                56,
                "Updated Chola Coin",
                "Gold",
                "Updated description",
                "coin.jpg",
                1,
                1020,
                1,
                1);

        dao.updateArtefact(a);

    }
}
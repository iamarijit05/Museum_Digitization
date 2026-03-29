import dao.ArtefactDAO;
import model.Artefact;

public class TestInsert {

    public static void main(String[] args) {

        Artefact artefact = new Artefact(
            "Maurya Jewl",           // name
            "Gold",                      // material
            "Ancient coin from Maurya Empire", // description
            "maurya_coin.jpg",            // image_path
            1,                           // category_id
            320,                         // discovered_year
            2,                           // period_id
            2                            // region_id
        );

        ArtefactDAO dao = new ArtefactDAO();
        dao.addArtefact(artefact);

    }
}
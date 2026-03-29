package model;

public class Artefact {
    private int artefactId;
    private String name;
    private String material;
    private String description;
    private String imagePath;
    private int category_id;
    private int discovered_year;
    private int period_id;
    private int region_id;

    public Artefact() {

    }

    // Constructor without ID (used when inserting)
    public Artefact(String name, String material, String description, String imagePath,
                    int category_id, int discovered_year, int period_id, int region_id) {
        this.name = name;
        this.material = material;
        this.description = description;
        this.imagePath = imagePath;
        this.category_id = category_id;
        this.discovered_year = discovered_year;
        this.period_id = period_id;
        this.region_id = region_id;
        System.out.println("hello");
    }

     // Constructor with ID (used when retrieving from DB)
    public Artefact(int artefactId, String name, String material, String description, String imagePath,
                    int category_id, int discovered_year, int period_id, int region_id) {
        this.artefactId = artefactId;
        this.name = name;
        this.material = material;
        this.description = description;
        this.imagePath = imagePath;
        this.category_id = category_id;
        this.discovered_year = discovered_year;
        this.period_id = period_id;
        this.region_id = region_id;
    }

    //getters and setters 
    public int getArtefactId() {
        return artefactId;
    }
    public void setArtefactId(int artefactId) {
        this.artefactId = artefactId;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getMaterial() {
        return material;
    }
    public void setMaterial(String material) {
        this.material = material;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }

    public int getDiscovered_year() {
        return discovered_year;
    }

    public void setDiscovered_year(int discovered_year) {
        this.discovered_year = discovered_year;
    }

    public int getPeriod_id() {
        return period_id;
    }

    public void setPeriod_id(int period_id) {
        this.period_id = period_id;
    }

    public int getRegion_id() {
        return region_id;
    }

    public void setRegion_id(int region_id) {
        this.region_id = region_id;
    }
}

package hepl.xml.laboxml;

import java.util.ArrayList;

public class Etude {
    private String id;
    private String patientRef;

    private ArrayList<Image> images;

    public Etude(String id, String patientRef) {
        this.id = id;
        this.patientRef = patientRef;
        this.images = new ArrayList<Image>();
    }

    public String getId() {
        return id;
    }

    public String getPatientRef() {
        return patientRef;
    }

    public ArrayList<Image> getImages() {
        return images;
    }

    public void ajouterImage(Image image) {
        images.add(image);
    }
}

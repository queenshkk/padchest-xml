package hepl.xml.laboxml;

import java.util.ArrayList;

public class Patient { private String id;
    private String naissance;
    private String sexe;
    private ArrayList<Etude> etudes;


    public Patient(String id, String naissance, String sexe) {
        this.id = id;
        this.naissance = naissance;
        this.sexe = sexe;
        this.etudes = new ArrayList<Etude>();
    }

    public String getId() {
        return id;
    }

    public String getNaissance() {
        return naissance;
    }

    public String getSexe() {
        return sexe;
    }

    public ArrayList<Etude> getEtudes() {
        return etudes;
    }

    public void ajouterEtude(Etude etude) {
        etudes.add(etude);
    }
}

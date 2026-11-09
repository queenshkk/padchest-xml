package hepl.xml.laboxml.csv;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;

public class VérificationRapports {
    public static void main(String[] args) throws Exception {

        HashMap<String, String> rapports = new HashMap<>();
        HashMap<String, Integer> valeursPediatric = new HashMap<>();
        int doublons = 0;
        int conflits = 0;
        int sansID = 0;

        try (BufferedReader br = new BufferedReader(new FileReader("PADCHEST_chest_x_ray_images_labels_160K_01.02.19.csv"))) {

            br.readLine(); // Ignorer l'en-tête

            String ligne;

            while ((ligne = LectureCSV.lireEnregistrement(br)) != null) {

                String[] champs = LectureCSV.decouperLigne(ligne);

                String pediatric = champs[11];

                if (!valeursPediatric.containsKey(pediatric)) {
                    valeursPediatric.put(pediatric, 1);
                } else {
                    valeursPediatric.put(pediatric, valeursPediatric.get(pediatric) + 1);
                }

                String reportID = champs[28];
                String report = champs[29];

                if (reportID.isEmpty()) {
                    sansID++;
                    continue;
                }

                if (rapports.containsKey(reportID)) {
                    doublons++;

                    if (!rapports.get(reportID).equals(report)) {
                        conflits++;
                    }
                } else {
                    rapports.put(reportID, report);
                }
            }
        }

        System.out.println("Rapports distincts : " + rapports.size());
        System.out.println("ReportID répétés : " + doublons);
        System.out.println("Rapports avec texte différent : " + conflits);
        System.out.println("Images sans ReportID : " + sansID);
        System.out.println("Valeurs Pediatric : " + valeursPediatric);
    }
}

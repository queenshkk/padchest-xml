package hepl.xml.laboxml.conversion;

import hepl.xml.laboxml.csv.LectureCSV;
import hepl.xml.laboxml.model.Etude;
import hepl.xml.laboxml.model.Image;
import hepl.xml.laboxml.model.Patient;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class ConversionXML {
    public static void main(String[] args) {
        String fichierCSV = "PADCHEST_chest_x_ray_images_labels_160K_01.02.19.csv";
        String fichierXML = "PadChest.xml";

        ArrayList<Patient> patients = new ArrayList<Patient>();
        HashMap<String, Patient> patientsParId = new HashMap<>();
        HashMap<String, Etude> etudesParId = new HashMap<>();
        LinkedHashMap<String, String> rapportsParId = new LinkedHashMap<>();

        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(fichierCSV), StandardCharsets.UTF_8));
            br.readLine();

            System.out.println("Début de la lecture du CSV");
            String ligne;
            int compteur = 0;
            int imagesAjoutees = 0;

            while ((ligne = LectureCSV.lireEnregistrement(br)) != null) {
                compteur++;

                String[] champs = LectureCSV.decouperLigne(ligne);

                String imageID = champs[1];
                String imageDir = champs[2];

                String studyID = champs[4];

                String patientID = champs[5];
                String naissance = normaliserNaissance(champs[6]);                String sexe = champs[7];

                String projection = champs[9];
                String methodProjection = champs[10];
                String pediatric = champs[11];

                String reportID = champs[28];
                String report = champs[29];
                String methodLabel = champs[30];
                String labels = champs[31];
                String localizations = champs[32];
                String labelsLocalizations = champs[33];
                String labelCUIS = champs[34];
                String localizationsCUIS = champs[35];

                if (patientID == null || patientID.isEmpty() || studyID == null || studyID.isEmpty()) {
                    continue;
                }

                if (!rapportsParId.containsKey(reportID)) {
                    rapportsParId.put(reportID, report);
                }

                Patient patient = patientsParId.get(patientID);

                if (patient == null) {
                    patient = new Patient(patientID, naissance, sexe);

                    patients.add(patient);
                    patientsParId.put(patientID, patient);
                }

                String cleEtude = patientID + "_" + studyID;

                Etude etude = etudesParId.get(cleEtude);

                if (etude == null) {
                    etude = new Etude(studyID, patientID);

                    patient.ajouterEtude(etude);
                    etudesParId.put(cleEtude, etude);
                }

                Image image = new Image(
                        imageID,
                        imageDir,
                        projection,
                        methodProjection,
                        pediatric,
                        reportID,
                        report,
                        methodLabel,
                        labels,
                        localizations,
                        labelsLocalizations,
                        labelCUIS,
                        localizationsCUIS
                );
                etude.ajouterImage(image);
                imagesAjoutees++;
            }


        br.close();

        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fichierXML), StandardCharsets.UTF_8));

        bw.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        bw.newLine();

        bw.write("<!DOCTYPE PadChest SYSTEM \"2_structure/PadChest.dtd\">");
        bw.newLine();

        bw.write("<PadChest>");
        bw.newLine();

        bw.write("  <Rapports>");
        bw.newLine();

        for (String reportID : rapportsParId.keySet()) {
            bw.write("    <Rapport id=\"R" + echapperXML(reportID) + "\">");
            bw.write(echapperXML(rapportsParId.get(reportID)));
            bw.write("</Rapport>");
            bw.newLine();
        }

        bw.write("  </Rapports>");
        bw.newLine();

        for (Patient patient : patients) {
            bw.write("  <Patient id=\"P" + echapperXML(patient.getId()) + "\">");
            bw.newLine();

            if (!estManquant(patient.getNaissance())) {
                bw.write("    <Naissance>" + echapperXML(patient.getNaissance()) + "</Naissance>");
                bw.newLine();
            }

            if (!estManquant(patient.getSexe())) {
                bw.write("    <Sexe>" + echapperXML(patient.getSexe()) + "</Sexe>");
                bw.newLine();
            }

            for (Etude etude : patient.getEtudes()) {
                bw.write("    <Etude id=\"E" + echapperXML(etude.getPatientRef())
                        + "_" + echapperXML(etude.getId()) + "\">");
                bw.newLine();

                for (Image image : etude.getImages()) {
                    bw.write("      <Image id=\"I" + echapperXML(image.getId())
                            + "\" rapportRef=\"R" + echapperXML(image.getReportID()) + "\">");
                    bw.newLine();

                    bw.write("        <ImageDir>" + echapperXML(image.getImageDir()) + "</ImageDir>");
                    bw.newLine();

                    bw.write("        <Projection>" + echapperXML(image.getProjection()) + "</Projection>");
                    bw.newLine();

                    bw.write("        <MethodProjection>" + echapperXML(image.getMethodProjection()) + "</MethodProjection>");
                    bw.newLine();

                    bw.write("        <Pediatric>" + echapperXML(image.getPediatric()) + "</Pediatric>");
                    bw.newLine();

                    bw.write("        <MethodLabel>" + echapperXML(image.getMethodLabel()) + "</MethodLabel>");
                    bw.newLine();

                    bw.write("        <Labels>" + echapperXML(image.getLabels()) + "</Labels>");
                    bw.newLine();

                    bw.write("        <Localizations>" + echapperXML(image.getLocalizations()) + "</Localizations>");
                    bw.newLine();

                    bw.write("        <LabelsLocalizationsBySentence>" + echapperXML(image.getLabelsLocalizations()) + "</LabelsLocalizationsBySentence>");
                    bw.newLine();

                    bw.write("        <LabelCUIS>" + echapperXML(image.getLabelCUIS()) + "</LabelCUIS>");
                    bw.newLine();

                    bw.write("        <LocalizationsCUIS>" + echapperXML(image.getLocalizationsCUIS()) + "</LocalizationsCUIS>");
                    bw.newLine();

                    bw.write("      </Image>");
                    bw.newLine();
                }

                bw.write("    </Etude>");
                bw.newLine();
            }

                bw.write("  </Patient>");
                bw.newLine();
            }

            bw.write("</PadChest>");

            bw.close();

            System.out.println("Enregistrements CSV lus : " + compteur);
            System.out.println("Images ajoutées : " + imagesAjoutees);
            System.out.println("Conversion terminée.");
        } catch (Exception e) {
            System.out.println("Erreur : " + e.getMessage());
        }

    }

    public static String echapperXML(String texte) {
        if (texte == null) {
            return "";
        }

        texte = texte.replace("&", "&amp;");
        texte = texte.replace("<", "&lt;");
        texte = texte.replace(">", "&gt;");
        texte = texte.replace("\"", "&quot;");

        return texte;
    }

    public static String normaliserNaissance(String naissance) {
        if (naissance.matches("\\d{4}\\.0")) {
            return naissance.substring(0, 4);
        }
        return naissance;
    }

    public static boolean estManquant(String valeur) {
        return valeur == null || valeur.trim().isEmpty() || valeur.trim().equals("nan");
    }
}
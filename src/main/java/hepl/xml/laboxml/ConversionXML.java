package hepl.xml.laboxml;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;

public class ConversionXML {
    public static void main(String[] args) {
        String fichierCSV = "C:/Users/User/Documents/HEPL informatique/BAC 3/Q1/XML et BD avancées/Labo/PADCHEST_chest_x_ray_images_labels_160K_01.02.19.csv";
        String fichierXML = "PadChest.xml";

        ArrayList<Patient> patients = new ArrayList<Patient>();

        try {
            BufferedReader br = new BufferedReader(new FileReader(fichierCSV));

            // Ignorer l'entête
            br.readLine();

            String ligne;
            int compteur = 0;

            while ((ligne = lireEnregistrement(br)) != null && compteur < 1000) {
                compteur++;

                String[] champs = decouperLigne(ligne);

                String imageID = champs[1];
                String imageDir = champs[2];

                String studyID = champs[4];

                String patientID = champs[5];
                String naissance = champs[6];
                String sexe = champs[7];

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

                // Si la ligne n'a pas de PatientID, on passe à la suivante
                if (patientID == null) {
                    continue;
                }

                // Chercher le patient
                Patient patient = null;
                for (Patient p : patients) {
                    if (p.getId().equals(patientID)) {
                        patient = p;
                    }
                }

                // S'il n'existe pas, on le crée
                if (patient == null) {
                    patient = new Patient(patientID, naissance, sexe);
                    patients.add(patient);
                }

                if (studyID == null) {
                    continue;
                }
                // Chercher l'étude
                Etude etude = null;
                for (Etude e : patient.getEtudes()) {
                    if (e.getId().equals(studyID)) {
                        etude = e;
                    }
                }

                // Si elle n'existe pas, on la crée
                if (etude == null) {
                    etude = new Etude(studyID, patientID);
                    patient.ajouterEtude(etude);
                }

                // Créer l'image
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
            }


        br.close();

        // Création du fichier XML
        BufferedWriter bw = new BufferedWriter(new FileWriter(fichierXML));

        bw.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        bw.newLine();

        bw.write("<!DOCTYPE PadChest SYSTEM \"PadChest.dtd\">");
        bw.newLine();

        bw.write("<PadChest>");
        bw.newLine();

        // Parcourir les patients
        for (Patient patient : patients) {
            bw.write("  <Patient id=\"P" + echapperXML(patient.getId()) + "\">");
            bw.newLine();

            bw.write("    <Naissance>" + echapperXML(patient.getNaissance()) + "</Naissance>");
            bw.newLine();

            bw.write("    <Sexe>" + echapperXML(patient.getSexe()) + "</Sexe>");
            bw.newLine();

            // Parcourir les études
            for (Etude etude : patient.getEtudes()) {
                bw.write("    <Etude id=\"E" + echapperXML(etude.getId())
                        + "\" patientRef=\"P" + echapperXML(etude.getPatientRef()) + "\">");

                bw.newLine();

                // Parcourir les images
                for (Image image : etude.getImages()) {
                    bw.write("      <Image id=\"I" + echapperXML(image.getId()) + "\">");
                    bw.newLine();

                    bw.write("        <ImageDir>" + echapperXML(image.getImageDir()) + "</ImageDir>");
                    bw.newLine();

                    bw.write("        <Projection>" + echapperXML(image.getProjection()) + "</Projection>");
                    bw.newLine();

                    bw.write("        <MethodProjection>" + echapperXML(image.getMethodProjection()) + "</MethodProjection>");
                    bw.newLine();

                    bw.write("        <Pediatric>" + echapperXML(image.getPediatric()) + "</Pediatric>");
                    bw.newLine();

                    bw.write("        <ReportID>" + echapperXML(image.getReportID()) + "</ReportID>");
                    bw.newLine();

                    bw.write("        <Report>" + echapperXML(image.getReport()) + "</Report>");
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

            System.out.println("Conversion terminée.");
        } catch (Exception e) {
            System.out.println("Erreur : " + e.getMessage());
        }

    }
    public static String[] decouperLigne(String ligne) {
        String[] champs = new String[36];

        String valeur = "";
        int colonne = 0;
        boolean guillemets = false;

        for (int i = 0; i < ligne.length(); i++) {

            char caractere = ligne.charAt(i);

            if (caractere == '"') {
                if (guillemets == false) {
                    guillemets = true;
                }
                else {
                    guillemets = false;
                }
            }
            else if (caractere == ',' && guillemets == false) {
                champs[colonne] = valeur;
                colonne++;
                valeur = "";
            }
            else {
                valeur = valeur + caractere;
            }
        }

        champs[colonne] = valeur;

        return champs;
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

    public static String lireEnregistrement(BufferedReader br) throws Exception {
        String ligne = br.readLine();

        if (ligne == null) {
            return null;
        }

        int nombreGuillemets = 0;

        // Compter les guillemets
        for (int i = 0; i < ligne.length(); i++) {
            if (ligne.charAt(i) == '"') {
                nombreGuillemets++;
            }
        }

        // Si le nombre est impair, l'enregistrement continue
        while (nombreGuillemets % 2 != 0) {
            String ligneSuivante = br.readLine();

            if (ligneSuivante == null) {
                break;
            }

            ligne = ligne + " " + ligneSuivante;

            for (int i = 0; i < ligneSuivante.length(); i++) {
                if (ligneSuivante.charAt(i) == '"') {
                    nombreGuillemets++;
                }
            }
        }

        return ligne;
    }
}
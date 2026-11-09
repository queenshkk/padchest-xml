package hepl.xml.laboxml.csv;

import java.io.BufferedReader;

public class LectureCSV {
    public static String lireEnregistrement(BufferedReader br) throws Exception {
        String ligne = br.readLine();

        if (ligne == null) {
            return null;
        }

        int nombreGuillemets = 0;

        for (int i = 0; i < ligne.length(); i++) {
            if (ligne.charAt(i) == '"') {
                nombreGuillemets++;
            }
        }

        while (nombreGuillemets % 2 != 0) {
            String ligneSuivante = br.readLine();

            if (ligneSuivante == null) {
                System.out.println("Attention : dernier enregistrement CSV incomplet, ignoré.");
                return null;
            }

            ligne = ligne + "\n" + ligneSuivante;

            for (int i = 0; i < ligneSuivante.length(); i++) {
                if (ligneSuivante.charAt(i) == '"') {
                    nombreGuillemets++;
                }
            }
        }

        return ligne;
    }

    public static String[] decouperLigne(String ligne) {
        String[] champs = new String[36];

        StringBuilder valeur = new StringBuilder();
        int colonne = 0;
        boolean guillemets = false;

        for (int i = 0; i < ligne.length(); i++) {
            char caractere = ligne.charAt(i);

            if (caractere == '"') {
                if (guillemets && i + 1 < ligne.length() && ligne.charAt(i + 1) == '"') {
                    valeur.append('"');
                    i++;
                } else {
                    guillemets = !guillemets;
                }
            }
            else if (caractere == ',' && !guillemets) {
                if (colonne >= 35) {
                    throw new IllegalArgumentException("Trop de colonnes CSV");
                }

                champs[colonne] = valeur.toString();
                colonne++;
                valeur.setLength(0);
            }
            else {
                valeur.append(caractere);
            }
        }

        if (guillemets) {
            throw new IllegalArgumentException("Guillemets CSV non fermés");
        }

        if (colonne != 35) {
            throw new IllegalArgumentException("Nombre de colonnes incorrect : " + (colonne + 1));
        }

        champs[colonne] = valeur.toString();

        return champs;
    }
}
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

            ligne = ligne + " " + ligneSuivante;

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

        String valeur = "";
        int colonne = 0;
        boolean guillemets = false;

        for (int i = 0; i < ligne.length(); i++) {
            char caractere = ligne.charAt(i);

            if (caractere == '"') {
                guillemets = !guillemets;
            }
            else if (caractere == ',' && !guillemets) {
                champs[colonne] = valeur;
                colonne++;
                valeur = "";
            }
            else {
                valeur = valeur + caractere;
            }
        }

        if (colonne != 35) {
            throw new IllegalArgumentException("Nombre de colonnes incorrect : " + (colonne + 1)
            );
        }

        champs[colonne] = valeur;

        return champs;
    }
}

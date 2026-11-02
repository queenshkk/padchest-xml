package hepl.xml.laboxml.sax;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.HashMap;

public class MonHandler extends DefaultHandler {
    private int nombreImages = 0;
    private int nombreImagesRight = 0;
    private String texte = "";
    private HashMap<String, Integer> compteurLabels = new HashMap<>();
    private int nombrePatients = 0;
    private HashMap<String, Integer> compteurProjections = new HashMap<>();

    public void warning(SAXParseException e) throws SAXException {
        System.out.println("WARNING : " + e.getMessage());
    }

    public void error(SAXParseException e) throws SAXException {
        System.out.println("ERROR : " + e.getMessage());
        throw e;
    }

    public void fatalError(SAXParseException e) throws SAXException {
        System.out.println("FATAL : " + e.getMessage());
        throw e;
    }

    public void startElement(String uri, String localName, String qName, Attributes attr) throws SAXException {
        if (qName.equals("Image")) {
            nombreImages++;
        }
        texte = "";

        if (qName.equals("Patient")) {
            nombrePatients++;
        }
    }

    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (qName.equals("Localizations")) {

            String localizations = texte.replace("[", "")
                    .replace("]", "")
                    .replace("'", "");

            String[] liste = localizations.split(",");

            for (String localisation : liste) {
                if (localisation.trim().equals("loc right")) {
                    nombreImagesRight++;
                    break;
                }
            }
        }

        if (qName.equals("Labels")) {

            String labels = texte.replace("[", "")
                    .replace("]", "")
                    .replace("'", "");

            String[] liste = labels.split(",");

            for (String label : liste) {
                label = label.trim();

                if (!label.isEmpty()) {
                    if (compteurLabels.containsKey(label)) {
                        compteurLabels.put(label, compteurLabels.get(label) + 1);
                    } else {
                        compteurLabels.put(label, 1);
                    }
                }
            }
        }

        if (qName.equals("Projection")) {

            String projection = texte.trim();

            if (compteurProjections.containsKey(projection)) {
                compteurProjections.put(projection, compteurProjections.get(projection) + 1);
            } else {
                compteurProjections.put(projection, 1);
            }
        }
    }

    public void endDocument() throws SAXException {
        System.out.println("Nombre d'images : " + nombreImages);
        System.out.println("Images avec loc right : " + nombreImagesRight);

        ArrayList<String> labels = new ArrayList<>(compteurLabels.keySet());

        for (int i = 0; i < labels.size(); i++) {
            for (int j = i + 1; j < labels.size(); j++) {

                if (compteurLabels.get(labels.get(j)) >
                        compteurLabels.get(labels.get(i))) {

                    String temp = labels.get(i);
                    labels.set(i, labels.get(j));
                    labels.set(j, temp);
                }
            }
        }
        System.out.println("Top 10 des labels :");

        for (int i = 0; i < 10 && i < labels.size(); i++) {
            String label = labels.get(i);
            System.out.println(label + " : " + compteurLabels.get(label));
        }

        System.out.println("Nombre de patients distincts : " + nombrePatients);

        System.out.println("Nombre d'images par projection :");

        for (String projection : compteurProjections.keySet()) {
            System.out.println(projection + " : " + compteurProjections.get(projection));
        }

    }

    public void characters(char[] ch, int start, int length) throws SAXException {
        texte = texte + new String(ch, start, length);
    }
}

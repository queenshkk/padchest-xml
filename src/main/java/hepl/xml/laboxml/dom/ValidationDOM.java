package hepl.xml.laboxml.dom;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class ValidationDOM {
    public static void main(String[] args) {

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

            factory.setValidating(true);

            DocumentBuilder builder = factory.newDocumentBuilder();

            builder.setErrorHandler(new DefaultHandler() {

                @Override
                public void error(SAXParseException e) throws SAXException {
                    throw e;
                }

                @Override
                public void fatalError(SAXParseException e) throws SAXException {
                    throw e;
                }
            });

            Document document = builder.parse(new File("PadChest.xml"));

            System.out.println("Lecture DOM terminée !");
            System.out.println("Élément racine : " + document.getDocumentElement().getTagName());

            NodeList images = document.getElementsByTagName("Image");
            System.out.println("Nombre total d'images : " + images.getLength());

            NodeList patients = document.getElementsByTagName("Patient");
            System.out.println("Nombre de patients distincts : " + patients.getLength());

            int imagesRight = 0;

            for (int i = 0; i < images.getLength(); i++) {

                org.w3c.dom.Element image = (org.w3c.dom.Element) images.item(i);

                String localisations = image
                        .getElementsByTagName("Localizations")
                        .item(0)
                        .getTextContent();

                localisations = localisations.replace("[", "")
                        .replace("]", "")
                        .replace("'", "");

                String[] liste = localisations.split(",");

                for (String localisation : liste) {
                    if (localisation.trim().equals("loc right")) {
                        imagesRight++;
                        break;
                    }
                }
            }

            System.out.println("Images avec loc right : " + imagesRight);

            HashMap<String, Integer> projections = new HashMap<>();

            for (int i = 0; i < images.getLength(); i++) {

                org.w3c.dom.Element image = (org.w3c.dom.Element) images.item(i);

                String projection = image
                        .getElementsByTagName("Projection")
                        .item(0)
                        .getTextContent();

                projections.put(projection, projections.getOrDefault(projection, 0) + 1);
            }

            System.out.println("Images par projection :");

            for (String projection : projections.keySet()) {
                System.out.println(projection + " : " + projections.get(projection));
            }

            HashMap<String, Integer> compteLabels = new HashMap<>();

            for (int i = 0; i < images.getLength(); i++) {

                org.w3c.dom.Element image = (org.w3c.dom.Element) images.item(i);

                String labels = image
                        .getElementsByTagName("Labels")
                        .item(0)
                        .getTextContent();

                labels = labels.replace("[", "")
                        .replace("]", "")
                        .replace("'", "");

                for (String label : labels.split(",")) {
                    label = label.trim();

                    if (!label.isEmpty()) {
                        compteLabels.put(label, compteLabels.getOrDefault(label, 0) + 1);
                    }
                }
            }

            System.out.println("Top 10 des labels :");

            compteLabels.entrySet()
                    .stream()
                    .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                    .limit(10)
                    .forEach(entree ->
                            System.out.println(entree.getKey() + " : " + entree.getValue())
                    );
        } catch (Exception e) {
            System.out.println("Erreur DOM : " + e.getMessage());
        }
    }
}

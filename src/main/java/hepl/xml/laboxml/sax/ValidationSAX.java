package hepl.xml.laboxml.sax;

import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class ValidationSAX {
    public static void main(String[] args) {
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            factory.setValidating(true);
            factory.setNamespaceAware(true);

            SAXParser sp = factory.newSAXParser();

            sp.parse("PadChest.xml", new MonHandler());

            System.out.println("XML valide.");

        } catch (Exception e) {
            System.out.println("Erreur : " + e.getMessage());
        }
    }
}
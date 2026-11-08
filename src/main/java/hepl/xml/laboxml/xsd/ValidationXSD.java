package hepl.xml.laboxml.xsd;

import javax.xml.XMLConstants;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.File;

public class ValidationXSD {
    public static void main(String[] args) {

        try {
            SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

            Schema schema = factory.newSchema(new File("2_structure/PadChest.xsd"));

            Validator validator = schema.newValidator();

            validator.validate(new javax.xml.transform.stream.StreamSource(new File("PadChest.xml")));

            System.out.println("Validation XSD réussie !");

        } catch (Exception e) {
            System.out.println("Erreur de validation XSD :");
            System.out.println(e.getMessage());
        }
    }
}

package hepl.xml.laboxml.xslt;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.File;

public class GenerationExtrait {
    public static void main(String[] args) throws Exception {

        TransformerFactory factory = new net.sf.saxon.TransformerFactoryImpl();

        Transformer transformer = factory.newTransformer(new StreamSource(new File("4_xslt/Extrait.xsl")));

        transformer.transform(new StreamSource(new File("PadChest.xml")), new StreamResult(new File("extrait.xml")));

        System.out.println("Extrait créé !");
    }
}

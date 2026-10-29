package hepl.xml.laboxml.xslt;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.File;

public class TransformationXSLT {
    public static void main(String[] args) throws Exception {

        TransformerFactory factory = new net.sf.saxon.TransformerFactoryImpl();
        Transformer transformer = factory.newTransformer(new StreamSource(new File("4_xslt/PadChest.xsl")));

        transformer.transform(new StreamSource(new File("extrait.xml")), new StreamResult(new File("4_xslt/resultat.html")));

        System.out.println("Transformation terminée !");
    }
}

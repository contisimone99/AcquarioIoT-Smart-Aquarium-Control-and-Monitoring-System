import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.io.File;
public class Conf {
    public String broker = "localhost";
    public String confdir = "CONFIG";
    public String conffile = "CONFIG/conf.xml";
    public String protocol = "tcp";
    public String homedir = "home";
    private Document doc;

    public Conf(String args[])
    {
        try {
	    if(args.length >= 1) conffile = args[0];
            File file = new File(conffile);
            
            // Defines a factory API that enables
            // applications to obtain a parser that produces
            // DOM object trees from XML documents.
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            
            DocumentBuilder db = dbf.newDocumentBuilder();
  
            doc = db.parse(file);
            //doc.getDocumentElement().normalize();

	     confdir = doc.getDocumentElement().getElementsByTagName("confdir").item(0).getTextContent();
	     broker = doc.getDocumentElement().getElementsByTagName("broker").item(0).getTextContent();
	     homedir = doc.getDocumentElement().getElementsByTagName("homedir").item(0).getTextContent();
	     protocol = doc.getDocumentElement().getElementsByTagName("protocol").item(0).getTextContent();
	}
        catch (Exception e) {
            System.out.println(e);
        }
    }

    public String get(String field) {
       String res = "";
       try {
	   res = doc.getDocumentElement().getElementsByTagName(field).item(0).getTextContent();
       }
       catch (Exception e) {
           System.out.println(e);
       }
       return res;
    }

}



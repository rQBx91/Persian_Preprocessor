import java.io.IOException;
import java.util.Scanner;
import java.io.File;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


public class Main {

    public static void main(String[] args) throws IOException,ParserConfigurationException,SAXException {

        DocStore store = new DocStore();
        InvertedIndex index = new InvertedIndex();

        try {

            File file = new File("./resources/simple.xml");
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document document = db.parse(file);
            document.getDocumentElement().normalize();

            NodeList nList = document.getElementsByTagName("doc");
            long max = nList.getLength();
            System.out.printf("Enter number of docs to be loaded (max %d): ", max);
            Scanner scanner = new Scanner(System.in);
            long m = scanner.nextLong();
            while (max < m | m <= 0){
                System.out.print("Invalid Input. Try again: ");
                m = scanner.nextLong();
            }
            max = m;

            for (int temp = 0; temp < max; temp++) {
                Node nNode = nList.item(temp);
                System.out.println("\nCurrent Element :" + nNode.getNodeName() + " " + temp);
                String url_part = "";
                String abstract_part = "";
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    url_part = eElement.getElementsByTagName("url").item(0).getTextContent();
                    System.out.println("url: " + url_part);

                    try {
                        abstract_part = eElement.getElementsByTagName("abstract").item(0).getTextContent();
                    } catch (NullPointerException e) {
                        abstract_part = "";
                    }
                    System.out.println("abstract: " + abstract_part);
                }

                Doc doc = new Doc(url_part, abstract_part);
                store.add(doc);
                index.add(doc);

            }
        } catch (Exception e) {
            System.out.println("Something went wrong.\n"+ e.getMessage());
        }

        if (index.saveIndex("./resources/index.txt"))
            System.out.println("\nsaved index to file.\n");

        Normalizer normalizer = new Normalizer();
        while (true){
            System.out.print("Enter your query: ");
            Scanner scanner = new Scanner(System.in);
            String query = scanner.nextLine().toLowerCase();
            if (query.equals("exit")){
                return;
            }
            try {
                PostingList list = index.get(normalizer.run(query));
                for (Integer docId : list.getDocIds()) {
                    System.out.println(store.get(docId));
                }
                System.out.println();
            }
            catch (NullPointerException ex) {
                System.out.println("couldn't find any result.");
            }
        }

    }
}

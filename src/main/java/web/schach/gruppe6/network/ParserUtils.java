package web.schach.gruppe6.network;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import web.schach.gruppe6.network.exceptions.ParseException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringWriter;
import java.util.Iterator;

public class ParserUtils {
	
	public static final Transformer DEFAULT_TRANSFORMER;
	
	static {
		try {
			DEFAULT_TRANSFORMER = TransformerFactory.newInstance().newTransformer();
			DEFAULT_TRANSFORMER.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
			DEFAULT_TRANSFORMER.setOutputProperty(OutputKeys.METHOD, "xml");
			DEFAULT_TRANSFORMER.setOutputProperty(OutputKeys.INDENT, "yes");
			DEFAULT_TRANSFORMER.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
		} catch (TransformerConfigurationException e) {
			throw new RuntimeException(e);
		}
	}
	
	public static DocumentBuilder createXmlDocumentBuilder() {
		try {
			return DocumentBuilderFactory.newInstance().newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			throw new RuntimeException(e);
		}
	}
	
	//entry key resolver
	public static Node getEntryKeyFirstNode(Element root, String message) throws ParseException {
		//the old line using streams; replaced with the code below
//		return StreamSupport.stream(nodeListIterable(root.getElementsByTagName("entry")).spliterator(), false).filter(node -> message.equals(node.getAttributes().getNamedItem("key").getNodeValue())).findFirst().orElseThrow(() -> new ParseException("No Entry " + message));
		
		for (Node node : nodeListIterable(root.getElementsByTagName("entry"))) {
			if (message.equals(node.getAttributes().getNamedItem("key").getNodeValue()))
				return node;
		}
		throw new ParseException("No Entry " + message);
	}
	
	//NodeList
	public static Iterable<Node> nodeListIterable(NodeList list) {
		return () -> new Iterator<Node>() {
			int i;
			
			@Override
			public boolean hasNext() {
				return i < list.getLength();
			}
			
			@Override
			public Node next() {
				return list.item(i++);
			}
		};
	}
	
	//debug
	public static String debugPrint(Node doc) {
		try {
			StringWriter writer = new StringWriter();
			DEFAULT_TRANSFORMER.transform(new DOMSource(doc), new StreamResult(writer));
			return writer.toString();
		} catch (TransformerException e) {
			return e.getMessage();
		}
	}
}

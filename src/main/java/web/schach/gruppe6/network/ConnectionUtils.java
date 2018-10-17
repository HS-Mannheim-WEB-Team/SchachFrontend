package web.schach.gruppe6.network;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import web.schach.gruppe6.network.exceptions.ParseException;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
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
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.Iterator;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class ConnectionUtils {
	
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
	
	public static Client createClientIgnoreSsl() {
		try {
			SSLContext sslcontext = SSLContext.getInstance("TLS");
			sslcontext.init(null, new TrustManager[] {new X509TrustManager() {
				public void checkClientTrusted(X509Certificate[] arg0, String arg1) {
				}
				
				public void checkServerTrusted(X509Certificate[] arg0, String arg1) {
				}
				
				public X509Certificate[] getAcceptedIssuers() {
					return new X509Certificate[0];
				}
			}}, new java.security.SecureRandom());
			return ClientBuilder.newBuilder().sslContext(sslcontext).hostnameVerifier((s1, s2) -> true).build();
		} catch (NoSuchAlgorithmException | KeyManagementException e) {
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
	public static Stream<Node> getEntryKeyNodes(Element root, String key) {
		return nodeListStream(root.getElementsByTagName("entry")).filter(node -> key.equals(node.getAttributes().getNamedItem("key").getNodeValue()));
	}
	
	public static Node getEntryKeyFirstNode(Element root, String message) throws ParseException {
		return getEntryKeyNodes(root, message).findFirst().orElseThrow(ParseException::new);
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
	
	public static Stream<Node> nodeListStream(NodeList list) {
		return StreamSupport.stream(nodeListIterable(list).spliterator(), false);
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

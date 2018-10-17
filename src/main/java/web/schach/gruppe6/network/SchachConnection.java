package web.schach.gruppe6.network;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;
import web.schach.gruppe6.network.exceptions.ParseException;
import web.schach.gruppe6.network.exceptions.ServerErrorException;

import javax.ws.rs.client.Client;
import javax.xml.parsers.DocumentBuilder;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;

import static web.schach.gruppe6.network.ConnectionUtils.getEntryKeyFirstNode;

public class SchachConnection {
	
	public static final String DEFAULT_URL = "http://www.game-engineering.de:8080/rest/schach";
	
	private static String rethrowIfServerError(Element root) throws ServerErrorException {
		String klasse = getEntryKeyFirstNode(root, "klasse").getTextContent();
		String message = getEntryKeyFirstNode(root, "meldung").getTextContent();
		if ("D_Fehler".equals(klasse))
			throw new ServerErrorException(message);
		return message;
	}
	
	public final String Url;
	private final Client CLIENT;
	private final DocumentBuilder XML_PARSER;
	
	public SchachConnection(String url) {
		this(url, ConnectionUtils.createClientIgnoreSsl(), ConnectionUtils.createXmlDocumentBuilder());
	}
	
	public SchachConnection(String url, Client CLIENT, DocumentBuilder XML_PARSER) {
		Url = url;
		this.CLIENT = CLIENT;
		this.XML_PARSER = XML_PARSER;
	}
	
	//utility
	protected Document getAndParse(String relativePath) throws IOException {
		try {
			return XML_PARSER.parse(CLIENT.target(Url + relativePath).request().accept("application/xml").get(InputStream.class));
		} catch (SAXException e) {
			throw new ParseException(e);
		}
	}
	
	//impl
	public String newGame(int id) throws IOException, ServerErrorException {
		return rethrowIfServerError(getAndParse("/spiel/admin/neuesSpiel/" + id).getDocumentElement());
	}
	
	public String loadGame(int id, String path) throws IOException, ServerErrorException {
		return rethrowIfServerError(getAndParse("/spiel/admin/ladenSpiel/" + id + "/" + URLEncoder.encode(path, "UTF-8")).getDocumentElement());
	}
	
	public String saveGame(int id, String path) throws IOException, ServerErrorException {
		return rethrowIfServerError(getAndParse("/spiel/admin/speichernSpiel/" + id + "/" + URLEncoder.encode(path, "UTF-8")).getDocumentElement());
	}
}

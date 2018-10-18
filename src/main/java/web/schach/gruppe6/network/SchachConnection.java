package web.schach.gruppe6.network;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;
import web.schach.gruppe6.network.exceptions.ParseException;
import web.schach.gruppe6.network.exceptions.ServerErrorException;
import web.schach.gruppe6.obj.Layout;
import web.schach.gruppe6.obj.Move;
import web.schach.gruppe6.obj.Position;
import web.schach.gruppe6.obj.SchachPositionNotation;

import javax.ws.rs.client.Client;
import javax.xml.parsers.DocumentBuilder;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;

import static web.schach.gruppe6.network.ParserUtils.getEntryKeyFirstNode;

public class SchachConnection {
	
	public static final String DEFAULT_URL = "http://www.game-engineering.de:8080/rest/schach";
	
	private static void rethrowIfServerError(Element root) throws ServerErrorException {
		String klasse = getEntryKeyFirstNode(root, "klasse").getTextContent();
		if ("D_Fehler".equals(klasse))
			throw new ServerErrorException(getEntryKeyFirstNode(root, "meldung").getTextContent());
	}
	
	public final String Url;
	private final Client CLIENT;
	private final DocumentBuilder XML_PARSER;
	
	public SchachConnection() {
		this(DEFAULT_URL);
	}
	
	public SchachConnection(String url) {
		this(url, NetworkUtils.createClientIgnoreSsl(), ParserUtils.createXmlDocumentBuilder());
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
	public void newGame(int id) throws IOException {
		rethrowIfServerError(getAndParse("/spiel/admin/neuesSpiel/" + id).getDocumentElement());
	}
	
	public void loadGame(int id, String path) throws IOException {
		rethrowIfServerError(getAndParse("/spiel/admin/ladenSpiel/" + id + "/" + URLEncoder.encode(path, "UTF-8")).getDocumentElement());
	}
	
	public void saveGame(int id, String path) throws IOException {
		rethrowIfServerError(getAndParse("/spiel/admin/speichernSpiel/" + id + "/" + URLEncoder.encode(path, "UTF-8")).getDocumentElement());
	}
	
	public int moveCount(int id) throws IOException {
		Element root = getAndParse("/spiel/getSpielDaten/" + id).getDocumentElement();
		rethrowIfServerError(root);
		return Integer.parseInt(ParserUtils.getEntryKeyFirstNode(root, "anzahlZuege").getTextContent());
	}
	
	public Move getChange(int id, int moveId, Layout currentLayout) throws IOException {
		Element root = getAndParse("/spiel/getBelegung/" + id + "/" + moveId).getDocumentElement();
		rethrowIfServerError(root);
		
		for (Node figure : ParserUtils.nodeListIterable(root.getElementsByTagName("properties"))) {
			if ("D_Belegung".equals(ParserUtils.getEntryKeyFirstNode(root, "klasse").getTextContent())) {
				Position from = SchachPositionNotation.fromSchachPosition(ParserUtils.getEntryKeyFirstNode(root, "von").getTextContent());
				Position to = SchachPositionNotation.fromSchachPosition(ParserUtils.getEntryKeyFirstNode(root, "nach").getTextContent());
				return new Move(currentLayout.at(from), from, to);
			}
		}
		throw new ParseException("'Klasse = D_Belegung' not found!");
	}
}

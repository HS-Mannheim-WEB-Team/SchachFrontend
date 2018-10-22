package web.schach.gruppe6.network;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;
import web.schach.gruppe6.network.exceptions.ParseException;
import web.schach.gruppe6.network.exceptions.ServerErrorException;
import web.schach.gruppe6.obj.ChessPositionNotation;
import web.schach.gruppe6.obj.Layout;
import web.schach.gruppe6.obj.Move;
import web.schach.gruppe6.obj.Position;

import javax.ws.rs.client.Client;
import javax.xml.parsers.DocumentBuilder;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URLEncoder;

import static web.schach.gruppe6.network.ParserUtils.getEntryKeyFirstNode;
import static web.schach.gruppe6.obj.ChessPositionNotation.toChessNotation;

public class ChessConnection {
	
	public static final String DEFAULT_URL = "http://www.game-engineering.de:8080/rest/schach";
	
	private static void rethrowIfServerError(Element root) throws ServerErrorException {
		String klasse = getEntryKeyFirstNode(root, "klasse").getTextContent();
		if ("D_Fehler".equals(klasse))
			throw new ServerErrorException(getEntryKeyFirstNode(root, "meldung").getTextContent());
	}
	
	public final String Url;
	private final Client CLIENT;
	private final ThreadLocal<DocumentBuilder> XML_PARSER = ThreadLocal.withInitial(ParserUtils::createXmlDocumentBuilder);
	
	public ChessConnection() {
		this(DEFAULT_URL);
	}
	
	public ChessConnection(String url) {
		this(url, NetworkUtils.createClientIgnoreSsl());
	}
	
	public ChessConnection(String url, Client CLIENT) {
		Url = url;
		this.CLIENT = CLIENT;
	}
	
	//utility
	protected Document getAndParse(String relativePath) throws IOException {
		try {
			String content = CLIENT.target(Url + relativePath).request().accept("application/xml").get(String.class);
			return XML_PARSER.get().parse(new ByteArrayInputStream(content.getBytes()));
		} catch (SAXException e) {
			throw new ParseException(e);
		}
	}
	
	//impl
	public void newGame(int id) throws IOException, ServerErrorException {
		rethrowIfServerError(getAndParse("/spiel/admin/neuesSpiel/" + id).getDocumentElement());
	}
	
	public void loadGame(int id, String path) throws IOException, ServerErrorException {
		rethrowIfServerError(getAndParse("/spiel/admin/ladenSpiel/" + id + "/" + URLEncoder.encode(path, "UTF-8")).getDocumentElement());
	}
	
	public void saveGame(int id, String path) throws IOException, ServerErrorException {
		rethrowIfServerError(getAndParse("/spiel/admin/speichernSpiel/" + id + "/" + URLEncoder.encode(path, "UTF-8")).getDocumentElement());
	}
	
	public void takeMove(int id, Position from, Position to) throws IOException, ServerErrorException {
		rethrowIfServerError(getAndParse("/spiel/ziehe/" + id + "/" + toChessNotation(from) + "/" + toChessNotation(to)).getDocumentElement());
	}
	
	public int moveCount(int id) throws IOException, ServerErrorException {
		Element root = getAndParse("/spiel/getSpielDaten/" + id).getDocumentElement();
		rethrowIfServerError(root);
		return Integer.parseInt(ParserUtils.getEntryKeyFirstNode(root, "anzahlZuege").getTextContent());
	}
	
	public Move getChange(int id, int moveId, Layout currentLayout) throws IOException, ServerErrorException {
		Element root = getAndParse("/spiel/getBelegung/" + id + "/" + moveId).getDocumentElement();
		rethrowIfServerError(root);
		
		for (Node figure : ParserUtils.nodeListIterable(root.getElementsByTagName("properties"))) {
			if ("D_Belegung".equals(ParserUtils.getEntryKeyFirstNode(root, "klasse").getTextContent())) {
				Position from = ChessPositionNotation.fromChessNotation(ParserUtils.getEntryKeyFirstNode(root, "von").getTextContent());
				Position to = ChessPositionNotation.fromChessNotation(ParserUtils.getEntryKeyFirstNode(root, "nach").getTextContent());
				return new Move(currentLayout.at(from), from, to);
			}
		}
		throw new ParseException("'Klasse = D_Belegung' not found!");
	}
}

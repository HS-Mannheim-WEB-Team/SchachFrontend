package web.schach.gruppe6.network;

import javafx.util.Pair;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;
import web.schach.gruppe6.network.exceptions.ParseException;
import web.schach.gruppe6.network.exceptions.ServerErrorException;
import web.schach.gruppe6.obj.FigureType;
import web.schach.gruppe6.obj.Figures;
import web.schach.gruppe6.obj.GameState;
import web.schach.gruppe6.obj.Layout;
import web.schach.gruppe6.obj.PlayerColor;
import web.schach.gruppe6.util.ChessConstants;
import web.schach.gruppe6.util.Grid;
import web.schach.gruppe6.util.Position;

import javax.ws.rs.client.Client;
import javax.xml.parsers.DocumentBuilder;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static web.schach.gruppe6.network.ParserUtils.*;
import static web.schach.gruppe6.obj.ChessPositionNotation.*;

public class ChessConnection {
	
	public static final String DEFAULT_URL = "http://www.game-engineering.de:8080/rest/schach";
	public static final HashMap<Pair<PlayerColor, String>, FigureType> NAME_TO_TYPE = new HashMap<>();
	public static final HashMap<String, GameState> NAME_TO_STATE = new HashMap<>();
	
	static {
		NAME_TO_TYPE.put(new Pair<>(PlayerColor.WHITE, "Turm"), FigureType.WHITE_ROOK);
		NAME_TO_TYPE.put(new Pair<>(PlayerColor.WHITE, "Springer"), FigureType.WHITE_KNIGHT);
		NAME_TO_TYPE.put(new Pair<>(PlayerColor.WHITE, "Laeufer"), FigureType.WHITE_BISHOP);
		NAME_TO_TYPE.put(new Pair<>(PlayerColor.WHITE, "Dame"), FigureType.WHITE_QUEEN);
		NAME_TO_TYPE.put(new Pair<>(PlayerColor.WHITE, "Koenig"), FigureType.WHITE_KING);
		NAME_TO_TYPE.put(new Pair<>(PlayerColor.WHITE, "Bauer"), FigureType.WHITE_PAWN);
		
		NAME_TO_TYPE.put(new Pair<>(PlayerColor.BLACK, "Turm"), FigureType.BLACK_ROOK);
		NAME_TO_TYPE.put(new Pair<>(PlayerColor.BLACK, "Springer"), FigureType.BLACK_KNIGHT);
		NAME_TO_TYPE.put(new Pair<>(PlayerColor.BLACK, "Laeufer"), FigureType.BLACK_BISHOP);
		NAME_TO_TYPE.put(new Pair<>(PlayerColor.BLACK, "Dame"), FigureType.BLACK_QUEEN);
		NAME_TO_TYPE.put(new Pair<>(PlayerColor.BLACK, "Koenig"), FigureType.BLACK_KING);
		NAME_TO_TYPE.put(new Pair<>(PlayerColor.BLACK, "Bauer"), FigureType.BLACK_PAWN);
		
		NAME_TO_STATE.put("WeissImSchach", GameState.WHITE_CHECK);
		NAME_TO_STATE.put("SchwarzImSchach", GameState.BLACK_CHECK);
		NAME_TO_STATE.put("WeissSchachMatt", GameState.WHITE_CHECK_MATE);
		NAME_TO_STATE.put("SchwarzSchachMatt", GameState.BLACK_CHECK_MATE);
		NAME_TO_STATE.put("Patt", GameState.STALEMATE);
	}
	
	private static void rethrowIfServerError(Element root) throws ServerErrorException {
		Node klasse = getEntryKeyFirstOrNull(root, "klasse");
		if (klasse == null)
			return;
		if ("D_Fehler".equals(klasse.getTextContent()))
			throw new ServerErrorException(getEntryKeyFirstOrThrow(root, "meldung").getTextContent());
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
		return Integer.parseInt(getEntryKeyFirstOrThrow(root, "anzahlZuege").getTextContent());
	}
	
	public Layout getChange(int id, int moveId, Layout currentLayout) throws IOException, ServerErrorException {
		Element root = getAndParse("/spiel/getBelegung/" + id + "/" + moveId).getDocumentElement();
		rethrowIfServerError(root);
		
		Element notationQuery = getAndParse("/spiel/getZugHistorie/" + id).getDocumentElement();
		rethrowIfServerError(notationQuery);
		String notation = getEntryKeyFirstOrThrow(propertiesArrayStream(notationQuery).skip(moveId - 1).findFirst().orElseThrow(ParseException::new), "zug").getTextContent();
		
		//parse all figures
		EnumMap<Figures, Boolean> sameFigures = new EnumMap<>(Figures.class);
		EnumMap<FigureType, List<Position>> newFigures = new EnumMap<>(FigureType.class);
		GameState state = null;
		
		outer:
		for (Node properties : propertiesArrayNodeList(root)) {
			if ("D_Figur".equals(getEntryKeyFirstOrThrow(properties, "klasse").getTextContent())) {
				
				FigureType figureType = NAME_TO_TYPE.get(new Pair<>(
						Boolean.parseBoolean(getEntryKeyFirstOrThrow(properties, "isWeiss").getTextContent()) ? PlayerColor.WHITE : PlayerColor.BLACK,
						getEntryKeyFirstOrThrow(properties, "typ").getTextContent()
				));
				String positionStr = getEntryKeyFirstOrThrow(properties, "position").getTextContent();
				Position position = positionStr.isEmpty() ? null : fromChessNotation(positionStr);
				
				//try to find a figure on the same place
				for (Figures figure : Figures.values()) {
					if (Objects.equals(currentLayout.get(figure), position) && currentLayout.getType(figure) == figureType && sameFigures.get(figure) != Boolean.TRUE) {
						//same place
						sameFigures.put(figure, true);
						continue outer;
					}
				}
				
				//changed
				newFigures.computeIfAbsent(figureType, figureType1 -> new ArrayList<>()).add(position);
			} else if ("D_Belegung".equals(getEntryKeyFirstOrThrow(properties, "klasse").getTextContent())) {
				state = NAME_TO_STATE.get(getEntryKeyFirstOrThrow(properties, "status").getTextContent());
			}
		}
		
		//copy same figures and resolve removed ones
		Layout layout = new Layout(moveId + ": " + notation, moveId, state);
		EnumMap<FigureType, List<Figures>> removedFigures = new EnumMap<>(FigureType.class);
		for (Figures figure : Figures.values()) {
			if (sameFigures.get(figure) == Boolean.TRUE) {
				layout.put(figure, currentLayout.get(figure));
				layout.putType(figure, currentLayout.getType(figure));
			} else {
				removedFigures.computeIfAbsent(currentLayout.getType(figure), figureType -> new ArrayList<>()).add(figure);
			}
		}
		
		//map all of same type (not perfect if multiple of one type are moved)
		for (FigureType figureType : FigureType.values()) {
			List<Figures> removedFigureTypes = removedFigures.get(figureType);
			List<Position> newFigureTypes = newFigures.get(figureType);
			if (removedFigureTypes == null || newFigureTypes == null)
				continue;
			int min = Math.min(removedFigureTypes.size(), newFigureTypes.size());
			for (int i = 0; i < min; i++) {
				Figures figure = removedFigureTypes.remove(0);
				layout.put(figure, newFigureTypes.remove(0));
				layout.putType(figure, figureType);
			}
		}
		
		//map all remaining and remap their type (not perfect if multiple are left over)
		List<Figures> removedLeftOver = removedFigures.values().stream().flatMap(Collection::stream).collect(Collectors.toList());
		List<Pair<FigureType, Position>> newLeftOver = newFigures.entrySet().stream().flatMap(entry -> entry.getValue().stream().map(position -> new Pair<>(entry.getKey(), position))).collect(Collectors.toList());
		int leftOverCnt = removedLeftOver.size();
		if (leftOverCnt != newLeftOver.size())
			throw new RuntimeException("Figures got added(" + newLeftOver.size() + ") or removed(" + leftOverCnt + ")!");
		for (int i = 0; i < leftOverCnt; i++) {
			Figures figure = removedLeftOver.get(i);
			Pair<FigureType, Position> entry = newLeftOver.get(i);
			layout.put(figure, entry.getValue());
			layout.putType(figure, entry.getKey());
		}
		
		return layout;
	}
	
	public Grid<Boolean> getPermittedMoves(int id, Position position) throws IOException, ServerErrorException {
		Element root = getAndParse("/spiel/getErlaubteZuege/" + id + "/" + toChessNotation(position)).getDocumentElement();
		rethrowIfServerError(root);
		
		Grid<Boolean> grid = ChessConstants.createChessGrid();
		for (Node node : propertiesArrayNodeList(root))
			if ("D_Zug".equals(getEntryKeyFirstOrThrow(node, "klasse").getTextContent()))
				grid.set(fromChessNotation(getEntryKeyFirstOrThrow(node, "nach").getTextContent()), true);
		return grid;
	}
}

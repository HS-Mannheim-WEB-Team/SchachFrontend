package web.schach.gruppe6.network;

import javafx.util.Pair;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;
import web.schach.gruppe6.network.exceptions.ParseException;
import web.schach.gruppe6.network.exceptions.ServerErrorException;
import web.schach.gruppe6.obj.ChessPositionNotation;
import web.schach.gruppe6.obj.FigureType;
import web.schach.gruppe6.obj.Figures;
import web.schach.gruppe6.obj.Layout;
import web.schach.gruppe6.obj.PlayerColor;
import web.schach.gruppe6.obj.Position;

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
import static web.schach.gruppe6.obj.ChessPositionNotation.toChessNotation;

public class ChessConnection {
	
	public static final String DEFAULT_URL = "http://www.game-engineering.de:8080/rest/schach";
	public static final HashMap<Pair<PlayerColor, String>, FigureType> NAME_TO_TYPE = new HashMap<>();
	
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
		
		//parse which figures have stayed the same / which got added
		EnumMap<Figures, Boolean> sameFigures = new EnumMap<>(Figures.class);
		EnumMap<FigureType, List<Position>> newFigures = new EnumMap<>(FigureType.class);
		for (Node properties : ParserUtils.nodeListIterable(root.getElementsByTagName("properties"))) {
			if ("D_Figur".equals(getEntryKeyFirstOrThrow(properties, "klasse").getTextContent())) {
				
				FigureType figureType = NAME_TO_TYPE.get(new Pair<>(
						Boolean.parseBoolean(getEntryKeyFirstOrThrow(properties, "isWeiss").getTextContent()) ? PlayerColor.WHITE : PlayerColor.BLACK,
						getEntryKeyFirstOrThrow(properties, "typ").getTextContent()
				));
				String positionStr = getEntryKeyFirstOrThrow(properties, "position").getTextContent();
				if (positionStr.isEmpty()) {
					//figure is beaten: unknown (until first:)
					newFigures.computeIfAbsent(figureType, figureType1 -> new ArrayList<>()).add(null);
				} else {
					
					Position position = ChessPositionNotation.fromChessNotation(positionStr);
					Figures oldFigure = currentLayout.at(position);
					if (oldFigure != null && oldFigure.type == figureType) {
						//same
						sameFigures.put(oldFigure, true);
					} else {
						//different
						newFigures.computeIfAbsent(figureType, figureType1 -> new ArrayList<>()).add(position);
					}
				}
			}
		}
		
		//copy same figures and resolve removed ones
		Layout layout = new Layout("After move " + moveId);
		EnumMap<FigureType, List<Figures>> removedFigures = new EnumMap<>(FigureType.class);
		for (Figures figure : Figures.values()) {
			if (sameFigures.get(figure) == Boolean.TRUE)
				layout.put(figure, currentLayout.get(figure));
			else
				removedFigures.computeIfAbsent(figure.type, figureType -> new ArrayList<>()).add(figure);
		}
		
		//first: filter beaten figures
		for (FigureType figureType : FigureType.values()) {
			List<Figures> removedFigureTypes = removedFigures.get(figureType);
			List<Position> newFigureTypes = newFigures.get(figureType);
			if (removedFigureTypes == null || newFigureTypes == null)
				continue;
			
			int removedIndex = 0;
			int newIndex = 0;
			label:
			for (; removedIndex < removedFigureTypes.size(); removedIndex++) {
				if (currentLayout.get(removedFigureTypes.get(removedIndex)) == null) {
					
					for (; newIndex < newFigureTypes.size(); newIndex++) {
						if (newFigureTypes.get(newIndex) == null) {
							removedFigureTypes.remove(removedIndex);
							newFigureTypes.remove(newIndex);
							continue label;
						}
					}
					break;
				}
			}

//			List<Figures> removedFigureTypesFiltered = removedFigureTypes.stream().filter(figure -> currentLayout.get(figure) == null).collect(Collectors.toList());
//			List<ServerFigureVariant> newFigureTypesFiltered = newFigureTypes.stream().filter(entry -> entry.position == null).collect(Collectors.toList());
//			int min = Math.min(removedFigureTypesFiltered.size(), newFigureTypesFiltered.size());
//			for (int i = 0; i < min; i++) {
//				layout.put(removedFigureTypesFiltered.get(i), null);
//				removedFigureTypes.remove(removedFigureTypesFiltered.get(i));
//				newFigureTypes.remove(newFigureTypesFiltered.get(i));
//			}
		}
		
		//second: same type (not perfect if multiple of one type are moved)
		for (FigureType figureType : FigureType.values()) {
			List<Figures> removedFigureTypes = removedFigures.get(figureType);
			List<Position> newFigureTypes = newFigures.get(figureType);
			if (removedFigureTypes == null || newFigureTypes == null)
				continue;
			int min = Math.min(removedFigureTypes.size(), newFigureTypes.size());
			for (int i = 0; i < min; i++)
				layout.put(removedFigureTypes.remove(0), newFigureTypes.remove(0));
		}
		
		//third: all remaining (not perfect if multiple are left over)
		List<Figures> removedLeftOver = removedFigures.values().stream().filter(Objects::nonNull).flatMap(Collection::stream).collect(Collectors.toList());
		List<Position> newLeftOver = newFigures.values().stream().filter(Objects::nonNull).flatMap(Collection::stream).collect(Collectors.toList());
		int leftOverCnt = removedLeftOver.size();
		if (leftOverCnt != newLeftOver.size())
			throw new RuntimeException("Figures got added(" + newLeftOver.size() + ") or removed(" + leftOverCnt + ")!");
		for (int i = 0; i < leftOverCnt; i++)
			layout.put(removedLeftOver.get(i), newLeftOver.get(i));
		
		return layout;
	}
}

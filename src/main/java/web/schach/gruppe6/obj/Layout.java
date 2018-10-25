package web.schach.gruppe6.obj;

import web.schach.gruppe6.util.Position;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.Objects;

public class Layout {
	
	public static final Layout INITIAL_LAYOUT;
	public static final Layout INITIAL_BEATEN;
	
	static {
		INITIAL_LAYOUT = new Layout("Initial", 0, null);
		INITIAL_BEATEN = new Layout("AllBeaten", -1, null);
		for (Figures figure : Figures.values()) {
			INITIAL_LAYOUT.put(figure, figure.positionInitial);
			INITIAL_LAYOUT.putType(figure, figure.typeInitial);
			INITIAL_BEATEN.put(figure, null);
			INITIAL_BEATEN.putType(figure, figure.typeInitial);
		}
	}
	
	public final String name;
	public final int moveId;
	public final GameState state;
	
	private EnumMap<Figures, Position> layout;
	private EnumMap<Figures, FigureType> typeMap;
	
	public Layout(String name, int moveId, GameState state) {
		this(name, moveId, state, new EnumMap<>(Figures.class), new EnumMap<>(Figures.class));
	}
	
	private Layout(String name, int moveId, GameState state, EnumMap<Figures, Position> layout, EnumMap<Figures, FigureType> typeMap) {
		this.name = name;
		this.moveId = moveId;
		this.state = state;
		this.layout = layout;
		this.typeMap = typeMap;
	}
	
	//state
	public PlayerColor playerColorCurrent() {
		return moveId % 2 == 0 ? PlayerColor.BLACK : PlayerColor.WHITE;
	}
	
	public PlayerColor playerColorNext() {
		return moveId % 2 == 0 ? PlayerColor.WHITE : PlayerColor.BLACK;
	}
	
	//layout
	public Position get(Figures key) {
		return layout.get(key);
	}
	
	@SuppressWarnings("UnusedReturnValue")
	public Position put(Figures key, Position value) {
		return layout.put(key, value);
	}
	
	public Figures at(Position pos) {
		for (Figures figure : Figures.values()) {
			if (Objects.equals(get(figure), pos))
				return figure;
		}
		return null;
	}
	
	//typeMap
	public FigureType getType(Figures key) {
		return typeMap.get(key);
	}
	
	@SuppressWarnings("UnusedReturnValue")
	public FigureType putType(Figures key, FigureType value) {
		return typeMap.put(key, value);
	}
	
	//toString
	@Override
	public String toString() {
		return toStringColored(false);
	}
	
	public String toStringColored() {
		return toStringColored(true);
	}
	
	private String toStringColored(boolean colored) {
		String[][] field = new String[8][8];
		for (int i = 0; i < 8; i++)
			Arrays.fill(field[i], " ");
		
		StringBuilder beatenWhite = new StringBuilder();
		StringBuilder beatenBlack = new StringBuilder();
		
		for (Figures figure : Figures.values()) {
			Position pos = get(figure);
			FigureType type = getType(figure);
			if (pos != null)
				field[pos.y][pos.x] = (colored ? (type.color == PlayerColor.WHITE ? "\u001b[36m" : "\u001b[30m") : "") + type.character;
			else
				(type.color == PlayerColor.WHITE ? beatenWhite : beatenBlack).append(type.character);
		}
		
		StringBuilder b = new StringBuilder();
		if (colored)
			b.append("\u001b[36m");
		b.append("--------\n");
		b.append(beatenWhite).append('\n');
		b.append("--------\n");
		if (colored)
			b.append("\u001b[30m");
		
		for (int y = 0; y < 8; y++) {
			for (int x = 0; x < 8; x++) {
				b.append(field[y][x]);
			}
			b.append('\n');
		}
		if (colored)
			b.append("\u001b[30m");
		
		b.append("--------\n");
		b.append(beatenBlack).append('\n');
		b.append("--------");
		return b.toString();
	}
}

package web.schach.gruppe6.obj;

import web.schach.gruppe6.util.Position;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;

public class Layout {
	
	public static final Layout INITIAL_LAYOUT;
	public static final Layout INITIAL_BEATEN;
	
	static {
		INITIAL_LAYOUT = new Layout("Initial", 0, null);
		for (Figures figure : Figures.values()) {
			INITIAL_LAYOUT.layout.put(figure, figure.positionInitial);
		}
		INITIAL_BEATEN = new Layout("AllBeaten", -1, null);
	}
	
	public final String name;
	public final int moveId;
	public final GameState state;
	private EnumMap<Figures, Position> layout;
	
	public Layout(String name, int moveId, GameState state) {
		this(name, moveId, state, new EnumMap<>(Figures.class));
	}
	
	private Layout(String name, int moveId, GameState state, EnumMap<Figures, Position> layout) {
		this.name = name;
		this.moveId = moveId;
		this.state = state;
		this.layout = layout;
	}
	
	public PlayerColor playerColorCurrent() {
		return moveId % 2 == 0 ? PlayerColor.BLACK : PlayerColor.WHITE;
	}
	
	public PlayerColor playerColorNext() {
		return moveId % 2 == 0 ? PlayerColor.WHITE : PlayerColor.BLACK;
	}
	
	public Position get(Figures key) {
		return layout.get(key);
	}
	
	public Position put(Figures key, Position value) {
		return layout.put(key, value);
	}
	
	public Figures at(Position pos) {
		for (Entry<Figures, Position> entry : layout.entrySet()) {
			if (Objects.equals(entry.getValue(), pos))
				return entry.getKey();
		}
		return null;
	}
	
	public Set<Entry<Figures, Position>> entrySet() {
		return layout.entrySet();
	}
	
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
		
		for (Entry<Figures, Position> entry : layout.entrySet()) {
			Position pos = entry.getValue();
			Figures figure = entry.getKey();
			if (pos != null)
				field[pos.y][pos.x] = (colored ? (figure.type.color == PlayerColor.WHITE ? "\u001b[36m" : "\u001b[30m") : "") + figure.type.character;
			else
				(figure.type.color == PlayerColor.WHITE ? beatenWhite : beatenBlack).append(figure.type.character);
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

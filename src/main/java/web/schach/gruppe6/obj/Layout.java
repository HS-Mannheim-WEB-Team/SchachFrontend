package web.schach.gruppe6.obj;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.Map.Entry;

public class Layout {
	
	public static final Layout INITIAL_LAYOUT;
	
	static {
		INITIAL_LAYOUT = new Layout(new EnumMap<>(Figures.class));
		for (Figures figure : Figures.values()) {
			INITIAL_LAYOUT.layout.put(figure, figure.initial);
		}
	}
	
	public EnumMap<Figures, Position> layout;
	
	private Layout(EnumMap<Figures, Position> layout) {
		this.layout = layout;
	}
	
	public Position get(Figures key) {
		return layout.get(key);
	}
	
	public Position put(Figures key, Position value) {
		return layout.put(key, value);
	}
	
	public void apply(Move move) {
		Position old = get(move.figure);
		if (!old.equals(move.from))
			throw new IllegalArgumentException("Figure " + move.figure + " at " + old + " != " + move.from);
		put(move.figure, move.to);
	}
	
	public Figures at(Position pos) {
		for (Entry<Figures, Position> entry : layout.entrySet()) {
			if (entry.getValue().equals(pos))
				return entry.getKey();
		}
		return null;
	}
	
	@SuppressWarnings("MethodDoesntCallSuperMethod")
	public Layout clone() {
		return new Layout(layout.clone());
	}
	
	@Override
	public String toString() {
		char[][] field = new char[8][8];
		for (int i = 0; i < 8; i++)
			Arrays.fill(field[i], ' ');
		
		for (Entry<Figures, Position> entry : layout.entrySet()) {
			Position pos = entry.getValue();
			field[pos.y][pos.x] = entry.getKey().type.character;
		}
		
		StringBuilder b = new StringBuilder();
		for (int i = 0; i < 8; i++)
			b.append(field[i]).append('\n');
		b.setLength(b.length() - 1);
		return b.toString();
	}
	
	public String toStringColored() {
		String[][] field = new String[8][8];
		for (int i = 0; i < 8; i++)
			Arrays.fill(field[i], " ");
		
		for (Entry<Figures, Position> entry : layout.entrySet()) {
			Position pos = entry.getValue();
			Figures figure = entry.getKey();
			field[pos.y][pos.x] = (figure.color == PlayerColor.WHITE ? "\u001b[36m" : "\u001b[30m") + figure.type.character;
		}
		
		StringBuilder b = new StringBuilder();
		for (int y = 0; y < 8; y++) {
			for (int x = 0; x < 8; x++) {
				b.append(field[y][x]);
			}
			b.append('\n');
		}
		b.setLength(b.length() - 1);
		b.append("\u001b[30m");
		return b.toString();
	}
}

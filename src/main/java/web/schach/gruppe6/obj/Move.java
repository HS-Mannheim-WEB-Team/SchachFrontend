package web.schach.gruppe6.obj;

public class Move {
	
	public final Figures figure;
	public final Position from;
	public final Position to;
	
	public Move(Figures figure, Position from, Position to) {
		this.figure = figure;
		this.from = from;
		this.to = to;
	}
}

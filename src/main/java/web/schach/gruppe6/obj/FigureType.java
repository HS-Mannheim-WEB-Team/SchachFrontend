package web.schach.gruppe6.obj;

public enum FigureType {
	
	PAWN("gui/iconsAndImages/iconset1/pawn_white.png", "gui/iconsAndImages/iconset1/pawn_black.png"),
	KING("gui/iconsAndImages/iconset1/king_white.png", "gui/iconsAndImages/iconset1/king_black.png"),
	QUEEN("gui/iconsAndImages/iconset1/queen_white.png", "gui/iconsAndImages/iconset1/queen_black.png"),
	KNIGHT("gui/iconsAndImages/iconset1/knight_white.png", "gui/iconsAndImages/iconset1/knight_black.png"),
	ROOK("gui/iconsAndImages/iconset1/rook_white.png", "gui/iconsAndImages/iconset1/rook_black.png"),
	BISHOP("gui/iconsAndImages/iconset1/bishop_white.png", "gui/iconsAndImages/iconset1/bishop_black.png");
	
	public final String iconPathWhite;
	public final String iconPathBlack;
	
	FigureType(String iconPathWhite, String iconPathBlack) {
		this.iconPathWhite = iconPathWhite;
		this.iconPathBlack = iconPathBlack;
	}
	
}

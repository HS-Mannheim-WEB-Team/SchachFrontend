package web.schach.gruppe6.obj;

public enum FigureType {
	
	PAWN('P', "web/schach/gruppe6/gui/iconsAndImages/iconset1/pawn_white.png", "web/schach/gruppe6/gui/iconsAndImages/iconset1/pawn_black.png"),
	KING('*', "web/schach/gruppe6/gui/iconsAndImages/iconset1/king_white.png", "web/schach/gruppe6/gui/iconsAndImages/iconset1/king_black.png"),
	QUEEN('Q', "web/schach/gruppe6/gui/iconsAndImages/iconset1/queen_white.png", "web/schach/gruppe6/gui/iconsAndImages/iconset1/queen_black.png"),
	KNIGHT('K', "web/schach/gruppe6/gui/iconsAndImages/iconset1/knight_white.png", "web/schach/gruppe6/gui/iconsAndImages/iconset1/knight_black.png"),
	ROOK('R', "web/schach/gruppe6/gui/iconsAndImages/iconset1/rook_white.png", "web/schach/gruppe6/gui/iconsAndImages/iconset1/rook_black.png"),
	BISHOP('B', "web/schach/gruppe6/gui/iconsAndImages/iconset1/bishop_white.png", "web/schach/gruppe6/gui/iconsAndImages/iconset1/bishop_black.png");
	
	public final char character;
	public final String iconPathWhite;
	public final String iconPathBlack;
	
	FigureType(char character, String iconPathWhite, String iconPathBlack) {
		this.character = character;
		this.iconPathWhite = iconPathWhite;
		this.iconPathBlack = iconPathBlack;
	}
}

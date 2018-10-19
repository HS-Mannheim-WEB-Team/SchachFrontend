package web.schach.gruppe6.obj;

public enum FigureType {

	PAWN('P', "web/schach/gruppe6/gui/iconsAndImages/iconset1/pawn_black.png", "web/schach/gruppe6/gui/iconsAndImages/iconset1/pawn_white.png"),
	KING('*', "gui/iconsAndImages/iconset1/king_white.png", "gui/iconsAndImages/iconset1/king_black.png"),
	QUEEN('Q', "gui/iconsAndImages/iconset1/queen_white.png", "gui/iconsAndImages/iconset1/queen_black.png"),
	KNIGHT('K', "gui/iconsAndImages/iconset1/knight_white.png", "gui/iconsAndImages/iconset1/knight_black.png"),
	ROOK('R', "gui/iconsAndImages/iconset1/rook_white.png", "gui/iconsAndImages/iconset1/rook_black.png"),
	BISHOP('B', "gui/iconsAndImages/iconset1/bishop_white.png", "gui/iconsAndImages/iconset1/bishop_black.png");
	
	public final char character;
	public final String iconPathWhite;
	public final String iconPathBlack;
	
	FigureType(char character, String iconPathWhite, String iconPathBlack) {
		this.character = character;
		this.iconPathWhite = iconPathWhite;
		this.iconPathBlack = iconPathBlack;
	}
}

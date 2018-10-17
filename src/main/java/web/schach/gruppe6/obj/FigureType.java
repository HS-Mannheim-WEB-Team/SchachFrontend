package web.schach.gruppe6.obj;

public enum FigureType {
	
	PAWN("GUI/iconsAndImages/iconset1/pawn_white.png", "GUI/iconsAndImages/iconset1/pawn_black.png"),
	KING("GUI/iconsAndImages/iconset1/king_white.png", "GUI/iconsAndImages/iconset1/king_black.png"),
	QUEEN("GUI/iconsAndImages/iconset1/queen_white.png", "GUI/iconsAndImages/iconset1/queen_black.png"),
	KNIGHT("GUI/iconsAndImages/iconset1/knight_white.png", "GUI/iconsAndImages/iconset1/knight_black.png"),
	ROOK("GUI/iconsAndImages/iconset1/rook_white.png", "GUI/iconsAndImages/iconset1/rook_black.png"),
	BISHOP("GUI/iconsAndImages/iconset1/bishop_white.png", "GUI/iconsAndImages/iconset1/bishop_black.png");
	
	public final String iconPathWhite;
	public final String iconPathBlack;
	
	FigureType(String iconPathWhite, String iconPathBlack) {
		this.iconPathWhite = iconPathWhite;
		this.iconPathBlack = iconPathBlack;
	}
	
	public String getIconPath(boolean isWhite) {
		return isWhite ? iconPathWhite : iconPathBlack;
	}
	
}

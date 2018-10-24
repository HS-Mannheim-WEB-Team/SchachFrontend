package web.schach.gruppe6.obj;

public enum GameState {
	
	WHITE_CHECK(PlayerColor.WHITE),
	WHITE_CHECK_MATE(PlayerColor.WHITE),
	BLACK_CHECK(PlayerColor.BLACK),
	BLACK_CHECK_MATE(PlayerColor.BLACK),
	STALEMATE(null);
	
	public final PlayerColor color;
	
	GameState(PlayerColor color) {
		this.color = color;
	}
}

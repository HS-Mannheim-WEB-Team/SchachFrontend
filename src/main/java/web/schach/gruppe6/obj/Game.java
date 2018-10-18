package web.schach.gruppe6.obj;

import web.schach.gruppe6.network.SchachConnection;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Game {
	
	public final int id;
	public final SchachConnection connection = new SchachConnection();
	
	public List<Layout> layouts = new ArrayList<>();
	
	public Game(int id) {
		this.id = id;
		layouts.add(Layout.INITIAL_LAYOUT);
	}
	
	public void update() throws IOException {
		int moveCount = connection.moveCount(id);
		
		for (int moveId = layouts.size() - 1; moveId < moveCount; moveId++) {
			Layout layout = layouts.get(moveId).clone();
			layout.apply(connection.getChange(id, moveId + 1, layout));
			layouts.add(layout);
		}
	}
}

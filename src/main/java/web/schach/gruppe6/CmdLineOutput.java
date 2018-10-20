package web.schach.gruppe6;

import web.schach.gruppe6.obj.Game;
import web.schach.gruppe6.obj.Layout;

import java.io.IOException;

public class CmdLineOutput {
	
	public static void main(String[] args) throws IOException, InterruptedException {
		Game game = new Game(1);
		
		int moveId = 0;
		//noinspection InfiniteLoopStatement
		while (true) {
			for (; moveId < game.layouts.size(); moveId++)
				printLayout(moveId, game.layouts.get(moveId));
			game.update();
			
			Thread.sleep(1000);
		}
	}
	
	private static void printLayout(int moveId, Layout layout) {
		System.out.println(moveId + ": ");
		System.out.println(layout.toStringColored());
		System.out.println();
	}
}

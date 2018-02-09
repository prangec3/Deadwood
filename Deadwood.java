// ToDo: All the things, console commands, etc
import java.util.*;

/*
hi its christine!

*/


public class Deadwood {

	public static void main(String[] args) {


		Board model = new Board();
		View view = new GUIView(model);
		GameController control = new GameController(view ,model);


		// Pairing: controller listens for view's input
		// and view listens for model updates
		view.addListener(control);
		model.addListener(view);

		control.RunGame();

		}




}

// ToDo: Implementation
import java.util.*;
public class GameController implements Listener {



private View CV =  null;
private Board board = null;
private boolean needNumPlayers = true;
boolean gameOver = false;

public GameController()
{
	this.CV = new ConsoleView();
}

public GameController(View view)
{
	this.CV = view;
}
public GameController(View view, Board board)
{
	this.CV = view;
	this.board = board;
}




// Reset Action not needed if fail cases do nothing
// public void resetAction() {
// }


// End turn not needed if switch statment handles it
// public void endTurn() {
// }


// Moved to Board.java
// public void moveAllToTrailer() {
// }

private void promptForNumPlayers()
{
	boolean valid = false;
	int num_players = 0;
	while(!valid)
	{
		try
		{
			CV.Update("Please enter number of players (2-3):\n");
			num_players = Integer.parseInt(CV.GetUserInput());
			if (num_players > 1 && num_players < 4)
				valid = true;
		}
		catch(Exception ex)
		{
			// do nothing, reprompt
		}

	}
	needNumPlayers = false;
	board = new Board(3, num_players);


		// Pairing: GC listens for view's input
		// and CV listens for Board updates
		CV.addListener(this);
		board.addListener(this.CV);

		board.Intro();
		board.Help();
		board.NextPlayer();
}

public void RunGame()
{

	if (board == null)
	{
			promptForNumPlayers();
	}
	else
	{
		board.Intro();
		board.AskNumPlayers();
	}
	CV.Run();

}





/* changed: originally playerTurn */
public void changed(String str){

	try
	{

		if (needNumPlayers)
		{
			try
			{
				int num_players = Integer.parseInt(str);
				if (num_players > 1 && num_players < 4)
				{
					needNumPlayers = false;
					board.initDays_Players(3, num_players);
					board.Intro();
					board.Help();
					board.NextPlayer();
				}
				else
				{
					board.Invalid();
					board.AskNumPlayers();
				}
			}
			catch (Exception ex)
			{
				board.Invalid();
				board.AskNumPlayers();
			}
		}
		else
		{
			processInput(str);

		}

	}
	catch (Exception ex)
	{
    CV.Update("Exception: [GameController]: " + ex.getMessage() + "\n\n");
	}

}   /* End of playersTurn() */

private void processInput(String str)
{
	String[] parsed = {"", "", "", ""};
	String cmd;
	if (parse_input(str, parsed, 4) > 4)
		cmd = "";
	else
		cmd = parsed[0].toLowerCase();

	switch (cmd) {
	case "who":
		/* Goal: identifies current player and *
		* any parts the player is working.    */
		board.GetPlayerInfo();
		break;

	case "where":
		/* Goal: describes current player's *
		* room and any active scenes.      */
		board.GetCurrentPlayerLocation();
		break;

	case "move":
		/* Goal: current player moves to *
		* indicated room.               */
		board.MovePlayer(parsed);

		break;

	case "work":
		/* Goal: current player takes  indicated role */
		board.WorkPlayer(parsed);
		break;

	case "upgrade":
		/* Goal: upgrade current player to indicated level */
		board.UpgradePlayer(parsed);
		break;

	case "rehearse":
		/* Goal: current player rehearses */
		board.RehearsePlayer();
		break;

	case "act":
		/* Goal: current player performs in current role */
		board.ActPlayer();
		break;

	case "end":
		/* Marks end of current player's turn */
		board.NextPlayer();
		break;

	case "help":
		/* Displays all commands */
		board.Help();
		break;

	//for debugging purposes
	case "~teleport":
		board.teleport(parsed);
		break;

	case "~endday":
		board.resetDay();
		break;

	case "~endgame":
		board.endGame();
		break;

	case "~gorandy":
		board.goRandy();
		break;

	case "quit":
	case "exit":
		board.Quit();
		/* Since the view is running the show, it'll terminate the program */
		//endOfTurn = true;
		//gameOver = true;
		break;

	default:
		board.Invalid();
	}
	board.FlushMsg();
}

int parse_input(final String input, String[] out, final int size)
{
	Scanner scan = new Scanner(input);
	String holder;

	int found = 0;
	while (scan.hasNext())
	{
		if (found == 1)
		{
			holder = scan.nextLine().trim();
		}
		else
			holder = scan.next();
		if (found < size)
			out[found] = holder;

		found++;
	}

	scan.close();
	return found;
}


}

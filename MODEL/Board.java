import java.util.*;

public class Board extends Observer implements Listener
{
// Observer grants broadcast(String msg) - will pass to View
// Comment from Christine
// Comment from Chris

private final String intro =
	"Welcome to DeadwoodTM!\n" +
	"This has been made by Christine, Christopher, Cody, and Lizzy!\n";

private final String help =
	"*===============================================*\n"+
	"*               DeadWood Commands               *\n"+
	"*-----------------------------------------------*\n"+
	"*   who, move (room name), work (part name),    *\n"+
	"*   upgrade $ (level), upgrade cr (level),      *\n"+
	"*   Rehearse, act, end, help, quit/exit         *\n"+
	"*===============================================*\n";

private final String invalid_input = "Invalid input.\n";

private boolean alreadyLoaded = false;
private int days;
private int currentDay;
private ArrayList<Player> players = new ArrayList<Player>();
private Player curPlayer = null;
private Card[] cardDeck;
private Location[] locations;
private Set[] sets;
private Office office;
private Location trailer;

public static final String quit_cmd = "[quit]";
public static final String init_cmd = "[init]";
public static final String wrapup_cmd = "[wrappup]";
private int wrappedUpCounter = 0;

public Board()
{

}


public Board(int num_days, int num_players)
{
	initDays_Players(num_days, num_players);
}

public void initialize()
{
	// LoadXML is now in board
	shuffleCards();
	distributeCards();
	moveAllToTrailer();
	resetShotCounters();
	broadcast(init_cmd);
}

public Set[] GetSetArray()
{
	return sets;
}

public Card[] GetCardDeck()
{
	return cardDeck;
}

public void initDays_Players(int num_days, int num_players)
{
	days = num_days;
	currentDay = 1;
	int pcount = 0;
	while (num_players > 0)
	{
		pcount++;

		players.add(new Player("Player " + pcount, null));
		num_players--;
	}

	curPlayer = players.get(0);
	XMLLoad();
	initialize();
}

private void XMLLoad()
{
	if (!alreadyLoaded)
	{
		office = MessingAroundWithXML.loadingOffice();
		trailer = MessingAroundWithXML.loadingTrailer();
		cardDeck = MessingAroundWithXML.loadingCards();
		sets = MessingAroundWithXML.loadingSets();

		for (Set set : sets)
		{
			set.addListener(this);
		}

		int size = sets.length + 2;
		locations = new Location[size];
		locations[0] = office;
		locations[1] = trailer;
		for (int i = 2; i < size; ++i)
		{
			locations[i] = sets[i-2];
		}
		alreadyLoaded = true;
		// broadcast("XML Has been loaded");
		// for (Card card : cardDeck)
		// {
		// 	broadcast("Got a card: " + card.GetName());
		// }
	}
}

public void AskNumPlayers()
{
	append_msg("Please enter number of players (2-3):\n");
	broadcast_staged();
}

// Not needed due to XMLLoad()
//public void Add(Location newLocation)
//{
//	locations.add(newLocation);
//}
//
//public void Add(Card newCard)
//{
//	cardDeck.add(newCard);
//}

public void Invalid()
{
	append_msg(invalid_input);
	broadcast_staged();
}

public Location GetLocationByName(String name)
{
	try
	{
		for (Location loc : locations)
		{
			if (name.toLowerCase().equals(loc.GetName().toLowerCase()))
				return loc;
		}

	return null;
	}
	catch(Exception ex)
	{
		append_msg("Exception [Board.GetLocationName]: " + ex.getMessage());
		broadcast_staged();
		return null;
	}

}

public void Intro()
{
	append_msg(intro);
}

public void Help()
{
	append_msg(help);
	broadcast_staged();
}

public void NextPlayer() {
	// Gets current player and readds him to end of array
	curPlayer = players.get(0);
	players.remove(0);
	players.add(curPlayer);
	append_msg("It is now " + curPlayer.GetName() + "'s Turn!\n");
	broadcast_staged();
	curPlayer.SignalTurn();
}

private void NotImp(String str)
{
	append_msg("[Not Implemented]" + str + "\n");
	broadcast_staged();
}

public void shuffleCards() {

	Random roller = new Random();
	Card[] tempDeck = new Card[cardDeck.length];
	int tempInt = roller.nextInt(tempDeck.length - 1);
	for (int i = 0; i < tempDeck.length; i++){
		tempInt = roller.nextInt(tempDeck.length - 1);
		while(tempDeck[tempInt] != null){
			if (tempInt < 39){
				tempInt++;
			} else{
				tempInt = 0;
			}
		}

		tempDeck[tempInt] = cardDeck[i];
	}

	cardDeck = tempDeck;

}


public void clearSets() {
}
public void distributeCards() {
	for (Card card : cardDeck)
		card.HideCard();

	int i = 0;
	for (Set set : sets)
	{
		set.setScene(cardDeck[i++]);

	}
}

public void GetCurrentPlayerName()
{
	broadcast(curPlayer.GetName());
}

public void GetPlayerInfo()
{
	// Format: player_name ($, cr) [working part, title]
	String msg = curPlayer.GetName() +
	 " ($"
	 + curPlayer.GetMoney() +
	 ", " + curPlayer.GetFame() +
	 "cr, rank: " +curPlayer.getRank() + ")";


	if (curPlayer.HasRole())
	{
		// Know: Player has a role
		msg += "\nworking " + curPlayer.GetRoleName() + ", "
		+ "\"" + curPlayer.GetRoleDescription() + "\"\n"+
		"Rehearsal Tokens: " + curPlayer.GetRehearsal();

	}




	append_msg(msg);

}

public void Quit()
{
	// Default implementation of this will be just fine
	broadcast("Quitting Deadwood....\n\n");
	broadcast(quit_cmd);
}

public Location GetActivePlayerLocation()
{
	return curPlayer.GetLocation();
}
public void GetCurrentPlayerLocation()
{

	Location loca = curPlayer.GetLocation();
	Set set = (loca instanceof Set ? (Set)loca : null);

	String msg =
		curPlayer.GetLocationName()+"\n";

		if (set != null)
		{

			Card scene = set.getScene();
			if (scene != null)
			{
				msg +=
					"Scene: " + set.getScene().GetName() + "\n" +
					"Shots left: " + set.getShotCount() + "\n" +
					"Budget: " + set.getScene().getBudget() + "\n" +
					"\nMain Roles:\n";

				for (Role r : scene.getRoles())
				{
					msg += "---> " + r.getName() + " (rank " + r.getRank() + ")\n";
				}



				msg += "\nExtra Roles:\n";
				for (Part r : set.getParts())
				{
					msg += "---> " + r.getName() + " (rank " + r.getRank() + ")\n";
				}
			}
			else
			{
				append_msg("The scene is wrapped up!");

			}
		}

			// for (Role role : scene.getMainRoles())

			// for (Role : )

		msg += "Neighbors: ";

	boolean first = true;
	for (String loc : curPlayer.GetLocation().GetAdjacentLocationNames())
	{
		if (first)
		{
			msg += loc;
			first = false;
		}
		else
			msg += ", " + loc;
	}
	append_msg(msg);
}

public void MovePlayer(String[] parsed)
{
	try
	{
		String res = parsed[1];
		String str = "";
		for (int i = 2; i < 4; ++i)
		{
			str = parsed[i];
			if (str.toLowerCase() != "move" && str != "")
				res += " " + str;
		}


		if(curPlayer.Move(GetLocationIfAdjacent(res)))
		{
			append_msg("Successful move to " + res);
			if (curPlayer.GetLocation() instanceof Set)
			{
				Set curloc = (Set) curPlayer.GetLocation();
				if (curloc.Scene())
				{
					append_msg("New scene appears");
				}
			}
		}
		else
			append_msg("You can't move because you are acting, you have moved this turn, or you requested an invalid location.");
		}

	catch (Exception ex)
	{
		append_msg("Exception [Board.MovePlayer]: " + ex.getMessage());
	}
}

public void WorkPlayer(String[] parsed)
{
	try
	{
		if(curPlayer.TakeRole(parsed[1]))
		{
			append_msg("Took the role successfully!");
		}
		else
		{
			append_msg("Cannot take the role specified.");
		}
	}
	catch (Exception ex)
	{
		append_msg("Exception [WorkPlayer]: " + ex.getMessage());
	}
}

public void UpgradePlayer(String[] parsed)
{
	String[] res = {"", "", "", ""};
	Scanner scan = new Scanner(parsed[1]);
	String holder = "";
	int found = 0;
	while (scan.hasNext())
	{
		holder = scan.next();
		if (found < 3)
			res[found++] = holder;
	}
	//NotImp("Upgrade");
	// parsed: "upgrade choice level"
	// imported from Cody's code in Player.java
	try
	{
	String choice = res[0];
	int level = Integer.parseInt(res[1]);
	String curloc = curPlayer.GetLocationName().toLowerCase();

	if (curloc != "office")
		append_msg("You're not in the casting office.");
	else
	{
		switch(choice)
		{
		case "$":
		case "money":
			if(curPlayer.Upgrade(choice, level))
				append_msg("Successfully set rank to " + level);
			else
				append_msg("Upgrade unsuccessful...");
			break;

		case "cr":
			if (curPlayer.Upgrade(choice, level))
				append_msg("Successfully set rank to " + level);
			else
				append_msg("Upgrade unsuccessful...");
			break;
		}
	}



	}
	catch(Exception ex)
	{
		append_msg("Upgrade unsuccessful...");
	}
	broadcast_staged();
}
public int GetCurrentPlayerID()
{
	return (curPlayer == null ? -1 : curPlayer.GetID());
}
private Location GetLocationIfAdjacent(String room)
{
	// Assumed to be referring to current player's Location
	Location ploc = curPlayer.GetLocation();
	try
	{
		for (String loc : ploc.GetAdjacentLocationNames())
		{
			if (loc.toLowerCase().equals(room.toLowerCase()))
			{
				return GetLocationByName(loc);
			}
		}
		return null;
	}
	catch (Exception ex)
	{
		broadcast("Exception [Board.GetLocationIfAdjacent]: " + ex.getMessage());
		return null;
	}
}

public void RehearsePlayer()
{
	if (curPlayer.rehearse())
	{
		append_msg("+1 rehearse token");
	}
	else
	{
		append_msg("You cannot rehearse at this time");
	}
}

public void ActPlayer()
{
	if (curPlayer.CanAct())
	{
		if (curPlayer.Act())
		{
			append_msg("Hey you acted successfully! Not a bad actor...");
			Set s = (curPlayer.GetLocation() instanceof Set ? (Set) curPlayer.GetLocation() : null);
			if (s != null)
			{
				if (s.getWrappedUp())
				{
					for (Player p : players)
					{
						if (p.GetLocation() == (Location)s)
						{
							p.SetRole(null);
						}
					}
					append_msg("Wrapping up scene!");
				}
			}
		}

		else
			append_msg("Due to acting poorly, you don't get anything...");
	}
	else
	{
		append_msg("You cannot act.");
	}
}

public void moveAllToTrailer()
{
	for (Player p : players)
	{
		p.SendToTrailer(trailer);
	}
	append_msg("All players moved to the trailer.");
}

//debugging method to quickly move players from room to room
public void teleport(String[] parsed){
	try
	{
		String res = parsed[1];
		String str = "";
		for (int i = 2; i < 4; ++i)
		{
			str = parsed[i];
			if (str != "")
				res += " " + str;
		}

		if(curPlayer.Move(GetLocationByName(res.toLowerCase())))
			append_msg("Successful teleporation to " + res);
		else
			append_msg("You can't move because you are acting, you have moved this turn, or you requested an invalid location.");
	}
	catch (Exception ex)
	{
		append_msg("Exception [Board.teleport]: " + ex.getMessage());
	}
}

public void resetDay()
{
	append_msg("The end of the day has been reached.");
	this.currentDay++;
	if(currentDay>days)
	{
		this.endGame();
	}
	else
	{
		this.moveAllToTrailer();
		for(Player curr: players)
		{
			curr.SetRole(null);
			curr.setRehearsals(0);
		}
		this.clearSets();
		this.shuffleCards();
		this.distributeCards();
		// this.
		resetShotCounters();
		NextPlayer();
		append_msg("It is now day "+currentDay+" out of "+days+" days.");

	}

}

private void resetShotCounters()
{
	for(Set curr: sets)
	{
		curr.setShotCount(curr.getMaxTakes());
	}
}

public void endGame()
{
	append_msg("The end of the game has been reached.");
	int winnerPoints = 0;
	String winnerName = null;
	for(Player curr: this.players)
	{
		int currScore = curr.CalcScore();
		if(currScore > winnerPoints)
		{
			winnerPoints = currScore;
			winnerName = curr.GetName();
		}
		append_msg(curr.GetName()+" has a final score of "+currScore+" points.");
	}
	append_msg("\nThe winner is "+winnerName+" with "+winnerPoints+" points.");
	broadcast_staged();
	broadcast(quit_cmd);
}

public ArrayList<Player> GetPlayerList()
{
	ArrayList<Player> newList = new ArrayList<Player>();
	int pcount = players.size();
	int find_id = 0;
	while (pcount > 0)
	{
		for (Player p : players)
		{
			if (p.GetID() == find_id)
			{
				find_id++;
				pcount--;
				newList.add(p);
			}
		}
	}

	return newList;
}

public void changed(String str)
{
	/* used for scenes wrapping up */
	if (str == wrapup_cmd)
	{
		wrappedUpCounter++;
		if (wrappedUpCounter == 9)
		{
			append_msg("9 scenes have been completed!");
			// I know there are 9 scenes wrapped up
			resetDay();
		}
	}
}

public void goRandy()
{
	Player randy = new Player();
	players.add(randy);
	curPlayer = randy;
	randy.SendToTrailer(trailer);

	append_msg("A new player has joined..........");
}

public void FlushMsg()
{
	broadcast_staged();
}


}

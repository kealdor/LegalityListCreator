package analysis;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import utility.FileConverter;

public class ChangeAnalyzer {
	
	public static void main(String[] args) throws IOException{
		String OLD_FILE_PATH = null;
		String NEW_FILE_PATH;
		if (args.length == 1){
			NEW_FILE_PATH = args[0];
		}
		else if (args.length == 2){
			//File holding the old legality list, set to first argument from the command line
			OLD_FILE_PATH = args[0];
			//File holding the new one, set to second argument from the command line
			NEW_FILE_PATH = args[1];			
		}
		else
		{
			NEW_FILE_PATH = "legal_cards.txt";
		}
		
		//Read the lists to arrays so we can use them
		Set<String> oldCards;
		if (OLD_FILE_PATH == null)
			oldCards = new HashSet<>(FileConverter.readToList(new URL("http://pdmtgo.com/legal_cards.txt")));
		else
			oldCards = new HashSet<>(FileConverter.readToList(OLD_FILE_PATH));
		Set<String> newCards = new HashSet<>(FileConverter.readToList(NEW_FILE_PATH));
		
		//Create a printwriter to spit out all this info for us.
		PrintWriter pw = new PrintWriter("stats.txt");

		//Find all cards that weren't legal last rotation.
		List<String> entering = newCards.stream().filter(c -> !oldCards.contains(c)).collect(Collectors.toList());
		pw.println("Entering Cards:");
		entering.forEach(pw::println);
		
		//Find all cards that have been priced out of the format.
		List<String> leaving = newCards.stream().filter(c -> !newCards.contains(c)).collect(Collectors.toList());
		pw.println("\nLeaving Cards:");
		leaving.forEach(pw::println);
		
		//Find the total change in how many cards are legal (should normally be positive, given that newly printed cards should enter the format)
		pw.println("\nNet change of "+(newCards.size()-oldCards.size())+" cards.");
		
		//Calculate the percent of cards from the old list that aren't legal anymore, and format it as a percentage to two decimal points.
		String percentLeaving = String.format("%3.2f",(double)(leaving.size())/(double)(oldCards.size())*100.0);
		//Calculate the percentage of cards now legal that weren't legal before, and format it as a percentage to two decimal points.
		String percentEntering = String.format("%3.2f", (double)(entering.size())/(double)(newCards.size())*100.0);
		
		//Print out the previously calculated values
		pw.println(percentLeaving+"% of cards rotated out.");
		pw.println(percentEntering+"% of cards are new.");
		
		//Close the printwriter.
		pw.close();
	}
}

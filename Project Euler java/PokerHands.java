import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

class Result {
	public int playerscore;
	public String playerhighestRank = "";
	// in case of a 3 of a kind and a pair or 2 pairs need to store 2nd highest card
	// set
	public String playerhighestRank2 = "";

	public Result(int playerScore, String playerHighestRank, String playerHighestRank2) {
		this.playerscore = playerScore;
		this.playerhighestRank = playerHighestRank;
		this.playerhighestRank2 = playerHighestRank2;
	}

	public int getPlayerScore() {
		return playerscore;
	}

	public String getPlayerHighestRank() {
		return playerhighestRank;
	}

	public String getPlayerHighestRank2() {
		return playerhighestRank2;
	}
}

public class PokerHands {
	// count for player 1 wins
	public static int player1HandsWon = 0;

	// initializing public variables
	public static int player1score;
	public static int player2score;

	public static String player1HighestRank = "";
	public static String player2HighestRank = "";

	// in case of a 3 of a kind and a pair or 2 pairs need to store 2nd highest card
	// set
	public static String player1HighestRank2 = "";
	public static String player2HighestRank2 = "";

	public static void main(String args[]) {
		// read in line for h1 and h2

		try {
			Scanner sc = new Scanner(new File("poker.txt"));

			while (sc.hasNextLine()) {
				String game = sc.nextLine();
				//debug System.out.println("Game " + game);

				String[] gameOfCards = game.split(" ");

				/*
				 * debug for (String i : gameOfCards) { System.out.println("Values in game " +
				 * i); }
				 */
				// initialize player hands
				String unsortedhand1 = "";
				String unsortedhand2 = "";

				// store 1st half of game into player 1 hand and 2nd half into player 2 hand

				for (int i = 0; i < gameOfCards.length / 2; i++) {
					unsortedhand1 += gameOfCards[i];
				}
				// debug System.out.println("method 2 " + unsortedhand1);

				for (int i = 5; i <= gameOfCards.length - 1; i++) {
					unsortedhand2 += gameOfCards[i];
				}

				// debug System.out.println(unsortedhand2);

				// store values into h1 and h2
				// debug String unsortedhand1 = "8C TS KC 9H 4S";
				// debug String unsortedhand2 = "7D 2S 5D 3S AC";

				// initial variables for player 1 map and player 2 map
				HashMap<String, Integer> hand1rank = new HashMap<String, Integer>();
				HashMap<String, Integer> hand1suit = new HashMap<String, Integer>();
				HashMap<String, Integer> hand2rank = new HashMap<String, Integer>();
				HashMap<String, Integer> hand2suit = new HashMap<String, Integer>();

				// call replace high cards (T-A) with number method for player 1 and player 2
				// hands
				String unSortedHighNumberReplacedHand1 = replaceHighCard(unsortedhand1);
				String unSortedHighNumberReplacedHand2 = replaceHighCard(unsortedhand2);

				// call find multiple ranks method for player 1 and player 2 hands
				String unSortedHand1Ranks = findMulitpleRanks(unSortedHighNumberReplacedHand1, hand1rank);
				String unSortedHand2Ranks = findMulitpleRanks(unSortedHighNumberReplacedHand2, hand2rank);

				// call sort method to sort cards from lowest to highest
				String sortedHand1Ranks = sortLowestToHighest(unSortedHand1Ranks);
				String sortedHand2Ranks = sortLowestToHighest(unSortedHand2Ranks);

				// call find multiple suits method for player 1 and player 2 hands
				findMulitpleSuits(unSortedHighNumberReplacedHand1, hand1suit);
				findMulitpleSuits(unSortedHighNumberReplacedHand2, hand2suit);

				// call poker hand method decides ranking of hand for each player
				Result player1Result = pokerHand(sortedHand1Ranks, hand1rank, hand1suit, player1HighestRank,
						player1HighestRank2);
				Result player2Result = pokerHand(sortedHand2Ranks, hand2rank, hand2suit, player2HighestRank,
						player2HighestRank2);

				// call compare method to compares scores from both hands and decides who the
				// winner is
				compareScores(player1Result, sortedHand1Ranks, player2Result, sortedHand2Ranks);
				System.out.println("player1HandsWon " + player1HandsWon);
			}

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void compareScores(Result player1Result, String sortedHand1Ranks, Result player2Result,
			String sortedHand2Ranks) {
		// Player 1
		player1score = player1Result.getPlayerScore();
		player1HighestRank = player1Result.getPlayerHighestRank();
		player1HighestRank2 = player1Result.getPlayerHighestRank2();

		// Player 2
		player2score = player2Result.getPlayerScore();
		player2HighestRank = player2Result.getPlayerHighestRank();
		player2HighestRank2 = player2Result.getPlayerHighestRank2();

		if (player1score > player2score) {
			player1HandsWon++;
		} else if (player1score == player2score) {
			if (player1score == 8 || player1score == 7 || player1score == 4 || player1score == 3 || player1score == 2) {

				if (Integer.parseInt(player1HighestRank) > Integer.parseInt(player2HighestRank)) {
					player1HandsWon++;
					return;
				}
				else if (Integer.parseInt(player1HighestRank) < Integer.parseInt(player2HighestRank)) {
					return; 
				}
				else if (Integer.parseInt(player1HighestRank) == Integer.parseInt(player2HighestRank)) {
					if (player1score == 7 || player1score == 3) {
						if (Integer.parseInt(player1HighestRank2) > Integer.parseInt(player2HighestRank2)) {
							player1HandsWon++;
							return;
						}
					}
					findHighestRankedCard(sortedHand1Ranks, sortedHand2Ranks);
				}
			}
			// if tie go to findHighest Ranked card method to decide who wins
			findHighestRankedCard(sortedHand1Ranks, sortedHand2Ranks);
		}
	}

	private static void findHighestRankedCard(String sortedHand1Ranks, String sortedHand2Ranks) {
		String[] hand1HighestCard = sortedHand1Ranks.split(" ");
		String[] hand2HighestCard = sortedHand2Ranks.split(" ");

		int[] sortedHand1HighestCard = new int[hand1HighestCard.length];
		int[] sortedHand2HighestCard = new int[hand2HighestCard.length];

		for (int i = 0; i < hand1HighestCard.length; i++) {
			sortedHand1HighestCard[i] = Integer.parseInt(hand1HighestCard[i]);
		}
		for (int i = 0; i < hand2HighestCard.length; i++) {
			sortedHand2HighestCard[i] = Integer.parseInt(hand2HighestCard[i]);
		}

		/*
		 * debug for(int i: sortedHand1HighestCard) { System.out.println(i); }
		 */

		/*
		 * debug for(int i : sortedHand2HighestCard) { System.out.println(i); }
		 */

		for (int i = sortedHand1HighestCard.length - 1; i >= 0; i--) {
			if (sortedHand1HighestCard[i] > sortedHand2HighestCard[i]) {
				System.out.println(sortedHand1HighestCard[i] + " is greater than " + sortedHand2HighestCard[i]);
				player1HandsWon++;
				break;
			} else if (sortedHand1HighestCard[i] < sortedHand2HighestCard[i]) {
				System.out.println(sortedHand1HighestCard[i] + " is less than " + sortedHand2HighestCard[i]);
				break;
			}
		}

	}

	private static Result pokerHand(String sortedHandRanks, HashMap<String, Integer> handRank,
			HashMap<String, Integer> handSuit, String findHighestRank, String find2ndHighestRank) {
		findHighestRank = "";
		find2ndHighestRank = ""; 
		String royalFlush = "10 11 12 13 14";

		// debug System.out.println(royalFlush);
		System.out.println(sortedHandRanks);

		ArrayList<String> straight = new ArrayList<String>();
		straight.add("2 3 4 5 6 ");
		straight.add("3 4 5 6 7 ");
		straight.add("4 5 6 7 8 ");
		straight.add("5 6 7 8 9 ");
		straight.add("6 7 8 9 10 ");
		straight.add("7 8 9 10 11 ");
		straight.add("8 9 10 11 12 ");
		straight.add("9 10 11 12 13 ");

		/*
		 * debug for(String set: straight) { System.out.println(set); }
		 */

		if (sortedHandRanks.contains(royalFlush) && handSuit.containsValue(5)) {
			System.out.println("Royal Flush");
			return new Result(10, findHighestRank, find2ndHighestRank);
		}

		else if (straight.contains(sortedHandRanks) && handSuit.containsValue(5)) {
			System.out.println("Stright Flush");
			return new Result(9, findHighestRank, find2ndHighestRank);
		}

		else if (handRank.containsValue(4)) {
			for (String card : handRank.keySet()) {
				if (handRank.get(card).equals(4)) {
					findHighestRank = card;
					System.out.println("HighestRank " + findHighestRank);
				}
			}

			System.out.println("4 of a kind");
			return new Result(8, findHighestRank, find2ndHighestRank);
		}

		else if (handRank.containsValue(3) && handRank.containsValue(2)) {
			for (String card : handRank.keySet()) {
				if (handRank.get(card).equals(3)) {
					findHighestRank = card;
					System.out.println("HighestRank " + findHighestRank);
				}
				if (handRank.get(card).equals(2)) {
					find2ndHighestRank = card;
					System.out.println("2nd Highest Rank " + find2ndHighestRank);
				}
			}
			System.out.println("Full House");
			return new Result(7, findHighestRank, find2ndHighestRank);
		}

		else if (handSuit.containsValue(5)) {
			System.out.println("Flush");
			return new Result(6, findHighestRank, find2ndHighestRank);
		}

		else if (straight.contains(sortedHandRanks) || sortedHandRanks.contains(royalFlush)) {
			System.out.println("Straight");
			return new Result(5, findHighestRank, find2ndHighestRank);
		}

		else if (handRank.containsValue(3)) {
			for (String card : handRank.keySet()) {
				if (handRank.get(card).equals(3)) {
					findHighestRank = card;
					// debug System.out.println("HighestRank " + findHighestRank);
				}
			}
			System.out.println("Three of a Kind");
			return new Result(4, findHighestRank, find2ndHighestRank);
		}

		else if (handRank.containsValue(2)) {
			int rankCount = 0;

			List<Integer> temp = new ArrayList<Integer>();
			for (String card : handRank.keySet()) {
				if (handRank.get(card).equals(2)) {
					rankCount++;
					findHighestRank = card;
					temp.add(Integer.parseInt(card));	
				}
			}
			
			if (rankCount == 2) {
				Collections.sort(temp);
				System.out.println(temp);
				find2ndHighestRank = temp.get(0).toString();
				findHighestRank = temp.get(1).toString();
				
				System.out.println("rank = 1, highest card " + findHighestRank);
				System.out.println("rank = 2, 2nd highest card " + find2ndHighestRank);
				
				System.out.println("Two pair");
				rankCount = 0;
				return new Result(3, findHighestRank, find2ndHighestRank);
			} else {
				findHighestRank = temp.get(0).toString();
				System.out.println("rank = 1, highest card " + findHighestRank);
				
				System.out.println("One pair");
				rankCount = 0;
				return new Result(2, findHighestRank, find2ndHighestRank);
			}
		}

		return new Result(1, findHighestRank, find2ndHighestRank);
	}

	private static String findMulitpleRanks(String sortedhand, Map handRank) {
		List<String> cardlist = new ArrayList<String>(Arrays.asList(sortedhand.split("[A-Z]+\\s+|[A-Z]+")));

		StringBuilder buildRank = new StringBuilder();
		for (String card : cardlist) {
			buildRank.append(card + " ");
		}
		String rank = buildRank.toString();
		// don't use Arrays.toString()....Arrays.toString(cardlist.toArray());
		//debug 
		System.out.println("Rank " + rank);

		for (String card : cardlist) {
			if (handRank.containsKey(card)) {
				handRank.replace(card, (int) handRank.get(card) + 1);
				// debug System.out.println(card + " " + (int) handRank.get(card));
			}

			else {
				handRank.put(card, 1);
				// debug System.out.println(card + " " + handRank.get(card).toString());
			}

		}
		// debug handRank.entrySet().forEach(System.out::println);
		return rank;
	}

	private static String findMulitpleSuits(String sortedhand, Map handRank) {
		List<String> cardListSuit = new ArrayList<String>(Arrays.asList(sortedhand.split("[0-9]+|\\s+[0-9]+")));

		StringBuilder buildSuits = new StringBuilder();
		for (String card : cardListSuit) {
			buildSuits.append(card + " ");
		}

		String suits = buildSuits.toString();
		// dont use Arrays.toString(cardListSuit.toArray());

		//debug 
		System.out.println("Suits " + suits);

		for (String card : cardListSuit) {
			if (handRank.containsKey(card)) {
				handRank.replace(card, (int) handRank.get(card) + 1);
				// debug System.out.println(card + " " + (int) handRank.get(card));
			}

			else {
				handRank.put(card, 1);
				// debug System.out.println(card + " " + handRank.get(card).toString());
			}

		}
		// debug handRank.entrySet().forEach(System.out::println);
		return suits;
	}

	private static String sortLowestToHighest(String unSortedHandRanks) {
		//debug System.out.println("unsorted " + unSortedHandRanks);
		String[] unSortedHand = unSortedHandRanks.split(" ");
		int[] sortedIntHand = new int[unSortedHand.length];
		String sortedHand = "";

		for (int i = 0; i < unSortedHand.length; i++) {
			sortedIntHand[i] = Integer.parseInt(unSortedHand[i]);
		}
		/*
		 * debug for (int i : sortedHand ) { System.out.println("Unsorted " + i); }
		 */

		Arrays.sort(sortedIntHand);
		/*
		 * debug for (int i : sortedHand ) { System.out.println("Sorted " + i); }
		 */

		for (int i : sortedIntHand) {
			sortedHand += i + " ";
		}
		//debug System.out.println("sorted " + sortedHand);

		return sortedHand;
	}

	private static String replaceHighCard(String unsortedhand) {
		// debug System.out.println(unsortedhand + " unsorted ");
		String sortedhand = unsortedhand.replace("T", "10");
		sortedhand = sortedhand.replace("J", "11");
		sortedhand = sortedhand.replace("Q", "12");
		sortedhand = sortedhand.replace("K", "13");
		sortedhand = sortedhand.replace("A", "14");

		// debug System.out.println("sorted " + sortedhand);
		return sortedhand;
	}

}

/*
 * old way of finding hightranked card when scores tied
 * 
 * for(String player1Card: hand1rank.keySet()) {
 * if(hand1rank.get(player1Card).equals(player1score)) { player1HighestRank =
 * player1Card; System.out.println("player1HighestRank " + player1HighestRank);
 * } } for(String player2Card: hand2rank.keySet()) {
 * if(hand2rank.get(player2Card).equals(player2score)) { player2HighestRank =
 * player2Card; System.out.println("player2HighestRank " + player2HighestRank);
 * } }
 */

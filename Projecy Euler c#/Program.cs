using System;
using System.Collections;
using System.Collections.Generic;
using System.IO;
using System.Text;
using System.Text.RegularExpressions;

namespace Project_Euler
{
    class Result
    {
        public int playerscore;
        public string playerhighestRank = "";
        // in case of a 3 of a kind and a pair or 2 pairs need to store 2nd highest card
        // set
        public string playerhighestRank2 = "";

        public Result(int playerScore, string playerHighestRank, string playerHighestRank2)
        {
            this.playerscore = playerScore;
            this.playerhighestRank = playerHighestRank;
            this.playerhighestRank2 = playerHighestRank2;
        }

        public int getPlayerScore()
        {
            return playerscore;
        }

        public string getPlayerHighestRank()
        {
            return playerhighestRank;
        }

        public string getPlayerHighestRank2()
        {
            return playerhighestRank2;
        }
    }
    class Program
    {
        // count for player 1 wins
        public static int player1HandsWon = 0;

        // initializing public variables
        public static int player1score;
        public static int player2score;

        public static string player1HighestRank = "";
        public static string player2HighestRank = "";

        // in case of a 3 of a kind and a pair or 2 pairs need to store 2nd highest card
        // set
        public static string player1HighestRank2 = "";
        public static string player2HighestRank2 = "";

        public static void Main(string[] args)
        {
            // read in line for h1 and h2

           try
            {
                string pathSource = @"\Users\Emily\source\repos\Project Euler\Project Euler\Poker.txt";
                StreamReader file = new StreamReader( new FileStream(pathSource, FileMode.Open, FileAccess.Read)); 

               while (!file.EndOfStream)
                {
                    String game = file.ReadLine();

                    String[] gameOfCards = game.Split(null);

                    // initialize player hands
                    String unsortedhand1 = "";
                    String unsortedhand2 = "";

                    // store 1st half of game into player 1 hand and 2nd half into player 2 hand

                    for (int i = 0; i < gameOfCards.Length/ 2; i++)
                    {
                        unsortedhand1 += gameOfCards[i];
                    }


                    for (int i = 5; i <= gameOfCards.Length - 1; i++)
                    {
                        unsortedhand2 += gameOfCards[i];
                    }

                    // store values into h1 and h2

                    // initial variables for player 1 map and player 2 map
                    Dictionary<string, int> hand1rank = new Dictionary<string, int>();
                    Dictionary<string, int> hand1suit = new Dictionary<string, int>();
                    Dictionary<string, int> hand2rank = new Dictionary<string, int>();
                    Dictionary<string, int> hand2suit = new Dictionary<string, int>();

                    // call replace high cards (T-A) with number method for player 1 and player 2
                    // hands
                    string unSortedHighNumberReplacedHand1 = replaceHighCard(unsortedhand1);
                    string unSortedHighNumberReplacedHand2 = replaceHighCard(unsortedhand2);

                    // call find multiple ranks method for player 1 and player 2 hands
                    string unSortedHand1Ranks = findMulitpleRanks(unSortedHighNumberReplacedHand1, hand1rank);
                    string unSortedHand2Ranks = findMulitpleRanks(unSortedHighNumberReplacedHand2, hand2rank);

                    // call sort method to sort cards from lowest to highest
                    string sortedHand1Ranks = sortLowestToHighest(unSortedHand1Ranks);
                    string sortedHand2Ranks = sortLowestToHighest(unSortedHand2Ranks);

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
                    Console.WriteLine("player1HandsWon " + player1HandsWon);
            }

             }
            catch (Exception e)
            {
                Console.WriteLine("The file could not be read:");
                Console.WriteLine(e.Message);
            }
            Console.Read();
        }

        private static void compareScores(Result player1Result, string sortedHand1Ranks, Result player2Result,
            string sortedHand2Ranks)
        {
            // Player 1
            player1score = player1Result.getPlayerScore();
            player1HighestRank = player1Result.getPlayerHighestRank();
            player1HighestRank2 = player1Result.getPlayerHighestRank2();

            // Player 2
            player2score = player2Result.getPlayerScore();
            player2HighestRank = player2Result.getPlayerHighestRank();
            player2HighestRank2 = player2Result.getPlayerHighestRank2();

            if (player1score > player2score)
            {
                player1HandsWon++;
            }
            else if (player1score == player2score)
            {
                if (player1score == 8 || player1score == 7 || player1score == 4 || player1score == 3 || player1score == 2)
                {

                    if (Int32.Parse(player1HighestRank) > Int32.Parse(player2HighestRank))
                    {
                        player1HandsWon++;
                        return;
                    }
                    else if (Int32.Parse(player1HighestRank) < Int32.Parse(player2HighestRank))
                    {
                        return;
                    }
                    else if (Int32.Parse(player1HighestRank) == Int32.Parse(player2HighestRank))
                    {
                        if (player1score == 7 || player1score == 3)
                        {
                            if (Int32.Parse(player1HighestRank2) > Int32.Parse(player2HighestRank2))
                            {
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

        private static void findHighestRankedCard(string sortedHand1Ranks, string sortedHand2Ranks)
        {
            string[] hand1HighestCard = sortedHand1Ranks.Trim().Split(null);
            string[] hand2HighestCard = sortedHand2Ranks.Trim().Split(null);

            int[] sortedHand1HighestCard = new int[hand1HighestCard.Length];
            int[] sortedHand2HighestCard = new int[hand2HighestCard.Length];

            for (int i = 0; i < hand1HighestCard.Length; i++)
            {
                sortedHand1HighestCard[i] = Int32.Parse(hand1HighestCard[i]);
            }
            for (int i = 0; i < hand2HighestCard.Length; i++)
            {
                sortedHand2HighestCard[i] = Int32.Parse(hand2HighestCard[i]);
            }


            for (int i = sortedHand1HighestCard.Length - 1; i >= 0; i--)
            {
                if (sortedHand1HighestCard[i] > sortedHand2HighestCard[i])
                {
                    //Console.WriteLine(sortedHand1HighestCard[i] + " is greater than " + sortedHand2HighestCard[i]);
                    player1HandsWon++;
                    break;
                }
                else if (sortedHand1HighestCard[i] < sortedHand2HighestCard[i])
                {
                    //Console.WriteLine(sortedHand1HighestCard[i] + " is less than " + sortedHand2HighestCard[i]);
                    break;
                }
            }

        }

        private static Result pokerHand(string sortedHandRanks, Dictionary<string, int> handRank,
                Dictionary<string, int> handSuit, string findHighestRank, string find2ndHighestRank)
        {
            findHighestRank = "";
            find2ndHighestRank = "";
            string royalFlush = "10 11 12 13 14";

            //Console.WriteLine(sortedHandRanks);

            ArrayList straight  = new ArrayList();
            straight.Add("2 3 4 5 6 ");
            straight.Add("3 4 5 6 7 ");
            straight.Add("4 5 6 7 8 ");
            straight.Add("5 6 7 8 9 ");
            straight.Add("6 7 8 9 10 ");
            straight.Add("7 8 9 10 11 ");
            straight.Add("8 9 10 11 12 ");
            straight.Add("9 10 11 12 13 ");

            if (sortedHandRanks.Contains(royalFlush) && handSuit.ContainsValue(5))
            {
                //Console.WriteLine("Royal Flush");
                return new Result(10, findHighestRank, find2ndHighestRank);
            }

            else if (straight.Contains(sortedHandRanks) && handSuit.ContainsValue(5))
            {
                //Console.WriteLine("Stright Flush");
                return new Result(9, findHighestRank, find2ndHighestRank);
            }

            else if (handRank.ContainsValue(4))
            {
                foreach (string card in handRank.Keys)
                {
                    if (handRank[card].Equals(4))
                    {
                        findHighestRank = card;
                        //Console.WriteLine("HighestRank " + findHighestRank);
                    }
                }

                //Console.WriteLine("4 of a kind");
                return new Result(8, findHighestRank, find2ndHighestRank);
            }

            else if (handRank.ContainsValue(3) && handRank.ContainsValue(2))
            {
                foreach (string card in handRank.Keys)
                {
                    if (handRank[card].Equals(3))
                    {
                        findHighestRank = card;
                        //Console.WriteLine("HighestRank " + findHighestRank);
                    }
                    if (handRank[card].Equals(2))
                    {
                        find2ndHighestRank = card;
                        //Console.WriteLine("2nd Highest Rank " + find2ndHighestRank);
                    }
                }
                //Console.WriteLine("Full House");
                return new Result(7, findHighestRank, find2ndHighestRank);
            }

            else if (handSuit.ContainsValue(5))
            {
                //Console.WriteLine("Flush");
                return new Result(6, findHighestRank, find2ndHighestRank);
            }

            else if (straight.Contains(sortedHandRanks) || sortedHandRanks.Contains(royalFlush))
            {
                //Console.WriteLine("Straight");
                return new Result(5, findHighestRank, find2ndHighestRank);
            }

            else if (handRank.ContainsValue(3))
            {
                foreach (string card in handRank.Keys)
                {
                    if (handRank[card].Equals(3))
                    {
                        findHighestRank = card;
                    }
                }
                //Console.WriteLine("Three of a Kind");
                return new Result(4, findHighestRank, find2ndHighestRank);
            }

            else if (handRank.ContainsValue(2))
            {
                int rankCount = 0;

                ArrayList temp = new ArrayList();
                foreach (string card in handRank.Keys)
                {
                    if (handRank[card].Equals(2))
                    {
                        rankCount++;
                        findHighestRank = card;
                        temp.Add(Int32.Parse(card));
                    }
                }

                if (rankCount == 2)
                {
                    temp.Sort(); 
                    //Console.WriteLine(temp);
                    find2ndHighestRank = temp[0].ToString();
                    findHighestRank = temp[1].ToString();

                    //Console.WriteLine("rank = 1, highest card " + findHighestRank);
                    //Console.WriteLine("rank = 2, 2nd highest card " + find2ndHighestRank);

                    //Console.WriteLine("Two pair");
                    rankCount = 0;
                    return new Result(3, findHighestRank, find2ndHighestRank);
                }
                else
                {
                    findHighestRank = temp[0].ToString();
                    //Console.WriteLine("rank = 1, highest card " + findHighestRank);

                    //Console.WriteLine("One pair");
                    rankCount = 0;
                    return new Result(2, findHighestRank, find2ndHighestRank);
                }
            }

            return new Result(1, findHighestRank, find2ndHighestRank);
        }

        private static String findMulitpleRanks(string sortedhand, Dictionary<string, int> handRank)
        {
            string[] cardlist = Regex.Split(sortedhand, "[A-Z]+\\s+|[A-Z]+");

            StringBuilder buildRank = new StringBuilder();
            foreach (string card in cardlist)
            {
                buildRank.Append(card + " ");
            }
            String rank = buildRank.ToString();
            
            //Console.WriteLine("Rank " + rank);

            foreach (string card in cardlist)
            {
                if (handRank.ContainsKey(card))
                {
                    handRank[card] = (int)handRank[card] + 1;
                }

                else
                {
                    handRank.Add(card, 1);
                }

            }
            return rank;
        }

        private static String findMulitpleSuits(string sortedhand, Dictionary<string, int> handRank)
        {
            string[] cardListSuit = Regex.Split(sortedhand, "[0-9]+|\\s+[0-9]+");

            StringBuilder buildSuits = new StringBuilder();
            foreach (string card in cardListSuit)
            {
                buildSuits.Append(card + " ");
            }

            String suits = buildSuits.ToString();

            //Console.WriteLine("Suits " + suits);

            foreach (string card in cardListSuit)
            {
                if (handRank.ContainsKey(card))
                {
                    handRank[card] = (int)handRank[card] + 1;
                }

                else
                {
                    handRank.Add(card, 1);
                }

            }
            return suits;
        }

        private static string sortLowestToHighest(string unSortedHandRanks)
        {
            string[] unSortedHand = unSortedHandRanks.Trim().Split(null);
            int[] sortedIntHand = new int[unSortedHand.Length];
            string sortedHand = "";

            for (int i = 0; i < unSortedHand.Length; i++)
            {
                try
                {
                    sortedIntHand[i] = Int32.Parse(unSortedHand[i]);
                }
                catch
                {
                    Console.WriteLine(unSortedHand[i]); 
                }
            }
            
            Array.Sort(sortedIntHand);
            
            foreach (int i in sortedIntHand)
            {
                sortedHand += i + " ";
            }
            
            return sortedHand;
        }

        private static String replaceHighCard(String unsortedhand)
        {
            String sortedhand = unsortedhand.Replace("T", "10");
            sortedhand = sortedhand.Replace("J", "11");
            sortedhand = sortedhand.Replace("Q", "12");
            sortedhand = sortedhand.Replace("K", "13");
            sortedhand = sortedhand.Replace("A", "14");

            return sortedhand;
        }
    }

}

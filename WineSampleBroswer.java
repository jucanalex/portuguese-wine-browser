package assignment2019;
/* MAIN CLASS */

import java.awt.Container;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;

import assignment2019.codeprovided.Query;
import assignment2019.codeprovided.QueryCondition;
import assignment2019.codeprovided.WineSample;
import assignment2019.codeprovided.WineType;

/**
 * WineSampleBrowser.java
 * 
 * Class that extends JFrame in order to create the frame for the GUI It is the
 * class that creates all the objects and runs the application. It shows and
 * answer the questions from the assignment brief in console as well as the
 * queries from the txt file. It creates the GUI elements.
 * 
 * @version 1.0 10.05.2019
 * @author Alexandru Jucan
 *
 */
public class WineSampleBroswer extends JFrame {

	// constructor
	public WineSampleBroswer(WineSampleBroswerPanel a_cellar) {

		setTitle("Portugese Wine Broswer");
		setSize(1488, 869);

		Container contentPane = this.getContentPane();
		contentPane.add(a_cellar);
	}

	public static void main(String[] args) {

		// The reading bit
		if (args.length == 0) {
			args = new String[] { "resources/winequality-red.csv", "resources/winequality-white.csv",
					"resources/queries.txt" };
		}
		String redWineFile = args[0];
		String whiteWineFile = args[1];
		String queriesFile = args[2];

		// Instantiating the GUI objects, the main program
		WineSampleCellar cellar = new WineSampleCellar(redWineFile, whiteWineFile, queriesFile);
		WineSampleBroswerPanel winebroswer_panel = new WineSampleBroswerPanel(cellar);
		WineSampleBroswer winebrowser = new WineSampleBroswer(winebroswer_panel);

		winebrowser.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		winebrowser.setVisible(true);

		// ====================================== THE QUESTIONS  ============================================
		System.out.println("======================== THE QUESTIONS =============================");

		// Questions 1 to 3 - how many samples of each type are there (RED, WHITE, ALL)
		System.out.println("Q1. There are " + cellar.getWineSampleCount(WineType.ALL) + " wine samples");
		System.out.println("Q2. There are " + cellar.getWineSampleCount(WineType.RED) + " red wine samples");
		System.out.println("Q3. There are " + cellar.getWineSampleCount(WineType.WHITE) + " white wine samples");

		// Question 4 - shows the samples graded with the best quality
		System.out.println("Q4. The wine samples graded with the best quality are: ");
		List<WineSample> bestQualWineList = cellar.bestQualityWine(WineType.ALL);
		for (WineSample sample : bestQualWineList)
			System.out.println("* Wine ID " + sample.getId() + " of type " + sample.getType()
					+ " with a quality score of " + sample.getQuality());

		// Question 5 - shows the samples graded with the worst quality
		System.out.println("Q5. The wine samples graded with the worst quality are: ");
		List<WineSample> worstQualWineList = cellar.worstQualityWine(WineType.ALL);
		for (WineSample sample : worstQualWineList)
			System.out.println("* Wine ID " + sample.getId() + " of type " + sample.getType()
					+ " with a quality score of " + sample.getQuality());

		// Question 6 - shows the samples with the highes PH
		System.out.println("Q6. The wine samples graded with the highest PH are: ");
		List<WineSample> highPhWineList = cellar.highestPH(WineType.ALL);
		for (WineSample sample : highPhWineList)
			System.out.println("* Wine ID " + sample.getId() + " of type " + sample.getType()
					+ " with a quality score of " + sample.getpH());

		// Question 7 - shows the samples with the lowest PH
		System.out.println("Q7. The wine samples graded with the lowest PH are: ");
		List<WineSample> lowPhWineList = cellar.lowestPH(WineType.ALL);
		for (WineSample sample : lowPhWineList)
			System.out.println("* Wine ID " + sample.getId() + " of type " + sample.getType()
					+ " with a quality score of " + sample.getpH());

		// Question 8 - shows the highest value of alcohol for RED wines
		System.out.println(
				"Q8. The highest value of alcohol in RED wines is:  " + cellar.highestAlcoholContent(WineType.RED));

		// Question 9 - shows the lowest value of citric acid for WHITE wines samples
		System.out.println(
				"Q9. The lowest value of citric acid in WHITE wines is:  " + cellar.lowestCitricAcid(WineType.WHITE));

		// Question 10 - shows the average value of a lochol grade for the whole samples
		// of white wines
		System.out.println("Q10. There average value of alcohol in white wines is:  "
				+ cellar.averageAlcoholContent(WineType.WHITE));

		// ================================== SOLVING THE QUERIES ===================================

		// creates the list of strings where the txt "words" are stored
		List<String> querywords = new ArrayList<>();
		querywords = WineSampleCellar.readQueryFile(queriesFile);

		// creates the list of queries where query filters will be stored
		List<Query> thequeries = new ArrayList<>();
		thequeries.addAll(cellar.readQueries(querywords));

		// solves the queries stored in thequeries list
		System.out.println(" \n\n ======================== THE QUERIES ========================");
		for (Query query : thequeries) {
			query.solveQuery();
			cellar.displayQueryResults(query);
		}

	}// main close
}

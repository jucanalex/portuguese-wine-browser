package assignment2019;

import java.util.ArrayList;
import java.util.List;

import assignment2019.codeprovided.AbstractWineSampleCellar;
import assignment2019.codeprovided.Query;
import assignment2019.codeprovided.QueryCondition;
import assignment2019.codeprovided.WineProperty;
import assignment2019.codeprovided.WineSample;
import assignment2019.codeprovided.WineType;

/**
 * WineSampleCellar.java
 * 
 * Class that extends AbstractWineSampleCellar.java It implements the methods to
 * make the query list and solve it, as well as the other methods in the
 * assignment brief
 * 
 * @version 1.0 10.05.2019
 * @author Alexandru Jucan
 *
 */
public class WineSampleCellar extends AbstractWineSampleCellar {
	
	//constructor
	public WineSampleCellar(String redWineFilename, String whiteWineFilename, String queryFilename) {
		super(redWineFilename, whiteWineFilename, queryFilename);
	}

	/**
	 * Reads a list of strings and from the list it constructs a new list of query objects
	 * 
	 *   @param - the list of strings
	 *   @return - the list of query objects
	 */
	@Override
	public List<Query> readQueries(List<String> queryList) {

		// the full list of queries
		List<Query> queries = new ArrayList<>();

		// an array list which stores the indexes of where the "select" word is found
		List<Integer> select = new ArrayList<>();
		for (int i = 0; i < queryList.size(); i++) {
			if (queryList.get(i).compareToIgnoreCase("select") == 0) {
				select.add(i);
			}
		}
		select.add(queryList.size() + 1);

		// iterating thru the "select" operators
		for (int i = 0; i < select.size() - 1; i++) {

			int queryiterator = select.get(i);
			// select the right types of wine for the query
			WineType winetype = null;

			if (queryList.get(queryiterator + 2).compareToIgnoreCase("or") == 0) {
				winetype = WineType.ALL;
				queryiterator = queryiterator + 5;

			} else if (queryList.get(queryiterator + 1).compareToIgnoreCase("red") == 0) {
				winetype = WineType.RED;
				queryiterator = queryiterator + 3;
			} else {
				winetype = WineType.WHITE;
				queryiterator = queryiterator + 3;
			}

			List<WineSample> querylist = new ArrayList<>();
			querylist = this.getWineSampleList(winetype);

			// creating the query condtions list to construct the query condition list
			List<QueryCondition> qcondList = new ArrayList<>();

			// iterating thru a single query after "select" key word
			int j = queryiterator;
			while (j < select.get(i + 1)) {
				WineProperty property = null;
				property = WineProperty.fromFileIdentifier(queryList.get(j));

				String operator = queryList.get(j + 1);
				String stringvalue = null;
				double value = 0.0;

				for (int k = 0; k < operator.length(); k++) {
					char c = operator.charAt(k);
					if (Character.isDigit(c)) {
						stringvalue = operator.substring(k);
						operator = operator.substring(0, k);
						value = Double.parseDouble(stringvalue);
						break;
					}
				}

				if (stringvalue != null) {
					j = j + 3;
				} else {
					// System.out.println(queryList.get(j+2) + " ");
					value = Double.parseDouble(queryList.get(j + 2));
					j = j + 4;
				}

				QueryCondition querycond = new QueryCondition(property, operator, value);
				qcondList.add(querycond);

			}

			queries.add(new Query(querylist, qcondList, winetype));

		}

		return queries;
	}

	/**
	 * Updates the cellar to contain all the winetypes
	 */
	@Override
	public void updateCellar() {
		List<WineSample> allwinelist = new ArrayList<>();
		allwinelist.addAll(getWineSampleList(WineType.RED));
		allwinelist.addAll(getWineSampleList(WineType.WHITE));
		wineSampleRacks.put(WineType.ALL, allwinelist);

	}

	/**
	 * Displays the wine samples filtered by the query taken as a param
	 * 
	 * @param - the query you want to filter the wine sample list with
	 */
	@Override
	public void displayQueryResults(Query query) {
		System.out.print("* select " + query.getWineType() + " where ");
		List<QueryCondition> qcond = new ArrayList<>();
		qcond = query.getQueryConditionList();
		for (QueryCondition cond : qcond)
		{
			System.out.print(cond + " ");
		}
		
		List<WineSample> winelist = query.getWineList();
		System.out.println("In total, " + winelist.size() + " " + query.getWineType() + " wine samples match your query");
		System.out.println("The list of wine samples are: ");
		
		if (winelist.size() == 0)
		{
			System.out.println("The are no wine samples to be shown. \n");
		}
		else {
			for (int i = 0; i < winelist.size(); i++)
			{
			WineSample sample = winelist.get(i);
			System.out.println(sample.getType() + " wine, sample #" + sample.getId() + " with the following properties: alcohol" + sample.getAlcohol());
			}
			System.out.println("");
		}

	}

	/**
	 * Returns a list with the best quality of the wines from a cellar
	 */
	@Override
	public List<WineSample> bestQualityWine(WineType wineType) {
		// the wine list with wineType to work on
		List<WineSample> winelist = getWineSampleList(wineType);

		// the list with the best quality wines to be returned
		List<WineSample> bestQualWineList = new ArrayList<>();
		double max = 0;
		for (WineSample sample : winelist) {
			double qual = sample.getQuality();
			if (qual > max) {
				bestQualWineList.clear();
				bestQualWineList.add(sample);
				max = qual;
			} else if (qual == max) {
				bestQualWineList.add(sample);
			}
		}

		return bestQualWineList;
	}

	/**
	 * Returns a list with the worst quality of the wines from a cellar
	 */
	@Override
	public List<WineSample> worstQualityWine(WineType wineType) {
		// the wine list with wineType to work on
		List<WineSample> winelist = getWineSampleList(wineType);

		// the list with the best quality wines to be returned
		List<WineSample> worstQualWineList = new ArrayList<>();
		double min = 11;
		for (WineSample sample : winelist) {
			double qual = sample.getQuality();
			if (qual < min) {
				worstQualWineList.clear();
				worstQualWineList.add(sample);
				min = qual;
			} else if (qual == min) {
				worstQualWineList.add(sample);
			}
		}

		return worstQualWineList;
	}

	/**
	 * Returns a list with the higest ph wines samples from a cellar
	 */
	@Override
	public List<WineSample> highestPH(WineType wineType) {
		// the wine list with wineType to work on
		List<WineSample> winelist = getWineSampleList(wineType);

		// the list with the best quality wines to be returned
		List<WineSample> highPHWineList = new ArrayList<>();
		double max = -1;
		for (WineSample sample : winelist) {
			double ph = sample.getpH();
			if (ph > max) {
				highPHWineList.clear();
				highPHWineList.add(sample);
				max = ph;
			} else if (ph == max) {
				highPHWineList.add(sample);
			}
		}

		return highPHWineList;
	}

	/**
	 * Returns a list with the lowest ph wines samples from a cellar
	 */
	@Override
	public List<WineSample> lowestPH(WineType wineType) {
		// the wine list with wineType to work on
		List<WineSample> winelist = getWineSampleList(wineType);

		// the list with the best quality wines to be returned
		List<WineSample> lowPHWineList = new ArrayList<>();
		double min = 15;
		for (WineSample sample : winelist) {
			double ph = sample.getpH();
			if (ph < min) {
				lowPHWineList.clear();
				lowPHWineList.add(sample);
				min = ph;
			} else if (ph == min) {
				lowPHWineList.add(sample);
			}
		}

		return lowPHWineList;
	}

	/**
	 * Returns a doulbe with the higest alchohol content found in the wines samples from a cellar
	 */
	@Override
	public double highestAlcoholContent(WineType wineType) {
		// the wine list with wineType to work on
		List<WineSample> winelist = getWineSampleList(wineType);

		double max = 0;
		for (WineSample sample : winelist)
			if (sample.getAlcohol() > max)
				max = sample.getAlcohol();

		return max;
	}
	
	/**
	 * Returns a double value with the lowest citric acid found in the wines samples from a cellar
	 */
	@Override
	public double lowestCitricAcid(WineType wineType) {
		// the wine list with wineType to work on
		List<WineSample> winelist = getWineSampleList(wineType);

		double min = 100;
		for (WineSample sample : winelist)
			if (sample.getCitricAcid() < min)
				min = sample.getCitricAcid();

		return min;
	}

	/**
	 * Returns a double value with the average alcohol content found in the wines samples from a cellar
	 */
	@Override
	public double averageAlcoholContent(WineType wineType) {
		// the wine list with wineType to work on
		List<WineSample> winelist = getWineSampleList(wineType);

		double sum = 0;
		if (!winelist.isEmpty()) {
			for (WineSample sample : winelist) {
				sum += sample.getAlcohol();
			}
			return sum / winelist.size();
		}
		return 0;
	}

}

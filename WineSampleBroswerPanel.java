package assignment2019;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JTextArea;

import assignment2019.codeprovided.AbstractWineSampleBrowserPanel;
import assignment2019.codeprovided.AbstractWineSampleCellar;
import assignment2019.codeprovided.WineSample;
import assignment2019.codeprovided.WineType;
import assignment2019.codeprovided.Query;
import assignment2019.codeprovided.QueryCondition;
import assignment2019.codeprovided.WineProperty;

/**
 * WineSampleBroswerPanel.java
 * 
 *  Class that extends AbstractWineSampleBroserpanel.java
 *  It implements the elements of the GUI as discussed in the handout
 *  and add several more methods to make the code readable and make the methods short
 * 
 * @version 1.0 10.05.2019
 * @author Alexandru Jucan
 *
 */
public class WineSampleBroswerPanel extends AbstractWineSampleBrowserPanel {

	//constructor
	public WineSampleBroswerPanel(AbstractWineSampleCellar cellar) {
		super(cellar);
		updateStatistics();
		updateWineList();
//		int fontPoints = 16; 
//		filteredWineSamplesTextArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, fontPoints ));
//		statisticsTextArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, fontPoints ));
	}

	/**
	 * It is adds the listeners to 
	 *    - button AddFilter
	 *    - button ClearFilter
	 *    - combobox for the winetype (ALL, RED, WHITE)
	 */
	@Override
	public void addListeners() {
		buttonAddFilter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addFilter();
			}

		});

		buttonClearFilters.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				clearFilters();
			}
		});

		comboWineTypes.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String type = (String) comboWineTypes.getSelectedItem();
				wineType = WineType.valueOf(type);
				updateStatistics();
			}
		});

	}

	/**
	 * Adds the filter that a person selected from the GUI, adds it to the querycondition list
	 */
	@Override
	public void addFilter() {
		try {
			String wineProp = (String) comboProperties.getSelectedItem();
			wineProp = wineProp.replaceAll("\\s+", ""); // losing the space in order to convert to WineProperty
			WineProperty wineProperty = WineProperty.valueOf(wineProp);
			String operator = (String) comboOperators.getSelectedItem();
			double thevalue = Double.parseDouble(value.getText());

			// adds the new query to the list and updates the GUI
			queryConditionList.add(new QueryCondition(wineProperty, operator, thevalue));
			this.updateStatistics();
		}

		catch (Exception e) {
			queryConditionsTextArea.setText("\t\t\t\t\t\tWARNING! You must write a value to have a filter. ");
		}
	}

	/**
	 * It clears the QueryConditionList in order to have no more filters applied to the winelist
	 */
	@Override
	public void clearFilters() {
		this.getQueryConditionList().removeAll(this.getQueryConditionList());
		this.updateStatistics();
	}

	/**
	 * Updates the 3 text areas - queryCondtionTextArea, statisticsTextarea and
	 * filteredWineSamplesTextArea to the new criteria
	 */
	@Override
	public void updateStatistics() {
		this.updateWineList();

		if (!filteredWineSampleList.isEmpty()) {
			this.updateQueryConditionTextArea();
			this.updateStatisticsTextArea();
		} else {
			statisticsTextArea.setText(null);
			queryConditionsTextArea.setText("\t\t\t\t\t\tSORRY! There are no wine samples that match your query :(");
		}

	}

	/**
	 * Updates filteredWineList to the queries that are applied to the original wine
	 * list
	 */
	@Override
	public void updateWineList() {
		filteredWineSampleList = cellar.getWineSampleList(wineType);
		this.executeQuery();

		filteredWineSamplesTextArea
				.setText("WINE TYPE \t ID \t Fixed Acidity \t Volatile Acidity \t Citric Acidity \t Residual Sugar \t  Chlorides"
						+ " \t Free Sulfur dioxide \t Total sulfur dioxide \t Density \t PH \t Sulphayyes \t Alchohol \t Quality \n ");
		for (WineSample sample : filteredWineSampleList) {
			filteredWineSamplesTextArea
					.append(sample.getType() + " \t " + sample.getId() + " \t " + sample.getFixedAcidity()
							+ " \t" + sample.getVolatileAcidity() + " \t\t " + sample.getCitricAcid()
							+ " \t " + sample.getResidualSugar() + " \t\t " + sample.getChlorides() + " \t "
							+ sample.getFreeSulfurDioxide() + " \t\t " + sample.getTotalSulfurDioxide() + " \t\t "
							+ sample.getDensity() + " \t " + sample.getpH() + " \t " + sample.getSulphates()
							+ " \t " + sample.getAlcohol() + " \t " + sample.getQuality() + " \n");
		}

	}

	/**
	 * Executes the query list applied to the winelist in order to filter the list
	 */
	@Override
	public void executeQuery() {
		if (!this.getQueryConditionList().isEmpty()) {
			Query panelQuery = new Query(cellar.getWineSampleList(wineType), this.getQueryConditionList(), wineType);
			filteredWineSampleList = panelQuery.solveQuery();
		}
	}

	
	/**
	 * Updates the queryStatisticsTextArea to to show the min,max and avg of the winelist queries, on the GUI
	 */
	public void updateStatisticsTextArea() {
		statisticsTextArea
				.setText(" \t\t Fixed Acidity \t\t Volatile Acidity \t Citric Acidity \t\t Residual Sugar \t Chlorides"
						+ " \t\t Free Sulfur Dioxide \t Total sulfur dioxide \t Density \t"
						+ " \t PH \t\t Sulphates \t\t Alchohol \t\t Quality \n");

		// finds the max of each property - using the findMaxProperties method
		double[] maxProperty = new double[12];
		maxProperty = this.findMaxProperties(filteredWineSampleList);
		statisticsTextArea.append("Max \t\t ");
		for (int i = 0; i < maxProperty.length; i++) {
			statisticsTextArea.append(maxProperty[i] + " \t\t ");
		}

		// find the min of each property - using the findMinProperties method
		double[] minProperty = new double[12];
		minProperty = this.findMinProperties(filteredWineSampleList);
		statisticsTextArea.append("\nMin \t\t ");
		for (int i = 0; i < minProperty.length; i++) {
			statisticsTextArea.append(minProperty[i] + " \t\t ");
		}

		// find the avg of each property - using the findAvgProperties method
		double[] avgProperty = new double[12];
		avgProperty = this.findAvgProperties(filteredWineSampleList);
		statisticsTextArea.append("\nAvg \t\t ");
		for (int i = 0; i < avgProperty.length; i++) {
			statisticsTextArea.append(avgProperty[i] + " \t\t ");
		}

		// showing how many samples shown out of all wines of the type selected
		int totalSamples = cellar.getWineSampleCount(wineType);
		int filteredSamples = filteredWineSampleList.size();
		statisticsTextArea.append(" \n Showing " + filteredSamples + " out of " + totalSamples + " samples.");

	}

	/**
	 * Updates the queryCondtionTextArea to to show all the queries in the Q Condtion List on the GUI
	 */
	public void updateQueryConditionTextArea() {
		queryConditionsTextArea.setText(null);
		for (QueryCondition cond : queryConditionList) {
			queryConditionsTextArea.append(cond.toString() + "; ");
		}
	}

	/**
	 * Finds the maximum value of each property in a winelist
	 * 
	 * @param winelist - the winelist to find the maxium values of the properties
	 * @return an pointer to an array with the maximum of each property
	 */
	private double[] findMaxProperties(List<WineSample> winelist) {
		double[] maxProperty = new double[12];
		Arrays.fill(maxProperty, Double.MIN_VALUE);

		for (WineSample sample : filteredWineSampleList) {
			if (sample.getFixedAcidity() > maxProperty[0])
				maxProperty[0] = sample.getFixedAcidity();

			if (sample.getVolatileAcidity() > maxProperty[1])
				maxProperty[1] = sample.getVolatileAcidity();

			if (sample.getCitricAcid() > maxProperty[2])
				maxProperty[2] = sample.getCitricAcid();

			if (sample.getResidualSugar() > maxProperty[3])
				maxProperty[3] = sample.getResidualSugar();

			if (sample.getChlorides() > maxProperty[4])
				maxProperty[4] = sample.getChlorides();

			if (sample.getFreeSulfurDioxide() > maxProperty[5])
				maxProperty[5] = sample.getFreeSulfurDioxide();

			if (sample.getTotalSulfurDioxide() > maxProperty[6])
				maxProperty[6] = sample.getTotalSulfurDioxide();

			if (sample.getDensity() > maxProperty[7])
				maxProperty[7] = sample.getDensity();

			if (sample.getpH() > maxProperty[8])
				maxProperty[8] = sample.getpH();

			if (sample.getSulphates() > maxProperty[9])
				maxProperty[9] = sample.getSulphates();

			if (sample.getAlcohol() > maxProperty[10])
				maxProperty[10] = sample.getAlcohol();

			if (sample.getQuality() > maxProperty[11])
				maxProperty[11] = sample.getQuality();
		}

		return maxProperty;
	}

	/**
	 * Finds the minimum value of each property in a winelist
	 * 
	 * @param winelist - the winelist to find the minimum values of the properties
	 * @return an pointer to an array with the minimum of each property
	 */
	private double[] findMinProperties(List<WineSample> winelist) {
		double[] minProperty = new double[12];
		Arrays.fill(minProperty, Double.MAX_VALUE);

		for (WineSample sample : filteredWineSampleList) {
			if (sample.getFixedAcidity() < minProperty[0])
				minProperty[0] = sample.getFixedAcidity();

			if (sample.getVolatileAcidity() < minProperty[1])
				minProperty[1] = sample.getVolatileAcidity();

			if (sample.getCitricAcid() < minProperty[2])
				minProperty[2] = sample.getCitricAcid();

			if (sample.getResidualSugar() < minProperty[3])
				minProperty[3] = sample.getResidualSugar();

			if (sample.getChlorides() < minProperty[4])
				minProperty[4] = sample.getChlorides();

			if (sample.getFreeSulfurDioxide() < minProperty[5])
				minProperty[5] = sample.getFreeSulfurDioxide();

			if (sample.getTotalSulfurDioxide() < minProperty[6])
				minProperty[6] = sample.getTotalSulfurDioxide();

			if (sample.getDensity() < minProperty[7])
				minProperty[7] = sample.getDensity();

			if (sample.getpH() < minProperty[8])
				minProperty[8] = sample.getpH();

			if (sample.getSulphates() < minProperty[9])
				minProperty[9] = sample.getSulphates();

			if (sample.getAlcohol() < minProperty[10])
				minProperty[10] = sample.getAlcohol();

			if (sample.getQuality() < minProperty[11])
				minProperty[11] = sample.getQuality();
		}

		return minProperty;
	}

	/**
	 * Finds the average value of each property in a winelist
	 * 
	 * @param winelist - the winelist to find the average values of the properties
	 * @return an pointer to an array with the average of each property
	 */
	private double[] findAvgProperties(List<WineSample> winelist) {
		double[] avgProperty = new double[12];
		Arrays.fill(avgProperty, 0);

		for (WineSample sample : filteredWineSampleList) {

			avgProperty[0] += sample.getFixedAcidity();
			avgProperty[1] += sample.getVolatileAcidity();
			avgProperty[2] += sample.getCitricAcid();
			avgProperty[3] += sample.getResidualSugar();
			avgProperty[4] += sample.getChlorides();
			avgProperty[5] += sample.getFreeSulfurDioxide();
			avgProperty[6] += sample.getTotalSulfurDioxide();
			avgProperty[7] += sample.getDensity();
			avgProperty[8] += sample.getpH();
			avgProperty[9] += sample.getSulphates();
			avgProperty[10] += sample.getAlcohol();
			avgProperty[11] += sample.getQuality();

		}

		for (int i = 0; i < 12; i++) {
			avgProperty[i] = avgProperty[i] / winelist.size();
			avgProperty[i] = Math.floor(avgProperty[i] * 100) / 100;
		}

		return avgProperty;
	}

}

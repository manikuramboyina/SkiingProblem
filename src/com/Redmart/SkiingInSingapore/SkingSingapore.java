package com.Redmart.SkiingInSingapore;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Random;
import java.util.Scanner;


/**
 * @author Mani
 * 
 * Skiing in Singapore:
 * 
 *Evaluates the deepest point in a map. If there is a depth match i. e both point A and point B have a height of x and x,
 *then we check the fall1 from point A to its end point and fall2 from point B to its end point.
 *The elevation with the bigger fall will be returned.
 * 
 */
public class SkingSingapore {

	int mapX, mapY;
	Elevation[][] elevationPoints;

	public SkingSingapore(Elevation[][] elevationPoints) {
		this.elevationPoints = elevationPoints;
		this.mapX = elevationPoints.length;
		this.mapY =  elevationPoints[0].length;
		this.initializeValues();
		this.calculateDepthOfAllPoints();
		
	}

	/**
	 * Method to calculate depth of all the elevation points, which inturn calls the 
	 * recursive depth of each node.
	 */
	private void calculateDepthOfAllPoints() {
		// TODO Auto-generated method stub
		for (int i = 0; i < mapX; i++) {
			for (int j = 0; j < mapY; j++) {
				calculateDepth(elevationPoints[i][j]);
			}
		}
	}


	/**
	 * Method to recursively search depth of a node and update the maximum depth,
	 * next deepest point, and its ending elevation point on its way up.
	 * 
	 * It also doesn't search a visited node, which makes it more efficient.
	 * 
	 * @param parentElevation
	 * @param currentElevation
	 * @return
	 */
	private Elevation calculateDepth(Elevation currentElevation) {

		if (currentElevation.visited) {
			return currentElevation;
		}

		else {
			currentElevation.visited = true;

			List<Elevation> adjSteepNodes = currentElevation.AdjSteepNodes;

			if (adjSteepNodes.isEmpty()) {
				currentElevation.maxDepth = 0;
				currentElevation.nextDeepest = null;
			} else {
				Elevation maxElevation = calculateDepth(adjSteepNodes.get(0));
				for (int i = 1; i < adjSteepNodes.size(); i++) {
					Elevation nextElevation = calculateDepth(adjSteepNodes.get(i));
					if(maxElevation.maxDepth < nextElevation.maxDepth)
						maxElevation = nextElevation;
					else
						{
						if (nextElevation.maxDepth == maxElevation.maxDepth) {
							maxElevation = 	getDeepestElevation(nextElevation, maxElevation);
						}
					}
							
					}
				
				currentElevation.maxDepth = maxElevation.maxDepth;
				currentElevation.nextDeepest = maxElevation;
				currentElevation.endingElevation = maxElevation.endingElevation;
				}
				
			}
			currentElevation.maxDepth++;
			return currentElevation;
		}
	
	/**
	 * Get highest elevation(maximum depth) in the entire map.
	 * In case of a match in max depth, check which fall is higher.
	 * 
	 */
	private Elevation highestElevation(){
		Elevation highestElevation = elevationPoints[0][0];
		for (int i = 0; i < mapX; i++) {
			for (int j = 0; j < mapY; j++) {
				if (elevationPoints[i][j].maxDepth < highestElevation.maxDepth) {
					continue;
				} else if (elevationPoints[i][j].maxDepth > highestElevation.maxDepth) {
					highestElevation = elevationPoints[i][j];
				} else {
					highestElevation = getDeepestElevation(highestElevation,elevationPoints[i][j]);		
				}
			}
		}
		return highestElevation;
	}
	
	/**
	 * Checks which fall is deeper between the two elevation points given.
	 * 
	 * @param depthMatchElevation1
	 * @param depthMatchElevation2
	 * @return
	 */
	private Elevation getDeepestElevation(Elevation depthMatchElevation1,
			Elevation depthMatchElevation2) {
		// TODO Auto-generated method stub
		int fall1 = depthMatchElevation1.elevation
				- depthMatchElevation1.endingElevation;
		int fall2 = depthMatchElevation2.elevation
				- depthMatchElevation2.endingElevation;
		return (fall1 > fall2) ? depthMatchElevation1: depthMatchElevation2;
	}

	/**
	 * 
	 * This method prints the Highest fall from the starting Elevation
	 * 
	 * @param highestElevation
	 */
	public void printHighestFall() {
		// TODO Auto-generated method stub
		Elevation elevation = highestElevation();
		System.out.println("\n\n ############## Highest Fall ################# \n\n");
		while (elevation.nextDeepest != null) {
			System.out.println(elevation.elevation + " -> ");
			elevation = elevation.nextDeepest;
		}

		System.out.println(elevation.elevation + " -> ");

	}

	/**
	 * 
	 * This method initializes the adjacent steep nodes(less elevation) for each elevation in the map.
	 * 
	 */
	private void initializeValues() {
		// TODO Auto-generated method stub
		for (int i = 0; i < this.mapX; i++) {
			for (int j = 0; j < this.mapY; j++) {
				if (validateBoundary(i, j - 1)
						&& this.elevationPoints[i][j - 1].elevation < this.elevationPoints[i][j].elevation)
					this.elevationPoints[i][j].AdjSteepNodes
							.add(this.elevationPoints[i][j - 1]);
				if (validateBoundary(i, j + 1)
						&& this.elevationPoints[i][j + 1].elevation < this.elevationPoints[i][j].elevation)
					this.elevationPoints[i][j].AdjSteepNodes
							.add(this.elevationPoints[i][j + 1]);
				if (validateBoundary(i - 1, j)
						&& this.elevationPoints[i - 1][j].elevation < this.elevationPoints[i][j].elevation)
					this.elevationPoints[i][j].AdjSteepNodes
							.add(this.elevationPoints[i - 1][j]);
				if (validateBoundary(i + 1, j)
						&& this.elevationPoints[i + 1][j].elevation < this.elevationPoints[i][j].elevation)
					this.elevationPoints[i][j].AdjSteepNodes
							.add(this.elevationPoints[i + 1][j]);
			}
		}
	}

	public static void main(String[] args){
		
		int mapSizeX = 1000;
		int mapSizeY = 1000;
		
		// Test by inserting random data into the file with width and height
		
//		File randomSmallDataFile = new File("testSmallData.txt");
//		insertRandomData(randomSmallDataFile,10,10);
		
//		File randomMediumDataFile = new File("testMediumData.txt");
//		insertRandomData(randomMediumDataFile,100,100);

		  try{
			  
//		File randomLargeDataFile = new File("randomLargeDataFile.txt");
//		insertRandomData(randomLargeDataFile,mapSizeX,mapSizeY);
		File redMartMapFile = new File("map.txt");

		final long startTime = System.currentTimeMillis();

		//Loading elevation point heights from the test data file.
		
		Elevation[][] elevationPoints= readElevationValues(redMartMapFile);
		
		SkingSingapore skiing = new SkingSingapore(elevationPoints);		

		final long endTime = System.currentTimeMillis();

//		printElevationMap(skiing);

		// Testing purpose for printing the depth Map & adjacent minimum map
		
//		printDepthMap(skiing);

//		printAdjacentMinimumMap(skiing);

		skiing.printHighestFall();

		System.out.println("Total execution time: " + (endTime - startTime));
    }catch(Exception e){
    	System.out.println("Error occurrend in main:"+e.getMessage());
    }
	}

	/**
	 * 
	 * Just a tester,helper method which prints all the Elevations.
	 * 
	 * @param skiing
	 * @throws FileNotFoundException 
	 */
	private static void printElevationMap(SkingSingapore skiing){
		// TODO Auto-generated method stub
		System.out
		.println("\n\n ################### Elevation Map #################### \n\n");

		for (int i = 0; i < skiing.mapX; i++) {
			for (int j = 0; j < skiing.mapY; j++)
				System.out.printf("%6s",skiing.elevationPoints[i][j].elevation);
			System.out.println();
		}
		
	}

	/**
	 * 
	 * Just a tester,helper method which prints the Maximum or highest possible fall from each Elevation.
	 * 
	 * @param skiing
	 * @throws FileNotFoundException 
	 */
	private static void printDepthMap(SkingSingapore skiing) throws FileNotFoundException {

		System.out
		.println("\n\n ################### Maximum Depth from Each Elevation #################### \n\n");
		for (int i = 0; i < skiing.mapX; i++) {
			for (int j = 0; j < skiing.mapY; j++)
				System.out.printf("%3s",skiing.elevationPoints[i][j].maxDepth);
			System.out.println();
		}

		System.out.println();
		System.out.println();

	}

	/**
	 * 
	 * Just a tester,helper method which prints the Elevation and its adjacent
	 * Elevations which are less than the current elevation.
	 * 
	 * @param skiing
	 */
	private static void printAdjacentMinimumMap(SkingSingapore skiing) {
		// TODO Auto-generated method stub

		System.out.println();
		System.out
				.println(" ################### Adjacent Minimum Elevations for Each Elevation ####################"+"\n");
		for (int i = 0; i < skiing.mapX; i++) {
			for (int j = 0; j < skiing.mapY; j++) {
				System.out.print(skiing.elevationPoints[i][j].elevation + "{");
				for (int z = 0; z < skiing.elevationPoints[i][j].AdjSteepNodes
						.size(); z++)
					System.out
							.print(""
									+ skiing.elevationPoints[i][j].AdjSteepNodes
											.get(z).elevation + ",");
				System.out.print("} ");
			}
			System.out.println();
		}
	}



	private static void insertRandomData(File file,int x,int y) throws FileNotFoundException {
		// TODO Auto-generated method stub
		//
		PrintWriter writer = new PrintWriter(file);
		Random r = new Random();
		int Low = 0;
		int High = 1501;
		int R = 0;
		writer.println(x+" "+y);
		for (int j = 0; j < x; j++) {
			String row = "";
			for (int i = 0; i < y; i++) {
				R = r.nextInt(High - Low) + Low;
				row = row + R + " ";
			}
			writer.println(row.trim());
		}
		writer.close();
	}

	/**
	 * 
	 * Validates while checking the adjacent steep elevation points. Avoids array index out of bounds.
	 * @param i
	 * @param j
	 * @return
	 */
	private boolean validateBoundary(int i, int j) {
		// TODO Auto-generated method stub
		boolean valid = false;
		if (i >= 0 && j >= 0 && i < mapX && j < mapY)
			valid = true;
		else
			valid = false;
		return valid;
	}

	/**
	 * 
	 * Reads values from a data file into a 2d elevation array.
	 * 
	 * Format of file:
	 * 3 3
	 * 6 7 8
	 * 4 5 6
	 * 9 4 6
	 * 
	 * @param dataFile
	 * @return
	 * @throws FileNotFoundException
	 * @throws UnsupportedEncodingException
	 */
	private static Elevation[][] readElevationValues(File dataFile) {
		// TODO Auto-generated method stub		
		try
		{
		Elevation[][] elevationPoints= null;
		Scanner sc = new Scanner(dataFile);

		String[] fields = null;
		int[] size = new int[2];
		if (sc.hasNextLine()) {
			fields = sc.nextLine().split(" ");

			if (fields.length != 2) {
				throw new IllegalArgumentException("Invalid Size");
			} else {
				for (int i = 0; i < fields.length; i++)
					size[i] = Integer.parseInt(fields[i]);
				elevationPoints = new Elevation[size[0]][size[1]];
			}

		}

		int rowIndex = 0;
		while (sc.hasNextLine()) {
			String line = sc.nextLine();
			if (line.isEmpty())
				break;
			fields = line.split(" ");
			if (fields.length != size[1])
				throw new IllegalArgumentException("No of columns doesn't match the given size");

			for (int colIndex = 0; colIndex < fields.length; colIndex++)
				elevationPoints[rowIndex][colIndex] = new Elevation(
						Integer.parseInt(fields[colIndex]));
			rowIndex++;
		}
		
		if (rowIndex != size[0])
			throw new IllegalArgumentException("No of rows doesn't match the given size");
		
		return elevationPoints;
		}
		catch(Exception e){
	    	System.out.println("Error occurred in while reading values from file:"+e.getMessage());
	    	return null;
	    }
	}
}

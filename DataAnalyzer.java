import java.io.*;
import java.util.Scanner;
import java.lang.*;

public class DataAnalyzer {
	private static BufferedWriter out;
	//private String[] rawFilesDelaunay;
	//private String[] rawFilesPotential;
	
	public static void main (String args[]) throws IOException{
		//Delaunay Analysis
		String[] rawFilesDelaunay = {"delaunayStats10Zombies.txt",
												"delaunayStats15Zombies.txt",
												"delaunayStats20Zombies.txt",
												"delaunayStats25Zombies.txt"};
		
		String[] delaunayZCount = {"10", "15", "20", "25"};
												
		out = new BufferedWriter(new FileWriter("DelaunaySummaryStats.txt"));
		out.write("Delaunay Triangulation Algorithm Data:\n");
		out.write("Number Of Trials: 50\n");
		out.write("World Height: 1171\n");
		out.write("World Width: 1399\n");
		out.write("Distance From Resource To Base: 500-510\n");
		out.write("=======================================\n");
		for (int i=0; i<4; i++){
			out.write("\nWith "+delaunayZCount[i]+" zombies:\n");
			analyze(rawFilesDelaunay[i]);
		}
		out.close();
		
		//Potential Analysis
		String[] rawFilesPotential = {"potentialStats10Zombies.txt",
												 "potentialStats15Zombies.txt",
												 "potentialStats20Zombies.txt",
												 "potentialStats25Zombies.txt",
												 "potentialStats50Zombies.txt",
												 "potentialStats100Zombies.txt",
												 "potentialStats150Zombies.txt"};
												 
		String[] potentialZCount = {"10", "15", "20", "25", "50", "100", "150"};
		
		out = new BufferedWriter(new FileWriter("PotentialSummaryStats.txt"));
		out.write("Potential Fields Algorithm Data:\n");
		out.write("Number Of Trials: 50\n");
		out.write("World Height: 1171\n");
		out.write("World Width: 1399\n");
		out.write("Distance From Resource To Base: 500-510\n");
		out.write("=======================================\n");
		for (int i=0; i<7; i++){
			out.write("\nWith "+potentialZCount[i]+" zombies:\n");
			analyze(rawFilesPotential[i]);
		}
		out.close();
		System.exit(0);
	}
	
	public static void analyze(String fileName) throws IOException{
		Scanner scanner = new Scanner(new BufferedReader(new FileReader(fileName)));
		int sum = 0;
		int successes = 0;
		scanner.nextLine();
		while (scanner.hasNext()){
			String line = scanner.nextLine();
			try {
				sum += Integer.parseInt(line);
				successes++;
			} catch (NumberFormatException e) {}
		}
		out.write("Success Ratio: "+successes+"/50\n");
		out.write("Average Number Of Timesteps In Successful Trips: "+sum/successes+"\n");
	}
}

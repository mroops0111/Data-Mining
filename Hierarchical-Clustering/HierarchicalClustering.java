/**
 * A hierarchical clustering algorithm
 * 
 * @author  yuntai
 * @version 1.0
 */

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class HierarchicalClustering{
	
	//number of cluster the algorithm will create
	private int clusterAmount = 1;
	
	//use to manage each cluster
	private ArrayList<Cluster> clusterList = new ArrayList<Cluster>();
	private ArrayList<float[]> distanceList = new ArrayList<float[]>();
	
	public HierarchicalClustering(int clusterAmount){
		this.clusterAmount = clusterAmount;
	}
	
	/**
	 * read the data from file, and each data with multi-dimension is a cluster itself
	 */
	public void constructClusterList(String filename){
		this.clusterList.clear();
		
		BufferedReader reader;
		try{
			reader = new BufferedReader(new FileReader(filename));
			
			String line = "";
			while((line=reader.readLine())!=null){
				
				String splitLine[] = line.split(" ");
				float value[] = new float[splitLine.length];
				for(int i=0; i<value.length; i++){
					value[i] = Float.parseFloat(splitLine[i]);
				}
				
				//store the value in each dimension in order and create cluster
				ArrayList<Item> arrList = new ArrayList<Item>();
				arrList.add(new Item(value));
				this.clusterList.add(new Cluster(arrList));
				
			}
			reader.close();
			//showList();
			
		}catch(IOException e){
			e.getStackTrace();
		}
	}
	
	/**
	 * compute the distance between each two cluster in clusterList
	 */
	public void construcrtDistanceList(){
		this.distanceList.clear();
		
		int size = this.clusterList.size();
		for(int i=0; i<size; i++){
			Cluster cluster1 = this.clusterList.get(i);
		
			float[] distance = new float[size-i-1];
			for(int j=i+1; j<size; j++){
				Cluster cluster2 = this.clusterList.get(j);
				distance[j-i-1] = Cluster.getDistance(cluster1, cluster2);
			}
			this.distanceList.add(distance);
		}
		//showDistanceList();
	}
	
	public void showList(){
		System.out.println("========================================");
		for(int i=0; i<this.clusterList.size(); i++){
			Cluster cluster = this.clusterList.get(i);
			System.out.println(cluster);
		}
	}
	
	public void showDistanceList(){
		System.out.println("========================================");
		for(int i=0; i<this.distanceList.size(); i++){
			float d[] = this.distanceList.get(i);
			for(int j=0; j<d.length; j++){
				System.out.print(d[j] + " ");
			}
			System.out.println();
		}
	}
	
	/**
	 * combine two clusters with minimum distance 
	 */
	public void combine(){
		
		//get the two clusters whose distance between them is minimum
		int combine1=0, combine2=0;
		float min = Float.POSITIVE_INFINITY;
		for(int i=0; i<this.clusterList.size(); i++){
			float[] arr = this.distanceList.get(i);
			for(int j=0; j<arr.length; j++){
				if(arr[j]<min){
					combine1 = i;
					combine2 = i+j+1;
					min = arr[j];
				}
			}
		}
		
		this.clusterList.add(Cluster.combine(this.clusterList.get(combine1), this.clusterList.get(combine2)));
		this.clusterList.remove(combine1>combine2?combine1:combine2);
		this.clusterList.remove(combine1<combine2?combine1:combine2);	
		//showList();
	}
	
	public void output(String outputFile){
		BufferedWriter writer;
		try{
			writer = new BufferedWriter(new FileWriter(outputFile));
			for(int i=0; i<this.clusterList.size(); i++){
				ArrayList<Item> itemList = this.clusterList.get(i).getItemList();
				for(int j=0; j<itemList.size(); j++){
					writer.write(itemList.get(j).toString() + "  -->" + (i+1));
					writer.newLine();
				}
			}
			writer.close();
		}catch(IOException e){
			e.getStackTrace();
		}
	}	
	
	/**
	 * steps of hierarchical clustering algorithm
	 * 
	 * @params inputFile  data file
	 * @params outputFile  result file 
	 */
	public void run(String inputFile, String outputFile){
		constructClusterList(inputFile);
		while(this.clusterList.size()>this.clusterAmount){
			construcrtDistanceList();
			combine();
		}
		output(outputFile);
	}
	
	public static void main(String[] args){
		HierarchicalClustering hc = new HierarchicalClustering(5);
		hc.run("0_random.txt", "0_output.txt");
		hc.run("1_random.txt", "1_output.txt");
		hc.run("2_random.txt", "2_output.txt");
	}
}

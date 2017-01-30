import java.io.*;
import java.util.Set;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;

public class Apriori{
	
	//customized setting
	double minSupport, minConf;
	String inputFile;
	String outputFile = "output.txt";
	
	//for data
	private ArrayList<Set<Itemset>> aprioriList = new ArrayList<Set<Itemset>>();
	private ArrayList<AssociationRule> associationRule = new ArrayList<AssociationRule>();
	
	//for record
	private int transaction_cnt = 0;
	private int scan_cnt = 0;
	
	public Apriori(double minSupport, double minConf, String dataFile){
		this.minSupport = minSupport;
		this.minConf = minConf;
		this.inputFile = dataFile;
		this.addSet();
	}
	
	public void addSet(){
		this.aprioriList.add(new HashSet<Itemset>());
	}
	
	/**
	 * scan the database 
	 */
	public void scan(){
		System.out.println("========================================");
		this.scan_cnt +=1;
		
		BufferedReader reader;
		try{
			String line;
			String[] item;
			reader = new BufferedReader(new FileReader(inputFile));
			
			//scanning the database
			int cnt = 0;
			System.out.print("scanning......");
			while((line = reader.readLine()) != null) {
				cnt += 1;
				
				item = line.split(" ");					
				if(this.scan_cnt == 1){
					//scan database first time to create initial itemsets
					for(int i=0; i<item.length; i++){
						String[] itemArr = {item[i]};
						checkExistAndAddItemset(itemArr);
					}
					this.transaction_cnt += 1;
				}else{
					checkContainAndAddSupport(item);
				}
			}
			
			System.out.println(cnt + " transactions");
			reader.close();
			
		}catch(IOException e){
			e.getStackTrace();
		}
	}
	
	/**
	 * check if there is an Apriori's Itemset object whose set is the same as the "item" array, if yes add support, else new an Itemset object
	 * @param item  Integer array
	 */
	public void checkExistAndAddItemset(String[] item){
		
		Set<String> set = new HashSet<String>(Arrays.asList(item));
		Itemset itemset = new Itemset(set);		
		 
		Set<Itemset> aprioriItemsets = this.aprioriList.get(this.scan_cnt-1);
		Iterator iter = aprioriItemsets.iterator();
		boolean exist = false;
		while (iter.hasNext()) {
			Itemset i = (Itemset) iter.next();
			if(i.equals(itemset)){
				//System.out.println("contains!");
				i.addSupport(1);
				exist = true;
				break;
			}
		}
		if(!exist){
			itemset.addSupport(1);
			aprioriItemsets.add(itemset);
		}
	}
	
	/**
	 * check if the "item" array contains the set of each existing Apriori's Itemset objects, if yes add support
	 * @param item
	 */
	public void checkContainAndAddSupport(String[] item){
		Set<String> set = new HashSet<String>(Arrays.asList(item));
		Itemset itemset = new Itemset(set);	

		Set<Itemset> aprioriItemsets = this.aprioriList.get(this.scan_cnt-1);
		Iterator iter = aprioriItemsets.iterator();
		while (iter.hasNext()) {
			Itemset i = (Itemset) iter.next();
			if(itemset.getSet().containsAll(i.getSet())){
				i.addSupport(1);
			}
		}
	}
	
	/**
	 * remove Apriori's Itemset whose support is less than minimum support
	 */
	public void removeLowerSupport(){
		Set<Itemset> aprioriItemsets = this.aprioriList.get(this.scan_cnt-1);
		Iterator iter = aprioriItemsets.iterator();
		while (iter.hasNext()) {
			Itemset i = (Itemset) iter.next();
			double support = (double)i.getSupport()/(double)this.transaction_cnt;
			if(support < this.minSupport){
				iter.remove();
			}
		}
	}
	
	/**
	 * generate all possible candidates whose size is "amount"
	 * @param amount  size of candidate
	 */
	public void generateCandidate(int amount){
		Set<Itemset> aprioriItemsets = this.aprioriList.get(this.scan_cnt-1);
		Set<Set<String>> itemsets = new HashSet<Set<String>>();
		Itemset[] itemsetArray = aprioriItemsets.toArray(new Itemset[0]);
		
		//possible candidate
		Set<Set<String>> candidateSets = new HashSet<Set<String>>();
		Set<String> set;
		for(int i=0; i<itemsetArray.length; i++){
			itemsets.add(itemsetArray[i].getSet());
			for(int y=i+1; y<itemsetArray.length; y++){
				set = new HashSet<String>();
				set.addAll(itemsetArray[i].getSet());
				set.addAll(itemsetArray[y].getSet());
				if(set.size() != amount){
					continue;
				}else{
					candidateSets.add(set);
				}
			}
		}
		
		//possible candidate set
		System.out.println("========================================");
		System.out.println("possible candidate set: ");
		Iterator iter = candidateSets.iterator();
		while (iter.hasNext()) {
			System.out.print(iter.next());
		}
		System.out.println();
		
		//remove candidate that is not complete
		//for example, abc generated from ab, ac and bc, otherwise it is not complete
		Set<String>[] candidateArray = candidateSets.toArray(new Set[0]);
		for(int i=0; i<candidateArray.length; i++){
			String[] integerCandidateArray = candidateArray[i].toArray(new String[0]);
			
			Set<Set<String>> combination = new HashSet<Set<String>>();
			for(int j=0; j<integerCandidateArray.length; j++){
				
				set = new HashSet<String>(candidateArray[i]);
				set.remove(integerCandidateArray[j]);
				//System.out.print(set);
				combination.add(set);
			}

			if(!itemsets.containsAll(combination)){
				candidateSets.remove(candidateArray[i]);
			}
			combination.clear();
		}
		
		//final candidate set
		System.out.println("========================================");
		System.out.println("final candidate set");
		iter = candidateSets.iterator();
		while (iter.hasNext()) {
			System.out.print(iter.next());
		}
		System.out.println();
		
		if(!candidateSets.isEmpty()){
			addAprioriSet(candidateSets, this.scan_cnt);
		}else{
			System.out.println("NO CANDIDATE SET!");
			System.out.println("FINISH!");
		}
	}
	
	/**
	 * convert each sub-"sets" to Itemset objects, and add into aprioriList 
	 * @param sets  a set of set of Integer
	 * @param level  the level of Apriori's itemset
	 */
	public void addAprioriSet(Set<Set<String>> sets, int level){
		if(this.aprioriList.size()>=level){
			addSet();
		}
		
		Iterator iter = sets.iterator();
		while(iter.hasNext()){
			Set<String> set = (Set<String>) iter.next();
			Itemset itemset = new Itemset(set);
			this.aprioriList.get(level).add(itemset);
		}
	}
	
	/**
	 * convert each frequent candidates in aprioriList to association rule 
	 */
	public void generateAssociationRule(){
		for(int i=1; i<this.aprioriList.size(); i++){
			Set<Itemset> itemsets = this.aprioriList.get(i);
			Iterator iter = itemsets.iterator();
			while(iter.hasNext()){
				Itemset itemset = (Itemset) iter.next();
				Set<String> set = itemset.getSet();
				ArrayList<Set<String>> result = new ArrayList<Set<String>>();
				
				//get all combination
				combination(result, new ArrayList<String>(), set.toArray(new String[0]), 0);
				
				//generate association rule
				for(int j=0; j<result.size(); j++){
					Set<String> set1 = new HashSet<String>(result.get(j));
					Set<String> set2 = new HashSet<String>(set);
					set2.removeAll(set1);
					System.out.println(set1 + " " + set2);
					
					//check confidence
					int totalSupport = findSupport(set, set.size()-1);
					int support1 = findSupport(set1, set1.size()-1);
					int support2 = findSupport(set2, set2.size()-1);
					
					double confidence = 0.0;
					if((double)totalSupport/(confidence=(double)support1) >= this.minConf){												
						AssociationRule rule = new AssociationRule(new Itemset(set1, support1), new Itemset(set2, support2));
						rule.setConfidence(support1, totalSupport);		
						associationRule.add(rule);
					}
				}						
			}
		}
	}
	
	/**
	 * a recursive to get all the subsets excluding itself from a set
	 * for example, subsets of "abcd" are a, b, c, d, ab, ac, ad, bc, bd, cd, abc, abd, acd, bcd 
	 * @param result  all the subsets
	 * @param temp  a temporarily space to store the set
	 * @param input  an Integer array of set
	 * @param index  start from the index of the array
	 */
	public static void combination(ArrayList<Set<String>> result, ArrayList<String> temp, String[] input, int index){
		for(int i=index; i<input.length; ++i ){
            temp.add(input[i]);
            result.add(new HashSet<String>(temp));
            if(temp.size()<input.length-1 && i<input.length-1){
            	combination(result, temp, input, i+1);
            }
            temp.remove(temp.size()-1);
        }
	}
	
	/**
	 * search the existing Itemset objects to find the match one and get its support
	 * @param set  check if "set" matches one of the set of  existing Itemset objects
	 * @param index  the level of Apriori's
	 * @return  support itemset
	 */
	public int findSupport(Set<String> set, int index){
		Set<Itemset> itemsets = this.aprioriList.get(index);
		Iterator iter = itemsets.iterator();
		while(iter.hasNext()){
			Itemset itemset = (Itemset)iter.next();
			if(set.equals(itemset.getSet())){
				return itemset.getSupport();
			}
		}
		return 0;
	}
	
	public void showItemsets(int level){
		System.out.println("========================================");
		System.out.println("Transaction count: " + this.transaction_cnt);
		Set<Itemset> aprioriItemsets = this.aprioriList.get(level);
		Iterator iter = aprioriItemsets.iterator();
		while (iter.hasNext()) {
			Itemset i = (Itemset) iter.next();
			System.out.print(i.getSet());
			System.out.println("     \tsupport: " + i.getSupport());
		}
		System.out.println("========================================");
	}
	
	public void showRules(){
		System.out.println("========================================");
		System.out.println("Rules:");
		for(int i=0; i<this.associationRule.size(); i++){
			AssociationRule rule = this.associationRule.get(i);
			System.out.print(rule.getPreItenset().getSet() + " --> " + rule.getImplyItemset().getSet());
			System.out.println(" (" + rule.getTotalSupport() + "/" + rule.getPreItemsetSupport() + ")(" + rule.getConfidence() + ")");
		}
		System.out.println("========================================");
	}
	
	/**
	 * write the result to the file
	 */
	public void output(){
		BufferedWriter writer;
		try {
			writer = new BufferedWriter(new FileWriter(outputFile));
			for(int i=0; i<this.associationRule.size(); i++){
				AssociationRule rule = this.associationRule.get(i);
				writer.write(rule.getPreItenset().getSet() + " --> " + rule.getImplyItemset().getSet());
				writer.write(" (" + rule.getTotalSupport() + "/" + rule.getPreItemsetSupport() + ")");
				writer.newLine();
			}
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void run(){
		while(this.aprioriList.size() > this.scan_cnt){
			//apriori steps
			scan();
			removeLowerSupport();
			showItemsets(this.scan_cnt-1); //show current result after removing lower support
			generateCandidate(this.scan_cnt+1);
		}
		this.generateAssociationRule();
		this.showRules();
		this.output();
	}
	
	public static void main(String[] args){
		Apriori apriori = new Apriori(0.05, 0.5, "input.txt");
		apriori.run();
	}
	
}

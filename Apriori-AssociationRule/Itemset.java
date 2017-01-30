import java.util.HashSet;
import java.util.Set;

public class Itemset<T>{
	
	private Set<T> set = new HashSet<T>();;
	private int support = 0;
	
	public Itemset(Set<T> set){
		this.set = set;
		this.support = 0;
	}
	
	public Itemset(Set<T> set, int support){
		this.set = set;
		this.support = support;
	}
	
	public void addSupport(int add){
		this.support += add;
	}
	
	public Set<T> getSet(){
		return this.set;
	}
	
	public int getSupport(){
		return this.support;
	}
	
	public boolean equals(Itemset itemset){
		return this.getSet().equals(itemset.getSet());
	}
}

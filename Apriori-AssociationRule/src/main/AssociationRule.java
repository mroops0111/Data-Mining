public class AssociationRule {
	private Itemset preItemset, implyItemset;
	private int preItemsetSupport, totalSupport;
	private double confidence;
	
	public AssociationRule(Itemset preItemset, Itemset implyItemset){
		this.preItemset = preItemset;
		this.implyItemset = implyItemset;
	}
	
	public void setConfidence(int preItemsetSupport, int totalSupport){
		this.preItemsetSupport = preItemsetSupport;
		this.totalSupport = totalSupport;
		this.confidence = (double)this.totalSupport/(double)this.preItemsetSupport;
	}
	
	public double getConfidence(){
		return this.confidence;
	}
	
	public Itemset getPreItenset(){
		return this.preItemset;
	}
	
	public Itemset getImplyItemset(){
		return this.implyItemset;
	}
	
	public int getPreItemsetSupport(){
		return this.preItemsetSupport;
	}
	
	public int getTotalSupport(){
		return this.totalSupport;
	}
}

import java.util.ArrayList;

public class Cluster {
	
	private int dimension;
	private int amount;
	private ArrayList<Item> itemList = new ArrayList<Item>();
	private float[] centerValue;
	
	public Cluster(ArrayList<Item> itemList){
		this.itemList = itemList;
		this.dimension = this.itemList.get(0).getSize();
		this.amount = this.itemList.size();
		setCenterValue();
	}
	
	public int getDimension(){
		return this.dimension;
	}
	
	public float[] getCenterValue(){
		return this.centerValue;
	}
	
	public ArrayList<Item> getItemList(){
		return this.itemList;
	}
	
	public void addItem(Item item){
		this.itemList.add(item);
		this.amount++;
		setCenterValue();
	}
	
	public void setCenterValue(){
		float[] centerValue = new float[this.dimension];
		for(int i=0; i<this.itemList.size(); i++){
			Item item = this.itemList.get(i);
			for(int j=0; j<item.getSize(); j++){
				centerValue[j] += item.getValue()[j];
			}
		}
		
		for(int i=0; i<centerValue.length; i++){
			centerValue[i] = centerValue[i]/this.amount;
		}
		this.centerValue = centerValue;
	}
	
	public static float getDistance(Cluster cluster1, Cluster cluster2){
		float distance = 0;
		for(int i=0; i<cluster1.getDimension(); i++){
			distance += Math.pow((float)cluster1.getCenterValue()[i] - (float)cluster2.getCenterValue()[i], 2);
		}
		distance = (float) Math.sqrt(distance);
		
		return distance;
	}
	
	public static Cluster combine(Cluster cluster1, Cluster cluster2){
		ArrayList<Item> itemList = new ArrayList<Item>();
		itemList.addAll(cluster1.getItemList());
		itemList.addAll(cluster2.getItemList());
		
		return new Cluster(itemList);
	}
	
	public String toString(){
		String str = "";
		for(int i=0; i<this.amount; i++){
			str += "(" + this.itemList.get(i).toString() + ")";
			if(i != this.amount-1){
				 str += ", ";
			}
		}
		
		String centerStr = "";
		for(int i=0; i<this.dimension; i++){
			centerStr += this.centerValue[i];
			if(i != this.dimension-1){
				centerStr += " ";
			}
		}
		str += " cneter: (" + centerStr + ")";
		return str;
	}
}

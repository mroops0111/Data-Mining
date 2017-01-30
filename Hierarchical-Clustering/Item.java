/**
 * An item is a multi-dimension node in space.
 * Values of node in each dimension is a float number
 *
 */

public class Item {
	
	private int size;
	private float[] value;
	
	public Item(float[] value){
		this.size = value.length;
		this.value = value;
	}
	
	public int getSize(){
		return this.size;
	}
	
	public float[] getValue(){
		return this.value;
	}
	
	public String toString(){
		String str = "";
		for(int i=0; i<this.size; i++){
			str += value[i];
			if(i != this.size-1){
				 str += " ";
			}
		}
		return str;
	}
}

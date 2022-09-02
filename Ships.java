/* Team D: Amrin, Grace, Nivaethan, Jishnu
 * ICS4U0
 * Wednesday, April 21, 2021
 * Group Project: Battleship
 * Team D has programmed a virtual game of Battleship where the user will be able to vs the AI
 * ships object
 */


public class Ships {
	
	//declaring the variables 
	int size, hit, position;
	boolean sunk;

	
	//constructor
	public Ships(int size, int hit, int position, boolean sunk) {
		this.size = size;
		this.hit = hit;
		this.position = position;
		this.sunk = sunk;
	
	}
	
	public int getSize () { // gets and returns name
		return size;
	}

	public void setSize(int size) { // Updates variable from object 
		this.size = size;
	}
	
	public int getHit () { // gets and returns name
		return hit;
	}

	public void setHit(int hit) { // Updates variable from object 
		this.hit = hit;
	}
	
	public int getPosition () { // gets and returns name
		return position;
	}

	public void setPosition(int position) { // Updates variable from object 
		this.position = position;
	}
	
	public boolean getSunk () { // gets and returns name
		return sunk;
	}

	public void setSunk(boolean sunk) { // Updates variable from object 
		this.sunk = sunk;
	}
	

}

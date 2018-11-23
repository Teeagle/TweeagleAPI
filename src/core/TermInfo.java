package core;

import java.io.Serializable;
import java.util.ArrayList;

public class TermInfo implements Serializable{
	private int tf;
	private ArrayList<Integer> positions;

	public TermInfo() {
		this.positions = new ArrayList<>();
		this.tf = 0;
	}

	public int getTf() {
		return tf;
	}

	public void setTf(int tf) {
		this.tf = tf;
	}

	public ArrayList<Integer> getPositions() {
		return positions;
	}

	public void setPositions(ArrayList<Integer> positions) {
		this.positions = positions;
	}
	
	public void increaseTF() {
		this.tf++;
	}
	
	public void addPosition(int pos) {
		this.positions.add(pos);
	}

	@Override
	public String toString() {
		return "TermInfo [tf=" + tf + ", positions=" + positions + "]";
	}

}
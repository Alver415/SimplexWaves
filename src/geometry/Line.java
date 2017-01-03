package geometry;

public class Line {

	private Vector start;
	private Vector end;
	
	public Line(Vector start, Vector end){
		this.start = start;
		this.end = end;
	}
	
	public Vector getStart(){
		return start;
	}
	public Vector getEnd(){
		return end;
	}
}

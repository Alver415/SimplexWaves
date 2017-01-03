package geometry;

public class Vector extends Matrix{
	
	public static Vector ZERO(){ 		return new Vector(0,0,0);	}
	public static Vector LEFT(){ 		return new Vector(-1,0,0);	}
	public static Vector RIGHT(){ 		return new Vector(1,0,0);	}
	public static Vector UP(){	  		return new Vector(0,1,0);	}
	public static Vector DOWN(){		return new Vector(0,-1,0);	}
	public static Vector FORWARD(){		return new Vector(0,0,1);	}
	public static Vector BACKWARD(){	return new Vector(0,0,-1);	}
	
	public Vector(){
		super(4,1);
		set(0,0,0);
		setElement(3, 1);
	}
	public Vector(double x, double y, double z){
		super(4,1);
		set(x,y,z);
		setElement(3, 1);
	}
	public Vector(double x, double y, double z, double w){
		super(4,1);
		set(x,y,z,w);
	}
	public void set(Vector  v){
		set(v.getX(), v.getY(), v.getZ(), v.getZ());
	}
	
	public void set(double x, double y, double z){
		setX(x);
		setY(y);
		setZ(z);
	}
	public void set(double x, double y, double z, double w){
		setX(x);
		setY(y);
		setZ(z);
		setW(w);
	}
	public double getX(){
		return getElement(0);
	}
	public double getY(){
		return getElement(1);
	}
	public double getZ(){
		return getElement(2);
	}
	public double getW(){
		return getElement(3);
	}
	public void setX(double x){
		setElement(0, x);
	}
	public void setY(double y){
		setElement(1, y);
	}
	public void setZ(double z){
		setElement(2, z);
	}
	public void setW(double w){
		setElement(3, w);
	}
	
	public Vector add(Vector vector){
		Vector result = new Vector();
		for (int i = 0; i < 3; i++){
			result.setElement(i, getElement(i) + vector.getElement(i));
		}
		return result;
	}
	public Vector subtract(Vector vector){
		Vector result = new Vector();
		for (int i = 0; i < 3; i++){
			result.setElement(i, getElement(i) - vector.getElement(i));
		}
		return result;
	}
	public Vector scalar(double scale){
		Vector result = new Vector();
		for (int i = 0; i < 3; i++){
			result.setElement(i, getElement(i) * scale);
		}
		return result;
	}
	public Vector cross(Vector p){
		return cross(this, p);
	}
	public static Vector cross(Vector a, Vector b){
		return new Vector (	(a.getY() * b.getZ()) - (a.getZ() * b.getY()),
							(a.getZ() * b.getX()) - (a.getX() * b.getZ()),
							(a.getX() * b.getY()) - (a.getY() * b.getX()));
	}
	public double dot(Vector p){
		return getX() * p.getX() + getY() * p.getY() + getZ() * p.getZ();
	}
	public static double dot(Vector a, Vector b){
		return a.getX() * b.getX() + a.getY() * b.getY() + a.getZ() * b.getZ();
	}
	public Vector normal(){
		double mag = magnitude();
		return new Vector(getX() / mag, getY() / mag, getZ() / mag);
	}
	public double magnitude(){
		return (double) Math.sqrt(getX() * getX() + getY() * getY() + getZ() * getZ());
	}
	public double magnitudeSquare(){
		return getX() * getX() + getY() * getY() + getZ() * getZ();
	}
	public double distance(Vector p){
		return subtract(p).magnitude();
	}
	public double distanceSquare(Vector p){
		return subtract(p).magnitudeSquare();
	}
	public Vector midpoint(Vector v){
		return new Vector((getX() + v.getX()) / 2, (getY() + v.getY()) / 2, (getZ() + v.getZ()) / 2);
	}
	public static Vector midpoint(Vector v1, Vector v2){
		return new Vector((v1.getX() + v2.getX()) / 2, (v1.getY() + v2.getY()) / 2, (v1.getZ() + v2.getZ()) / 2);
	}
	
	public Vector clone(){
		return new Vector(getX(), getY(), getZ(), getW());
	}
	@Override
	public String toString(){
		return "[" + getX() + ", " + getY() + ", " + getZ() + "]";
	}
	
}

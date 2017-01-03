package geometry;

import java.util.ArrayList;

public class Transform extends Matrix{
	
	private Transform parent;
	private ArrayList<Transform> children = new ArrayList<Transform>();
	
	public static Transform getPerspective(double fov, double aspect, double near, double far){
		double focal_length = 1.0f / (double) Math.tan(fov * Math.PI / 360);
		Transform matrix = new Transform();
		matrix.setElement(0, focal_length / aspect);	matrix.setElement(1, 0);				matrix.setElement(2, 0);							matrix.setElement(3, 0);
		matrix.setElement(4, 0);						matrix.setElement(5, focal_length);		matrix.setElement(6, 0);							matrix.setElement(7, 0);
		matrix.setElement(8, 0);						matrix.setElement(9, 0);				matrix.setElement(10, -(far + near)/(far - near));	matrix.setElement(11, 2.0f*(near*far)/(far - near));
		matrix.setElement(12, 0);						matrix.setElement(13, 0);				matrix.setElement(14, 1);							matrix.setElement(15, 0);
		return matrix;
	}
	
	public Transform(){
		super(4);
		setElement(0,  1);
		setElement(5,  1);
		setElement(10, 1);
		setElement(15, 1);
	}
	public Transform(Matrix m){
		super(4);
		set(m);
	}

	public void set(Matrix m){
		if (m.getHeight() != getHeight() || m.getWidth() != getWidth())
			System.out.println("ERROR in Transform.set(Matrix m): Different sizes.");
		for (int i = 0; i < 16; i++){
			setElement(i, m.getElement(i));
		}
	}
	
	public static Transform getTranslationMatrix(double x, double y, double z){
		Transform t = new Transform();
		t.setElement(3,  x);
		t.setElement(7,  y);
		t.setElement(11, z);
		return t;
	}
	public static Transform getRotationMatrix(double x, double y, double z){
		Transform r = new Transform();
		r.set(getXRotationMatrix(x).multiply(getYRotationMatrix(y).multiply(getZRotationMatrix(z))));
		return r;
	}
	public static Transform getXRotationMatrix(double t){
		Transform r = new Transform();
		double cos = cos(t);
		double sin = sin(t);
		r.setElement(5, cos);
		r.setElement(6, - sin);
		r.setElement(9, sin);
		r.setElement(10, cos);
		return r;
	}
	public static Transform getYRotationMatrix(double t){
		Transform r = new Transform();
		double cos = cos(t);
		double sin = sin(t);
		r.setElement(0, cos);
		r.setElement(2, sin);
		r.setElement(8, -sin);
		r.setElement(10, cos);
		return r;
	}
	public static Transform getZRotationMatrix(double t){
		Transform r = new Transform();
		double cos = cos(t);
		double sin = sin(t);
		r.setElement(0, cos);
		r.setElement(1, -sin);
		r.setElement(4, sin);
		r.setElement(5, cos);
		return r;
	}
	public static Transform getScaleMatrix(double x, double y, double z){
		Transform s = new Transform();
		s.setElement(0,  x);
		s.setElement(5,  y);
		s.setElement(10, z);
		return s;	
	}
	
	
	public void translate(Transform translation){
		set(this.multiply(translation));
	}
	public void translate(Vector v){
		translate(getTranslationMatrix(v.getX(), v.getY(), v.getZ()));
	}
	public void translate(double x, double y, double z){
		translate(getTranslationMatrix(x, y, z));
	}
	public void rotate(Transform rotation){
		set(this.multiply(rotation));
	}
	public void rotate(double x, double y, double z){
		translate(getRotationMatrix(x, y, z));
	}
	public void scale(Transform scale){
		set(this.multiply(scale));
	}
	public void scale(double x, double y, double z){
		translate(getScaleMatrix(x, y, z));
	}
	
	public Vector getTranslationVector(){
		return new Vector(getElement(3),getElement(7),getElement(11));
	}
	public void setTranslation(Vector v){
		setElement(3, v.getX());
		setElement(7, v.getY());
		setElement(11, v.getZ());
	}
	public void setTranslation(double x, double y, double z){
		setElement(3, x);
		setElement(7, y);
		setElement(11, z);
	}
	
	public Transform invert() {
	    Transform m = new Transform();

	    double s0 = getElement(0,0) * getElement(1,1) - getElement(1,0) * getElement(0,1);
	    double s1 = getElement(0,0) * getElement(1,2) - getElement(1,0) * getElement(0,2);
	    double s2 = getElement(0,0) * getElement(1,3) - getElement(1,0) * getElement(0,3);
	    double s3 = getElement(0,1) * getElement(1,2) - getElement(1,1) * getElement(0,2);
	    double s4 = getElement(0,1) * getElement(1,3) - getElement(1,1) * getElement(0,3);
	    double s5 = getElement(0,2) * getElement(1,3) - getElement(1,2) * getElement(0,3);

	    double c5 = getElement(2,2) * getElement(3,3) - getElement(3,2) * getElement(2,3);
	    double c4 = getElement(2,1) * getElement(3,3) - getElement(3,1) * getElement(2,3);
	    double c3 = getElement(2,1) * getElement(3,2) - getElement(3,1) * getElement(2,2);
	    double c2 = getElement(2,0) * getElement(3,3) - getElement(3,0) * getElement(2,3);
	    double c1 = getElement(2,0) * getElement(3,2) - getElement(3,0) * getElement(2,2);
	    double c0 = getElement(2,0) * getElement(3,1) - getElement(3,0) * getElement(2,1);

	    // Should check for 0 determinant

	    double invdet = 1 / (s0 * c5 - s1 * c4 + s2 * c3 + s3 * c2 - s4 * c1 + s5 * c0);

	    m.setElement(0,0, (getElement(1,1) * c5 - getElement(1,2) * c4 + getElement(1,3) * c3) * invdet);
	    m.setElement(0,1, (-getElement(0,1) * c5 + getElement(0,2) * c4 - getElement(0,3) * c3) * invdet);
	    m.setElement(0,2, (getElement(3,1) * s5 - getElement(3,2) * s4 + getElement(3,3) * s3) * invdet);
	    m.setElement(0,3, (-getElement(2,1) * s5 + getElement(2,2) * s4 - getElement(2,3) * s3) * invdet);

	    m.setElement(1,0, (-getElement(1,0) * c5 + getElement(1,2) * c2 - getElement(1,3) * c1) * invdet);
	    m.setElement(1,1, (getElement(0,0) * c5 - getElement(0,2) * c2 + getElement(0,3) * c1) * invdet);
	    m.setElement(1,2, (-getElement(3,0) * s5 + getElement(3,2) * s2 - getElement(3,3) * s1) * invdet);
	    m.setElement(1,3, (getElement(2,0) * s5 - getElement(2,2) * s2 + getElement(2,3) * s1) * invdet);

	    m.setElement(2,0, (getElement(1,0) * c4 - getElement(1,1) * c2 + getElement(1,3) * c0) * invdet);
	    m.setElement(2,1, (-getElement(0,0) * c4 + getElement(0,1) * c2 - getElement(0,3) * c0) * invdet);
	    m.setElement(2,2, (getElement(3,0) * s4 - getElement(3,1) * s2 + getElement(3,3) * s0) * invdet);
	    m.setElement(2,3, (-getElement(2,0) * s4 + getElement(2,1) * s2 - getElement(2,3) * s0) * invdet);

	    m.setElement(3,0, (-getElement(1,0) * c3 + getElement(1,1) * c1 - getElement(1,2) * c0) * invdet);
	    m.setElement(3,1, (getElement(0,0) * c3 - getElement(0,1) * c1 + getElement(0,2) * c0) * invdet);
	    m.setElement(3,2, (-getElement(3,0) * s3 + getElement(3,1) * s1 - getElement(3,2) * s0) * invdet);
	    m.setElement(3,3, (getElement(2,0) * s3 - getElement(2,1) * s1 + getElement(2,2) * s0) * invdet);

	    return m;
	}
	
	//TREE FUNCTIONALITY

	public Transform getParent(){
		return parent;
	}
	public void setParent(Transform parent){
		//this.parent.children.remove(this);
		parent.children.add(this);
		this.parent = parent;
	}
	public ArrayList<Transform> getChildren(){
		return children;
	}
	public void addChild(Transform child){
		if (child.parent != null)
		child.parent.children.remove(child);
		children.add(child);
		child.parent = this;
	}
	public void removeChild(Transform child){
		children.remove(child);
		child.parent = null;
	}
	public Transform toGlobal(){
		Transform n = this;
		Transform t = new Transform(n);
		while (n.getParent() != null){
			n = n.getParent();
			t.set(t.multiply(n));
		}
		return t;
	}
	
	//MISCELLANIOUS
	public static double sin(double t){
		return (double) Math.sin(t);
	}
	public static double cos(double t){
		return (double) Math.cos(t);
	}
	public static String round(double f){
		return (Math.round(f * 10) / 10f) + "";
	}
}

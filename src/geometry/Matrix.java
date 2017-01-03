package geometry;

public class Matrix {
	
	public double[][] matrix;
	
	public static final Matrix getIdentity(int size){
		Matrix identity = new Matrix(size, size);
		for (int i = 0; i < size; i++){
			identity.setElement(i, i, 1);
		}
		return identity;
	}
	
	public Matrix(int height, int width){
		matrix = new double[height][width];
	}
	public Matrix(int size){
		matrix = new double[size][size];
	}
	
	public boolean isSquare(){
		return getWidth() == getHeight();
	}
	public int getSize(){
		return getWidth() * getHeight();
	}
	public int getWidth(){
		return matrix[0].length;
	}
	public int getHeight(){
		return matrix.length;
	}

	public int getRows(){
		return getHeight();
	}
	public int getColumns(){
		return getWidth();
	}

	public double getElement(int index){
		return matrix[index / getWidth()][index % getWidth()];
	}
	public double getElement(int column, int row){
		return matrix[column][row];
	}
	
	public void set(double[][] matrix){
		this.matrix = matrix;
	}
	public void setElement(int index, double value){
		this.matrix[index / getWidth()][index % getWidth()] = value;
	}
	public void setElement(int column, int row, double value){
		this.matrix[column][row] = value;
	}
	

	public Matrix add(Matrix matrix){
		return Matrix.add(this, matrix);
	}
	public static Matrix add(Matrix m1, Matrix m2) {
		if (m1.getHeight() != m2.getHeight() || m1.getWidth() != m2.getWidth()){
			System.out.println("Matrix Error: Cannot add Matrices with different dimensions");
		}
		Matrix result = new Matrix(m1.getHeight(), m1.getWidth());
		for (int i = 0; i < m1.getSize(); i++){
			result.setElement(i, m1.getElement(i) + m2.getElement(i));
		}
		return result;
	}
	
	public Matrix subtract(Matrix matrix){
		return Matrix.subtract(this, matrix);
	}
	public static Matrix subtract(Matrix m1, Matrix m2) {
		if (m1.getHeight() != m2.getHeight() || m1.getWidth() != m2.getWidth()){
			System.out.println("Matrix Error: Cannot subtract Matrices with different dimensions");
		}
		Matrix result = new Matrix(m1.getHeight(), m1.getWidth());
		for (int i = 0; i < m1.getSize(); i++){
			result.setElement(i, m1.getElement(i) - m2.getElement(i));
		}
		return result;
	}
	
	public Matrix scalar(double d){
		return Matrix.scalar(this, d);
	}
	public static Matrix scalar(Matrix matrix, double d) {
		Matrix result = new Matrix(matrix.getHeight(), matrix.getWidth());
		for (int i = 0; i < matrix.getSize(); i++){
			result.setElement(i, matrix.getElement(i) * d);
		}
		return result;
	}

	public Matrix multiply(Matrix matrix) {
		return Matrix.multiply(this, matrix);
	}
	public Vector multiply(Vector vector) {
		return (Vector) Matrix.multiply(this, vector);
	}
	public static Matrix multiply(Matrix m1, Matrix m2) {
		if (m1.getWidth() != m2.getHeight()){
			System.out.println("Matrix Error: Dimensions incompatible for multiplication");
		}
		Matrix result;
		if (m1.getHeight() == 4 && m2.getWidth() == 1){
			result = new Vector();
			result.setElement(3, 0);
		}
		else{
			result = new Matrix(m1.getHeight(), m2.getWidth());	
		}
		for(int i = 0; i < result.getHeight(); i++) {        
            for(int j = 0; j < result.getWidth(); j++) {     
                for(int k = 0; k < result.getHeight(); k++) { 
                    result.setElement(i, j, result.getElement(i, j) + m1.getElement(i,k) * m2.getElement(k,j));
                }
            }
        }
		return result;
	}
	
	public Matrix invert() {
	    Matrix m = new Matrix(this.getHeight(), this.getWidth());

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
	
	public Matrix transpose(){
		Matrix transpose = new Matrix(getWidth(), getHeight());
		for (int x = 0; x < getWidth(); x++){
		for (int y = 0; y < getHeight(); y++){
			transpose.setElement(x, y, getElement(y, x));
		}
		}
		return transpose;
	}
	public static Matrix transpose(Matrix matrix){
		Matrix transpose = new Matrix(matrix.getWidth(), matrix.getHeight());
		for (int x = 0; x < matrix.getWidth(); x++){
		for (int y = 0; y < matrix.getHeight(); y++){
			transpose.setElement(x, y, matrix.getElement(y, x));
		}
		}
		return transpose;
	}
	
	public String toString(){
		String s = "[";
		
		for (int x = 0; x < getRows(); x++){
		for (int y = 0; y < getColumns(); y++){
			s += matrix[x][y] + ", \t";
		}		
			s += "\n";
		}
		
		return s;
	}
}

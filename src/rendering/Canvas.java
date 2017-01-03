package rendering;

import geometry.*;

public class Canvas {

	private final int[] pixels;
	private final double[] z_buff;
	private final int width;
	private final int height;	

	private Color background = Color.BLACK;
	
	public Canvas(){
		this(100);
	}
	public Canvas(int size){
		this(size, size);
	}
	public Canvas(int width, int height){
		this.width = width;
		this.height = height;
		this.pixels = new int[width * height];
		this.z_buff = new double[width * height];
		clear();
	}

	public void setBackground(Color c){
		this.background = c;
	}
	
	public int getWidth(){
		return width;
	}
	public int getHeight(){
		return height;
	}
	public int getSize(){
		return pixels.length;
	}

	public int[] getIntArray(){
		return pixels;
	}
	public int getPixel(int x, int y){
		return pixels[getSize() - (width - x) + y * width];
	}
	public double getZBuffAt(int x, int y){
		return z_buff[getSize() - (width - x) - y * width];
	}
	public void setPixel(int x, int y, int color){
		pixels[getSize() - (width - x) - y * width] = color;
	}
	public void setPixelAt(int x, int y, double z, Color color){
		if (x >= width || x < 0 || y >= height || y < 0){	
			return;
		}
		if (z <= getZBuffAt(x, y)){
			setZBuff(x, y, z);
			setPixel(x, y, color.argb());
		}
	}
	public void setZBuff(int x, int y, double z){
		z_buff[getSize() - (width - x) - y * width] = z;
	}
	
	public void clear(){
		for (int i = 0; i < getSize(); i++){
			pixels[i] = background.argb();
			z_buff[i] = Double.MAX_VALUE;
		}
	}

	public void drawLine(Vector m, Vector n, Color c){
		Vector a = m.clone();
		Vector b = n.clone();
		
		int Ax = (int) a.getX();
		int Bx = (int) b.getX();
		int Ay = (int) a.getY();
		int By = (int) b.getY();
		double Az = a.getZ();
		double Bz = b.getZ();
		
		int dX = Math.abs(Bx - Ax);	// store the change in X and Y of the line endpoints
		int dY = Math.abs(By - Ay);
	    double dZ = Bz - Az;
	    
	    double curZ = Az;

		int Xincr, Yincr;
		if (Ax > Bx) { Xincr=-1; } else { Xincr=1; }	// which direction in X?
		if (Ay > By) { Yincr=-1; } else { Yincr=1; }	// which direction in Y?

		if (dX >= dY){							// if X is the independent variable
			int dPr 	= dY << 1;            	// amount to increment decision if right is chosen (alwAys)
			int dPru 	= dPr - (dX<<1);   	 	// amount to increment decision if up is chosen
			int P 		= dPr - dX;  			// decision variable start value

			for (int counter = 0; counter <= dX; counter++){
				setPixelAt(Ax, Ay, curZ, c );
				curZ += dZ / dX;
				if (P > 0){            // is the pixel going right AND up?
					Ax+=Xincr;	       // increment independent variable
					Ay+=Yincr;         // increment dependent variable
					P+=dPru;           // increment decision (for up)
				}
				else {                 // is the pixel just going right?
					Ax+=Xincr;         // increment independent variable
					P+=dPr;            // increment decision (for right)
				}
			}
		}
		else  {           				   // if Y is the independent variable
			int dPr 	= dX<<1;           // amount to increment decision if right is chosen (alwAys)
			int dPru 	= dPr - (dY<<1);   // amount to increment decision if up is chosen
			int P 		= dPr - dY;  	   // decision variable start value

			for (int counter = 0; counter <= dY; counter++) {      
				setPixelAt (Ax, Ay, curZ, c );
	            curZ += dZ / dY;
				if (P > 0){              // is the pixel going up AND right?
					Ax+=Xincr;           // increment dependent variable
					Ay+=Yincr;           // increment independent variable
					P+=dPru;             // increment decision (for up)
				}
				else {                    // is the pixel just going up?
					Ay+=Yincr;            // increment independent variable
					P+=dPr;            	  // increment decision (for right)
				}
			}
		}
	}
	public void fillTriangle(Vector a, Vector b, Vector c, Color color){	
		double Ax = a.getX();		double Ay = a.getY();		double Az = a.getZ();
		double Bx = b.getX();		double By = b.getY();		double Bz = b.getZ();
		double Cx = c.getX();		double Cy = c.getY();		double Cz = c.getZ();

		double t;
		if (Ay < By){											//Bubble sort vectors by Y
			t = Ax; 		Ax = Bx;		Bx = t;
			t = Ay; 		Ay = By;		By = t;
			t = Az; 		Az = Bz;		Bz = t;
		}
		if (By < Cy){
			t = Cx; 		Cx = Bx;		Bx = t;
			t = Cy; 		Cy = By;		By = t;
			t = Cz; 		Cz = Bz;		Bz = t;
		}
		if (Ay < By){	
			t = Ax; 		Ax = Bx;		Bx = t;
			t = Ay; 		Ay = By;		By = t;
			t = Az; 		Az = Bz;		Bz = t;
		}
		a = new Vector(Ax, Ay, Az);
		b = new Vector(Bx, By, Bz);
		c = new Vector(Cx, Cy, Cz);

		if (Ay == By){ 						//Flat Top
			fillTopFlatTriangle(a, b, c, color);
		}
		else if (By == Cy){ 				//Flat Bottom
			fillBotFlatTriangle(a, b, c, color);
		}
		else if (Ay == Cy){ 				//Line
			drawLine(a, b, color);
			drawLine(b, c, color);
		}
		else{								//Standard case
			Vector d = new Vector(	Ax + ((By - Ay) / (Cy - Ay)) * (Cx - Ax), 
									By, 
									Az + ((By - Ay) / (Cy - Ay)) * (Cz - Az)	);
			fillBotFlatTriangle(a, b, d, color);
			fillTopFlatTriangle(b, d, c, color);
		}
	}
	public void fillTopFlatTriangle(Vector a, Vector b, Vector c, Color color){
		double Ax = a.getX();		double Ay = a.getY();		double Az = a.getZ();
		double Bx = b.getX();		double By = b.getY();		double Bz = b.getZ();
		double Cx = c.getX();		double Cy = c.getY();		double Cz = c.getZ();
		
		double invslopex1 = (Cx - Ax) / (Cy - Ay);
		double invslopex2 = (Cx - Bx) / (Cy - By);
		double curx1 = Ax;
		double curx2 = Bx;

		double invslopez1 = (Cz - Az) / (Cy - Ay);
		double invslopez2 = (Cz - Bz) / (Cy - By);
		double curz1 = Az;
		double curz2 = Bz;
		
		for (int scanlineY = (int) Ay; scanlineY >= Cy; scanlineY--){	
			drawLine(new Vector(curx1, scanlineY, curz1), new Vector(curx2, scanlineY, curz2), color);
			curx1 -= invslopex1;
			curx2 -= invslopex2;
			curz1 -= invslopez1;
			curz2 -= invslopez2;
		}
	}
	public void fillBotFlatTriangle(Vector a, Vector b, Vector c, Color color){		
		double Ax = a.getX();		double Ay = a.getY();		double Az = a.getZ();
		double Bx = b.getX();		double By = b.getY();		double Bz = b.getZ();
		double Cx = c.getX();		double Cy = c.getY();		double Cz = c.getZ();

		double invslopex1 = (Bx - Ax) / (By - Ay);
		double invslopex2 = (Cx - Ax) / (Cy - Ay);
		double curx1 = Ax;
		double curx2 = Ax;

		double invslopez1 = (Bz - Az) / (By - Ay);
		double invslopez2 = (Cz - Az) / (Cy - Ay);
		double curz1 = Az;
		double curz2 = Az;

		for (int scanlineY = (int) Ay; scanlineY >= By; scanlineY--){	
			drawLine(new Vector(curx1, scanlineY, curz1), new Vector(curx2, scanlineY, curz2), color);
			curx1 -= invslopex1;
			curx2 -= invslopex2;
			curz1 -= invslopez1;
			curz2 -= invslopez2;
		}
	}	

	public void drawTriangle(Vector a, Vector b, Vector c, Color color){
		drawLine(a, b, color);
		drawLine(b, c, color);
		drawLine(c, a, color);
	}

    public boolean clipToEdges(Vector a, Vector b) {
        double u1 = 0;
        double u2 = 1;
        int x0 = (int) a.getX();
        int y0 = (int) a.getY();
        int x1 = (int) b.getX();
        int y1 = (int) b.getY();
        
        int xMax = width;
        int yMax = height;
        int xMin = 0;
        int yMin = 0	;
        
        int dx = x1 - x0;
        int dy = y1 - y0;
        int p[] = {-dx, dx, -dy, dy};
        int q[] = {x0 - xMin, xMax - x0, y0 - yMin, yMax - y0};
        for (int i = 0; i < 4; i++) {
            if (p[i] == 0) {
                if (q[i] < 0) {
                    return true;
                }
            } else {
                double u = (double) q[i] / p[i];
                if (p[i] < 0) {
                    u1 = Math.max(u, u1);
                } else {
                    u2 = Math.min(u, u2);
                }
            }
        }
        if (u1 > u2) {
            return true;
        }
        int nx0, ny0, nx1, ny1;
        nx0 = (int) (x0 + u1 * dx);
        ny0 = (int) (y0 + u1 * dy);
        nx1 = (int) (x0 + u2 * dx);
        ny1 = (int) (y0 + u2 * dy);
        a.set(nx0, ny0, a.getZ());
        b.set(nx1, ny1, b.getZ());
        return false;
    }
}
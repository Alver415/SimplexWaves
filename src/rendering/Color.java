package rendering;


public class Color {

	public static final Color WHITE  	= new Color(0xFFFFFFFF);
	public static final Color GRAY 		= new Color(0xFF7F7F7F);
	public static final Color BLACK 	= new Color(0xFF000000);
	
	public static final Color RED 		= new Color(0xFFFF0000);
	public static final Color GREEN		= new Color(0xFF00FF00);
	public static final Color BLUE 		= new Color(0xFF0000FF);
	
	public static final Color YELLOW  	= new Color(0xFFFFFF00);
	public static final Color ORANGE  	= new Color(0xFFFF7F00);
	public static final Color PURPLE  	= new Color(0xFFFF00FF);

	private final int argb_value;
	
	private Color(int argb_value){
		this.argb_value = argb_value;
	}

	private Color(double alpha, double red, double green, double blue){
		alpha 	= Math.max(Math.min(alpha 	* 255, 255), 0);
		red 	= Math.max(Math.min(red 	* 255, 255), 0);
		green 	= Math.max(Math.min(green 	* 255, 255), 0);
		blue 	= Math.max(Math.min(blue 	* 255, 255), 0);
		
		argb_value = (((byte) alpha 	& 0xFF) << 24) |
					 (((byte) red 		& 0xFF) << 16) |
					 (((byte) green 	& 0xFF) << 8)  |
					 (((byte) blue  	& 0xFF) << 0);
	}
	public static Color rgb(double red, double green, double blue){
		return new Color(255, red, green, blue);
	
	}
	public static Color argb(double alpha, double red, double green, double blue){
		return new Color(alpha, red, green, blue);
	}
	/**
	 * @param hue [0, 360]
	 * @param saturation [0,1]
	 * @param value [0,1]
	 */
	public static Color hsv(double hue, double saturation, double value){
		hue %= 360;
		if (hue < 0)
			hue = 360 + hue;
		saturation = Math.max(Math.min(saturation, 1), 0);
		value = Math.max(Math.min(value, 1), 0);
		
		
		double C = value * saturation;
		double X = C * (1 - Math.abs((hue / 60) % 2 - 1));
		double m = value - C;
		
		double r = 0;
		double g = 0;
		double b = 0;
		
		int region = (int) (hue / 60);
		switch(region){
		case (0):	r = C;	g = X;	b = 0;		break;
		case (1):	r = X;	g = C;	b = 0;		break;
		case (2):	r = 0;	g = C;	b = X;		break;
		case (3):	r = 0;	g = X;	b = C;		break;
		case (4):	r = X;	g = 0;	b = C;		break;
		case (5):	r = C;	g = 0;	b = X;		break;
		}
		
		return rgb(r + m, g + m, b + m);
	}
	
	public static Color random() {
		return rgb(Math.random(), Math.random(), Math.random());
	}
	public int argb(){
		return argb_value;
	}
	
	public double getAlpha(){
		return (argb_value >> 24 & 0xFF) / 255.0;
	}
	public double getRed(){
		return (argb_value >> 16 & 0xff) / 255.0;
	}
	public double getGreen(){
		return (argb_value >> 8  & 0xff) / 255.0;
	}
	public double getBlue(){
		return (argb_value >> 0  & 0xff) / 255.0;
	}
	
	public double getHue(){
		double r = getRed();
		double g = getGreen();
		double b = getBlue();

		double max = Math.max(Math.max(r, g), b);
		double min = Math.min(Math.min(r, g), b);
		
		double delta = max - min;
		
		if (delta < 0.0001)
			return 0;
		else if (r >= g && r >= b)
			return 60 * (((g - b) / delta) % 6);
		else if (g >= r && g >= b)
			return 60 * (2 + ((b - r) / delta));
		else if (b >= r && b >= g)
			return 60 * (4 + ((r - g) / delta));
		
		return 0;
	}
	public double getSaturation(){
		double r = getRed();
		double g = getGreen();
		double b = getBlue();

		double max = Math.max(Math.max(r, g), b);
		double min = Math.min(Math.min(r, g), b);

		double delta = max - min;
		
		if (max == 0)
			return 0;
		else
			return delta / max;
	}
	public double getValue(){
		return Math.max(Math.max(getRed(), getBlue()), getGreen());
	}

	
	@Override
	public String toString(){
		return Integer.toHexString(argb_value);
	}
	@Override
	public boolean equals(Object obj){
		if (this == obj)
			return true;
		if (obj instanceof Color){
			Color c = (Color) obj;
			return this.argb_value == c.argb_value;
		}
		return false;
	}
	 
	public Color clone(){
		return new Color(argb_value);
	}
}

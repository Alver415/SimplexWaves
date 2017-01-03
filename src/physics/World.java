package physics;

import geometry.Vector;


import rendering.Color;
import rendering.SimplexNoise;


public class World {

	public Camera camera;
	
	public int w = 100;
	public int l = 100;

	public Vector[][] grid = new Vector[l][w];
	public Color[][] cgrid = new Color[l][w];
	public double seed = Math.random() * 10000;
	public double delta = 0.3;
	public double amplitude = 0.5;
		
	public World(){
		camera = new Camera(1920,1080);	
		camera.getTransform().rotate(-20 * Math.PI / 180, 0, 0);
		grid = new Vector[l][w];

		update();
	}
	
	public void update(){ 
		seed += delta;
		for (int i = 0; i < l; i++){
		for (int j = 0; j < w; j++){
			double x = (j - w/2) / 2.0;
			double z = -(i - 30) / 2.0;
			double n = Math.sin(Math.sqrt(x * x + z * z) - seed) * SimplexNoise.noise(x / 10, z / 10, seed / 10);
			double y = amplitude * n;
			grid[i][j] = new Vector(x, y, z);
			
			cgrid[i][j] = Color.hsv(Math.sin(-y) * 90 - seed * 10, (Math.sin(y) + 0.5), (Math.sin(y) + 0.5));
			//cgrid[i][j] = Color.hsv(-Math.sqrt(x * x + z * z) / 2 * 60 - seed * 10, (Math.sin(y) + 1) / 2, (Math.sin(y) + 1) / 2);
		}	
		}
	}
	

}


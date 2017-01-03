package rendering;

import geometry.Line;
import geometry.Matrix;
import geometry.Transform;
import geometry.Vector;
import physics.World;

public class GraphicsPipeLine {

	public static Canvas canvas;
	public static World world;
	public static Transform camera_inv_pos;
		
	public static void renderWorld(World world){
		canvas.clear();
		GraphicsPipeLine.camera_inv_pos = world.camera.pos.invert();

		
		for (int i = 0; i < world.l-1; i++){
		for (int j = 0; j < world.w-1; j++){
			Vector v1 = world.grid[i][j];
			Vector v2 = world.grid[i + 1][j];
			Vector v3 = world.grid[i][j + 1];
			Vector v4 = world.grid[i + 1][j + 1];
			Color c = world.cgrid[i][j];
			//renderFace(v1, v2, v3, c);
			//renderFace(v2, v3, v4, c);
			renderLine(v1, v2, c);
			renderLine(v2, v3, c);
			renderLine(v3, v1, c);

		}	
		}
	}
	public static void renderFace(Vector a, Vector b, Vector c, Color color){
		a = worldToView(a);
		b = worldToView(b);
		c = worldToView(c);
		
		a = viewToClip(a);
		b = viewToClip(b);
		c = viewToClip(c);

		a = clipToNorm(a);
		b = clipToNorm(b);
		c = clipToNorm(c);
		
		a = normToScreen(a);
		b = normToScreen(b);
		c = normToScreen(c);

		canvas.fillTriangle(a, b, c, color);
		
	}
	public static void renderLine(Vector a, Vector b, Color color){
		a = worldToView(a);
		b = worldToView(b);
		
		a = viewToClip(a);
		b = viewToClip(b);
		
		a = clipToNorm(a);
		b = clipToNorm(b);

		a = normToScreen(a);
		b = normToScreen(b);
		
		canvas.drawLine(a, b, color);
		
	}
	
	public static Vector worldToView(Vector world){
		return camera_inv_pos.multiply(world);
	}
	public static Vector viewToClip(Vector view){
		return world.camera.perspective.multiply(view);
	}
	public static Vector clipToNorm(Vector clip){
		return new Vector(clip.getX() / clip.getW(), clip.getY() / clip.getW(), clip.getZ() / clip.getW());
	}
	public static Vector normToScreen(Vector normalized){
		return new Vector((1 - normalized.getX()) * canvas.getWidth() / 2, (1 - normalized.getY()) * canvas.getHeight() / 2, normalized.getZ());
	}
	
}
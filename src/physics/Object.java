package physics;
import rendering.Color;
import geometry.*;

public class Object {

	public Color color;
	
	public Transform pos;
	public Vector vel;
	public Vector acc;
	
	public Object(){
		this.color = Color.WHITE;
		pos = new Transform();
		vel = new Vector();
		acc = new Vector();
	}
	
	public void update(double dt){
		
		Vector dv = acc.scalar(dt);
		Vector dp = vel.scalar(dt);

		vel = vel.add(dv);
		pos.translate(dp);
		
	}
	public Transform getTransform(){
		return pos;
	}
	
	public void setPosition(double x, double y, double z){
		pos.setTranslation(x, y, z);
	}
	public void setPosition(Vector position){
		pos.setTranslation(position);
	}
	public Vector getPosition(){
		return pos.getTranslationVector();
	}
	public void setVelocity(double x, double y, double z){
		vel.set(x, y, z);
	}
	public void setVelocity(Vector velocity){
		vel.set(velocity);
	}
	public Vector getVelocity(){
		return vel;
	}

	public void setAcceleration(double x, double y, double z){
		acc.set(x, y, z);
	}
	public void setAcceleration(Vector acceleration){
		acc.set(acceleration);
	}
	public Vector getAcceleration(){
		return acc;
	}
	
}

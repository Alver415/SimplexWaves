package physics;
import geometry.*;

public class Camera extends Object{
	
	public Transform perspective;
	
	private double pitch = 0;
	private double yaw = 0;
	
	public Camera(int width, int height){
		super();
		this.pos = new Transform();
		this.vel = new Vector();
		this.acc = new Vector();
		this.pos.translate(0, 5, 20);
		this.perspective = Transform.getPerspective(60,(double) width / height, 1, 50);
	}
	
	public Camera(double fov, double aspect, double near, double far){
		super();
		this.pos.translate(0, 5, 20);
		perspective = Transform.getPerspective(fov, aspect, near, far);
	}
	
	public void pitch(double pitch){
		setPitch(this.pitch + pitch);
	}
	public void yaw(double yaw){
		setYaw(this.yaw + yaw);
	}
	public void setPitch(double pitch){
		this.pitch = pitch;
	}
	public void setYaw(double yaw){
		this.yaw = Math.min(Math.max(yaw, -Math.PI / 2), Math.PI / 2);
	}
	public double getPitch(){
		return pitch;
	}
	public double yaw(){
		return yaw;
	}
	
	public void update(double dt){
		super.update(dt);
		
		Vector pos = getPosition();
		getTransform().set(new Transform());
		setPosition(pos);
		getTransform().rotate(0, pitch, 0);
		getTransform().rotate(yaw, 0, 0);
	}
}

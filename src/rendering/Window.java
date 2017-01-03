package rendering;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

import physics.World;

@SuppressWarnings({"serial"})
public class Window extends JFrame implements KeyListener{

	BufferedImage image;
	Canvas canvas;
	World world;
	
	private int width;
	private int height;
	
	public static void main(String[] args){
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Window();
            }
        });
	}
	
	public Window(){

		
    	Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
    	width  = (int) (screen.getWidth()  / 1f);
    	height = (int) (screen.getHeight() / 1f);

        setUndecorated(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("PerlinTerrain");
        setPreferredSize(new Dimension(width,height));
        setLocation((int)((screen.getWidth() - width) / 2), (int)((screen.getHeight() - height) / 2));
        pack();
        setVisible(true);
        addKeyListener(this);
        
        canvas = new Canvas(width, height);
		image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        setContentPane(new JPanel(){
        	@Override 
        	public void paintComponent(Graphics g) {
        		g.drawImage(image, 0, 0, null);
        	}
        });

        world = new World();
        GraphicsPipeLine.world = world;
        GraphicsPipeLine.canvas = canvas;
        GraphicsPipeLine.renderWorld(world);
		image.setRGB(0, 0, width, height, canvas.getIntArray(), 0, width);
        repaint();
        
        new Timer(1, new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				world.update();
				GraphicsPipeLine.renderWorld(world);
				image.setRGB(0, 0, width, height, canvas.getIntArray(), 0, width);
		        repaint();
				
			}
		}).start();
 
	}

	@Override
	public void keyPressed(KeyEvent e) {
		switch(e.getKeyCode()){
		case (KeyEvent.VK_UP):
			world.delta += 0.1;
			break;
		
		case (KeyEvent.VK_DOWN):
			world.delta -= 0.1;
			break;
			
		case (KeyEvent.VK_LEFT):
			world.amplitude -= 0.1;
			break;
		
		case (KeyEvent.VK_RIGHT):
			world.amplitude += 0.1;
			break;
		
		case (KeyEvent.VK_ESCAPE):
			System.exit(0);
			break;
		}
	}
	@Override
	public void keyReleased(KeyEvent e) {	}
	@Override
	public void keyTyped(KeyEvent e) {	}


}

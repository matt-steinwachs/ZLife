import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.Iterator;
import java.util.Random;
import java.util.Vector;

import javax.swing.JPanel;


public class MyDisplayPanel extends JPanel{
	private World world;
	private int zX = 200, zY = 200;
	
	public MyDisplayPanel(){
		super();
		
		world = new World(this.getWidth(), this.getHeight());
	}
	
	
	/*
	 * @Override
	 */
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		g.clearRect(0, 0, this.getWidth(), this.getHeight());
		g.fillRect(0, 0, this.getWidth(), this.getHeight());
		world.draw(g);
	}

	public void update(int delay) {
		world.update(delay);
	}
}

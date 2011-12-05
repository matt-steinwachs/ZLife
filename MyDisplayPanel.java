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
	private MyFrame myFrame;
	
	public MyDisplayPanel(MyFrame mf){
		super();
		myFrame = mf;
	}
	
	public void initWorld(){
		world = new World(this.getHeight(),this.getWidth(), myFrame);
	}
	
	
	/*
	 * @Override
	 */
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		g.clearRect(0, 0, this.getWidth(), this.getHeight());
		g.fillRect(0, 0, this.getWidth(), this.getHeight());
		if(world != null)
			world.draw(g);
		
		//System.out.println(this.getWidth()+"  "+this.getHeight());
	}

	public void update(int delay) {
		world.update(delay);
	}
	
	public World getWorld(){
		return world;
	}
}

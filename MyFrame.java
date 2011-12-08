import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;
import java.util.Vector;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.text.JTextComponent;

import java.io.*;
import java.lang.*;



public class MyFrame extends JFrame implements KeyListener, ActionListener{
	private static final long serialVersionUID = -2122161377842820073L;

	private JPanel panel1, panel2, panel3, topPanel, panel4;
	private JRadioButton radioButton1, radioButton2, radioButton3, radioButton4;
	private JButton button1, button2, button3;
	private JLabel text1;
	private MyDisplayPanel displayPanel;
	private ButtonGroup group1, group2;
	private int resources = 0, time = 0;
	private Timer timer;
	private static final Color background = new Color(0,0,0);
	private GraphicsDevice myGraphicsDevice;
	private boolean fullscreen = true;
	private BufferedWriter out;
	private boolean loggedWorld = false;
	private int runsLogged = 0;
	
	public MyFrame () {
		timer = new Timer(1, this);
		group1 = new ButtonGroup();
		group2 = new ButtonGroup();
		panel1 = new JPanel();
		panel1.setLayout(new BoxLayout(panel1, BoxLayout.PAGE_AXIS));
		panel2 = new JPanel();
		panel2.setLayout(new BoxLayout(panel2, BoxLayout.PAGE_AXIS));
		button1 = new JButton("Start");
		button2 = new JButton("Stop");
		button2.setToolTipText("<html>Will stop the simulation.<br>Will not be able to resume <br>(I'm guessing)</html>");
		button2.setEnabled(false);
		topPanel = new JPanel();
		button3 = new JButton("Reset");
		button1.addActionListener(this);
		button2.addActionListener(this);
		button3.addActionListener(this);
		
		//Open file for logging
		try {
			out = new BufferedWriter(new FileWriter("potentialStats150Zombies.txt"));
		} catch (IOException e) {System.exit(2);}
		
		
		text1 = new JLabel("<html>&nbsp;&nbsp;&nbsp;Resources gathered: " + resources + " | Time elapsed: " + time + "</html>");
		topPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
		//topPanel.setLayout(new BorderLayout());
		topPanel.add(text1, BorderLayout.CENTER);
		//topPanel.add(button3, BorderLayout.EAST);
		
		this.add(topPanel, BorderLayout.NORTH);
		
		radioButton1 = new JRadioButton("Ant Colony Optimization");
		radioButton1.setToolTipText("<html><u>Ant Colony Optimization:</u> <br>&nbsp;Details</html>");
		radioButton1.setSelected(true);
		radioButton2 = new JRadioButton("Artificial Bee Colony");
		radioButton2.setToolTipText("<html><u>Artificial Bee Colony:</u> <br>&nbsp;Details</html>");
		
		radioButton3 = new JRadioButton("Delaunay Triangulation");
		radioButton3.setToolTipText("<html><u>Delaunay Triangulation:</u> <br>&nbsp;Details</html>");
		radioButton3.setSelected(true);
		radioButton4 = new JRadioButton("Potential Fields");
		radioButton4.setToolTipText("<html><u>Potential Fields:</u> <br>&nbsp;Details</html>");
		
		group1.add(radioButton1);
		group1.add(radioButton2);
		group2.add(radioButton3);
		group2.add(radioButton4);
		
		panel1.add(radioButton1);
		panel1.add(radioButton2);
		panel2.add(radioButton3);
		panel2.add(radioButton4);
		panel1.setBorder(BorderFactory.createTitledBorder("Global Search Alg."));
		panel2.setBorder(BorderFactory.createTitledBorder("Local Search Alg."));
		
		panel3 = new JPanel();
		panel3.setLayout(new GridLayout(6, 1));
		panel4 = new JPanel();
		panel4.setLayout(new GridLayout(3, 1));
		//panel3.add(panel1);
		panel3.add(panel2);
		panel4.add(button1);
		panel4.add(button2);
		panel4.add(button3);
		panel3.add(panel4);
		this.add(panel3, BorderLayout.WEST);
		
		displayPanel = new MyDisplayPanel(this);
		displayPanel.setBackground(background);
		displayPanel.setDoubleBuffered(true);
		this.add(displayPanel, BorderLayout.CENTER);
		
		setTitle ("ZLife - The Artificial Unlife Simulator");
		setSize(800,600);
		
		setLocation(0,0);
		
		//For Fullscreen
		myGraphicsDevice = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
		if(myGraphicsDevice.isFullScreenSupported() && fullscreen){
			this.setUndecorated(true);
			myGraphicsDevice.setFullScreenWindow(this);
		}
		
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setVisible(true);
		
		displayPanel.initWorld();
		
		button1.addKeyListener(this);
		button2.addKeyListener(this);
		button3.addKeyListener(this);
		radioButton1.addKeyListener(this);
		radioButton2.addKeyListener(this);
		radioButton3.addKeyListener(this);
		radioButton4.addKeyListener(this);
		this.addKeyListener(this);
		this.requestFocus();
	}

	public void resourceGathered(){
		resources++;
	}
	
	@Override
	public void keyPressed(KeyEvent arg0) {
		// TODO Auto-generated method stub
		if(arg0.getKeyCode() == KeyEvent.VK_ESCAPE){
			try {
				out.close();
			} catch (IOException e) {System.exit(2);}
			System.exit(0);
		}
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		if(arg0.getSource().equals(timer)){
			time += 1;
			text1.setText("Resources gathered: " + resources + " | Time elapsed: " + time);
			displayPanel.setBackground(new Color(255,255,255));
			displayPanel.update(timer.getDelay());
			displayPanel.repaint();
			/* World wor = displayPanel.getWorld();
			if(time % 100 == 0 && wor.getSurvCollection().size() < 10)
				wor.getSurvCollection().add(wor.getBase());*/
		}
		else if(arg0.getSource().equals(button1)){
			start();
		}
		else if(arg0.getSource().equals(button2)){
			pause();
		}
		else if(arg0.getSource().equals(button3)){
			reset();
		}
	}
	
	public void reset(){
		time = 0;
		resources = 0;
		displayPanel.initWorld();
		displayPanel.repaint();
		text1.setText("Resources gathered: " + resources + " | Time elapsed: " + time);
	}
	
	public void pause(){
		button1.setEnabled(true);
		button2.setEnabled(false);
		button3.setEnabled(true);
		radioButton1.setEnabled(true);
		radioButton2.setEnabled(true);
		radioButton3.setEnabled(true);
		radioButton4.setEnabled(true);
		displayPanel.setBackground(background);
		timer.stop();
		this.requestFocus();
	}
	
	public void start(){
		button1.setEnabled(false);
		button2.setEnabled(true);
		button3.setEnabled(false);
		if(radioButton3.isSelected()){
			System.out.println("Delauney selected!");
			displayPanel.getWorld().getSurvCollection().setDelauney();
		}
		else if (radioButton4.isSelected()){
			System.out.println("Potential selected!?");
			displayPanel.getWorld().getSurvCollection().setPotential();
		}
		radioButton1.setEnabled(false);
		radioButton2.setEnabled(false);
		radioButton3.setEnabled(false);
		radioButton4.setEnabled(false);
		timer.start();
		this.requestFocus();
	}
	
	public void logTimeToGetOne(){
		try {
			out.write(Integer.toString(time)+"\n");
			runsLogged++;
			if (runsLogged == 50){
				out.close();
				System.exit(0);
			}
		} catch (IOException e) {System.exit(2);}
	}
	
	public void logDeath(){
		try {
			out.write("Death\n");
			runsLogged++;
			if (runsLogged == 50){
				out.close();
				System.exit(0);
			}
		} catch (IOException e) {System.exit(2);}
	}
	
	public void logWorldSize(double h, double w){
		System.out.println("logging world size");
		if (!loggedWorld){
			try {
				out.write("World Dimensions (height x width): "+h+" x "+w+"\n");
				loggedWorld = true;
			} catch (IOException e) {System.exit(2);}
		}
	}
}

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



public class MyFrame extends JFrame implements KeyListener, ActionListener{
	private static final long serialVersionUID = -2122161377842820073L;

	private JPanel panel1, panel2, panel3, topPanel, panel4;
	private JRadioButton radioButton1, radioButton2, radioButton3, radioButton4, radioButton5, radioButton6;
	private JButton button1, button2, button3;
	private JLabel text1;
	private MyDisplayPanel displayPanel;
	private ButtonGroup group1, group2;
	private int resources = 0, time = 0;
	private Timer timer;
	private static final Color background = new Color(0,0,0);
	private GraphicsDevice myGraphicsDevice;
	private boolean fullscreen = true;
	
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
		
		
		text1 = new JLabel("<html>&nbsp;&nbsp;&nbsp;Resources gathered: " + resources + " | Time elapsed: " + time + "</html>");
		topPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
		//topPanel.setLayout(new BorderLayout());
		topPanel.add(text1, BorderLayout.CENTER);
		//topPanel.add(button3, BorderLayout.EAST);
		
		this.add(topPanel, BorderLayout.NORTH);
		
		radioButton1 = new JRadioButton("10 Zombies");
		radioButton1.setSelected(true);
		radioButton2 = new JRadioButton("Mega Apocalypse");
		radioButton5 = new JRadioButton("Mega-Multi Apocalypse");
		radioButton6 = new JRadioButton("Hyper-Multi Apocalypse");
		
		radioButton3 = new JRadioButton("Delaunay Triangulation");
		radioButton3.setToolTipText("<html><u>Delaunay Triangulation:</u> <br>&nbsp;Details</html>");
		radioButton3.setSelected(true);
		radioButton4 = new JRadioButton("Potential Fields");
		radioButton4.setToolTipText("<html><u>Potential Fields:</u> <br>&nbsp;Details</html>");
		
		group1.add(radioButton1);
		group1.add(radioButton2);
		group1.add(radioButton5);
		group1.add(radioButton6);
		group2.add(radioButton3);
		group2.add(radioButton4);
		
		panel1.add(radioButton1);
		panel1.add(radioButton2);
		panel1.add(radioButton5);
		panel1.add(radioButton6);
		panel2.add(radioButton3);
		panel2.add(radioButton4);
		panel1.setBorder(BorderFactory.createTitledBorder("Apocalypse Severity"));
		panel2.setBorder(BorderFactory.createTitledBorder("Path-finding Alg."));
		
		panel3 = new JPanel();
		panel3.setLayout(new GridLayout(6, 1));
		panel4 = new JPanel();
		panel4.setLayout(new GridLayout(3, 1));
		panel3.add(panel1);
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
		setSize(1366,768);
		
		setLocation(0,0);
		
		//For Fullscreen
		myGraphicsDevice = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
		if(myGraphicsDevice.isFullScreenSupported() && fullscreen){
			this.setUndecorated(true);
			//myGraphicsDevice.setFullScreenWindow(this);
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
		radioButton5.addKeyListener(this);
		radioButton6.addKeyListener(this);
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
			if(radioButton5.isSelected() || radioButton6.isSelected()){
				World wor = displayPanel.getWorld();
				int timeSplit; int survivors;
				if(radioButton5.isSelected()){
					timeSplit = 100; survivors = 10;
				}
				else{
					timeSplit = 1; survivors = 10000;
				}
				if(time % timeSplit == 0 && wor.getSurvCollection().size() < survivors)
					wor.getSurvCollection().add(wor.getBase());
			}
		}
		else if(arg0.getSource().equals(button1)){
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
		else if(arg0.getSource().equals(button2)){
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
		else if(arg0.getSource().equals(button3)){
			time = 0;
			resources = 0;
			displayPanel.initWorld();
			displayPanel.repaint();
			text1.setText("Resources gathered: " + resources + " | Time elapsed: " + time);
		}
	}

	public int getApocalypse() {
		// TODO Auto-generated method stub
		if(radioButton1.isSelected())
			return 10;
		else
			return 100;
	}
}

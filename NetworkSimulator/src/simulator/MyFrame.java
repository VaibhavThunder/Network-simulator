package simulator;

import java.awt.BorderLayout;
import java.awt.Graphics;

import javax.swing.JFrame;
/**
 * This class is a custom implementation of JFrame. This is a main Jframe of the application
 * @author Vaibhav
 *
 */
@SuppressWarnings("serial")
public class MyFrame extends JFrame{
	
	MyPanel myPanel;
	
	public MyFrame(String title){
		super(title);
		setLayout(new BorderLayout());
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(getMaximumSize());
		myPanel= new MyPanel();
		add(myPanel,BorderLayout.CENTER);
		setVisible(true);
	
		
	}
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);
	}
	
	
}

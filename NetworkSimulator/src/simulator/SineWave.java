package simulator;

import static java.lang.Math.sin;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
/**
 * This class is used to draw analog signals from binary bits
 * @author Vaibhav
 *
 */
@SuppressWarnings("serial")
class SinX extends JPanel{
    
	private int width;
	private int[] bits;
	private Color repColor;
	public SinX(int[] bits,Color repColor){
		super(null);
		this.bits=bits;
		this.repColor=repColor;
		this.width=((bits==null || bits.length==0)?1:bits.length);
		setOpaque(true);
		setBackground(Color.WHITE);
		setVisible(true);
		setPreferredSize(new Dimension(width*250, 400));
	}
	
	@Override
	protected void paintComponent(Graphics g) 
	{
		super.paintComponent(g);
        g.drawLine(0,350,width*250,350); // x-axis
        g.setColor(repColor);
        int offset=0;
        for(int i=0;i<bits.length;i++){
        	drawSignal(g, bits[i], offset);
        	offset+=240;
        }
    }
    
    public void  drawSignal(Graphics g,int bit, int offset){
    	for(double x=240;x>=0;x=x-0.25)
        {
            double y = 100 * sin(x*(Math.PI/(Math.pow(2, bit)*60)));
            int Y = (int)y;
            int X = (int)x;
            g.drawLine(240-X+offset,350+Y,240-X+offset,350+Y);
        }
    }

}


public class SineWave {
    
    public static void showAnalogSignal(int[] bits, Color repColor)
    {
        JFrame frame = new JFrame();
        frame.setSize(1000, 800);
        frame.setTitle("Analog Signal via FSK");
        frame.setResizable(true);
        frame.add(new JScrollPane(new SinX(bits,repColor),ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER,ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED));
        frame.repaint();
        frame.setVisible(true);
       
    }
}
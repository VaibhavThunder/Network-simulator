package simulator;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.Timer;

/**
 * This class is a custom implementation of JPanel
 * 
 * @author Vaibhav
 *
 */
@SuppressWarnings("serial")
public class MyPanel extends JPanel implements MouseListener {
	// layer buttons
	private LayeredSystem btnLayerA, btnLayerB;
	private JButton a2bBtn;
	private boolean signal1, signal2;
	private int revSignal;
	private JTextField textBox1, textBox2;
	private List<Vertex> vertices;
	private List<Edge> edges1, edges2;
	private Graph g1, g2;
	private LinkedList<Vertex> path1, path2;
	private int pathIndex1, pathIndex2;
	private int x1, y1, x2, y2;
	private int xLimit1, yLimit1, xLimit2, yLimit2;
	private int timer1, timer2;
	private Ellipse2D oval1 = new Ellipse2D.Double();
	private Ellipse2D oval2 = new Ellipse2D.Double();

	public MyPanel() {
		super(null);
		setOpaque(true);
		setBackground(Color.WHITE);
		this.addMouseListener(this);
		a2bBtn = new JButton("Start");
		a2bBtn.setBounds(850, 50, 100, 25);
		a2bBtn.setLayout(null);
		add(a2bBtn);

		textBox1 = new JTextField();
		textBox1.setBounds(700, 0, 200, 30);
		add(textBox1);

		textBox2 = new JTextField();
		textBox2.setBounds(900, 0, 200, 30);
		add(textBox2);

		// initializing layers with coordinates and assign mac address
		btnLayerA = initializeLayers(200, 80, "56:AB:B2:12");
		btnLayerB = initializeLayers(1400, 80, "57:AC:B2:12");

		initializeGraph();

		class ButtonActionListener implements ActionListener {
			private LayeredSystem btnLayer1, btnLayer2;
			private int counter = 0;
			private int offset1 = 0;
			private int offset2 = 0;
			public ButtonActionListener(LayeredSystem btnLayerSystem1, LayeredSystem btnLayerSystem2) {
				this.btnLayer1 = btnLayerSystem1;
				this.btnLayer2 = btnLayerSystem2;

			}

			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (textBox1.getText() == null || textBox1.getText().trim().length() == 0 ||
						textBox2.getText() == null || textBox2.getText().trim().length() == 0 ) {
					JOptionPane.showMessageDialog(new JFrame(), "Please input two messages");
					return;
				}
				pathIndex1 = pathIndex2 = 0;
				for (Edge e : edges1) {
					e.setWeight(ThreadLocalRandom.current().nextInt(1, 8));
				}
				for (Edge e : edges2) {
					e.setWeight(ThreadLocalRandom.current().nextInt(1, 8));
				}
				DijkstraAlgorithm algorithm = new DijkstraAlgorithm(g1);
				algorithm.execute(vertices.get(0));
				path1 = algorithm.getPath(vertices.get(vertices.size() - 1));

				algorithm = new DijkstraAlgorithm(g2);
				algorithm.execute(vertices.get(0));
				path2 = algorithm.getPath(vertices.get(vertices.size() - 1));

				disableButtons(btnLayer1.getLayers(), textBox1.getText(), textBox2.getText(), btnLayer2.getMacAddress(),
						btnLayer1.getMacAddress(), false);
				disableButtons(btnLayer2.getLayers(), textBox1.getText(), textBox2.getText(), btnLayer2.getMacAddress(),
						btnLayer1.getMacAddress(), true);

				a2bBtn.setEnabled(false);
				counter = 0;
				offset1 = offset2=0;
				revSignal=0;
				repaint();
				timer.start();
			}

			// this timer manages the layer animation
			Timer timer = new Timer(1000, new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					if (offset1 == 5 && offset2 == 5) {
						a2bBtn.setEnabled(true);
						btnLayer2.getLayers().get(counter - offset1).setBackground(null);
						timer.stop();
						return;
					} else if (counter < 5) {
						btnLayer1.getLayers().get(counter).setBackground(Color.GREEN);
						btnLayer1.getLayers().get(counter).setEnabled(true);
						if (counter != 0)
							btnLayer1.getLayers().get(counter - 1).setBackground(null);
						counter++;
					} else if (counter == 5) {
						if (!signal1 && pathIndex1 != path1.size() - 1) {
							if (revSignal==0 && pathIndex1 == 2) {
								x1=path1.get(pathIndex1).getStartX();
								y1=path1.get(pathIndex1).getStartY();
								xLimit1=path1.get(pathIndex1-1).getStartX();
								yLimit1=path1.get(pathIndex1-1).getStartY();
								timerOval1.setDelay(10);
								pathIndex1--;
								revSignal=1;
							} else {
								x1 = path1.get(pathIndex1).getStartX();
								y1 = path1.get(pathIndex1).getStartY();
								pathIndex1++;
								xLimit1 = path1.get(pathIndex1).getStartX();
								yLimit1 = path1.get(pathIndex1).getStartY();
								
								for (Edge e1 : edges1) {
									if (e1.getSource().getStartX() == x1 && e1.getSource().getStartY() == y1
											&& e1.getDestination().getStartX() == xLimit1
											&& e1.getDestination().getStartY() == yLimit1) {
										timer1 = e1.getWeight();
									}
								}
								timerOval1.setDelay(timer1 * 5);
							}
							signal1=true;							
							timerOval1.start();
						}

						if (!signal2 && pathIndex2 != path2.size() - 1) {
							x2 = path2.get(pathIndex2).getStartX();
							y2 = path2.get(pathIndex2).getStartY();
							pathIndex2++;
							xLimit2 = path2.get(pathIndex2).getStartX();
							yLimit2 = path2.get(pathIndex2).getStartY();
							signal2 = true;
							for (Edge e2 : edges2) {
								if (e2.getSource().getStartX() == x2 && e2.getSource().getStartY() == y2
										&& e2.getDestination().getStartX() == xLimit2
										&& e2.getDestination().getStartY() == yLimit2) {
									timer2 = e2.getWeight();
								}
							}

							timerOval2.setDelay(timer2 * 5);
							timerOval2.start();

						}

						if (pathIndex1 == path1.size() - 1 && !signal1 && offset1!=5){
							repaint();
							btnLayer2.getLayers().get(counter - offset1 - 1).setBackground(Color.GREEN);
							btnLayer2.getLayers().get(counter - offset1 - 1).setEnabled(true);
							((MyButton)btnLayer2.getLayers().get(counter - offset1 - 1)).setSignal1(false);
							if (counter - offset1 != 5) {
								btnLayer2.getLayers().get(counter - offset1).setBackground(null);
							}

							offset1++;
						}
						if(pathIndex2 == path2.size() - 1 && !signal2 && offset2!=5)
						{
							repaint();
							btnLayer2.getLayers().get(counter - offset2 - 1).setBackground(Color.GREEN);
							btnLayer2.getLayers().get(counter - offset2 - 1).setEnabled(true);
							((MyButton)btnLayer2.getLayers().get(counter - offset2 - 1)).setSignal2(false);
							if (counter - offset2 != 5) {
								btnLayer2.getLayers().get(counter - offset2).setBackground(null);
							}

							offset2++;

						}

					}
				}

			});

			// this timer oval movement
			Timer timerOval1 = new Timer(10, new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					btnLayer1.getLayers().get(counter - 1).setBackground(null);
					if (x1 == xLimit1 && y1 == yLimit1) {
						if(revSignal==1)revSignal=2;
						signal1 = false;
						timerOval1.stop();
						return;
					}
					if (x1 < xLimit1)
						x1++;
					if (x1 > xLimit1)
						x1--;
					if (y1 < yLimit1)
						y1++;
					if (y1 > yLimit1)
						y1--;
					repaint();

				}
			});

			// this timer oval movement
			Timer timerOval2 = new Timer(10, new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					btnLayer1.getLayers().get(counter - 1).setBackground(null);
					if (x2 == xLimit2 && y2 == yLimit2) {
						signal2 = false;
						timerOval2.stop();
						return;
					}
					if (x2 < xLimit2)
						x2++;
					if (x2 > xLimit2)
						x2--;
					if (y2 < yLimit2)
						y2++;
					if (y2 > yLimit2)
						y2--;
					repaint();

				}
			});

		}

		a2bBtn.addActionListener(new ButtonActionListener(btnLayerA, btnLayerB));

	}

	private void initializeGraph() {
		vertices = new ArrayList<>();
		Vertex v1 = new Vertex("1", "R1", 400, 400);
		vertices.add(v1);
		Vertex v2 = new Vertex("2", "R2", 600, 200);
		vertices.add(v2);
		Vertex v3 = new Vertex("3", "R3", 1200, 200);
		vertices.add(v3);
		Vertex v5 = new Vertex("5", "R5", 750, 400);
		vertices.add(v5);
		Vertex v6 = new Vertex("6", "R6", 1050, 400);
		vertices.add(v6);
		Vertex v7 = new Vertex("7", "R7", 600, 600);
		vertices.add(v7);
		Vertex v8 = new Vertex("8", "R8", 1200, 600);
		vertices.add(v8);
		Vertex v4 = new Vertex("4", "R4", 1400, 400);
		vertices.add(v4);

		edges1 = new ArrayList<>();
		edges1.add(new Edge("R1-R2", v1, v2));
		edges1.add(new Edge("R1-R7", v1, v7));
		edges1.add(new Edge("R2-R5", v2, v5));
		edges1.add(new Edge("R2-R3", v2, v3));
		edges1.add(new Edge("R7-R5", v7, v5));
		edges1.add(new Edge("R5-R6", v5, v6));
		edges1.add(new Edge("R7-R8", v7, v8));
		edges1.add(new Edge("R3-R4", v3, v4));
		edges1.add(new Edge("R8-R4", v8, v4));
		edges1.add(new Edge("R6-R3", v6, v3));
		edges1.add(new Edge("R6-R8", v6, v8));

		g1 = new Graph(vertices, edges1);

		edges2 = new ArrayList<>();
		edges2.add(new Edge("R1-R2", v1, v2));
		edges2.add(new Edge("R1-R7", v1, v7));
		edges2.add(new Edge("R2-R5", v2, v5));
		edges2.add(new Edge("R2-R3", v2, v3));
		edges2.add(new Edge("R7-R5", v7, v5));
		edges2.add(new Edge("R5-R6", v5, v6));
		edges2.add(new Edge("R7-R8", v7, v8));
		edges2.add(new Edge("R3-R4", v3, v4));
		edges2.add(new Edge("R8-R4", v8, v4));
		edges2.add(new Edge("R6-R3", v6, v3));
		edges2.add(new Edge("R6-R8", v6, v8));

		g2 = new Graph(vertices, edges2);

	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		for (int i = 0; i < 5; i++) {
			g.drawLine(300, 100 + i * 30, 300, 250 + i * 30);
			g.drawLine(1500, 100 + i * 30, 1500, 250 + i * 30);

		}

		for (Edge e : edges1) {

			g.drawLine(e.getSource().getStartX(), e.getSource().getStartY(), e.getDestination().getStartX(),
					e.getDestination().getStartY());
			g.setColor(Color.BLUE);
			g.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 20));
			g.drawString(((Integer) e.getWeight()).toString(),
					e.getSource().getStartX() + (e.getDestination().getStartX() - e.getSource().getStartX()) / 2,
					e.getSource().getStartY() + (e.getDestination().getStartY() - e.getSource().getStartY()) / 2);
		}
		for (Edge e : edges2) {
			g.setColor(Color.RED);
			g.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 20));
			g.drawString(" , " + ((Integer) e.getWeight()).toString(),
					e.getSource().getStartX() + 10 + (e.getDestination().getStartX() - e.getSource().getStartX()) / 2,
					e.getSource().getStartY() + (e.getDestination().getStartY() - e.getSource().getStartY()) / 2);
		}

		for (Vertex v : vertices) {
			g.setColor(Color.BLACK);
			g.drawOval(v.getStartX() - 25, v.getStartY() - 25, 50, 50);
			g.fillOval(v.getStartX() - 25, v.getStartY() - 25, 50, 50);
			g.setColor(Color.WHITE);
			g.drawString(v.getName(), v.getStartX() - 10, v.getStartY());

		}
		if(path1!=null){
			int j=600;
			int k=800;
			g.setColor(Color.BLUE);
			g.drawString("Path 1:", j, k);
			j=j+80;
			for(Vertex v:path1){
				g.drawString(v.getName(), j, k);
				j=j+30;
			}
			
		}
		
		if(path2!=null){
			int j=1000;
			int k=800;
			g.setColor(Color.RED);
			g.drawString("Path 2:", j, k);
			j=j+80;
			for(Vertex v:path2){
				g.drawString(v.getName(), j, k);
				j=j+30;
			}
			
		}
		
		Graphics2D g2d = (Graphics2D) g;
		if (signal1) {
			if(revSignal==1){
				g2d.setColor(Color.YELLOW);		
				oval1.setFrame(x1, y1+5, 30, 30);
				g2d.fill(oval1);
				g2d.setColor(Color.BLACK);
				g2d.drawString("A", x1+10, y1+25);
				
			}else{
				g2d.setColor(Color.BLUE);
				oval1.setFrame(x1, y1+5, 30, 30);
				g2d.fill(oval1);
				
				
			}
			
			
		}

		if (signal2) {
			g2d.setColor(Color.RED);
			oval2.setFrame(x2, y2-5, 30, 30);
			g2d.fill(oval2);
		}

	}

	/**
	 * this method initialize layer structure
	 */
	private LayeredSystem initializeLayers(int startX, int startY, String macAddress) {
		List<JButton> btnLayer = new ArrayList<JButton>();
		String names[] = { "Application Layer", "Transport Layer", "Network Layer", "Datalink Layer",
				"Physical Layer" };
		String message[] = { "M1", "H1|M1", "H2|H1|M1", "0101010|1|D1|S1|H2|H1|M1|C1|", "B1" };
		MyButton newBtn = null;
		for (int i = 0; i < 5; i++) {
			newBtn = new MyButton(names[i], startX, startY + i * 50, message[i]);
			add(newBtn);
			btnLayer.add(i, newBtn);
		}
		add(getImage("computer.jpg", startX + 60, startY + 250));

		LayeredSystem system = new LayeredSystem(btnLayer, macAddress);
		return system;
	}

	/**
	 * This method created JLabel with image
	 * 
	 * @param name
	 * @param startX
	 * @param startY
	 * @return
	 */
	private JLabel getImage(String name, int startX, int startY) {
		BufferedImage image = null;
		try {
			image = ImageIO.read(getClass().getResource("/" + name));
		} catch (IOException e) {
			e.printStackTrace();
		}
		JLabel jlabel = new JLabel(new ImageIcon(image.getScaledInstance(75, 75, Image.SCALE_DEFAULT)));
		jlabel.setBounds(startX, startY, 75, 75);
		jlabel.setLayout(null);

		return jlabel;
	}

	/**
	 * This method disables layers if no data is being transmitted
	 * 
	 * @param btnLayer
	 * @param message1
	 * @param message2
	 * @param dest1Mac
	 * @param dest2Mac
	 * @param sourceMac
	 * @param isReceiver
	 */
	private void disableButtons(List<JButton> btnLayer, String message1, String message2, String destMac,
			String sourceMac, boolean isReceiver) {
		for (JButton b : btnLayer) {
			b.setEnabled(false);
			b.setBackground(null);
			((MyButton) b).setMessage1(message1);
			((MyButton) b).setMessage2(message2);
			((MyButton) b).setDestMacAddress(destMac);
			((MyButton) b).setSourceMac(sourceMac);
			((MyButton) b).setReceiver(isReceiver);
			((MyButton) b).setSignal1(true);
			((MyButton) b).setSignal2(true);

		}

	}

	/**
	 * This class is a representation of layers for a system
	 * 
	 * @author Vaibhav
	 *
	 */
	class LayeredSystem {
		private List<JButton> layers;
		private String macAddress;

		public String getMacAddress() {
			return macAddress;
		}

		public void setMacAddress(String macAddress) {
			this.macAddress = macAddress;
		}

		public LayeredSystem(List<JButton> layers, String macAddress) {
			super();
			this.layers = layers;
			this.macAddress = macAddress;
		}

		public List<JButton> getLayers() {
			return layers;
		}

		public void setLayers(List<JButton> layers) {
			this.layers = layers;
		}

	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (e.getButton() == 1) {
			if (oval1.contains(e.getX(), e.getY())) {
				SineWave.showAnalogSignal(Utility.getBinary(textBox1.getText()), Color.BLUE);
			}

			if (oval2.contains(e.getX(), e.getY())) {
				SineWave.showAnalogSignal(Utility.getBinary(textBox2.getText()), Color.RED);
			}
		}

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

}

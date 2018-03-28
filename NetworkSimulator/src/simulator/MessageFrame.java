package simulator;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

/**
 * MessageFrame is an implementation of JFrame to show messages at various steps
 * 
 * @author Vaibhav
 *
 */
@SuppressWarnings("serial")
public class MessageFrame extends JFrame {

	private JPanel contentPane;

	/**
	 * Create the frame.
	 */

	public MessageFrame(String template, String message1, String message2, String destMac, String sourceMac,
			boolean isReceiver, boolean signal1, boolean signal2) {

		setBounds(570, 300, 900, 300);
		contentPane = new JPanel() {

			@Override
			protected void paintComponent(Graphics g) {

				super.paintComponent(g);
				Graphics2D g2d = (Graphics2D) g;
				if (template.equals("B1")) {
					JTextArea textArea = new JTextArea();
					StringBuffer messageBuffer = new StringBuffer();
					if ((message1 != null && isReceiver && !signal1) || (message1 != null && !isReceiver)) {
						messageBuffer.append(Utility.getBinaryRep(message1)).append(" ")
								.append(CRCUtility.crcDivide(Utility.getBinary(message1)) + "\n");
					}
					if ((message2 != null && isReceiver && !signal2) || (message2 != null && !isReceiver)) {
						messageBuffer.append(Utility.getBinaryRep(message2)).append(" ")
								.append(CRCUtility.crcDivide(Utility.getBinary(message2)) + "\n");
					}

					textArea.setText(messageBuffer.toString());
					textArea.setLineWrap(true);
					textArea.setRows(5);
					textArea.setColumns(100);
					add(textArea);
				} else {
					String[] messageArray;
					int x;
					String crcStr = "";

					if ((message1 != null && isReceiver && !signal1) || (message1 != null && !isReceiver)) {
						if (template.contains("|D1|S1|")) {
							if (isReceiver)
								crcStr = "CRC passed";
							else
								crcStr = "CRC=" + CRCUtility.crcDivide(Utility.getBinary(message1));
							;
						}
						messageArray = template.replace("M1", message1).replace("D1", destMac).replace("S1", sourceMac)
								.replace("C1", crcStr).split("\\|");
						g.setColor(Color.BLUE);
						g.setFont(new Font("TimesRoman", Font.PLAIN, 25));
						x = 0;
						for (String s : messageArray) {
							g2d.drawRect(x, 10, s.length() * 16, 40);
							g2d.drawString(s, x, 30);
							x += s.length() * 16 + 10;
						}
					}
					if ((message2 != null && isReceiver && !signal2) || (message2 != null && !isReceiver)) {
						if (template.contains("|D1|S1|")) {
							if (isReceiver)
								crcStr = "CRC passed";
							else
								crcStr = "CRC=" + CRCUtility.crcDivide(Utility.getBinary(message2));
							;
						}
						messageArray = template.replace("M1", message2).replace("D1", destMac).replace("S1", sourceMac)
								.replace("C1", crcStr).split("\\|");
						g.setColor(Color.RED);
						g.setFont(new Font("TimesRoman", Font.PLAIN, 25));
						x = 0;
						for (String s : messageArray) {
							g2d.drawRect(x, 50, s.length() * 16, 40);
							g2d.drawString(s, x, 80);
							x += s.length() * 16 + 10;
						}
					}

				}
			}
		};
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
	}

}

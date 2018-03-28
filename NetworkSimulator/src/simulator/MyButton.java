package simulator;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

/**
 * This class is a custom implementation of JButton which is represented as
 * layers
 * 
 * @author Vaibhav
 *
 */
@SuppressWarnings("serial")
public class MyButton extends JButton {
	private String message1;
	private String message2;
	private String template;
	private String destMacAddress;
	private String sourceMac;
	private boolean isReceiver;
	public static final int WIDTH = 200;
	public static final int HEIGHT = 25;
	private boolean signal1;
	private boolean signal2;

	public MyButton(String name, int x, int y, String template) {
		super(name);
		setBounds(x, y, WIDTH, HEIGHT);
		setLayout(null);
		setEnabled(false);
		signal1=signal2=true;
		this.template = template;

		// on button click, message contained is shown in dialog box
		this.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				MessageFrame frame = new MessageFrame(template, message1, message2, destMacAddress, sourceMac,
						isReceiver, signal1, signal2);
				frame.setVisible(true);
			}
		});

	}

	public String getMessage1() {
		return message1;
	}

	public void setMessage1(String message) {
		this.message1 = message;
	}

	public String getMessage2() {
		return message2;
	}

	public void setMessage2(String message2) {
		this.message2 = message2;
	}

	public String getDestMacAddress() {
		return destMacAddress;
	}

	public void setDestMacAddress(String dest1MacAddress) {
		this.destMacAddress = dest1MacAddress;
	}

	public String getTemplate() {
		return template;
	}

	public void setTemplate(String template) {
		this.template = template;
	}

	public String getSourceMac() {
		return sourceMac;
	}

	public void setSourceMac(String sourceMac) {
		this.sourceMac = sourceMac;
	}

	public boolean isReceiver() {
		return isReceiver;
	}

	public void setReceiver(boolean isReceiver) {
		this.isReceiver = isReceiver;
	}

	public boolean isSignal1() {
		return signal1;
	}

	public void setSignal1(boolean signal1) {
		this.signal1 = signal1;
	}

	public boolean isSignal2() {
		return signal2;
	}

	public void setSignal2(boolean signal2) {
		this.signal2 = signal2;
	}

}

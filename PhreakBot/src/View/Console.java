package View;

import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Console extends WindowAdapter implements WindowListener, ActionListener, Runnable
{
	//private JFrame frame;
	private JTextArea textArea;
	private JScrollPane scroll;
	private Thread reader;
	private Thread reader2;
	private boolean quit;
	private JPanel mainPanel;
					
	private final PipedInputStream pin=new PipedInputStream(); 
	private final PipedInputStream pin2=new PipedInputStream(); 
	private boolean isCustomConsoleOn = false;

	
	public Console()
	{
		// create all components and add them
		//frame=new JFrame("Java Console");
		Dimension panelsize = new Dimension(470, 400);
		//Dimension frameSize=new Dimension((int)(screenSize.width/2),(int)(screenSize.height/2));
		//int x=(int)(frameSize.width/2);
		//int y=(int)(frameSize.height/2);
		//frame.setBounds(x,y,frameSize.width,frameSize.height);
		mainPanel = new JPanel();
		mainPanel.setPreferredSize(panelsize);
		textArea=new JTextArea();
		textArea.setEditable(false);
		textArea.setVisible(true);
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		textArea.setCaretPosition(textArea.getDocument().getLength());
		JButton button=new JButton("clear");
		scroll = new JScrollPane(textArea);
		mainPanel.setLayout(new BorderLayout());
		mainPanel.add(scroll,BorderLayout.CENTER);
		mainPanel.add(button,BorderLayout.SOUTH);
		mainPanel.setVisible(true);		
		
		//frame.addWindowListener(this);		
		button.addActionListener(this);
		
		try
		{
			if(isCustomConsoleOn) {
				PipedOutputStream pout=new PipedOutputStream(this.pin);
				System.setOut(new PrintStream(pout,true));
			}
		} 
		catch (java.io.IOException io)
		{
			textArea.append("Couldn't redirect STDOUT to this console\n"+io.getMessage());
			textArea.setCaretPosition(textArea.getDocument().getLength());
		}
		catch (SecurityException se)
		{
			textArea.append("Couldn't redirect STDOUT to this console\n"+se.getMessage());
			textArea.setCaretPosition(textArea.getDocument().getLength());
	    } 
		
		try 
		{
			PipedOutputStream pout2=new PipedOutputStream(this.pin2);
			System.setErr(new PrintStream(pout2,true));
		} 
		catch (java.io.IOException io)
		{
			textArea.append("Couldn't redirect STDERR to this console\n"+io.getMessage());
			textArea.setCaretPosition(textArea.getDocument().getLength());
		}
		catch (SecurityException se)
		{
			textArea.append("Couldn't redirect STDERR to this console\n"+se.getMessage());
			textArea.setCaretPosition(textArea.getDocument().getLength());
	    } 		
			
		quit=false; // signals the Threads that they should exit
				
		// Starting two seperate threads to read from the PipedInputStreams				
		//
		reader=new Thread(this);
		reader.setDaemon(true);	
		reader.start();	
		//
		reader2=new Thread(this);	
		reader2.setDaemon(true);	
		reader2.start();
				
						
	}
	
	public synchronized void windowClosed(WindowEvent evt)
	{
		quit=true;
		this.notifyAll(); // stop all threads
		try { reader.join(1000);pin.close();   } catch (Exception e){}		
		try { reader2.join(1000);pin2.close(); } catch (Exception e){}
		System.exit(0);
	}		
		
	public synchronized void windowClosing(WindowEvent evt)
	{
		//frame.setVisible(false); // default behaviour of JFrame	
		//frame.dispose();
	}
	
	public synchronized void actionPerformed(ActionEvent evt)
	{
		textArea.setText("");
	}

	public synchronized void run()
	{
		try
		{			
			while (Thread.currentThread()==reader)
			{
				try { this.wait(100);}catch(InterruptedException ie) {}
				if (pin.available()!=0)
				{
					String input=this.readLine(pin);
					if(input.toLowerCase().contains("login unsuccessful")) {
						textArea.append("  ****WARNING**** YOUR BOT'S NAME OR OATHKEY MAY BE INCORRECT.   ");
						textArea.append("  PLEASE VERIFY THIS INFORMATION IS CORRECT!   ");
						textArea.setCaretPosition(textArea.getDocument().getLength());
					}
					else if(input.toLowerCase().contains("connection timed out")) {
						textArea.append("  ****WARNING**** THE IP ADDRESS OR PORT MAY BE INCORRECT OR YOU MAY NOT BE CONNECTED TO THE INTERNET.   ");
						textArea.append("  PLEASE VERIFY THIS INFORMATION IS CORRECT!   ");
						textArea.setCaretPosition(textArea.getDocument().getLength());
					}
					textArea.append(input);
					textArea.setCaretPosition(textArea.getDocument().getLength());
				}
				if (quit) return;
			}
		
			while (Thread.currentThread()==reader2)
			{
				try { this.wait(100);}catch(InterruptedException ie) {}
				if (pin2.available()!=0)
				{
					String input=this.readLine(pin2);
					if(input.toLowerCase().contains("login unsuccessful")) {
						textArea.append("****WARNING**** YOUR BOT'S NAME OR OATHKEY MAY BE INCORRECT.   ");
						textArea.append("PLEASE VERIFY THIS INFORMATION IS CORRECT!   ");
						textArea.setCaretPosition(textArea.getDocument().getLength());
					}
					else if(input.toLowerCase().contains("connection timed out")) {
						textArea.append("  ****WARNING**** THE IP ADDRESS OR PORT MAY BE INCORRECT OR YOU MAY NOT BE CONNECTED TO THE INTERNET.   ");
						textArea.append("  PLEASE VERIFY THIS INFORMATION IS CORRECT!   ");
						textArea.setCaretPosition(textArea.getDocument().getLength());
					}
					textArea.append(input);
					textArea.setCaretPosition(textArea.getDocument().getLength());
				}
				if (quit) return;
			}			
		} catch (Exception e)
		{
			textArea.append("\nConsole reports an Internal error.");
			textArea.append("The error is: "+e);	
			textArea.setCaretPosition(textArea.getDocument().getLength());
		}
		
	
	}
	
	public synchronized String readLine(PipedInputStream in) throws IOException
	{
		String input="";
		do
		{
			int available=in.available();
			if (available==0) break;
			byte b[]=new byte[available];
			in.read(b);
			input=input+new String(b,0,b.length);														
		}while( !input.endsWith("\n") &&  !input.endsWith("\r\n") && !quit);
		return input;
	}	
	
	public JPanel getPanel() {
		return mainPanel;
		
	}
				
}



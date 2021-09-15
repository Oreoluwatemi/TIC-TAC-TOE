/*Oreoluwa Lawal
 * N01452264
 */
package client_server;

import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JTextArea;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.awt.event.ActionEvent;

@SuppressWarnings("serial")
public class Client extends JFrame{
	private DataOutputStream toServer;
	private Socket socket = null;
	JTextArea textArea;
	JButton btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9;
	private JButton btnSend;
	private JTextArea txtOut;
	String message;
	public Client() {
		//GUI layout
		getContentPane().setLayout(null);
		
		btn1 = new JButton("");
		btn1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textArea.setText("1");
				btn1.setEnabled(false);
			}
		});
		btn1.setActionCommand("1");
		btn1.setBounds(10, 24, 50, 35);
		getContentPane().add(btn1);
		
		btn2 = new JButton("");
		btn2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textArea.setText("2");
				btn2.setEnabled(false);
			}
		});
		btn2.setActionCommand("2");
		btn2.setBounds(64, 24, 50, 35);
		getContentPane().add(btn2);
		
		btn3 = new JButton("");
		btn3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textArea.setText("3");
				btn3.setEnabled(false);
			}
		});
		btn3.setActionCommand("3");
		btn3.setBounds(118, 24, 50, 35);
		getContentPane().add(btn3);
		
		btn4 = new JButton("");
		btn4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textArea.setText("4");
				btn4.setEnabled(false);
			}
		});
		btn4.setActionCommand("4");
		btn4.setBounds(10, 61, 50, 35);
		getContentPane().add(btn4);
		
		btn5 = new JButton("");
		btn5.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textArea.setText("5");
				btn5.setEnabled(false);
			}
		});
		btn5.setActionCommand("5");
		btn5.setBounds(64, 61, 50, 35);
		getContentPane().add(btn5);
		
		btn6 = new JButton("");
		btn6.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textArea.setText("6");
				btn6.setEnabled(false);
			}
		});
		btn6.setActionCommand("6");
		btn6.setBounds(118, 61, 50, 35);
		getContentPane().add(btn6);
		
		btn7 = new JButton("");
		btn7.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textArea.setText("7");
				btn7.setEnabled(false);
			}
		});
		btn7.setActionCommand("7");
		btn7.setBounds(10, 98, 50, 35);
		getContentPane().add(btn7);
		
		btn8 = new JButton("");
		btn8.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textArea.setText("8");
				btn8.setEnabled(false);
			}
		});
		btn8.setActionCommand("8");
		btn8.setBounds(64, 98, 50, 35);
		getContentPane().add(btn8);
		
		btn9 = new JButton("");
		btn9.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textArea.setText("9");
				btn9.setEnabled(false);
			}
		});
		btn9.setActionCommand("9");
		btn9.setBounds(118, 98, 50, 35);
		getContentPane().add(btn9);
		
		textArea = new JTextArea();
		textArea.setBounds(10, 177, 160, 22);
		getContentPane().add(textArea);
		
		btnSend = new JButton("Send");
		btnSend.addActionListener(new TextFieldListener());
		btnSend.setBounds(181, 178, 89, 23);
		getContentPane().add(btnSend);
		
		txtOut = new JTextArea();
		txtOut.setBounds(178, 24, 183, 28);
		getContentPane().add(txtOut);
		
		try {
			//connect to server
			socket = new Socket("localhost", 8000);

			
			// Create an output stream to send data to the server
			toServer = new DataOutputStream(socket.getOutputStream());

			//call the class
			new ServerChat(socket);
		}
		catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	
	private class TextFieldListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
		try {
			//get user input
			message = textArea.getText(); // 

			//send input to server
			toServer.writeUTF(message);
			toServer.flush();

		}
		catch (Exception ex) {
			System.err.println(ex);
		}
		}
	}
	
	//Thread
	class ServerChat implements Runnable {
		private Socket socket;

		public ServerChat(Socket socket) {
			this.socket = socket;

			// Create a thread
			Thread thread = new Thread(this);

			// start a thread
			thread.start();
		}

		// Run method
		public void run() {
			try {
				// receive message from the server
				DataInputStream fromServer = new DataInputStream(socket.getInputStream());
				while (true) {
					// read message coming from server
					String text = fromServer.readUTF();

					//check the message received from server
					if(text.contains("1")) {
						btn1.setText(text.substring(1)); 						
					}
					else if(text.contains("2")) {
						btn2.setText(text.substring(1)); 
					}
					else if(text.contains("3")) {
						btn3.setText(text.substring(1)); 
					}
					else if(text.contains("4")) {
						btn4.setText(text.substring(1)); 
					}
					else if(text.contains("5")) {
						btn5.setText(text.substring(1)); 
					}
					else if(text.contains("6")) {
						btn6.setText(text.substring(1)); 
					}
					else if(text.contains("7")) {
						btn7.setText(text.substring(1)); 
					}
					else if(text.contains("8")) {
						btn8.setText(text.substring(1)); 
					}
					else if(text.contains("9")) {
						btn9.setText(text.substring(1)); 
					}
					
					//check if any player won/lost or draw
					String status = check();
					if(status == "Player X won" || status == "Player O won" || status == "Draw" ) {
						textArea.setText("Game ended");
						break;
					}
					else {
						continue;
					}
					
				}
								
			} catch (IOException e) {
				e.printStackTrace();
			} 
		}
		
		public String check() {
			//check for a winner loser or tie
			if((btn1.getText().equals("X") && btn5.getText().equals("X") && btn9.getText().equals("X")) || 
					(btn1.getText().equals("X") && btn4.getText().equals("X") && btn7.getText().equals("X")) ||
					(btn3.getText().equals("X") && btn6.getText().equals("X") && btn9.getText().equals("X")) ||
					(btn1.getText().equals("X") && btn2.getText().equals("X") && btn3.getText().equals("X")) ||
					(btn7.getText().equals("X") && btn8.getText().equals("X") && btn9.getText().equals("X")) ||
					(btn3.getText().equals("X") && btn5.getText().equals("X") && btn7.getText().equals("X"))) {
				txtOut.setText("Player X won");
				return "Player X won";
			} else if ((btn1.getText().equals("O") && btn5.getText().equals("O") && btn9.getText().equals("O")) || 
					(btn1.getText().equals("O") && btn4.getText().equals("O") && btn7.getText().equals("O")) ||
					(btn3.getText().equals("O") && btn6.getText().equals("O") && btn9.getText().equals("O")) ||
					(btn1.getText().equals("O") && btn2.getText().equals("O") && btn3.getText().equals("O")) ||
					(btn7.getText().equals("O") && btn8.getText().equals("O") && btn9.getText().equals("O")) ||
					(btn3.getText().equals("O") && btn5.getText().equals("O") && btn7.getText().equals("O"))) {
				txtOut.setText("Player O won");
				return "Player O won";
			}
			else if(!btn1.getText().equals("") && !btn2.getText().equals("") && !btn3.getText().equals("") && !btn4.getText().equals("") &&
					!btn5.getText().equals("") && !btn6.getText().equals("") && !btn7.getText().equals("") && !btn8.getText().equals("") &&
					!btn9.getText().equals("") ) {
				txtOut.setText("Draw");
				return "Draw";
			}
			return "continue";
		}
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Client client = new Client();
		client.setSize(500, 400);
		client.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		client.setVisible(true); 
	}
}

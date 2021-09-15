/*Oreoluwa Lawal
 * N01452264
 */

package client_server;

import java.awt.BorderLayout;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.JFrame;
import javax.swing.JTextArea;

@SuppressWarnings("serial")
public class Server extends JFrame{
	//initialize varibles and array to store number of sockets
	JTextArea textArea;
	ArrayList<Socket> clientList = new ArrayList<Socket>();
	String message;
	String player;
	public static void main(String[] args) {
		new Server();
	}

	@SuppressWarnings("resource")
	public Server() {
		getContentPane().setLayout(new BorderLayout(0, 0));

		textArea = new JTextArea();
		getContentPane().add(textArea);
		setSize(500, 300);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);

		try { // surround with try and catch to get any error

			ServerSocket serverSocket = new ServerSocket(8000);
			textArea.append("MultiThreadServer started at " + new Date() + '\n');

			// Number a client
			int clientNo = 1;

			while (true) {
				// Listen for a new connection request
				Socket socket = serverSocket.accept();

				clientList.add(socket);
				// Display the client number
				textArea.append("Starting thread for player " + clientNo + " at " + new Date() + '\n');

				textArea.append("Player " + clientNo + " has joined ");

				ClientChat task = new ClientChat(socket, clientNo); // this is the Task class in multithread that should
																	// have been implemented from runnable interface
				// Start the new thread
				new Thread(task).start();

				// Increment clientNo
				clientNo++;

				if (clientNo > 2)
					break;

			}
		} catch (IOException ex) {
			System.err.println(ex);
		}
	}

	class ClientChat implements Runnable {
		private Socket socket; // A connected socket
		int clientNo;

		/** Construct a thread */
		public ClientChat(Socket socket, int clientNo) {
			this.socket = socket;
			this.clientNo = clientNo;
		}

		@Override /** Run a thread */
		public void run() {
			try {
				// Continuously serve the client
				while (true) {
					message = new DataInputStream(socket.getInputStream()).readUTF();
					// Receive message from the client

					//check which playey is playing
					 if(clientNo == 1) {
	                  	player = "X";
	                    }
					 if(clientNo == 2) {
	                    	player = "O";
	                    }
					 //check if game has ended
					if (message.equals("Game ended")) {
						textArea.append("\n" + new Date() + " Game Ended!");
						break;
					} else {

						for (Socket s : clientList) {
							// Send message back to the 2 players
							new DataOutputStream(s.getOutputStream()).writeUTF((message + "" + player));
						}

						textArea.append(" player: " + clientNo + "played : " + message + player + '\n');
					}
				}
				this.socket.close();
			} catch (IOException e) {
				System.err.println(e);
			}
		}
	}

}

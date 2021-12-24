package Client;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.text.JTextComponent;

import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.JButton;
import java.awt.Color;
import java.awt.Toolkit;
import java.awt.Window;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class ConnectGUI {

	private JFrame frame;
	private JTextField ipT;
	private JTextField portT;
	private JTextField idT;
	public static Socket mySocket;
	private boolean isConnected;
	private JLabel lblNewLabel_4;

	public ConnectGUI() {
		initialize();
	}

	
	public Socket connect(String ip, String port) {
        System.out.println("Attempting to make a connection..");
        //default ip and port are 127.0.0.1 and 5000
        try {
            int port1 = Integer.parseInt(port);
            mySocket = new Socket(ip, port1); //attempt socket connection (local address). This will wait until a connection is made
            InputStreamReader stream1 = new InputStreamReader(mySocket.getInputStream()); //Stream for network input
            
            SyncGUI.input = new BufferedReader(stream1);
            SyncGUI.output = new PrintWriter(mySocket.getOutputStream(),true); //assign printWriter to network stream
            frame.dispose();
            isConnected = true;
        } catch (Exception e) {  //connection error occurred
            System.out.println("Connection to Server Failed");
            lblNewLabel_4.setText("Wrong ip or port");
        }
        if (isConnected == true) {
            //this is to get rid of the first and second welcome message that disturbs the login
            try {
                if (SyncGUI.input.ready()) {
                	SyncGUI.input.readLine();
                }
            } catch (IOException ex) {
                System.out.println("Failed to receive msg from the server");
            }
            try {
                if (SyncGUI.input.ready()) {
                	SyncGUI.input.readLine();
                }
            } catch (IOException ex) {
                System.out.println("Failed to receive msg from the server");
            }
        }
        System.out.println("Connection made.");
        return mySocket;
    }
	
	
	private void initialize() {
		frame = new JFrame();
		frame.setResizable(false);
		frame.setVisible(true);
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage(ConnectGUI.class.getResource("/Client/icon.png")));
		frame.setBounds(100, 100, 450, 300);
		frame.getContentPane().setLayout(null);
		JLabel lblNewLabel = new JLabel("Connect");
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblNewLabel.setBounds(171, 22, 72, 41);
		frame.getContentPane().add(lblNewLabel);
		JLabel lblNewLabel_1 = new JLabel("ip");
		lblNewLabel_1.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblNewLabel_1.setBounds(89, 66, 72, 32);
		frame.getContentPane().add(lblNewLabel_1);
		JLabel lblNewLabel_2 = new JLabel("port");
		lblNewLabel_2.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblNewLabel_2.setBounds(89, 108, 85, 41);
		frame.getContentPane().add(lblNewLabel_2);
		ipT = new JTextField();
		ipT.setText("127.0.0.1");
		ipT.setBounds(181, 73, 192, 19);
		frame.getContentPane().add(ipT);
		ipT.setColumns(10);
		portT = new JTextField();
		portT.setText("5000");
		portT.setBounds(181, 122, 192, 19);
		frame.getContentPane().add(portT);
		portT.setColumns(10);
		JButton btnNewButton = new JButton("log In");
		btnNewButton.setFocusPainted(false);
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String host = ipT.getText();
                String port = portT.getText();
                String id = idT.getText();
                //connect
                 if(id.equals("")) {
                	 lblNewLabel_4.setText("id cannot be empty");
                 }else {
	                connect(host, port);
	                ipT.setText("");
	                portT.setText("");
	                if (isConnected) {
	                    //open login page
	                	//SyncGUI sync=new SyncGUI();
	                	PlayerGUI.syncStart();
	                	SyncGUI.output.println("id+"+id);
	                	System.out.println("id"+id);
	                	SyncGUI.output.flush();
	                	while(true) {
	           			 try {
	           				if (SyncGUI.input.ready()) { //check for an incoming message
	           				        String msg = SyncGUI.input.readLine();//get a message from the client
	           				        if(msg.startsWith("id+")) {
	           				        	SyncGUI.id=msg.substring(3);
	           				        	System.out.println("get");
	           				        	break;
	           				        }
	           				 }
	           			} catch (IOException e1) {
	           				// TODO Auto-generated catch block
	           				e1.printStackTrace();
	           			}
	           		}
	                }
                 }
			}
		});
		
		btnNewButton.setBounds(74, 219, 87, 21);
		frame.getContentPane().add(btnNewButton);
		JLabel lblNewLabel_3 = new JLabel("set id");
		lblNewLabel_3.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblNewLabel_3.setBounds(89, 159, 50, 19);
		frame.getContentPane().add(lblNewLabel_3);
		idT = new JTextField();
		idT.setBounds(181, 161, 192, 17);
		frame.getContentPane().add(idT);
		idT.setColumns(10);
		lblNewLabel_4 = new JLabel("New label");
		lblNewLabel_4.setForeground(Color.RED);
		lblNewLabel_4.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblNewLabel_4.setBounds(202, 219, 167, 19);
		frame.getContentPane().add(lblNewLabel_4);
	}


	public void close() {
		try {
			if(mySocket!=null) {
				mySocket.close();
			}
			if(SyncGUI.output!=null&&SyncGUI.input!=null) {
			SyncGUI.output.close();
			SyncGUI.input.close();
			//SyncGUI.
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}

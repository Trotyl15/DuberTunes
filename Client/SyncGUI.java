package Client;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.JScrollPane;
import javax.swing.JList;
import javax.swing.DefaultListModel;
import javax.swing.JComboBox;
import java.awt.Toolkit;
import java.awt.Dimension;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Scanner;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.ListSelectionModel;

public class SyncGUI {

	private JFrame frame;
	public static PrintWriter output;
	public static BufferedReader input;
	private DefaultListModel<Music> songs;
	public static OutputStream os;
	public static String id;
	
	/**
	 * Create the application.
	 */
	public SyncGUI(DefaultListModel s) {
		 songs=s;
		 
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				try {
					ConnectGUI.mySocket.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		frame.setResizable(false);
		frame.setMaximumSize(new Dimension(372, 498));
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage(SyncGUI.class.getResource("/Client/icon.png")));
		frame.setBounds(100, 100, 372, 498);
		JPanel panel = new JPanel();
		frame.getContentPane().add(panel, BorderLayout.CENTER);
		panel.setLayout(null);
		JPanel panel_1 = new JPanel();
		panel_1.setBounds(0, 0, 358, 124);
		panel.add(panel_1);
		panel_1.setLayout(null);
		JComboBox<String> idList = new JComboBox();
		idList.setFocusable(false);
		idList.setBounds(85, 11, 110, 21);
		panel_1.add(idList);
		
		JLabel sendToL = new JLabel("send to");
		sendToL.setFont(new Font("Tahoma", Font.PLAIN, 14));
		sendToL.setBounds(18, 9, 63, 21);
		panel_1.add(sendToL);
		
		JButton startButton = new JButton("start");
		startButton.setFocusPainted(false);
		
		startButton.setBounds(226, 11, 110, 21);
		panel_1.add(startButton);
		
		JLabel errorLabel = new JLabel("");
		errorLabel.setForeground(Color.RED);
		errorLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
		errorLabel.setBounds(18, 77, 318, 21);
		panel_1.add(errorLabel);
		
		JButton refresh = new JButton("refresh");
		refresh.setFocusPainted(false);
		refresh.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
					//get all the online users
					output.println("list");
					output.flush();
					idList.removeAllItems();
					while(true) {
						 try {
							if (input.ready()) { //check for an incoming message
							        String msg = input.readLine();//get a message from the client
							        if(msg.equals("end")) {
							        	break;
							        }
							        System.out.println("id"+id+"m"+msg);
							        if(!msg.equals(id)) {
							        	idList.addItem(msg);
							        }
							 }
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					}
			}
		});

		refresh.setBounds(85, 35, 110, 21);
		panel_1.add(refresh);
		JScrollPane jsp = new JScrollPane();
		jsp.setBounds(0, 124, 358, 336);
		panel.add(jsp);
		JList list = new JList<Music>(songs);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setFont(new Font("Tahoma", Font.PLAIN, 15));
		
		jsp.setViewportView(list);
		frame.setVisible(false);
		startButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("idList"+idList.getSelectedItem());
				if(idList.getSelectedItem()==null) {
					if(list.getSelectedIndex()!=-1) {
						output.println("send:"+"server");
						System.out.println("send:server");
						output.flush();
						try {
							
							send(songs.get(list.getSelectedIndex()).getName(),songs.get(list.getSelectedIndex()).getFilePath());
														
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					}
				}else {
					if (list.getSelectedIndex() != -1) {
						output.println("send:" + idList.getSelectedItem());
						output.flush();
						try {

							send(songs.get(list.getSelectedIndex()).getName(), songs.get(list.getSelectedIndex()).getFilePath());

						} catch (IOException e1) {
							e1.printStackTrace();
						}
					}
				}
			}
		});
		
	}
	public void setVisible() {
		frame.setVisible(true);
	}
	
	//sending file to server
	private void send(String fileName,String filePath) throws IOException {
		File myFile=new File(filePath);
		int fileSize=(int)myFile.length();
		os=ConnectGUI.mySocket.getOutputStream();
		BufferedInputStream bis=new BufferedInputStream(new FileInputStream(myFile));
		Scanner in=new Scanner(ConnectGUI.mySocket.getInputStream());
		output.println(fileName+".wav");
		output.println(fileSize);
		byte[]filebyte=new byte[fileSize];
		
		bis.read(filebyte,0,filebyte.length);
		os.write(filebyte,0,filebyte.length);
		os.flush();
		
		
	}
	
	
	
}

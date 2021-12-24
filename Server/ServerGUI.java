package Server;



import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.GridLayout;
import javax.swing.JScrollPane;
import javax.swing.JList;
import java.awt.Toolkit;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.SwingConstants;
import java.awt.Dimension;
import java.net.ServerSocket;
import javax.swing.ListSelectionModel;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * ServerGUI.java
 * @version 1.0
 * @author Trotyl
 * @since 2022 Jan.11
 * The server for sending music file
 */
public class ServerGUI {
	static ServerSocket serverSock;// server socket for connection
	static Boolean running = true; 
	private JFrame frmDubertunesServer;
	private Queue<String> users=new LinkedList<>();
	private DefaultListModel<String> usersList;
	public static Socket client; 
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		ServerGUI window = new ServerGUI();
				
		
	}

	//***** Inner class - thread for client connection
	  class ConnectionHandler implements Runnable { 
	    private PrintWriter output; //assign printwriter to network stream
	    private BufferedReader input; //Stream for network input
	    //keeps track of the client socket
	    //private boolean running;

	    /* ConnectionHandler
	     * Constructor
	     * @param the socket belonging to this client connection
	     */    
	    ConnectionHandler(Socket s) { 
	      client = s;  //constructor assigns client to this    
	      try {  //assign all connections to client
	        this.output = new PrintWriter(client.getOutputStream());
	        InputStreamReader stream = new InputStreamReader(client.getInputStream());
	        this.input = new BufferedReader(stream);
	      }catch(IOException e) {
	        e.printStackTrace();        
	      }            
	      running=true;
	    } //end of constructor
	  
	    
	    /* run
	     * executed on start of thread
	     */
	    public void run() {

	      //Get a message from the client
	      String msg="";
	      
          
	        
	      while(running) {  // loop unit a message is received        
	        try {
	          if (input.ready()) { //check for an incoming messge
	            msg = input.readLine();  //get a message from the client
	            //System.out.println("Received111: " + msg);
	            if(msg.startsWith("id+")) {
		            String tempId=msg.substring(3);
		            //System.out.println("t"+tempId);
		            if(usersList.contains(tempId)) {
		            	System.out.println("contain");
		            	boolean add=true;
		            	for(int i=2;add;i++) {
		            		if(!usersList.contains(tempId+"#"+i)) {
		            			//users.add(tempId+"#"+i);
		            			tempId=tempId+"#"+i;
		            			add=false;
		            		}
		            	}
		            }
		            System.out.println("t"+tempId);
		            usersList.addElement(tempId);
		            users.add(tempId);
		            output.println("id+"+tempId);
            		output.flush();
	            }else if(msg.equals("list")) {
	            	for(String str:users) {
	            		output.println(str);
	            		output.flush();
	            	}
	            	output.println("end");
	            	output.flush();
	            }else if(msg.startsWith("send:")) {
			        	receive();
			     }
	            //usersList.addElement("321");
	            //System.out.println(usersList.contains(tempId));
	            //output.println(msg); //echo the message back to the client ** This needs changing for multiple clients
	            //output.flush();             
	          }
	          }catch (IOException e) {
	            System.out.println("Failed to receive msg from the client");
	            e.printStackTrace();
	          }
	        //System.out.println("12113"+running);
	        }
	      
	      //Send a message to the client
	      output.println("We got your message! Goodbye.");
	      output.flush(); 
	      
	      //close the socket
	      try {
	        input.close();
	        output.close();
	        client.close();
	      }catch (Exception e) { 
	        System.out.println("Failed to close socket");
	      }
	    } // end of run()
	  } //end of inner class   
	
	
	
	static class Client {
        Socket s;
        String name;
        PrintWriter pw;
        long time;

        public Client(Socket s, String name, PrintWriter pw) {
            this.s = s;
            this.pw = pw;
           
        }// end of constructor
    }// end of class
	
	
	 
	
	
	/**
	 * Create the application.
	 */
	public ServerGUI() {
		initialize();
		go();
	}

	
	 public void go() {
        System.out.println("Waiting for a client connection..");
    
     Socket client = null;//hold the client connection
        
     try {
      serverSock = new ServerSocket(5000);  //assigns an port to the server
      System.out.println("123");
      serverSock.setSoTimeout(100000000);  //15 second timeout
      
      while(running) {//this loops to accept multiple clients
    	  System.out.println("12");
           client = serverSock.accept();  //wait for connection
     
           System.out.println("Client connected");
           //Note: you might want to keep references to all clients if you plan to broadcast messages
           //Also: Queues are good tools to buffer incoming/outgoing messages
           Thread t = new Thread(new ConnectionHandler(client)); //create a thread for the new client and pass in the socket
           t.start(); //start the new thread
     }
    }catch(Exception e) { 
    	e.printStackTrace();
      System.out.println("Error accepting connection");
      //close all and quit
      try {
        client.close();
      }catch (Exception e1) { 
        System.out.println("Failed to close socket");
      }
      System.exit(-1);
    }
  }
    
	
	
	
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		
		frmDubertunesServer = new JFrame();
		frmDubertunesServer.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				running=false;
				System.exit(1000);
//				try {
//					client.close();
//				} catch (IOException e1) {
//					// TODO Auto-generated catch block
//					e1.printStackTrace();
//				}
			}
		});
		//frmDubertunesServer.setFocusableWindowState(false);
		frmDubertunesServer.setResizable(false);
		frmDubertunesServer.setMaximumSize(new Dimension(385, 482));
		frmDubertunesServer.setTitle("DuberTunes Server");
		frmDubertunesServer.setIconImage(Toolkit.getDefaultToolkit().getImage(ServerGUI.class.getResource("/Client/icon.png")));
		frmDubertunesServer.setBounds(100, 100, 385, 482);
		//frmDubertunesServer.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmDubertunesServer.getContentPane().setLayout(new GridLayout(0, 1, 0, 0));
		
		JPanel panel = new JPanel();
		frmDubertunesServer.getContentPane().add(panel);
		panel.setLayout(null);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBounds(0, 0, 371, 108);
		panel.add(panel_1);
		panel_1.setLayout(null);
		
		JLabel lblOnlineUsers = new JLabel("online users");
		lblOnlineUsers.setVerticalAlignment(SwingConstants.TOP);
		lblOnlineUsers.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblOnlineUsers.setBounds(8, 34, 343, 45);
		panel_1.add(lblOnlineUsers);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(0, 108, 371, 335);
		panel.add(scrollPane);
		
		 //list = new JList();
		usersList=new DefaultListModel<String>();
		JList list = new JList<String>(usersList);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		//usersList.addElement("123");
		scrollPane.setViewportView(list);
		
		frmDubertunesServer.setVisible(true);
	}

	public void receive()  {
		running=false;
		Scanner in;
		try {
			in = new Scanner(client.getInputStream());
		
		InputStream is=client.getInputStream();
		String fileName=in.nextLine();
		int fileSize=in.nextInt();
		FileOutputStream fos=new FileOutputStream(fileName);
		
		BufferedOutputStream bos=new BufferedOutputStream(fos);
		//System.out.println(fileSize);
		byte[] filebyte=new byte[fileSize];
		int file=is.read(filebyte,0,filebyte.length);
		System.out.println("ss"+filebyte.length);
		bos.write(filebyte,0,file);
		System.out.println("fileName "+fileName);
		System.out.println("size "+fileSize);
		bos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private void saveFile(Socket clientSock) {
		int bytesRead;
	    int current = 0;
	    FileOutputStream fos = null;
	    BufferedOutputStream bos = null;
	    Socket sock = null;
	    try {
	      //sock = new Socket(SERVER, SOCKET_PORT);

	      // receive file
	      byte [] mybytearray  = new byte [30131550];
	      InputStream is = sock.getInputStream();
	      fos = new FileOutputStream("temp.wav");
	      bos = new BufferedOutputStream(fos);
	      bytesRead = is.read(mybytearray,0,mybytearray.length);
	      current = bytesRead;

	      do {
	         bytesRead =
	            is.read(mybytearray, current, (mybytearray.length-current));
	         if(bytesRead >= 0) current += bytesRead;
	      } while(bytesRead > -1);

	      bos.write(mybytearray, 0 , current);
	      bos.flush();
	      System.out.println("File " + "temp.wav"
	          + " downloaded (" + current + " bytes read)");
	    } catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    finally {
	    	try {
	      if (fos != null) fos.close();
	      if (bos != null) bos.close();
	      if (sock != null)
			
				sock.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }
	}
	
}
